package com.example.ahsapptest3.HomePage_News;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.HomePage_News.Featured_Display;

public class FeaturedArticle_PagerAdapter extends FragmentPagerAdapter
{
    private int NUM_PAGES;
    private Article[] articles;



    public FeaturedArticle_PagerAdapter(@NonNull FragmentManager fm, Article[] articles)
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
