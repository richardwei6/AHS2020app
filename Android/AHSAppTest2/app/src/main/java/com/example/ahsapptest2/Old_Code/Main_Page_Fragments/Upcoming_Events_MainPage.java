package com.example.ahsapptest2.Old_Code.Main_Page_Fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest2.R;


public class Upcoming_Events_MainPage extends Fragment {

    public Upcoming_Events_MainPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcoming_events__main_page_layout, container, false);
        /*ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.Upcoming_Events_Constraint_Layout);

        String [][] data = getInfo();
        if (data.length == 0) return view;

        FrameLayout [] frameLayouts = new FrameLayout[data.length];

        ConstraintSet set = new ConstraintSet();


        for(int i = 0; i < frameLayouts.length; i++)
        {
            frameLayouts[i] = new FrameLayout(this.getContext());
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(1010000+i);
            constraintLayout.addView(frameLayouts[i]);
            set.clone(constraintLayout);
            if(i == 0)
            {
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.LEFT,
                        constraintLayout.getId(),ConstraintSet.LEFT);
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.TOP,
                        R.id.Upcoming_Events_Titletext,ConstraintSet.BOTTOM,dp_to_pixel(16));
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.RIGHT,
                        constraintLayout.getId(),ConstraintSet.RIGHT);
            }
            else
            {
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.LEFT,
                        constraintLayout.getId(),ConstraintSet.LEFT);
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.TOP,
                        frameLayouts[i-1].getId(),ConstraintSet.BOTTOM,dp_to_pixel(16));
                set.connect(
                        frameLayouts[i].getId(),ConstraintSet.RIGHT,
                        constraintLayout.getId(),ConstraintSet.RIGHT);
            }
            set.applyTo(constraintLayout);
        }


        Upcoming_Events [] upcoming_events = new Upcoming_Events[data.length];


        for(int i = 0; i < upcoming_events.length; i++)
        {
            upcoming_events[i] = Upcoming_Events.newInstance(data[i][0],data[i][1],data[i][2]);
            getFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),upcoming_events[i])
                    .commit();
        }

        // Inflate the layout for this fragment
*/
        return view;
    }

    private String[][] getInfo()
    {
        // fully implement later
        return
                new String[][] {
                        {"1/01/2020","Title1","text1"},
                        {"1/02/2020","Title2","text2"},
                        {"1/03/2020","Title3","text3"},
                        {"1/04/2020","Title4","text4"},
        };
    }

    private int dp_to_pixel(int dp)
    {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
