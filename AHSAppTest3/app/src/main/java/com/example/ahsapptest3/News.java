package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Helper_Code.ValContainer;
import com.example.ahsapptest3.HomePage_News.News_Template;
import com.example.ahsapptest3.Setting_Activities.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class News extends FullScreenActivity implements Navigation, NotifBtn.Navigation{
    /*
    * TODO: Ask about new articles
    *  Update Saved page to support bookmarks
    * Update settings general notifications to actually work
    * Update settings font slider to actually work
    * Update settings pages ui
    * */
    /**
     * Be extremely!!! careful when changing these types! May cause problems with type conversion in ArticleDatabase
     * Also ArticleActivity since it uses toString() of type rather than having a conversion table
     * Need to be careful even when refactoring
     */
    public enum TYPE {
        ASB, SPORTS, DISTRICT
    }

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_layout);

        final String[] titles = {
                "ASB News",
                "Sports News",
                "District News",
                "Featured",
        };

        final View[] newsLayouts = new View[] {

                findViewById(R.id.home_asbNews_placeholder),
                findViewById(R.id.home_sportsNews_placeholder),
                findViewById(R.id.home_districtNews_placeholder),
                findViewById(R.id.home_featuredNews_placeholder),

        };

        final String [] data_ref = new String[] {
                "asb",
                "sports",
                "district",
        };

        final ArrayList<Article> allArticles = new ArrayList<>();
        final ArrayList<Article> featured_articles = new ArrayList<>();
        final Context context = this;
        final ArticleDatabase currentDatabase = ArticleDatabase.getInstance(context, ArticleDatabase.Option.CURRENT);

        /*for(Article article: currentDatabase.getAllArticles())
            Log.d(TAG, article.toString());*/

        final ValContainer<Boolean> firstTime = new ValContainer<>();
        firstTime.setVal(true);

        for(int i = 0; i < data_ref.length; i++)
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("homepage").child(data_ref[i]);

            final int finalI = i; // necessary cause inner class

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /*if(firstTime.getVal())
                    {
                        currentDatabase.deleteAll();
                        firstTime.setVal(false);
                    }*/

                    // use ArrayList b/c no idea how many articles
                    ArrayList<Article> articles = new ArrayList<>();

                    for (DataSnapshot child_sn: snapshot.getChildren()) {

                        String ID = child_sn.getKey();

                        String author = child_sn.child("articleAuthor").getValue().toString();
                        String title = child_sn.child("articleTitle").getValue().toString();
                        String body = child_sn.child("articleBody").getValue().toString();
                        // so html parse works correctly with new line characters
                        body = body.replace("\n","<br/>");
                        // cover all weird cases
                        body = body.replace("\\n","<br/>");

                        ArrayList<String> imagePathsList = new ArrayList<>();
                        for(DataSnapshot images_sn: child_sn.child("articleImages").getChildren())
                        {
                            imagePathsList.add(images_sn.getValue().toString());
                        }
                        String[] imagePaths = new String[imagePathsList.size()];
                        // convert Arraylist to array, it's faster since no more modification is needed
                        imagePathsList.toArray(imagePaths);

                        ArrayList<String> videoIDsList = new ArrayList<>();
                        for(DataSnapshot sn: child_sn.child("articleVideoIDs").getChildren())
                        {
                            videoIDsList.add(sn.getValue().toString());
                        }
                        String[] videoIDs = new String[videoIDsList.size()];
                        videoIDsList.toArray(videoIDs);

                        long article_time = (long) child_sn.child("articleUnixEpoch").getValue();
                        boolean is_featured = (boolean) child_sn.child("isFeatured").getValue();

                        // check if already bookmarked
                        ArticleDatabase bookMarkDatabase = ArticleDatabase.getInstance(context, ArticleDatabase.Option.BOOKMARK);
                        boolean is_bookmarked = bookMarkDatabase.alreadyAdded(ID);

                        Article article = new Article(ID,article_time,title,author,body,imagePaths,videoIDs,is_bookmarked,false, TYPE.values()[finalI]);

                        // add Article to ArrayList
                        articles.add(article); // default values for bookmark and notified


                        // add Article to current article database
                        if(!currentDatabase.alreadyAdded(ID))
                            currentDatabase.add(article);

                        if(is_featured)
                            featured_articles.add(article);

                    }

                    // convert ArrayList to array, it's faster since no more modification is needed
                    Article[] articles_array = new Article[articles.size()];
                    articles.toArray(articles_array);

                    // initialize the fragment
                    final News_Template newsFrag = News_Template.newInstanceOf(articles_array, titles[finalI], false);

                    // add the fragment to the view
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                                    .replace(newsLayouts[finalI].getId(),newsFrag)
                                    .commitAllowingStateLoss();
                        }
                    });


                    final int i = 3;
                    // handle featured articles
                    // initialize the fragment
                    Article[] featured_array = new Article[featured_articles.size()];
                    final News_Template featuredFrag = News_Template.newInstanceOf(featured_articles.toArray(featured_array), titles[i], true);

                    // add the fragment to the view
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                                    .replace(newsLayouts[i].getId(),featuredFrag)
                                    .commitAllowingStateLoss();
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i(TAG, error.getDetails());
                }
            });
        }

    }


    @Override
    public void goToHome() {

    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(News.this, Bulletin.class);
        News.this.startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(News.this, Saved.class);
        News.this.startActivity(myIntent);
    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(News.this, Settings.class);
        News.this.startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.home_page__scrollView;
    }


    @Override
    public void goToNotif() {
        Intent myIntent = new Intent(News.this, Notif_Activity.class);
        News.this.startActivity(myIntent);
    }
}