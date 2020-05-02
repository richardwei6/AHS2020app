package com.example.ahsapptest3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {


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
        final FrameLayout navBar= findViewById(R.id.nav_bar_FrameLayout);
        final ScrollView scrollView = findViewById(R.id.home_page__scrollView);
        final int scrollAnimBuffer = 4;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY() > y + scrollAnimBuffer) // scroll down, 2 is the buffer
                {
                    slideDown(navBar);
                    is_nav_bar_up = false;
                }
                else if (scrollView.getScrollY() < y - scrollAnimBuffer)
                {
                    slideUp(navBar);
                    is_nav_bar_up = true;
                }
                y= scrollView.getScrollY();
            }
        });

    }

    public void goToHome (View view)
    {

    }

    public void goToBulletin (View view)
    {
        Intent myIntent = new Intent(MainActivity.this,BulletinActivity.class);
        MainActivity.this.startActivity(myIntent);
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

    boolean is_nav_bar_up = true;
    // slide the view from below itself to the current position
    public void slideUp(View view){
        if(!is_nav_bar_up)
        {
            view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }
    }


    // slide the view from its current position to below itself
    public void slideDown(View view){
        if(is_nav_bar_up)
        {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }

    }

}

