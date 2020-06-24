package com.example.ahsapptest3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.HomePage_News.News_Template;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private ImageButton home_btn, bulletin_btn, bookmarks_btn, settings_btn;
    private ImageButton[] nav_btns;

    final String[] titles = {
            "FEATURED",
            "ASB NEWS",
            "SPORTS NEWS",
            "DISTRICT NEWS"
    };

    private int[] barColors;

   /* final Article[][] articles = {
            getFeatured_Articles(), //fix
            getASB_Articles(),
            getSports_Articles(),
            getDistrict_Articles()
    };*/

    private View[] newsLayouts;

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

        barColors = new int[]{
                ContextCompat.getColor(this, R.color.VomitYellow_DDCD3E__HOME), // featured
                ContextCompat.getColor(this, R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF), // asb news
                ContextCompat.getColor(this, R.color.VomitYellow_DDCD3E__HOME), // district news
                ContextCompat.getColor(this, R.color.SeaBlue_364D9E__HOME)
        };

        newsLayouts = new View[] {
                findViewById(R.id.home_featuredNews_placeholder),
                findViewById(R.id.home_asbNews_placeholder),
                findViewById(R.id.home_sportsNews_placeholder),
                findViewById(R.id.home_districtNews_placeholder)

        };
        getASB_Articles();


      /*  for(int i = 0; i < newsLayouts.length; i++)
        {
            News_Template newsFrag = News_Template.newInstanceOf(articles[i], titles[i], barColors[i], (i==0));

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(newsLayouts[i].getId(),newsFrag)
                    .commit();
        }*/




/*

        
        for(int i = 0; i < newsLayouts.length; i++)
        {
            TextView titleText = newsLayouts[i].findViewById(R.id.template_news__TitleText);
            titleText.setText(titles[i]);

            ImageView titleBar = newsLayouts[i].findViewById(R.id.template_news__rounded_bar);
            titleBar.setColorFilter(barColors[i]);


        }
int i = 1;
        LinearLayout newsLinearLayout = newsLayouts[i].findViewById(R.id.template_news__LinearLayout);
        if(newsLinearLayout==null) System.out.println(":::null");
        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(newsLinearLayout.getId());
        //if(i==0)
        viewPager.setAdapter(new FeaturedArticle_PagerAdapter(getSupportFragmentManager(),articles[i]));
            *//*else
                {viewPager.setAdapter(new Article_Stacked_PagerAdapter(getSupportFragmentManager(),articles[i],getNumStacked()));
                System.out.println("::: Reached article stacked");}*//*
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TabLayout tabLayout = newsLayouts[i].findViewById(R.id.template_news__TabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
        newsLinearLayout.addView(viewPager,viewPager.getLayoutParams());

        News_Template test = new News_Template() {
            public Article[] getData()
            {
                return articles[0];
            }

            public String getTitleText()
            {
                return titles[0];
            }

            public int getBarColor()
            {
                return barColors[0];
            }
        }*/
        /*for(int i = 0; i < newsLayouts.length; i++)
        {
            LinearLayout newsLinearLayout = newsLayouts[i].findViewById(R.id.template_news__LinearLayout);
            if(newsLinearLayout==null) System.out.println(":::null");
            EnhancedWrapContentViewPager viewPager = new EnhancedWrapContentViewPager(this);
            viewPager.setId(newsLinearLayout.getId());
        //if(i==0)
        viewPager.setAdapter(new FeaturedArticle_PagerAdapter(getSupportFragmentManager(),articles[i]));
            *//*else
                {viewPager.setAdapter(new Article_Stacked_PagerAdapter(getSupportFragmentManager(),articles[i],getNumStacked()));
                System.out.println("::: Reached article stacked");}*//*
            viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TabLayout tabLayout = newsLayouts[i].findViewById(R.id.template_news__TabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
        newsLinearLayout.addView(viewPager,viewPager.getLayoutParams());
        }*/

        //TabLayout tabLayout = newsLayouts[1].findViewById(R.id.template_news__TabLayout);
        //tabLayout.setupWithViewPager(viewPager, true);
/*
        View header = findViewById(R.id.home_header_test);
        TextView titleText = header.findViewById(R.id.template_news__TitleText);
        final String titletest = "FEATURED";
        titleText.setText(titletest);

        ImageView titleBar = header.findViewById(R.id.template_news__rounded_bar);
        titleBar.setColorFilter(ContextCompat.getColor(this, R.color.VomitYellow_DDCD3E__HOME));

        String[][] data = new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"ASB NEWS Title2", "summaryText2. This is a long sample summary. This should cut off at two lines, with an ellipsis."},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        Date date = calendar.getTime();

        Article[] articles = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles[i] = new Article(
                    date,
                    data[i][0],
                    data[i][1],
                    "N/A",
                    false);
        }

        EnhancedWrapContentViewPager viewPager = header.findViewById(R.id.template_news__ViewPager);
        viewPager.setAdapter(new FeaturedArticle_PagerAdapter(getSupportFragmentManager(),articles));
        TabLayout tabLayout = header.findViewById(R.id.template_news__TabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
//*
        View header2 = findViewById(R.id.asbNewstest);
        TextView titleText2 = header2.findViewById(R.id.template_news__TitleText);
        titleText2.setText(titles[1]);

        ImageView titleBar2 = header2.findViewById(R.id.template_news__rounded_bar);
        titleBar2.setColorFilter(barColors[1]);

        EnhancedWrapContentViewPager viewPager2 = header2.findViewById(R.id.template_news__ViewPager);
String [][] data = new String[][]
        {
                {"Lorem Ipsum a Very Long Title", "hello world what a nice day! This is the content inside the article."},
                {"ASB NEWS Title2", "summaryText2. This is a long sample summary. This should cut off at two lines, with an ellipsis."},
                {"Title3", "summaryText3. Content inside article, but it will be truncated."},
                {"Title4", "summaryText4"},
                {"Title5", "summaryText5"},
                {"Title6", "summaryText6"},
                {"Title7", "summaryText7"}
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        Date date = calendar.getTime();
        Article[] articles2 = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles2[i] = new Article(
                    date,
                    data[i][0],
                    data[i][1],
                    "N/A",
                    false);
        }

        viewPager2.setAdapter(new Article_Stacked_PagerAdapter(getSupportFragmentManager(),articles2,getNumStacked()));
        TabLayout tabLayout2 = header2.findViewById(R.id.template_news__TabLayout);
        tabLayout2.setupWithViewPager(viewPager2, true);
*/

        // apply scrolling animation to navigation bar

        final FrameLayout navBar= findViewById(R.id.nav_bar_FrameLayout);
        final ScrollView scrollView = findViewById(R.id.home_page__scrollView);
        final int scrollAnimBuffer = 4; // so the animation doesn't repeat on overly slight changes

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

    public Article[] getFeatured_Articles()
    {
        String[][] data = new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"NEWS Title2", "summaryText2. This is a long sample summary. This should cut off at two lines, with an ellipsis."},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        Date date = calendar.getTime();

        Article[] articles = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles[i] = new Article(
                    date,
                    data[i][0],
                    data[i][1],
                    "N/A",
                    false);
        }
        return articles;
    }

    public void getASB_Articles()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        final Date date = calendar.getTime();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("homepage").child("asb");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Article> articles = new ArrayList<>();
                int i = 1;
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    String title = childSnapshot.child("articleTitle").getValue().toString();
                    String summary = childSnapshot.child("articleBody").getValue().toString();
                    articles.add(new Article(date,title,summary,"N/A",false));

                }

                Article[] articles_array = new Article[articles.size()];
                articles.toArray(articles_array);
                News_Template newsFrag = News_Template.newInstanceOf(articles_array, titles[i], barColors[i], false);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(newsLayouts[i].getId(),newsFrag)
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Article[] getSports_Articles()
    {
        String [][] data = new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"Sports News Title2", "summaryText2"},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        Date date = calendar.getTime();
        Article[] articles = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles[i] = new Article(
                    date,
                    data[i][0],
                    data[i][1],
                    "N/A",
                    false);
        }
        return articles;
    }

    public Article[] getDistrict_Articles()
    {
        String [][] data = new String[][]
                {
                        {"Lorem Ipsum a Veryd Long Titled", "hello world what a nice day! This is the content inside the article."},
                        {"Title2f", "summaryText2"},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,5,12,12,7);

        Date date = calendar.getTime();
        Article[] articles = new Article[data.length];
        for(int i = 0; i < articles.length; i++)
        {
            articles[i] = new Article(
                    date,
                    data[i][0],
                    data[i][1],
                    "N/A",
                    false);
        }
        return articles;
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
            view.animate().translationY(0).setDuration(500);
            /*view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);*/
        }

    }


    // slide the view from its current position to below itself
    public void slideDown(View view){
        if(is_nav_bar_up)
        {
            view.animate().translationY(view.getHeight()).setDuration(500);
            /*TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);*/
        }

    }
}