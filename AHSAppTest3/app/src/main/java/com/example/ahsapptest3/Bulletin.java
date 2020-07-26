package com.example.ahsapptest3;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.Bulletin_SelectorView;
import com.example.ahsapptest3.Settings.SettingsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Bulletin extends AppCompatActivity implements Navigation, BulletinRecyclerAdapter.OnItemClick, NotifBtn.Navigation{

    private static final String TAG = "BulletinActivity";

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(Bulletin.this, News.class);
        Bulletin.this.startActivity(myIntent);
    }
    @Override
    public void goToBulletin() {

    }
    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(Bulletin.this, Saved.class);
        Bulletin.this.startActivity(myIntent);
    }
    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(Bulletin.this, SettingsActivity.class);
        Bulletin.this.startActivity(myIntent);
    }
    @Override
    public int getScrollingViewId() {
        return R.id.bulletin_outerScrollView;
    }

    @Override
    public void onClick(Bulletin_Data data) {
        data.setAlready_read(true);
        BulletinDatabase db = new BulletinDatabase(this);
        db.updateReadStatus(data);

        Intent intent = new Intent(Bulletin.this, Bulletin_Item_Activity.class);
        intent.putExtra("data", data);
        Bulletin.this.startActivity(intent);
    }

    @Override
    public void goToNotif() {
        Intent intent = new Intent(Bulletin.this, Notif_Activity.class);
        startActivity(intent);
    }

    public enum Type
    {
        SENIORS("Seniors"), EVENTS("Events"), COLLEGES("Colleges"), REFERENCE("Reference"), ATHLETICS("Athletics"), OTHERS("Others");
        private String name;
        Type(String name) {
            this.name = name;
        }
        String getName() {
            return name;
        }
    }
 /*   private boolean seniors_active = false, colleges_active = false, events_active = false, athletics_active = false, reference_active = false, others_active = false;
*/

    final Type[] types = Type.values();

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

        final String[] categories = new String[]
                {
                    "seniors",
                    "events",
                    "colleges",
                    "reference",
                    "athletics",
                    "others",
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
        final ArrayList<Bulletin_Data> data = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.bulletin_RecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        final BulletinRecyclerAdapter adapter = new BulletinRecyclerAdapter(this, data, this);


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

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("bulletin");
        final BulletinDatabase db = new BulletinDatabase(this);
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
                        String title = dataSnapshot.child("articleTitle").getValue().toString();
                        String body = dataSnapshot.child("articleBody").getValue().toString();
                        long time = (long) dataSnapshot.child("articleUnixEpoch").getValue();

                        Bulletin_Data info = new Bulletin_Data(ID, time, title, body, types[i],
                                db.getReadStatusByID(ID)
                        );

                        data.add(info);
                        /*Log.d(TAG, data.toString());*/
                        adapter.addItem(info);
                    }
                }

                db.deleteAll(); // clear array of old items not in the new week's bulletin
                db.add(data.toArray(new Bulletin_Data[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getDetails());
            }
        });

    }
}
