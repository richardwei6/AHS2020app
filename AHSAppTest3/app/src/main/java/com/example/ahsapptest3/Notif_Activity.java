package com.example.ahsapptest3;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Misc.FullScreenActivity;
import com.example.ahsapptest3.Setting_Activities.Settings_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notif_Activity extends FullScreenActivity implements Navigation, NotifRecyclerAdapter.OnItemClick {

    /*private static final String TAG = "Notif_Activity";*/
    private NotifRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        RecyclerView recyclerView = findViewById(R.id.notif_recyclerView);
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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArticleDatabase articleDatabase = ArticleDatabase.getInstance(getApplicationContext());
        final BulletinDatabase bulletinDatabase = BulletinDatabase.getInstance(getApplicationContext());
        final NotifDatabase notifDatabase = NotifDatabase.getInstance(getApplicationContext());

        adapter = new NotifRecyclerAdapter(new ArrayList<Notif_Data>(), this);
        recyclerView.setAdapter(adapter);

        final Resources r= getResources();
        final ArrayList<Notif_Data> notif_data = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_notif_key));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clearAll();
                notif_data.clear();
                for(DataSnapshot child_sn: snapshot.getChildren()) {
                    String notifID = child_sn.getKey();
                    final String articleID = child_sn.child(r.getString(R.string.fb_notif_artID)).getValue().toString();
                    String body = child_sn.child(r.getString(R.string.fb_notif_body)).getValue().toString();
                    String title = child_sn.child(r.getString(R.string.fb_notif_title)).getValue().toString();
                    long time = (long) child_sn.child(r.getString(R.string.fb_notif_time)).getValue();
                    int category = ((Long)child_sn.child(r.getString(R.string.fb_notif_cat)).getValue()).intValue();
                    final Notif_Data data = new Notif_Data(notifID, time, title, body, category, articleID,
                            notifDatabase.getReadStatusByID(notifID));
                    Article article = articleDatabase.getArticleById(articleID);
                    if(article != null)
                        data.setHolder(new Article_or_BulletinHolder(article));
                    else {
                        Bulletin_Article article1 = bulletinDatabase.getArticleByID(articleID);
                        if(article1 != null)
                            data.setHolder(new Article_or_BulletinHolder(article1));
                    /*new Runnable() {
                        @Override
                        public void run() {

                            }
                        }*/
                    }
                    notif_data.add(data);
                    adapter.addItem(data);
                }
                notifDatabase.updateData(notif_data.toArray(new Notif_Data[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               /* Log.e(TAG, error.getDetails());*/
            }
        });
    }

    private static final int REQUEST_CODE = 1;
    private int position;
    @Override
    public void onClick(@NonNull Notif_Data data, int position) {
        this.position = position;
        data.setNotified(true);
        NotifDatabase.getInstance(this).updateReadStatus(data);
        if(data.getHolder()!= null) {
            /*ArticleDatabase.getInstance(this, ArticleDatabase.Option.CURRENT).updateNotifiedStatus(data.getArticle().getID(),true);*/


            switch(data.getHolder().getOption()) {
                case ARTICLE:
                    Intent intent = new Intent(Notif_Activity.this, ArticleActivity.class);
                    intent.putExtra(ArticleActivity.data_key, data.getHolder().getArticle());
                    startActivityForResult(intent, REQUEST_CODE);
                    overridePendingTransition(R.anim.from_right, R.anim.maintain);
                    break;
                case BULLETIN_ARTICLE:
                    Intent intent1 = new Intent(Notif_Activity.this, Bulletin_Article_Activity.class);
                    intent1.putExtra(ArticleActivity.data_key, data.getHolder().getBulletin_article());
                    startActivityForResult(intent1, REQUEST_CODE);
                    overridePendingTransition(R.anim.from_right, R.anim.maintain);
            }



        } else {
            /*data.setNotified(true);
            NotifDatabase.getInstance(this).updateReadStatus(data);*/
            adapter.updateReadItemPosition(this.position);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                /*boolean thing = data.getBooleanExtra(read_KEY, true);*/
                adapter.updateReadItemPosition(position);
            } /*else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }*/
        }
    }

    @Override
    public void goToHome() {
        Intent intent = new Intent(Notif_Activity.this, News_Activity.class);
        startActivity(intent);
    }

    @Override
    public void goToBulletin() {
        Intent intent = new Intent(Notif_Activity.this, Bulletin_Activity.class);
        startActivity(intent);
    }

    @Override
    public void goToSaved() {
        Intent intent = new Intent(Notif_Activity.this, Saved_Activity.class);
        startActivity(intent);
    }

    @Override
    public void goToSettings() {
        Intent intent = new Intent(Notif_Activity.this, Settings_Activity.class);
        startActivity(intent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.notif_scrollView;
    }

    @Override
    public HighlightOption getHighlightOption() {
        return HighlightOption.NONE;
    }

}
