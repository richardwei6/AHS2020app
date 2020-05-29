package com.example.ahsapptest3;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.template_notif_display, container, false);
        if(getArguments() == null)
            return view;
        Article data = getArguments().getParcelable(ARTICLE_KEY);
        data.setTitleText_toView((TextView) view.findViewById(R.id.notif_display_titleText));
        data.setStoryText_toView((TextView) view.findViewById(R.id.notif_display_summaryText));
        data.setTime_Minutes_UpdatedText_toView((TextView) view.findViewById(R.id.notif_display_timeUpdatedText));

        if(data.alreadyNotified())
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
