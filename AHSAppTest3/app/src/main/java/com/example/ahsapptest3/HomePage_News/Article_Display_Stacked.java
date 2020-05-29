package com.example.ahsapptest3.HomePage_News;

import android.os.Bundle;
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
import com.example.ahsapptest3.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Article_Display_Stacked extends Fragment {

    public Article_Display_Stacked() {
        // Required empty public constructor
    }
    private final static String FRAG_KEY = "1", ID_KEY = "2", ARTICLE_KEY = "3"; // keys for bundle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template__stacked_article_display, container, false);

        displayFrags(view);
        return view;
    }

    public static Article_Display_Stacked newInstanceOf(Article_Display_Template[] frags, int startID)
    {
        Article_Display_Stacked thisFrag = new Article_Display_Stacked();
        Bundle args = new Bundle();
        args.putParcelableArray(FRAG_KEY,frags);
        args.putInt(ID_KEY,startID);
        thisFrag.setArguments(args);

        return thisFrag;
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
        LinearLayout layout = view.findViewById(R.id.LinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Article[] articles = (Article[]) getArguments().getParcelableArray(ARTICLE_KEY);
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

            articles[i].setArticleListener_toView(inflated[i],this.getContext());
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
                articles[i].setTime_Hours_UpdatedText_toView((TextView) inflated[i].findViewById(R.id.article_display__time_updated_Text));

                // set title
                articles[i].setTitleText_toView((TextView) inflated[i].findViewById(R.id.article_display__title_Text));

                // set summary/description
                articles[i].setStoryText_toView((TextView) inflated[i].findViewById(R.id.article_display__summary_Text));

                // setImage
                articles[i].setImage_toView((ImageView) inflated[i].findViewById(R.id.article_display__imageView));

                // set bookmarked button state
                final ImageButton btn = inflated[i].findViewById(R.id.article_display__bookmarked_button);
                articles[i].setBookmarked_toView(btn);
                articles[i].setBookMarkListener_toView(btn);
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
