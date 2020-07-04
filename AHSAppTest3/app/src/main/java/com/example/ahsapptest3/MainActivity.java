package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.HomePage_News.News_Template;
import com.example.ahsapptest3.Settings.SettingsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Navigation{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        final Context context = this;

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

                        // check if already bookmarked
                        BookmarkHandler bookmarkHandler = new BookmarkHandler(context);
                        boolean is_bookmarked = bookmarkHandler.alreadyBookmarked(ID);

                        Article article = new Article(ID,article_time,title,author,body,imagePaths,is_bookmarked,false);

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
                    System.out.println(error.getDetails());
                }
            });
        }


    }


    @Override
    public void goToHome() {

    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(MainActivity.this,BulletinActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(MainActivity.this,SavedActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.home_page__scrollView;
    }
}