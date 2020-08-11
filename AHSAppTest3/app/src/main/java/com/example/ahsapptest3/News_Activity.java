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
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class News_Activity extends FullScreenActivity implements Navigation, NotifBtn.Navigation, ArticleNavigation{
    /*
    * TODO:
        *  ask about error image and small icon
        * remove all logs
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
        *  reduce lag by blanket getting resource strings here in newsactivity
        interface News Activity
        * receive article Id data in notifications
        news section titles
        maybe default screen?
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


        final String[] titles = {
                "Featured",
                "ASB News",
                "District News",
                "General Info",

        };

        final Resources r = getResources();
        final String [] data_ref = new String[] {
                r.getString(R.string.fb_news_asb),
                r.getString(R.string.fb_news_sports),
                r.getString(R.string.fb_news_district),
        };


        final RecyclerView recyclerView = findViewById(R.id.news_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        final NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(getSupportFragmentManager(), titles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                adapter.articles.clear();
                adapter.featuredArticles.clear();
                ArrayList<Article_Slim> featuredArts = new ArrayList<>();
                for(int i = 0; i < data_ref.length; i++) {
                    ArrayList<Article_Slim> articles_categorized = new ArrayList<>();
                    DataSnapshot innerSnap = snapshot.child(data_ref[i]);
                    adapter.articles.add(new ArrayList<Article_Slim>());
                    // use ArrayList b/c no idea how many articles
                    for (DataSnapshot child_sn : innerSnap.getChildren()) {

                        String ID = child_sn.getKey();
                        String title = child_sn.child(r.getString(R.string.fb_art_title)).getValue().toString();
                        String body = child_sn.child(r.getString(R.string.fb_art_body)).getValue().toString();

                        String imagePath = null;
                        for (DataSnapshot images_sn : child_sn.child(r.getString(R.string.fb_art_images)).getChildren()) {
                            imagePath = (images_sn.getValue().toString());
                            break;
                        }

                        long article_time = (long) child_sn.child(r.getString(R.string.fb_art_time)).getValue();
                        boolean is_featured = (boolean) child_sn.child(r.getString(R.string.fb_art_featured)).getValue();

                        Article_Slim article = new Article_Slim(ID, article_time, title, body, imagePath, Article.Type.values()[i]);
                        articles_categorized.add(article);

                        if (is_featured) {
                            featuredArts.add(article);
                        }

                    }
                    adapter.articles.get(i).addAll(articles_categorized);
                    adapter.notifyItemChanged(i + 1);
                }
                adapter.featuredArticles.addAll(featuredArts);
                adapter.notifyItemChanged(0);

                new Runnable(){
                    @Override
                    public void run() {
                        final ArrayList<Article> allArticles = new ArrayList<>();
                        for(int i = 0; i < data_ref.length; i++) {
                            DataSnapshot innerSnap = snapshot.child(data_ref[i]); // asb news, district, etc. layer

                            // use ArrayList b/c no idea how many articles
                            ArrayList<Article> articles = new ArrayList<>();
                            for (DataSnapshot child_sn: innerSnap.getChildren()) { // each individual article layer

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

                                Article article = new Article(ID,article_time,title,author,body,imagePaths,videoIDs, Article.Type.values()[i]);

                                // add Article to ArrayList
                                articles.add(article); // default values for bookmark and notified

                                allArticles.add(article);
                            }
                        }
                        ArticleDatabase.getInstance(getApplicationContext()).updateArticles(allArticles.toArray(new Article[0]));
                    }
                }.run();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, error.getDetails());
            }
        });


        handler.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                createNotificationChannel();
                Settings settings = new Settings(getApplicationContext());
                settings.resubscribeToAll();
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
        Intent myIntent = new Intent(News_Activity.this, Bulletin_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(News_Activity.this, Saved_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(News_Activity.this, Settings_Activity.class);
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
        Intent myIntent = new Intent(News_Activity.this, Notif_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void onItemClicked(Article article) {
        Intent intent = new Intent(News_Activity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_key, article);
        startActivity(intent);
    }
/*
    @Override
    public void onClick(@NonNull Article data) {

    }*/
}