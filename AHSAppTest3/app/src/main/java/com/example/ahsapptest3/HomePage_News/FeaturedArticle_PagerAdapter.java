package com.example.ahsapptest3.HomePage_News;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ahsapptest3.Article_Slim;

public class FeaturedArticle_PagerAdapter extends FragmentPagerAdapter
{
    private final int NUM_PAGES;
    private final Article_Slim[] articles;



    public FeaturedArticle_PagerAdapter(@NonNull FragmentManager fm, Article_Slim[] articles)
    {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        NUM_PAGES = articles.length;
        this.articles = articles;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Featured_Display.newInstanceOf(articles[position]);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
