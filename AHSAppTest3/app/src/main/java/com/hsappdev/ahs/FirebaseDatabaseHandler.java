package com.hsappdev.ahs;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDatabaseHandler {

    private Context context;

    public FirebaseDatabaseHandler(Context context) {
        this.context = context;
    }

    public interface NewsArticleCallback {
        void onDataLoaded();
        void onArticleLoaded(Article article);
        void onFeaturedArticlesLoaded(ArrayList<Article> articles);
        void onCategoryArticlesLoaded(int position, ArrayList<Article> articles);
        void onAllArticlesLoaded(ArrayList<Article> articles);
        void onDatabaseError(@NonNull DatabaseError error);
    }

    public void getNewsArticles(final NewsArticleCallback callback) {
        final Resources r = context.getResources();

        final String [] data_ref = new String[] {
                r.getString(R.string.fb_news_general_info),
                r.getString(R.string.fb_news_district),
                r.getString(R.string.fb_news_asb),
        };
        final Article.Type[] categories = Article.Type.values();

        final String
                articleAuthor = r.getString(R.string.fb_art_author),
                articleTitle = r.getString(R.string.fb_art_title),
                articleBody =  r.getString(R.string.fb_art_body),
                articleImages = r.getString(R.string.fb_art_images),
                articleVideos = r.getString(R.string.fb_art_videos),
                articleTime = r.getString(R.string.fb_art_time),
                articleFeatured = r.getString(R.string.fb_art_featured);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                callback.onDataLoaded();
                final ArrayList<Article> allArticles = new ArrayList<>();
                ArrayList<Article> featuredArticles = new ArrayList<>();
                for(int i = 0; i < data_ref.length; i++) {
                    DataSnapshot innerSnap = snapshot.child(data_ref[i]);

                    // use ArrayList b/c no idea how many articles
                    ArrayList<Article> articles = new ArrayList<>();
                    for (DataSnapshot child_sn : innerSnap.getChildren()) {
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
                        boolean is_featured = (boolean) child_sn.child(articleFeatured).getValue();

                        if(is_featured)
                            featuredArticles.add(article);

                        articles.add(article);
                        allArticles.add(article);
                        callback.onArticleLoaded(article);
                    }
                    callback.onCategoryArticlesLoaded(i, articles);
                }
                callback.onFeaturedArticlesLoaded(featuredArticles);
                callback.onAllArticlesLoaded(allArticles);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onDatabaseError(error);
            }
        });
    }

    public interface BulletinArticleCallback {
        void onDataLoaded();
        void onArticleLoaded(Bulletin_Article article);
        void onAllArticlesLoaded(ArrayList<Bulletin_Article> articles);
        void onDatabaseError(@NonNull DatabaseError error);
    }

    public void getBulletinArticles(final BulletinArticleCallback callback) {
        final Bulletin_Article.Type[] types = Bulletin_Article.Type.values();
        final Resources r = context.getResources();
        final String[] categories = new String[]
                {
                        r.getString(R.string.fb_bull_academics),
                        r.getString(R.string.fb_bull_athletics),
                        r.getString(R.string.fb_bull_clubs),
                        r.getString(R.string.fb_bull_colleges),
                        r.getString(R.string.fb_bull_reference),
                };
        final String articleTitle = r.getString(R.string.fb_bull_artTitle),
                articleBody = r.getString(R.string.fb_bull_artBody),
                articleTime = r.getString(R.string.fb_bull_artTime);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_bull_key));
        final BulletinDatabase db = BulletinDatabase.getInstance(context);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onDataLoaded();
                final ArrayList<Bulletin_Article> data = new ArrayList<>();
                for(int i = 0; i < categories.length; i++)
                {
                    DataSnapshot snapshot1 = snapshot.child(categories[i]);

                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                        String ID = dataSnapshot.getKey();
                        if(ID == null)
                            continue;
                        String title = dataSnapshot.child(articleTitle).getValue(String.class);
                        if(title == null)
                            title = "";
                        String body = dataSnapshot.child(articleBody).getValue(String.class);
                        if(body == null)
                            body = "";
                        Long time_holder = dataSnapshot.child(articleTime).getValue(long.class);
                        long time = (time_holder != null) ? time_holder : 0;
                        Bulletin_Article info = new Bulletin_Article(ID, time, title, body, types[i],
                                db.getReadStatusByID(ID)
                        );

                        callback.onArticleLoaded(info);
                        data.add(info);

                    }
                }
                callback.onAllArticlesLoaded(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onDatabaseError(error);
            }
        });
    }
}
