package com.example.ahsapptest2.Main_Page_Fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest2.R;
import com.example.ahsapptest2.Upcoming_Events;


public class Upcoming_Events_MainPage extends Fragment {

    public Upcoming_Events_MainPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcoming_events__main_page_layout, container, false);

        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.Upcoming_Events_Constraint_Layout);
        Upcoming_Events frag1 = Upcoming_Events.newInstance("12/2/2020","Sample1","text1");
        Upcoming_Events frag2 = Upcoming_Events.newInstance("12/5/2020","Sample2","text2");


        FrameLayout frameLayout1 = new FrameLayout(this.getContext()); // Set up framelayout to put the fragment in
        frameLayout1.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,  //width
                FrameLayout.LayoutParams.WRAP_CONTENT   //height
        ));
        frameLayout1.setId(1012346); // Just a random id

        FrameLayout frameLayout2 = new FrameLayout(this.getContext()); // Set up framelayout to put the fragment in
        frameLayout2.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,  //width
                FrameLayout.LayoutParams.WRAP_CONTENT   //height
        ));
        frameLayout2.setId(1012347); // Just a random id
// uh
        constraintLayout.addView(frameLayout1);
        constraintLayout.addView(frameLayout2);

        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.connect(
                frameLayout1.getId(),ConstraintSet.LEFT,
                constraintLayout.getId(),ConstraintSet.LEFT);
        set.connect(
                frameLayout1.getId(),ConstraintSet.TOP,
                R.id.Upcoming_Events_Titletext,ConstraintSet.BOTTOM,dp_to_pixel(16));
        set.connect(
                frameLayout1.getId(),ConstraintSet.RIGHT,
                constraintLayout.getId(),ConstraintSet.RIGHT);
        //////////////////////////////////////////////////////////
        set.connect(
                frameLayout2.getId(),ConstraintSet.LEFT,
                constraintLayout.getId(),ConstraintSet.LEFT);
        set.connect(
                frameLayout2.getId(),ConstraintSet.TOP,
                frameLayout1.getId(),ConstraintSet.BOTTOM,dp_to_pixel(16));
        set.connect(
                frameLayout2.getId(),ConstraintSet.RIGHT,
                frameLayout1.getId(),ConstraintSet.RIGHT);
        set.applyTo(constraintLayout);

        getFragmentManager()
                .beginTransaction()
                .add(frameLayout1.getId(),frag1)
                .add(frameLayout2.getId(),frag2)
                .commit();
        // Inflate the layout for this fragment

        return view;
    }

    private int dp_to_pixel(int dp)
    {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
