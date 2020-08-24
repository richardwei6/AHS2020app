package com.hsappdev.ahs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.Misc.FullScreenActivity;
import com.hsappdev.ahs.Setting_Activities.Settings_Activity;

import java.util.ArrayList;

public class Saved_Activity extends FullScreenActivity implements Navigation, SavedRecyclerAdapter.OnItemClick, NotifBtn.Navigation {

    public static final String saved_status_changed_KEY = "saved_changed";
    /*private static final String TAG = "SavedActivity";*/
    private SavedRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_layout);

        RecyclerView recyclerView = findViewById(R.id.saved_recyclerView);
        // create recyclerview margins
        final int margin = (int) getResources().getDimension(R.dimen.Half_Padding);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = margin;
                outRect.right = margin;

                // account for special margins for first and last children
                int position = parent.getChildLayoutPosition(view);

                // first item
                outRect.top = (position == 0) ? margin : margin/2;
                // last item
                outRect.bottom = (position == state.getItemCount()-1) ? margin : margin/2;

            }
        });
        recyclerView.requestLayout();
        SavedDatabase articleDatabase = SavedDatabase.getInstance(this);

        adapter = new SavedRecyclerAdapter(new ArrayList<Article_or_BulletinHolder>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articleDatabase.getAllArticles_withCallBack(new SavedDatabase.ArticleRetrievedCallback() {
            @Override
            public void onArticleLoaded(Article_or_BulletinHolder article) {
                adapter.addItem(article);
            }
        });

    }

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(Saved_Activity.this, News_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(Saved_Activity.this, Bulletin_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public void goToSaved() {

    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(Saved_Activity.this, Settings_Activity.class);
        startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.saved_ScrollView;
    }

    @Override
    public HighlightOption getHighlightOption() {
        return HighlightOption.SAVED;
    }

    public static final int REQUEST_CODE = 1;
    private int position;
    @Override
    public void onItemClick(Article data, int position) {
        this.position = position;
        Intent intent = new Intent(Saved_Activity.this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.data_key, data);
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.from_right, R.anim.maintain);
    }

    @Override
    public void onItemClick(Bulletin_Article data, int position) {
        this.position = position;
        Intent intent = new Intent(Saved_Activity.this, Bulletin_Article_Activity.class);
        intent.putExtra(Bulletin_Article_Activity.data_KEY, data);
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.from_right, R.anim.maintain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
            if(resultCode == Activity.RESULT_OK) {
                boolean saved_status_changed = data.getBooleanExtra(saved_status_changed_KEY, false);
                if(saved_status_changed)
                    adapter.updateItemRemoved(position);
            }
    }

    @Override
    public void goToNotif() {
        Intent myIntent = new Intent(Saved_Activity.this, Notif_Activity.class);
        Saved_Activity.this.startActivity(myIntent);
    }
}
