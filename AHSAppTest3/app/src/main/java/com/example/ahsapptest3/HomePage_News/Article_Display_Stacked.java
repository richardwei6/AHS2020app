package com.example.ahsapptest3.HomePage_News;


import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.Helper_Code.Helper;
import com.example.ahsapptest3.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template__stacked_article_display, container, false);

        displayFrags(view);
        return view;
    }

    public static Article_Display_Stacked newInstanceOf(Article[] articles)
    {
        Article_Display_Stacked thisFrag = new Article_Display_Stacked();
        Bundle args = new Bundle();
        args.putParcelableArray(ARTICLE_KEY,articles);
        thisFrag.setArguments(args);

        return thisFrag;
    }

    public void displayFrags(View view)
    {
        LinearLayout layout = view.findViewById(R.id.article_stacked_LinearLayout);
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
            stubs[i].setLayoutResource(R.layout.template__article_display);
            inflated[i] = stubs[i].inflate();

            // this creates that ripple effect on default button
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
            inflated[i].setBackgroundResource(outValue.resourceId);

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
                        Helper.TimeFromNow(articles[i].getTimeUpdated()),
                        TimeUnit.HOURS);

                // set title
                Helper.setText_toView((TextView) inflated[i].findViewById(R.id.article_display__title_Text),articles[i].getTitle());

                // set summary/description
                Helper.setText_toView((TextView) inflated[i].findViewById(R.id.article_display__summary_Text), articles[i].getStory());

                // setImage
                Helper.setImage_toView_fromUrl((ImageView) inflated[i].findViewById(R.id.article_display__imageView),articles[i].getImagePaths()[0]);

                // set bookmarked button state
                final ImageButton bookmarkButton = inflated[i].findViewById(R.id.article_display__bookmarked_button);

                Helper.setBookmarked_toView(bookmarkButton,articles[i].isBookmarked());
                Helper.setBookMarkListener_toView(bookmarkButton, articles[i]);

            }
        }

        /*FrameLayout[] frameLayouts = new FrameLayout[frags.length];
        int startID = getArguments().getInt(ID_KEY);

        for(int i = 0; i <frameLayouts.length; i++)
        {
            frameLayouts[i] = new FrameLayout(this.getContext());
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(startID+i);
            layout.addView(frameLayouts[i],params);
        }

        for(int i = 0; i < frags.length; i++)
        {
            getFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),frags[i])
                    .commit();
        }*/

    }
}
