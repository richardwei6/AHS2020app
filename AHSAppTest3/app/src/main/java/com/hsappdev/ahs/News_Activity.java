package com.hsappdev.ahs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hsappdev.ahs.Misc.FullScreenActivity;
import com.hsappdev.ahs.Misc.ValContainer;
import com.hsappdev.ahs.Setting_Activities.Settings_Activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class News_Activity extends FullScreenActivity implements Navigation, NotifBtn.Navigation, ArticleNavigation{

    /*private static final String TAG = "News";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);

        final Resources r = getResources();

        final ValContainer<Boolean> notifArticleFound = new ValContainer<>(false);

        final String notif_articleID = getIntent().getStringExtra(getResources().getString(R.string.notif_articleID_key));
        if(notif_articleID != null) {
            final Article article = ArticleDatabase.getInstance(getApplicationContext()).getArticleById(notif_articleID);
            if(article != null) {
                notifArticleFound.setVal(true);
                Intent intent = new Intent(News_Activity.this, ArticleActivity.class);
                intent.putExtra(ArticleActivity.data_key, article);
                startActivity(intent);
            }
            if(!notifArticleFound.getVal()) {
                Bulletin_Article bulletin_article = BulletinDatabase.getInstance(getApplicationContext()).getArticleByID(notif_articleID);
                if(bulletin_article != null) {
                    notifArticleFound.setVal(true);
                    Intent intent = new Intent(News_Activity.this, Bulletin_Article_Activity.class);
                    intent.putExtra(Bulletin_Article_Activity.data_KEY, bulletin_article);
                    startActivity(intent);
                }
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("testing").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "testing subbed", Toast.LENGTH_SHORT).show();
            }
        });

        final String[] titles = {
                r.getString(R.string.news_featured_name),
                r.getString(R.string.news_generalInfo_name),
                r.getString(R.string.news_districtNews_name),
                r.getString(R.string.news_asbNews_name),
        };

        final RecyclerView recyclerView = findViewById(R.id.news_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        final NewsRecyclerAdapter adapter = new NewsRecyclerAdapter(titles, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseDatabaseHandler handler = new FirebaseDatabaseHandler(getApplicationContext());

        if(notif_articleID != null && !notifArticleFound.getVal()) {
            final byte NOT_FOUND = -1, LOADING = 0, FOUND = 1;
            final ValContainer<Byte> news_found = new ValContainer<>(LOADING),
                    bulletin_found = new ValContainer<>(LOADING);
            handler.getNewsArticles(new FirebaseDatabaseHandler.NewsArticleCallback() {
                @Override
                public void onDataLoaded() {

                }

                @Override
                public void onArticleLoaded(Article article) {
                    if(article.getID().equals(notif_articleID)) {
                        Log.d("frames", "FOUND!");
                        news_found.setVal(FOUND);
                        Intent intent = new Intent(News_Activity.this, ArticleActivity.class);
                        intent.putExtra(Bulletin_Article_Activity.data_KEY, article);
                        Log.d("frames", "start Activity");
                        startActivity(intent);
                    }
                }

                @Override
                public void onFeaturedArticlesLoaded(ArrayList<Article> articles) {
                    NewsRecyclerAdapter.FeaturedViewHolder featuredViewHolder = (NewsRecyclerAdapter.FeaturedViewHolder) recyclerView.findViewHolderForAdapterPosition(0);
                    if(featuredViewHolder != null) {
                        featuredViewHolder.clearAll();
                        featuredViewHolder.addArticles(Article_Slim.toArticle_Slim(articles));
                    }
                }

                @Override
                public void onCategoryArticlesLoaded(int position, ArrayList<Article> articles) {
                    NewsRecyclerAdapter.CategoryViewHolder categoryViewHolder = (NewsRecyclerAdapter.CategoryViewHolder)
                            recyclerView.findViewHolderForAdapterPosition(position+1);
                    if(categoryViewHolder != null) {
                        categoryViewHolder.clearAll();
                        categoryViewHolder.addArticles(Article_Slim.toArticle_Slim(articles));
                    }
                }

                @Override
                public void onAllArticlesLoaded(final ArrayList<Article> articles) {
                /*new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                    }
                }, 5000);*/

                    if(bulletin_found.getVal() == NOT_FOUND && news_found.getVal() != FOUND) {
                        startActivity(new Intent(News_Activity.this, Notif_Activity.class));
                        Log.d("frames", "start Activity");
                    }
                    new Runnable() {
                        @Override
                        public void run() {

                            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            Log.d("frames", "saving news");
                            ArticleDatabase.getInstance(getApplicationContext()).updateArticles(articles);
                        }
                    }.run();

                }

                @Override
                public void onDatabaseError(@NonNull DatabaseError error) {

                }
            });

            handler.getBulletinArticles(new FirebaseDatabaseHandler.BulletinArticleCallback() {
                @Override
                public void onDataLoaded() {

                }

                @Override
                public void onArticleLoaded(Bulletin_Article article) {
                    if(article.getID().equals(notif_articleID)) {
                        Log.d("frames", "FOUND!");
                        bulletin_found.setVal(FOUND);
                        Intent intent = new Intent(News_Activity.this, Bulletin_Article_Activity.class);
                        intent.putExtra(Bulletin_Article_Activity.data_KEY, article);
                        Log.d("frames", "start Activity");
                        startActivity(intent);
                    }

                }

                @Override
                public void onAllArticlesLoaded(final ArrayList<Bulletin_Article> articles) {

                    if(news_found.getVal() == NOT_FOUND && bulletin_found.getVal() != FOUND) {
                        startActivity(new Intent(News_Activity.this, Notif_Activity.class));
                        Log.d("frames", "start Activity");
                    }
                    new Runnable() {
                        @Override
                        public void run() {

                            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            Log.d("frames", "saving news");
                            BulletinDatabase.getInstance(getApplicationContext()).updateArticles(articles);
                        }
                    }.run();

                }

                @Override
                public void onDatabaseError(@NonNull DatabaseError error) {

                }
            });
        } else {
            handler.getNewsArticles(new FirebaseDatabaseHandler.NewsArticleCallback() {
                @Override
                public void onDataLoaded() {

                }

                @Override
                public void onArticleLoaded(Article article) {

                }

                @Override
                public void onFeaturedArticlesLoaded(ArrayList<Article> articles) {
                    NewsRecyclerAdapter.FeaturedViewHolder featuredViewHolder = (NewsRecyclerAdapter.FeaturedViewHolder) recyclerView.findViewHolderForAdapterPosition(0);
                    if(featuredViewHolder != null) {
                        featuredViewHolder.clearAll();
                        featuredViewHolder.addArticles(Article_Slim.toArticle_Slim(articles));
                    }
                }

                @Override
                public void onCategoryArticlesLoaded(int position, ArrayList<Article> articles) {
                    NewsRecyclerAdapter.CategoryViewHolder categoryViewHolder = (NewsRecyclerAdapter.CategoryViewHolder)
                            recyclerView.findViewHolderForAdapterPosition(position+1);
                    if(categoryViewHolder != null) {
                        categoryViewHolder.clearAll();
                        categoryViewHolder.addArticles(Article_Slim.toArticle_Slim(articles));
                    }
                }

                @Override
                public void onAllArticlesLoaded(final ArrayList<Article> articles) {
                /*new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                    }
                }, 5000);*/
                    new Runnable() {
                        @Override
                        public void run() {

                            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            Log.d("frames", "saving news");
                            ArticleDatabase.getInstance(getApplicationContext()).updateArticles(articles);
                        }
                    }.run();

                }

                @Override
                public void onDatabaseError(@NonNull DatabaseError error) {

                }
            });
            handler.getBulletinArticles(new FirebaseDatabaseHandler.BulletinArticleCallback() {
                @Override
                public void onDataLoaded() {

                }

                @Override
                public void onArticleLoaded(Bulletin_Article article) {

                }

                @Override
                public void onAllArticlesLoaded(final ArrayList<Bulletin_Article> articles) {
                    new Runnable() {
                        @Override
                        public void run() {

                            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            Log.d("frames", "saving bulletin news");
                            BulletinDatabase.getInstance(getApplicationContext()).updateArticles(articles);
                        }
                    }.run();

                }

                @Override
                public void onDatabaseError(@NonNull DatabaseError error) {

                }
            });
        }

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