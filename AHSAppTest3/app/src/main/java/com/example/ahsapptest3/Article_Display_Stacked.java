package com.example.ahsapptest3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Article_Display_Stacked extends Fragment {

    public Article_Display_Stacked() {
        // Required empty public constructor
    }
    private final static String FRAG_KEY = "1", ID_KEY = "2"; // keys for bundle

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

    public void displayFrags(View view)
    {
        LinearLayout layout = view.findViewById(R.id.LinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        Article_Display_Template[] frags = (Article_Display_Template[]) getArguments().getParcelableArray(FRAG_KEY);
        FrameLayout[] frameLayouts = new FrameLayout[frags.length];
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
        }

    }
}
