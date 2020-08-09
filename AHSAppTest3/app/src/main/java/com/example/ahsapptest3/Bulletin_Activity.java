package com.example.ahsapptest3;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.Bulletin_SelectorView;
import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Setting_Activities.Settings_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Bulletin_Activity extends FullScreenActivity implements Navigation, BulletinRecyclerAdapter.OnItemClick, NotifBtn.Navigation{

    private static final String TAG = "BulletinActivity";
    private BulletinRecyclerAdapter adapter;

    /*   private boolean seniors_active = false, colleges_active = false, events_active = false, athletics_active = false, reference_active = false, others_active = false;
*/

    final Bulletin_Article.Type[] types = Bulletin_Article.Type.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_layout);
/*
        ImageView notifButton = findViewById(R.id.bulletin_notif_Image);
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        final Resources r = getResources();
        final String[] categories = new String[]
                {
                    r.getString(R.string.fb_bull_seniors),
                    r.getString(R.string.fb_bull_events),
                    r.getString(R.string.fb_bull_colleges),
                    r.getString(R.string.fb_bull_references),
                    r.getString(R.string.fb_bull_athletics),
                    r.getString(R.string.fb_bull_others),
                };

        Bulletin_SelectorView 
                seniors_selector = findViewById(R.id.bulletin_seniors_selection),
                events_selector = findViewById(R.id.bulletin_events_selection),
                colleges_selector = findViewById(R.id.bulletin_colleges_selection),
                reference_selector = findViewById(R.id.bulletin_reference_selection),
                athletics_selector = findViewById(R.id.bulletin_athletics_selection),
                others_selector = findViewById(R.id.bulletin_others_selection);

        final Bulletin_SelectorView[] selectors =
                {
                    seniors_selector ,
                    events_selector ,
                    colleges_selector ,
                    reference_selector ,
                    athletics_selector ,
                    others_selector
                };
        final ArrayList<Bulletin_Article> data = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.bulletin_RecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new BulletinRecyclerAdapter(data, this);


        for(int i = 0; i < selectors.length; i++)
        {
            final int finalI = i;


            selectors[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adapter.selectors_active[finalI] = !adapter.selectors_active[finalI];
                    selectors[finalI].initDecoration(adapter.selectors_active[finalI]);
                    adapter.filterItems();
                }
            });
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_bull_key));
        final BulletinDatabase db = BulletinDatabase.getInstance(this);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear(); // so it doesn't just keep adding should the data change
                adapter.clearAll();

                for(int i = 0; i < categories.length; i++)
                    {
                    // Log.d(TAG, categories[i]);
                    DataSnapshot snapshot1 = snapshot.child(categories[i]);


                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                        String ID = dataSnapshot.getKey();
                        String title = dataSnapshot.child(r.getString(R.string.fb_bull_artTitle)).getValue().toString();
                        String body = dataSnapshot.child(r.getString(R.string.fb_bull_artBody)).getValue().toString();
                        long time = (long) dataSnapshot.child(r.getString(R.string.fb_bull_artTime)).getValue();

                        Bulletin_Article info = new Bulletin_Article(ID, time, title, body, types[i],
                                db.getReadStatusByID(ID)
                        );

                        data.add(info);
                        /*Log.d(TAG, data.toString());*/
                        adapter.addItem(info);
                    }
                }
                db.updateArticles(data.toArray(new Bulletin_Article[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getDetails());
            }
        });

    }

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(Bulletin_Activity.this, News_Activity.class);
        Bulletin_Activity.this.startActivity(myIntent);
    }
    @Override
    public void goToBulletin() {

    }
    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(Bulletin_Activity.this, Saved_Activity.class);
        Bulletin_Activity.this.startActivity(myIntent);
    }
    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(Bulletin_Activity.this, Settings_Activity.class);
        Bulletin_Activity.this.startActivity(myIntent);
    }
    @Override
    public int getScrollingViewId() {
        return R.id.bulletin_outerScrollView;
    }

    @Override
    public HighlightOption getHighlightOption() {
        return HighlightOption.BULLETIN;
    }


    private static final int REQUEST_CODE = 1;
    private int position;
    @Override
    public void onClick(Bulletin_Article data, int position) {
        data.setAlready_read(true);
        BulletinDatabase db = BulletinDatabase.getInstance(this);
        db.updateReadStatus(data);
        this.position = position;

        Intent intent = new Intent(Bulletin_Activity.this, Bulletin_Article_Activity.class);
        intent.putExtra(Bulletin_Article_Activity.data_KEY, data);
        Bulletin_Activity.this.startActivityForResult(intent, REQUEST_CODE);
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
    public void goToNotif() {
        Intent intent = new Intent(Bulletin_Activity.this, Notif_Activity.class);
        startActivity(intent);
    }
}
