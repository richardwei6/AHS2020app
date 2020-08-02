package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Setting_Activities.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notif_Activity extends FullScreenActivity implements Navigation, ArticleRecyclerAdapter.OnItemClick, NotifRecyclerAdapter.OnItemClick {

    private static final String TAG = "Notif_Activity";
    private static ArrayList<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        /*listLayout = findViewById(R.id.notif_linearLayout);*/

        RecyclerView recyclerView = findViewById(R.id.notif_recyclerView);
        // create recyclerview margins
        final int margin = (int) getResources().getDimension(R.dimen.EveryPage_Side_Padding_Half);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = margin;
                outRect.right = margin;

                // account for special margins for first and last children
                int position = parent.getChildLayoutPosition(view);

                // first item
                outRect.top = (position == 0) ? margin : margin/4;
                // last item
                outRect.bottom = (position == state.getItemCount()-1) ? margin : margin/4;

            }
        });
        recyclerView.requestLayout();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*final ArticleRecyclerAdapter adapter = new ArticleRecyclerAdapter(this, articles,this);
        recyclerView.setAdapter(adapter);*/


        final ArticleDatabase articleDatabase = ArticleDatabase.getInstance(this, ArticleDatabase.Option.CURRENT);

        final ArrayList<Notif_Data> dataArrayList = new ArrayList<>();
        final NotifRecyclerAdapter adapter = new NotifRecyclerAdapter(this, dataArrayList, this);
        recyclerView.setAdapter(adapter);
        final Context context = this;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notifications");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                adapter.clearAll();
                for(DataSnapshot child_sn: snapshot.getChildren())
                {
                    String articleID = child_sn.child("notificationArticleID").getValue().toString();
                    String body = child_sn.child("notificationBody").getValue().toString();
                    String title = child_sn.child("notificationTitle").getValue().toString();
                    long time = (long) child_sn.child("notificationUnixEpoch").getValue();
                    int category = ((Long)child_sn.child("notificationCategory").getValue()).intValue();
                    Notif_Data data = new Notif_Data(time, title, body, category,
                            articleDatabase.getArticleById(articleID));
                    if (data.getArticle() != null) {
                        Log.d(TAG, "from database" + ArticleDatabase.getInstance(context, ArticleDatabase.Option.CURRENT).getArticleById(articleID).toString());
                        Log.d(TAG, "from object" + data.getArticle().toString());
                    }
                    dataArrayList.add(data);
                    adapter.addItem(data);
                    //Log.d(TAG, child_sn.getKey());
                    //Log.d(TAG, "tried once, ID:" + ID);
                    /*if(articleDatabase.alreadyAdded(articleID))
                    {
                        *//*Log.d(TAG,"found articles");*//*
                        Article article = articleDatabase.getArticleById(articleID);
                        articles.add(article);
                        ArticleRecyclerAdapter.articles.add(article);
                        adapter.notifyDataSetChanged();
                    }*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getDetails());
            }
        });
    }

    @Override
    public void goToHome() {
        Intent intent = new Intent(Notif_Activity.this, News.class);
        startActivity(intent);
    }

    @Override
    public void goToBulletin() {
        Intent intent = new Intent(Notif_Activity.this, Bulletin.class);
        startActivity(intent);
    }

    @Override
    public void goToSaved() {
        Intent intent = new Intent(Notif_Activity.this, Saved.class);
        startActivity(intent);
    }

    @Override
    public void goToSettings() {
        Intent intent = new Intent(Notif_Activity.this, Settings.class);
        startActivity(intent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.notif_scrollView;
    }

    @Override
    public void onClick(Article data) {
        if(!data.isNotified())
            ArticleDatabase.getInstance(this, ArticleDatabase.Option.CURRENT).updateNotifiedStatus(data.getID(),true);
        Intent intent = new Intent(Notif_Activity.this, ArticleActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void onClick(@Nullable Article article, int position) {
        if(article!= null)
        {
            ArticleDatabase.getInstance(this, ArticleDatabase.Option.CURRENT).updateNotifiedStatus(article.getID(),true);

            Intent intent = new Intent(Notif_Activity.this, ArticleActivity.class);
            intent.putExtra("data", article);
            startActivity(intent);
        }
    }

}
