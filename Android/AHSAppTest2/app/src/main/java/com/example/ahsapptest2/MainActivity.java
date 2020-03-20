package com.example.ahsapptest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.example.ahsapptest2.Main_Page_Fragments.Article_MainPage;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    private ViewPager ausdNewsPager;
    private ViewPager sportsPager;


    //@SuppressLint("ResourceType")
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


        // viewpager
        // dots - https://github.com/ongakuer/CircleIndicator

        ausdNewsPager = (ViewPager) findViewById(R.id.ausdNewsPager);
        ausdNewsPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        CircleIndicator ausdDots = (CircleIndicator) findViewById(R.id.ausdindicator);
        ausdDots.setViewPager(ausdNewsPager);

        sportsPager = (ViewPager) findViewById(R.id.sportsPager);
        sportsPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        CircleIndicator sportsDots = (CircleIndicator) findViewById(R.id.sportsindicator);
        sportsDots.setViewPager(sportsPager);
    }
    @Override
    public void onBackPressed() {
        if (ausdNewsPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            ausdNewsPager.setCurrentItem(ausdNewsPager.getCurrentItem() - 1);
        }
        if (sportsPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            sportsPager.setCurrentItem(sportsPager.getCurrentItem() - 1);
        }
    }

    public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            /*switch (position) {
                case 0:
                    return new Article_MainPage();
            }
            return null;*/
            return new Article_MainPage();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
