package com.hsappdev.ahs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.Misc.FullScreenActivity;
import com.hsappdev.ahs.Setting_Activities.Settings_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class News_Activity extends FullScreenActivity implements Navigation, NotifBtn.Navigation, ArticleNavigation{

    /*private static final String TAG = "News";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Resources r = getResources();
        final String [] data_ref = new String[] {
                r.getString(R.string.fb_news_district),
                r.getString(R.string.fb_news_sports),
                r.getString(R.string.fb_news_asb),
        };
        final String
                articleAuthor = r.getString(R.string.fb_art_author),
                articleTitle = r.getString(R.string.fb_art_title),
                articleBody =  r.getString(R.string.fb_art_body),
                articleImages = r.getString(R.string.fb_art_images),
                articleVideos = r.getString(R.string.fb_art_videos),
                articleTime = r.getString(R.string.fb_art_time),
                articleFeatured = r.getString(R.string.fb_art_featured);

        final String notif_articleID = getIntent().getStringExtra(getResources().getString(R.string.notif_articleID_key));
        if(notif_articleID != null) {
            final Article article = ArticleDatabase.getInstance(getApplicationContext()).getArticleById(notif_articleID);
            if(article != null) {
                Intent intent = new Intent(News_Activity.this, ArticleActivity.class);
                intent.putExtra(ArticleActivity.data_key, article);
                startActivity(intent);
                return;
            }
            Bulletin_Article bulletin_article = BulletinDatabase.getInstance(getApplicationContext()).getArticleByID(notif_articleID);
            if(bulletin_article != null) {
                Intent intent = new Intent(News_Activity.this, Bulletin_Article_Activity.class);
                intent.putExtra(Bulletin_Article_Activity.data_KEY, bulletin_article);
                startActivity(intent);
                return;
            }

            final String[] categories = new String[]
                    {
                            r.getString(R.string.fb_bull_academics),
                            r.getString(R.string.fb_bull_athletics),
                            r.getString(R.string.fb_bull_clubs),
                            r.getString(R.string.fb_bull_colleges),
                            r.getString(R.string.fb_bull_reference),
                    };
            FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_bull_key)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(int i = 0; i < categories.length; i++) {
                        DataSnapshot innerSnap = snapshot.child(categories[i]);
                        for (DataSnapshot dataSnapshot : innerSnap.getChildren()) {
                            String ID = dataSnapshot.getKey();
                            if(ID == null)
                                continue;
                            if(ID.equals(notif_articleID)) {
                                String title = dataSnapshot.child(r.getString(R.string.fb_bull_artTitle)).getValue(String.class);
                                if(title == null)
                                    title = "";
                                String body = dataSnapshot.child(r.getString(R.string.fb_bull_artBody)).getValue(String.class);
                                if(body == null)
                                    body = "";
                                Long time_holder = dataSnapshot.child(r.getString(R.string.fb_bull_artTime)).getValue(long.class);
                                long time = (time_holder != null) ? time_holder : 0;
                                Bulletin_Article bulletin_article = new Bulletin_Article(ID, time, title, body, Bulletin_Article.Type.values()[i],
                                        BulletinDatabase.getInstance(getApplicationContext()).getReadStatusByID(ID)
                                );
                                Intent intent = new Intent(News_Activity.this, Bulletin_Article_Activity.class);
                                intent.putExtra(Bulletin_Article_Activity.data_KEY, bulletin_article);
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(int i = 0; i < data_ref.length; i++) {
                        DataSnapshot innerSnap = snapshot.child(data_ref[i]);
                        // use ArrayList b/c no idea how many articles
                        for (DataSnapshot child_sn: innerSnap.getChildren()) { // each individual article layer

                            String ID = child_sn.getKey();
                            if(ID == null)
                                continue;
                            if(ID.equals(notif_articleID)) {
                                String author = child_sn.child(articleAuthor).getValue(String.class);
                                if(author == null)
                                    author = "";
                                String title = child_sn.child(articleTitle).getValue(String.class);
                                if(title == null)
                                    title = "";
                                String body = child_sn.child(articleBody).getValue(String.class);
                                if(body == null)
                                    body = "";
                                // so html parse works correctly with new line characters
                                body = body.replace("\n","<br/>");
                        /*// cover all weird cases
                        body = body.replace("\\n","<br/>");*/

                                ArrayList<String> imagePathsList = new ArrayList<>();
                                for(DataSnapshot images_sn: child_sn.child(articleImages).getChildren())
                                {
                                    String imagePath = images_sn.getValue(String.class);
                                    if(imagePath != null)
                                        imagePathsList.add(imagePath);
                                }
                                String[] imagePaths = new String[imagePathsList.size()];
                                // convert Arraylist to array, it's faster since no more modification is needed
                                imagePathsList.toArray(imagePaths);

                                ArrayList<String> videoIDsList = new ArrayList<>();
                                for(DataSnapshot sn: child_sn.child(articleVideos).getChildren())
                                {
                                    String videoID = sn.getValue(String.class);
                                    if(videoID != null)
                                        videoIDsList.add(videoID);
                                }
                                String[] videoIDs = new String[videoIDsList.size()];
                                videoIDsList.toArray(videoIDs);

                                long article_time = (long) child_sn.child(articleTime).getValue();

                                Article article = new Article(ID,article_time,title,author,body,imagePaths,videoIDs, Article.Type.values()[i]);
                                Intent intent = new Intent(News_Activity.this, ArticleActivity.class);
                                intent.putExtra(ArticleActivity.data_key, article);
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        setContentView(R.layout.news_layout);

        final String[] titles = {
                "Featured",
                "General Info",
                "District News",
                "ASB News",
        };

        final RecyclerView recyclerView = findViewById(R.id.news_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

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
                        if(ID == null)
                            continue;

                        String title = child_sn.child(articleTitle).getValue(String.class);
                        if(title == null)
                            title = "";

                        String body = child_sn.child(articleBody).getValue(String.class);
                        if(body == null)
                            body = "";

                        String imagePath = null;
                        for (DataSnapshot images_sn : child_sn.child(articleImages).getChildren()) {
                            imagePath = (images_sn.getValue(String.class));
                            break;
                        }
                        if(imagePath == null)
                            imagePath = "";

                        long article_time = (long) child_sn.child(articleTime).getValue();
                        boolean is_featured = (boolean) child_sn.child(articleFeatured).getValue();

                        Article_Slim article = new Article_Slim(ID, article_time, title, body, imagePath, Article.Type.values()[i]);
                        articles_categorized.add(article);

                        if (is_featured) {
                            featuredArts.add(article);
                        }

                    }
                    Collections.sort(articles_categorized);
                    adapter.articles.get(i).addAll(articles_categorized);
                    adapter.notifyItemChanged(i + 1);
                }
                Collections.sort(featuredArts);
                adapter.featuredArticles.addAll(featuredArts);
                adapter.notifyItemChanged(0);

                new Runnable(){
                    @Override
                    public void run() {
                        final ArrayList<Article> allArticles = new ArrayList<>();
                        for(int i = 0; i < data_ref.length; i++) {
                            DataSnapshot innerSnap = snapshot.child(data_ref[i]); // asb news, district, etc. layer

                            // use ArrayList b/c no idea how many articles
                            for (DataSnapshot child_sn: innerSnap.getChildren()) { // each individual article layer

                                String ID = child_sn.getKey();
                                if(ID == null)
                                    continue;
                                String author = child_sn.child(articleAuthor).getValue(String.class);
                                if(author == null)
                                    author = "";
                                String title = child_sn.child(articleTitle).getValue(String.class);
                                if(title == null)
                                    title = "";
                                String body = child_sn.child(articleBody).getValue(String.class);
                                if(body == null)
                                    body = "";
                                // so html parse works correctly with new line characters
                                body = body.replace("\n","<br/>");
                        /*// cover all weird cases
                        body = body.replace("\\n","<br/>");*/

                                ArrayList<String> imagePathsList = new ArrayList<>();
                                for(DataSnapshot images_sn: child_sn.child(articleImages).getChildren())
                                {
                                    String imagePath = images_sn.getValue(String.class);
                                    if(imagePath != null)
                                        imagePathsList.add(imagePath);
                                }
                                String[] imagePaths = new String[imagePathsList.size()];
                                // convert Arraylist to array, it's faster since no more modification is needed
                                imagePathsList.toArray(imagePaths);

                                ArrayList<String> videoIDsList = new ArrayList<>();
                                for(DataSnapshot sn: child_sn.child(articleVideos).getChildren())
                                {
                                    String videoID = sn.getValue(String.class);
                                    if(videoID != null)
                                        videoIDsList.add(videoID);
                                }
                                String[] videoIDs = new String[videoIDsList.size()];
                                videoIDsList.toArray(videoIDs);

                                long article_time = (long) child_sn.child(articleTime).getValue();

                                Article article = new Article(ID,article_time,title,author,body,imagePaths,videoIDs, Article.Type.values()[i]);

                                allArticles.add(article);
                            }
                        }
                        ArticleDatabase.getInstance(getApplicationContext()).updateArticles(allArticles.toArray(new Article[0]));
                    }
                }.run();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /*Log.i(TAG, error.getDetails());*/
            }
        });


        new Handler().post(new Runnable() {
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
        overridePendingTransition(R.anim.from_right, R.anim.maintain);

    }
}