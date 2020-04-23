package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.Date;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter
{
    private int NUM_PAGES;
    private Article_Display_Stacked[] frags;
    private boolean[] already_added;

    Date date;
    public ScreenSlidePagerAdapter(@NonNull FragmentManager fm, Article_Display_Stacked[] frags, int num_pages) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        NUM_PAGES = num_pages;
        this.frags = frags;
        already_added = new boolean[frags.length];

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
