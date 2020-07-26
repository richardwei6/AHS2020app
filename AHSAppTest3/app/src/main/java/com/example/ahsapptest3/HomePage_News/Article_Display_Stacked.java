package com.example.ahsapptest3.HomePage_News;


import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.Helper_Code.Helper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout outerLayout = new LinearLayout(container.getContext());
        displayFrags(outerLayout);
        return outerLayout;
    }

    public static Article_Display_Stacked newInstanceOf(Article[] articles)
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

        assert parcelables != null;
        Article[] articles = Arrays.copyOf(parcelables,parcelables.length,Article[].class); // attempts to avoid classcastexception

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

            Helper.setArticleListener_toView(inflated[i], articles[i]);
        }

        for(int i = 0; i < inflated.length; i++)
        {
            if(articles[i].isBlank())
            {
                inflated[i].setVisibility(View.INVISIBLE);
            }
            else
            {
                // set time updated
                Helper.setTimeText_toView((TextView) inflated[i].findViewById(R.id.article_display__time_updated_Text),
                        Helper.TimeFromNow(articles[i].getTimeUpdated())
                        );

                // set title
                Helper.setText_toView((TextView) inflated[i].findViewById(R.id.article_display__title_Text),articles[i].getTitle());

                // set summary/description
                Helper.setHtmlParsedText_toView((TextView) inflated[i].findViewById(R.id.article_display__summary_Text), articles[i].getStory());
                Helper.setArticleListener_toView(inflated[i].findViewById(R.id.article_display__summary_Text), articles[i]);

                // setImage
                Helper.setImage_toView_fromUrl((ImageView) inflated[i].findViewById(R.id.article_display__imageView),articles[i].getImagePaths()[0]);


            }
        }
    }
}
