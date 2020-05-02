package com.example.ahsapptest3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public abstract class Featured_Template extends News_Template {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template__news_layout, container, false);

        TextView titleText = view.findViewById(R.id.template_news__TitleText);
        titleText.setText(getTitleText());

        ImageView titleBar = view.findViewById(R.id.template_news__rounded_bar);
        titleBar.setColorFilter(getBarColor());

        String[][] data = getData();

        Article[] articles = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles[i] = new Article(
                    getDate(""),
                    data[i][0],
                    data[i][1],
                    getImageFilePath(""),
                    isAlreadyBookmarked(""));
        }

        EnhancedWrapContentViewPager viewPager = view.findViewById(R.id.template_news__ViewPager);
        viewPager.setAdapter(new FeaturedArticle_PagerAdapter(getChildFragmentManager(),articles));
        Featured_Display[] display_frags = new Featured_Display[5];
        for(int i = 0; i < display_frags.length; i++)
        {
            display_frags[i] = new Featured_Display();
        }

        /*viewPager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager(),display_frags,display_frags.length,3000000));*/
        TabLayout tabLayout = view.findViewById(R.id.template_news__TabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        return view;
    }
}
