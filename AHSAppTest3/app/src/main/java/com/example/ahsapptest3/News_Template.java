package com.example.ahsapptest3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class News_Template extends Fragment {

    public News_Template() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template__news_layout, container, false);

        String[][] data = getData();
        if(data.length == 0) return view;

        int num_stacked = getNumStacked();
        final int startID = getStartID();

        Article_Display_Stacked[] display_frags = new Article_Display_Stacked[data.length/num_stacked];
        Article_Display_Template[] frags = new Article_Display_Template[data.length];
        for(int i = 0; i < frags.length; i++)
        {
            frags[i] = Article_Display_Template.newInstanceOf(
                    getDate(""),
                    data[i][0],
                    data[i][1],
                    getImageFilePath(""),
                    isAlreadyBookmarked(""));
        }

        int count = 0;
        for(int i = 0; i < display_frags.length; i++)
        {
            Article_Display_Template[] frags_stacked = new Article_Display_Template[num_stacked];
            for(int j = 0; j < frags_stacked.length; j++)
            {
                frags_stacked[j] = frags[count];
                count++;
                System.out.println("count:: " + count);
            }
            display_frags[i] = Article_Display_Stacked.newInstanceOf(frags_stacked,startID+i*1000);
        }

        System.out.println("length::" + display_frags.length);
        EnhancedWrapContentViewPager viewPager = view.findViewById(R.id.template_news__ViewPager);

       /* Date date = getDate("");

        Article_Display_Template[] thisfrags = {Article_Display_Template.newInstanceOf(
                date, "Lorem Ipsum a Long Title [][][]", "hello world what a nice day!","",false),
                Article_Display_Template.newInstanceOf(
                        date, "Lorem Ipsum a Long Title [][][]", "hello world what a nice day!","",false)};


        display_frags = new Article_Display_Stacked[]{Article_Display_Stacked.newInstanceOf(thisfrags,getStartID())};*/
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getFragmentManager(),display_frags,display_frags.length));
        TabLayout tabLayout = view.findViewById(R.id.template_news__TabLayout);
        tabLayout.setupWithViewPager(viewPager, true);


        return view;
    }

    public String[][] getData()
    {
        return new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"Title2", "summaryText2"},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
    }

    public Date getDate(String key)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,3,22,9,7);

        Date date = calendar.getTime();
        return date;
    }

    public Boolean isAlreadyBookmarked(String key)
    {
        return false;
    }

    public String getImageFilePath(String key)
    {
        return "";
    }

    public int getNumStacked()
    {
        return 2;
    }

    public int getStartID()
    {
        return 2000000;
    }
}
