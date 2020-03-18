package com.example.ahsapptest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Club_Template thing1 = new Club_Template();

        ConstraintLayout mainlayout = (ConstraintLayout) findViewById(R.id.Clubs_Constraint_Layout);

        FrameLayout frameLayout = new FrameLayout(this); // Set up framelayout to put the fragment in
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        frameLayout.setLayoutParams(params);
        frameLayout.setId(1012345); // Just a random id

        mainlayout.addView(frameLayout);

        ConstraintSet set2 = new ConstraintSet();
        set2.clone(mainlayout);
        set2.connect(frameLayout.getId(),ConstraintSet.LEFT,mainlayout.getId(),ConstraintSet.LEFT);
        set2.connect(frameLayout.getId(),ConstraintSet.TOP,mainlayout.getId(),ConstraintSet.TOP);
        set2.clear(R.id.firstView,ConstraintSet.START);
        set2.clear(R.id.firstView,ConstraintSet.LEFT);
        set2.connect(R.id.firstView, ConstraintSet.LEFT,frameLayout.getId(),ConstraintSet.RIGHT, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16,
                getResources().getDisplayMetrics()));
        set2.applyTo(mainlayout);

        getSupportFragmentManager()
            .beginTransaction()
            .add(frameLayout.getId(),thing1)
            .commit();

    }
}
