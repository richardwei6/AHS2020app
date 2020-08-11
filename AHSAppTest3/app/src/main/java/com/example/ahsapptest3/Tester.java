package com.example.ahsapptest3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Setting_Activities.Settings_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tester extends FullScreenActivity implements Navigation, NotifBtn.Navigation, ArticleNavigation{
    /*
    * TODO:
        *  ask about error image and small icon
        DONE: Update terms and agreements page
        fix subscribe all button on notif page
        *  fix home and bulletin page
        New icon on bulletin
        About us
        *  Use recyclerview for faster loading new page
        Change enums in Settings for NotifOption
        *  remove tester activity
        *  look into code minification
        *  find out how to update the app
        emoji support?
        stop toasts
    *   drag and zoom image?
    * */

    private static final String TAG = "News";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                createNotificationChannel();
                Settings settings = new Settings(getApplicationContext());
                settings.resubscribeToAll();
            }
        });

        final String[] titles = {
                "ASB News",
                "Sports News",
                "District News",
                "Featured",
        };

        final Resources r = getResources();
        final String [] data_ref = new String[] {
                r.getString(R.string.fb_news_asb),
                r.getString(R.string.fb_news_sports),
                r.getString(R.string.fb_news_district),
        };

        final RecyclerView recyclerView = findViewById(R.id.news_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        final ArrayList<Article> allArticles = new ArrayList<>();
        final ArrayList<Article> featured_articles = new ArrayList<>();
        final ArrayList<ArrayList<Article>> articles_categorized = new ArrayList<>();
        final Context context = this;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ArticleDatabase currentDatabase = ArticleDatabase.getInstance(context);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        featured_articles.clear(); // so stuff doesn't keep adding should the page refresh while the user is on the page
                        allArticles.clear();

                        for(int i = 0; i < data_ref.length; i++) {
                            DataSnapshot innerSnap = snapshot.child(data_ref[i]);

                            // use ArrayList b/c no idea how many articles
                            ArrayList<Article> articles = new ArrayList<>();
                            for (DataSnapshot child_sn: innerSnap.getChildren()) {

                                String ID = child_sn.getKey();
                                String author = child_sn.child(r.getString(R.string.fb_art_author)).getValue().toString();
                                String title = child_sn.child(r.getString(R.string.fb_art_title)).getValue().toString();
                                String body = child_sn.child(r.getString(R.string.fb_art_body)).getValue().toString();
                                // so html parse works correctly with new line characters
                                body = body.replace("\n","<br/>");
                                /*// cover all weird cases
                                body = body.replace("\\n","<br/>");*/

                                ArrayList<String> imagePathsList = new ArrayList<>();
                                for(DataSnapshot images_sn: child_sn.child(r.getString(R.string.fb_art_images)).getChildren())
                                {
                                    imagePathsList.add(images_sn.getValue().toString());
                                }
                                String[] imagePaths = new String[imagePathsList.size()];
                                // convert Arraylist to array, it's faster since no more modification is needed
                                imagePathsList.toArray(imagePaths);

                                ArrayList<String> videoIDsList = new ArrayList<>();
                                for(DataSnapshot sn: child_sn.child(r.getString(R.string.fb_art_videos)).getChildren())
                                {
                                    videoIDsList.add(sn.getValue().toString());
                                }
                                String[] videoIDs = new String[videoIDsList.size()];
                                videoIDsList.toArray(videoIDs);

                                long article_time = (long) child_sn.child(r.getString(R.string.fb_art_time)).getValue();
                                boolean is_featured = (boolean) child_sn.child(r.getString(R.string.fb_art_featured)).getValue();

                                Article article = new Article(ID,article_time,title,author,body,imagePaths,videoIDs, Article.Type.values()[i]);

                                // add Article to ArrayList
                                articles.add(article); // default values for bookmark and notified

                                if(is_featured)
                                    featured_articles.add(article);
                                allArticles.add(article);

                            }
                            articles_categorized.add(articles);

                        }
                        NewsRecyclerAdapter_Test adapter = new NewsRecyclerAdapter_Test(fragmentManager, featured_articles, articles_categorized);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.scheduleLayoutAnimation();

                        new Runnable(){
                            @Override
                            public void run() {
                                currentDatabase.updateArticles(allArticles.toArray(new Article[0]));
                            }
                        }.run();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG, error.getDetails());
                    }
                });
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notif_channel_name);
            String description = getString(R.string.notif_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.notif_channel_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void goToHome() {

    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(Tester.this, Bulletin_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(Tester.this, Saved_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(Tester.this, Settings_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.home_page__scrollView;
    }

    @Override
    public HighlightOption getHighlightOption() {
        return HighlightOption.HOME;
    }

    @Override
    public void goToNotif() {
        Intent myIntent = new Intent(Tester.this, Notif_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void onItemClicked(Article article) {

    }
/*
    @Override
    public void onClick(@NonNull Article data) {

    }*/
}