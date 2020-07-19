package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahsapptest3.Helper_Code.Helper;
import com.example.ahsapptest3.Settings.SettingsActivity;


import java.util.ArrayList;

public class Saved extends AppCompatActivity implements Navigation{

    private static final String TAG = "SavedActivity";
    private ArticleDatabase articleDatabase;
    private ArticleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_layout);

        RecyclerView recyclerView = findViewById(R.id.saved_recyclerView);
        articleDatabase = new ArticleDatabase(this, ArticleDatabase.Option.BOOKMARK);

        adapter = new ArticleRecyclerAdapter(this, articleDatabase.getAllArticles());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(ArticleDatabase.hasBookmarksChanged())
            updateBookmarkIcons();
    }

    public void updateBookmarkIcons()
    {
        ArticleDatabase articleDatabase = new ArticleDatabase(this, ArticleDatabase.Option.BOOKMARK);

        ArrayList<Article> articles = adapter.articles;
        for(int i = articles.size()-1; i >= 0; i--) // backwards loop cause remove
        {
            if(!articleDatabase.alreadyAdded(articles.get(i).getID()))
                articles.remove(i);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(Saved.this, News.class);
        Saved.this.startActivity(myIntent);
    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(Saved.this, Bulletin.class);
        Saved.this.startActivity(myIntent);
    }

    @Override
    public void goToSaved() {

    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(Saved.this, SettingsActivity.class);
        Saved.this.startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.saved_recyclerView;
    }

    public static class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>
    {
        private static final String TAG = "ArticleRecyclerAdapter";

        public ArrayList<Article> articles; // ArrayList instead of array in case we want to add or remove stuff (which will happen!)
        private android.content.Context context;

        public ArticleRecyclerAdapter(android.content.Context context, ArrayList<Article> articles) {
            this.articles = articles;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_stacked_display, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            // change this later, just for sample


            LinearLayout parentLayout = holder.parentLayout;
            ViewStub stub = new ViewStub(context);
            stub.setLayoutResource(R.layout.template__article_display);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            parentLayout.addView(stub, params);
            
            View inflated = stub.inflate();

            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
            inflated.setBackgroundResource(outValue.resourceId);

            final Article article = articles.get(position);
            
            Helper.setArticleListener_toView(inflated, article);

            Helper.setTimeText_toView((TextView) inflated.findViewById(R.id.article_display__time_updated_Text),
                    Helper.TimeFromNow(article.getTimeUpdated())
            );

            // set title
            Helper.setText_toView((TextView) inflated.findViewById(R.id.article_display__title_Text),article.getTitle());

            // set summary/description
            Helper.setText_toView((TextView) inflated.findViewById(R.id.article_display__summary_Text), article.getStory());

            // setImage
            Helper.setImage_toView_fromUrl((ImageView) inflated.findViewById(R.id.article_display__imageView),article.getImagePaths()[0]);

            // set bookmarked button state
            final ImageButton bookmarkButton = inflated.findViewById(R.id.article_display__bookmarked_button);

            Helper.setBookmarked_toView(bookmarkButton,article.isBookmarked());
            Helper.setBookMarkListener_toView(bookmarkButton, article);

            // must use custom method rather than using Helper as we need to notifyDataSetChanged()
            final ArticleDatabase articleDatabase = new ArticleDatabase(this.context, ArticleDatabase.Option.BOOKMARK);

            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    article.swapBookmark();
                    Helper.setBookmarked_toView(bookmarkButton,article.isBookmarked());
                    if(!articleDatabase.alreadyAdded(article.getID()))
                        articleDatabase.add(article);
                    else
                        {
                            articleDatabase.delete(article);
                        articles.remove(position);
                        notifyItemRemoved(position);
                        ArticleDatabase.setBookmarkChanged();}
                }
            });
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public LinearLayout parentLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout = itemView.findViewById(R.id.article_stacked_LinearLayout);
            }
        }

    }
}
