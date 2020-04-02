package com.example.ahsapptest2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest2.Article_Blurb_Template;
import com.example.ahsapptest2.R;


abstract class Main_Page_Scrolling_Template extends Fragment {

    public Main_Page_Scrolling_Template() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutId(), container, false);

        TextView titleText = view.findViewById(R.id.Scrolling_Template_Titletext);
        titleText.setText(getTitleText());

        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(getConstraintLayoutId());

        String [][] data = getInfo();
        if (data.length == 0) return view;

        FrameLayout[] frameLayouts = new FrameLayout[data.length];

        ConstraintSet set = new ConstraintSet();


        for(int i = 0; i < frameLayouts.length; i++)
        {
            frameLayouts[i] = new FrameLayout(this.getContext());
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(getIdRange()+i); //TODO: Work out a consistent system for this, please
            constraintLayout.addView(frameLayouts[i]);
            set.clone(constraintLayout);

            if(i == 0)
            {
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.TOP,
                        constraintLayout.getId(),ConstraintSet.TOP);
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.START,
                        constraintLayout.getId(),ConstraintSet.START);

            }
            else
            {
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.TOP,
                        constraintLayout.getId(),ConstraintSet.TOP);
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.START,
                        frameLayouts[i-1].getId(),ConstraintSet.END,dp_to_pixel(16));
            }
            set.applyTo(constraintLayout);
        }


        Article_Blurb_Template[] blurbs = new Article_Blurb_Template[data.length];


        for(int i = 0; i < blurbs.length; i++)
        {
            blurbs[i] = Article_Blurb_Template.newInstance(data[i][0],data[i][1]);
            getFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),blurbs[i])
                    .commit();
        }

        // Inflate the layout for this fragment

        return view;
    }

    public int dp_to_pixel(int dp)
    {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    abstract String[][] getInfo();

    abstract int getLayoutId();

    abstract int getConstraintLayoutId();

    abstract int getIdRange();

    abstract String getTitleText();
}
