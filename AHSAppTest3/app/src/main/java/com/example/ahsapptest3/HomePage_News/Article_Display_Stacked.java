package com.example.ahsapptest3.HomePage_News;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.ArticleDatabase;
import com.example.ahsapptest3.ArticleNavigation;
import com.example.ahsapptest3.Article_Slim;
import com.example.ahsapptest3.Misc.Helper;
import com.example.ahsapptest3.R;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Article_Display_Stacked extends Fragment {

    public Article_Display_Stacked() {
        // Required empty public constructor
    }
    private final static String ARTICLE_KEY = "1"; // keys for bundle
    private ArticleNavigation navigation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigation = (ArticleNavigation) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout outerLayout = new LinearLayout(getContext());
        displayFrags(outerLayout);
        return outerLayout;
    }

    public static Article_Display_Stacked newInstanceOf(Article_Slim[] articles)
    {
        Article_Display_Stacked thisFrag = new Article_Display_Stacked();
        Bundle args = new Bundle();
        args.putParcelableArray(ARTICLE_KEY,articles);
        thisFrag.setArguments(args);

        return thisFrag;
    }

    public void displayFrags(LinearLayout layout)
    {
        /*LinearLayout layout = view;*///.findViewById(R.id.article_stacked_LinearLayout);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        Parcelable[] parcelables = getArguments().getParcelableArray(ARTICLE_KEY);

        final Article_Slim[] articles = Arrays.copyOf(parcelables,parcelables.length,Article_Slim[].class); // attempts to avoid classcastexception

        ViewStub[] stubs = new ViewStub[articles.length];

        View[] inflated = new View[stubs.length];


        for(int i = 0; i < stubs.length; i++)
        {
            stubs[i] = new ViewStub(this.getContext());
            layout.addView(stubs[i],params);

            stubs[i].setLayoutResource(R.layout.news_article_template);
            inflated[i] = stubs[i].inflate();

            /*// this creates that ripple effect on default button
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
            inflated[i].setBackgroundResource(outValue.resourceId);*/
            final int finalI = i;
            inflated[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article article1 = ArticleDatabase.getInstance(getContext()).getArticleById(articles[finalI].getID());
                    if(article1 != null)
                        navigation.onItemClicked(article1);
                }
            });
        }

        for(int i = 0; i < inflated.length; i++)
        {
            // set time updated
            Helper.setTimeText_toView((TextView) inflated[i].findViewById(R.id.article_display__time_updated_Text),
                    Helper.TimeFromNow(articles[i].getTimeUpdated())
                    );

            // set title
            ((TextView) inflated[i].findViewById(R.id.article_display__title_Text)).setText(articles[i].getTitle());

            // set summary/description
            TextView summaryText = inflated[i].findViewById(R.id.article_display__summary_Text);
            Helper.setHtmlParsedText_toView(summaryText, articles[i].getStory());
            final int finalI = i;
            summaryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article article1 = ArticleDatabase.getInstance(getContext()).getArticleById(articles[finalI].getID());
                    if(article1 != null)
                        navigation.onItemClicked(article1);
                }
            });

            // setImage
            String imagePaths = articles[i].getImagePath();
            ImageView imageView = inflated[i].findViewById(R.id.article_display__imageView);
            if(imagePaths != null && imagePaths.length() > 0)
                Helper.setImageFromUrl_CenterCrop_FullSize(
                        imageView,
                        imagePaths
                );
            else
                imageView.setImageResource(R.drawable.image_bg);
        }
    }
}
