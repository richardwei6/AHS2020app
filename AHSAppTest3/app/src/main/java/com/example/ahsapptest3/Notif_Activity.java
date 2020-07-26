package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notif_Activity extends AppCompatActivity {

    private static final String TAG = "Notif_Activity";
    private static ArrayList<Article> articles = new ArrayList<>();
    private FrameLayout[] frameLayouts;
    private LinearLayout listLayout;
    /*private boolean[] justNotified;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        listLayout = findViewById(R.id.notif_linearLayout);
        /*String[][] data = getData();
        if(articles == null)
        {
            articles = new Article[data.length];
            for(int i = 0; i < articles.length; i++)
            {
                articles[i] =

                new Article("2342",238472394,data[i][0],"author",data[i][1],new String [] {"hello"},false,i < 3);
                // change this later
            }
        }*/

        final ArticleDatabase articleDatabase = ArticleDatabase.getInstance(this, ArticleDatabase.Option.CURRENT);
        final Context context = this;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notifications");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child_sn: snapshot.getChildren())
                {
                    String ID = child_sn.child("notificationArticleID").getValue().toString();
                    //Log.d(TAG, child_sn.getKey());
                    //Log.d(TAG, "tried once, ID:" + ID);
                    if(articleDatabase.alreadyAdded(ID))
                    {
                        /*Log.d(TAG,"found articles");*/
                        Article article = articleDatabase.getArticleById(ID);
                        articles.add(articleDatabase.getArticleById(ID));
                    }
                }


                //Log.d(TAG, String.valueOf(articles.size()));
                frameLayouts = new FrameLayout[articles.size()];
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0,0,0,0);

                for(int i = 0; i < frameLayouts.length; i++) {
                    frameLayouts[i] = new FrameLayout(context);
                    frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,  //width
                            FrameLayout.LayoutParams.WRAP_CONTENT   //height
                    ));
                    frameLayouts[i].setId(getIdRange()+i);
                    listLayout.addView(frameLayouts[i],params);

                    final View view = frameLayouts[i];
                    final Article article = articles.get(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(view.getContext(), ArticleActivity.class);
                            intent.putExtra("data", article);
                            view.getContext().startActivity(intent);
                            if(!article.alreadyNotified())
                                articleDatabase.updateNotifiedStatus(article.getID(),true);

                        }
                    });
                }

                Notif_Template[] items = new Notif_Template[articles.size()];
                for (int i = 0; i < items.length; i++)
                {
                    items[i] = Notif_Template.newInstanceOf(articles.get(i));
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(frameLayouts[i].getId(),items[i])
                            .commitAllowingStateLoss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getDetails());
            }
        });


        /*justNotified = new boolean[articles.length];
        for(int i = 0; i < articles.length; i++)
        {
            justNotified[i] = articles[i].alreadyNotified();
        }*/



        // set listener for back button
        ImageButton backButton = findViewById(R.id.notif_header_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*@Override
    public void onResume()
    {
        super.onResume();

        for(FrameLayout fl: frameLayouts) // slightly janky solution to the problem that when article back button pressed, android doesn't redraw the notif page,
        {
            fl.setVisibility(View.GONE); //thus changing notified boolean seems to have no effect, until you click back on notif then go back to notif again
            fl.setVisibility(View.VISIBLE);
        }
    }*/
    @Override
    public void onRestart(){
        super.onRestart();
        articles.clear();
        this.recreate(); // see comment above, only this actually works; maybe fix the listener to change flag variable to recreate? maybe sharedpreferences?
        // note, use recycler view and finagle with notifyDataSetChanged

    }

    private int getIdRange()
    {
        return 2000000;
    }

}
