package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    private ViewPager testPager;

    private ImageButton home_btn, bulletin_btn, bookmarks_btn, settings_btn;
    private ImageButton[] nav_btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_btn = findViewById(R.id.home_button);
        home_btn.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
        bulletin_btn = findViewById(R.id.bulletin_button);
        bookmarks_btn = findViewById(R.id.bookmarks_button);
        settings_btn = findViewById(R.id.settings_button);

        nav_btns = new ImageButton[]
                {
                        home_btn,bulletin_btn,bookmarks_btn,settings_btn
                };

        /*testPager = findViewById(R.id.test_viewpager);
        testPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        TabLayout testTab = findViewById(R.id.test_tablayout);
        testTab.setupWithViewPager(testPager,true);*/
    }



    public void goToHome (View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(home_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME)); // active
            else
                i.clearColorFilter(); // inactive
        }
    }

    public void goToBulletin (View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(bulletin_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
            else
                i.clearColorFilter();
        }
    }

    public void goToBookmarks(View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(bookmarks_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
            else
                i.clearColorFilter();
        }
    }

    public void goToSettings(View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(settings_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
            else
                i.clearColorFilter();
        }
    }
}

