package com.example.ahsapptest3.HomePage_News;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.HomePage_News.Article_Display_Stacked;
import com.example.ahsapptest3.HomePage_News.Article_Display_Template;

import java.util.Date;

public class Article_Stacked_PagerAdapter extends FragmentPagerAdapter
{
    private int NUM_PAGES;
    private int num_stacked;
    private int startID;
//    private Article_Display_Template[] frags;
    private Article[] infos;


    Date date;
    public Article_Stacked_PagerAdapter(@NonNull FragmentManager fm, Article_Display_Template[] frags, int num_stacked, int startID) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        NUM_PAGES = frags.length/num_stacked+frags.length%num_stacked;
        //this.frags = frags;
        this.num_stacked = num_stacked;
        this.startID = startID;


    }

    public Article_Stacked_PagerAdapter(@NonNull FragmentManager fm, Article[] frags, int num_stacked, int startID) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        NUM_PAGES = frags.length/num_stacked+frags.length%num_stacked;
        this.infos = frags;
        this.num_stacked = num_stacked;
        this.startID = startID;

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
        
        Article[] stacked_info = new Article[num_stacked];
        for(int i = 0; i < num_stacked; i++)
        {
            int i1 = i + position*num_stacked;
            if(i1 >= this.infos.length)
                stacked_info[i] = new Article();
            else
                stacked_info[i] = this.infos[i1];
        }
        
        return Article_Display_Stacked.newInstanceOf(stacked_info);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
