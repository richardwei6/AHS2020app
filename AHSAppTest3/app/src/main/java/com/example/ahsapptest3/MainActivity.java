package com.example.ahsapptest3;

import android.content.Intent;
import android.os.Bundle;

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

        final String[] titles = {
                "ASB NEWS",
                "SPORTS NEWS",
                "DISTRICT NEWS",
                "FEATURED",
        };

        final int[] barColors = new int[]{

                ContextCompat.getColor(this, R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF), // asb news
                ContextCompat.getColor(this, R.color.VomitYellow_DDCD3E__HOME), // district news
                ContextCompat.getColor(this, R.color.SeaBlue_364D9E__HOME), // sports news
                ContextCompat.getColor(this, R.color.VomitYellow_DDCD3E__HOME), // featured
        };

        final View[] newsLayouts = new View[] {

                findViewById(R.id.home_asbNews_placeholder),
                findViewById(R.id.home_sportsNews_placeholder),
                findViewById(R.id.home_districtNews_placeholder),
                findViewById(R.id.home_featuredNews_placeholder),

        };

        String [] data_ref = new String[] {
                "asb",
                "district",
                "sports"
        };
        final ArrayList<Article> featured_articles = new ArrayList<>();

        for(int i = 0; i < data_ref.length; i++)
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("homepage").child(data_ref[i]);

            final int finalI = i; // necessary cause inner class

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // use ArrayList b/c no idea how many articles
                    ArrayList<Article> articles = new ArrayList<>();

                    for (DataSnapshot child_sn: snapshot.getChildren()) {
                        int ID = Integer.parseInt(child_sn.child("ID").getValue().toString());
                        String author = child_sn.child("articleAuthor").getValue().toString();
                        String title = child_sn.child("articleTitle").getValue().toString();
                        String body = child_sn.child("articleBody").getValue().toString();

                        ArrayList<String> imagePathsList = new ArrayList<>();
                        for(DataSnapshot images_sn: child_sn.child("articleImages").getChildren())
                        {
                            imagePathsList.add(images_sn.getValue().toString());
                        }
                        String[] imagePaths = new String[imagePathsList.size()];
                        // convert Arraylist to array, it's faster since no more modification is needed
                        imagePathsList.toArray(imagePaths);

                        long article_time = (long) child_sn.child("articleUnixEpoch").getValue();
                        boolean is_featured = (boolean) child_sn.child("isFeatured").getValue();

                        Article article = new Article(ID,article_time,title,author,body,imagePaths,false,false);

                        // add Article to ArrayList
                        articles.add(article); // default values for bookmark and notified

                        if(is_featured)
                            featured_articles.add(article);

                    }

                    // convert ArrayList to array, it's faster since no more modification is needed
                    Article[] articles_array = new Article[articles.size()];
                    articles.toArray(articles_array);

                    // initialize the fragment
                    News_Template newsFrag = News_Template.newInstanceOf(articles_array, titles[finalI], barColors[finalI], false);

                    // add the fragment to the view
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(newsLayouts[finalI].getId(),newsFrag)
                            .commit();

                    int i = 3;
                    // handle featured articles
                    // initialize the fragment
                    Article[] featured_array = new Article[featured_articles.size()];
                    News_Template featuredFrag = News_Template.newInstanceOf(featured_articles.toArray(featured_array), titles[i], barColors[i], true);

                    // add the fragment to the view
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(newsLayouts[i].getId(),featuredFrag)
                            .commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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


    public void goToHome (View view)
    {

    }

    public void goToBulletin (View view)
    {
        Intent myIntent = new Intent(MainActivity.this,BulletinActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goToSaved(View view)
    {
        Intent myIntent = new Intent(MainActivity.this,SavedActivity.class);
        MainActivity.this.startActivity(myIntent);
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