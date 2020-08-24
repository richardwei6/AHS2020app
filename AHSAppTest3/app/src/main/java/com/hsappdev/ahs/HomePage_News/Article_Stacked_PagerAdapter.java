package com.hsappdev.ahs.HomePage_News;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hsappdev.ahs.Article_Slim;

public class Article_Stacked_PagerAdapter extends FragmentPagerAdapter
{
    private final int NUM_PAGES;
    private final int num_stacked;
    private final Article_Slim[] articles;

    public Article_Stacked_PagerAdapter(@NonNull FragmentManager fm, Article_Slim[] articles, int num_stacked) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        NUM_PAGES = articles.length/num_stacked+((articles.length%num_stacked == 0) ? 0 : 1);
        this.articles = articles;
        this.num_stacked = num_stacked;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        /*Fragment[] frags = new Article_Display_Template[num_stacked];
        for(int i = 0; i < num_stacked; i++)
        {
            int i1 = i + position*num_stacked;
            if(i1 >= this.frags.length)
                frags[i] = new Article_Display_Template();
            else
                frags[i] = this.frags[i1];
        }*/
        if(position < articles.length/num_stacked) {
            Article_Slim[] stacked_info = new Article_Slim[num_stacked];
            for(int i = 0; i < num_stacked; i++)
            {
                int i1 = i + position*num_stacked;
                /*if(i1 >= this.articles.length)
                    stacked_info[i] = new Article();
                else*/
                    stacked_info[i] = articles[i1];
            }
            return Article_Display_Stacked.newInstanceOf(stacked_info);
        }
        Article_Slim[] stacked_info = new Article_Slim[articles.length - position*num_stacked];
        if (articles.length - num_stacked * position >= 0)
            System.arraycopy(articles, num_stacked * position, stacked_info, 0, articles.length - num_stacked * position);
        return Article_Display_Stacked.newInstanceOf(stacked_info);

    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

}