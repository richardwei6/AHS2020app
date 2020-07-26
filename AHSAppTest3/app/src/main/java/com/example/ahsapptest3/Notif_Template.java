package com.example.ahsapptest3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Helper_Code.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notif_Template extends Fragment {

    public Notif_Template() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notif_template, container, false);
        if(getArguments() == null)
            return view;
        Article article = getArguments().getParcelable(ARTICLE_KEY);


        Helper.setText_toView( (TextView) view.findViewById(R.id.notif_display_titleText), article.getTitle());

        Helper.setText_toView( (TextView) view.findViewById(R.id.notif_display_summaryText), article.getStory());

        Helper.setTimeText_toView((TextView) view.findViewById(R.id.notif_display_timeUpdatedText),
                Helper.TimeFromNow(article.getTimeUpdated()));

        if(!article.alreadyNotified()) // article not yet notified
        {
            ImageView
                    indicator = view.findViewById(R.id.notif_display_indicatorImage),
                    background = view.findViewById(R.id.notif_display_backgroundImage);
            indicator.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.GoldenYellow_E6CD55__NOTIF,null));
            background.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }

        return view;
    }

    private final static String ARTICLE_KEY = "1";
    public static Notif_Template newInstanceOf(Article article)
    {
        Notif_Template thisFrag = new Notif_Template();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_KEY,article);
        thisFrag.setArguments(args);

        return thisFrag;
    }
}
