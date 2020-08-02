package com.example.ahsapptest3;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Setting_Activities.Settings;

public class Saved extends FullScreenActivity implements Navigation, ArticleRecyclerAdapter.OnItemClick, NotifBtn.Navigation {

    private static final String TAG = "SavedActivity";
    private ArticleDatabase articleDatabase;
    private ArticleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_layout);

        RecyclerView recyclerView = findViewById(R.id.saved_recyclerView);
        articleDatabase = ArticleDatabase.getInstance(this, ArticleDatabase.Option.BOOKMARK);

        adapter = new ArticleRecyclerAdapter(this, articleDatabase.getAllArticles(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
/*
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
    }*/

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
        Intent myIntent = new Intent(Saved.this, Settings.class);
        Saved.this.startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.saved_ScrollView;
    }

    @Override
    public void onClick(Article data) {
        Intent intent = new Intent(Saved.this, ArticleActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void goToNotif() {
        Intent myIntent = new Intent(Saved.this, Notif_Activity.class);
        Saved.this.startActivity(myIntent);
    }
}
