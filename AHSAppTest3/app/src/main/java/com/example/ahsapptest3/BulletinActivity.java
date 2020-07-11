package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ahsapptest3.Helper_Code.Helper;
import com.example.ahsapptest3.Settings.SettingsActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BulletinActivity extends AppCompatActivity implements Navigation{

    private static final String TAG = "BulletinActivity";
    FrameLayout[] frameLayouts;
    
    private Bulletin_Info [] data;
    private boolean[] is_active = new boolean[6];

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(BulletinActivity.this,MainActivity.class);
        BulletinActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToBulletin() {

    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(BulletinActivity.this,SavedActivity.class);
        BulletinActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToSettings() {
        Intent myIntent = new Intent(BulletinActivity.this, SettingsActivity.class);
        BulletinActivity.this.startActivity(myIntent);
    }

    @Override
    public int getScrollingViewId() {
        return R.id.bulletin_RecyclerView;
    }

    enum Type
    {
        SENIORS, EVENTS, COLLEGES, REFERENCE, ATHLETICS, OTHERS;
    }

    public boolean is_Type_Layout_Active(Type type) //"Translates" enum to is_active array location
    {
        switch(type)
        {
            case SENIORS:
                return is_active[0];
            case EVENTS:
                return is_active[1];
            case COLLEGES:
                return is_active[2];
            case REFERENCE:
                return is_active[3];
            case ATHLETICS:
                return is_active[4];
            case OTHERS:
                return is_active[5];
            default:
                return false; //this really shouldn't happen
        }
    }

    public void switch_Type_Layout_Active(Type type)
    {
        switch(type)
        {
            case SENIORS:
                is_active[0] = !is_active[0];
                break;
            case EVENTS:
                is_active[1] = !is_active[1];
                break;
            case COLLEGES:
                is_active[2] = !is_active[2];
                break;
            case REFERENCE:
                is_active[3] = !is_active[3];
                break;
            case ATHLETICS:
                is_active[4] = !is_active[4];
                break;
            case OTHERS:
                is_active[5] = !is_active[5];
                break;
        }
    }
    
    private boolean seniors_active = false, colleges_active = false, events_active = false, athletics_active = false, reference_active = false, others_active = false;

    private ImageView seniors_underline, colleges_underline, events_underline, athletics_underline, reference_underline, others_underline;
    
    public void setTabUnderline()
    {
        if(seniors_active)
            seniors_underline.setVisibility(View.VISIBLE);
        else
            seniors_underline.setVisibility(View.INVISIBLE);
        if(colleges_active)
            colleges_underline.setVisibility(View.VISIBLE);
        else
            colleges_underline.setVisibility(View.INVISIBLE);
        if(events_active)
            events_underline.setVisibility(View.VISIBLE);
        else
            events_underline.setVisibility(View.INVISIBLE);
        if(athletics_active)
            athletics_underline.setVisibility(View.VISIBLE);
        else
            athletics_underline.setVisibility(View.INVISIBLE);
        if(reference_active)
            reference_underline.setVisibility(View.VISIBLE);
        else
            reference_underline.setVisibility(View.INVISIBLE);
        if(others_active)
            others_underline.setVisibility(View.VISIBLE);
        else
            others_underline.setVisibility(View.INVISIBLE);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_layout);

        seniors_underline = findViewById(R.id.bulletin_seniors_underline);
        colleges_underline = findViewById(R.id.bulletin_colleges_underline);
        events_underline = findViewById(R.id.bulletin_events_underline);
        athletics_underline = findViewById(R.id.bulletin_athletics_underline);
        reference_underline = findViewById(R.id.bulletin_reference_underline);
        others_underline = findViewById(R.id.bulletin_others_underline);
        
        setTabUnderline();
        

        final String[] categories = new String[]
                {
                        "athletics",
                        "colleges",
                        "events",
                        "others",
                        "reference",
                        "seniors"
                };
        final Type[] types = new Type[]
                {
                        Type.ATHLETICS,
                        Type.COLLEGES,
                        Type.EVENTS,
                        Type.OTHERS,
                        Type.REFERENCE,
                        Type.SENIORS
                };

        final ArrayList<Bulletin_Info> data = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.bulletin_RecyclerView);
        final BulletinRecyclerAdapter adapter = new BulletinRecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int margin = (int) getResources().getDimension(R.dimen.EveryPage_Side_Padding);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = margin;
                outRect.left = margin;
                outRect.top = margin/8;
                outRect.bottom = margin/8;

            }
        });
        recyclerView.requestLayout();

        final
        TextView seniors_toggle =findViewById(R.id.bulletin_seniors_text),
                events_toggle = findViewById(R.id.bulletin_events_text),
                colleges_toggle = findViewById(R.id.bulletin_colleges_text),
                reference_toggle = findViewById(R.id.bulletin_reference_text),
                athletics_toggle = findViewById(R.id.bulletin_athletics_text),
                others_toggle = findViewById(R.id.bulletin_others_text);

        seniors_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seniors_active = !seniors_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        colleges_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colleges_active = !colleges_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        events_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events_active = !events_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        reference_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference_active = !reference_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        others_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                others_active = !others_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        athletics_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                athletics_active = !athletics_active;
                adapter.filterItems();
                setTabUnderline();
            }
        });

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("bulletin");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear(); // so it doesn't just keep adding should the data change
                for(int i = 0; i < categories.length; i++)
                    {
                    Log.d(TAG, categories[i]);
                    DataSnapshot snapshot1 = snapshot.child(categories[i]);

                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                        String title = dataSnapshot.child("articleTitle").getValue().toString();
                        String body = dataSnapshot.child("articleBody").getValue().toString();
                        long time = (long) dataSnapshot.child("articleUnixEpoch").getValue();
                        data.add(new Bulletin_Info(time, title, body, types[i]));
                        adapter.notifyDataCopyChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getDetails());
            }
        });





        /*frameLayouts = new FrameLayout[data.size()];

        LinearLayout bulletin_item_LinearLayout = findViewById(R.id.bulletin__LinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int)getResources().getDimension(R.dimen.Bulletin_Margin);
        params.setMargins(margin,0,margin,margin);
        for(int i = 0; i < frameLayouts.length; i++) {
            frameLayouts[i] = new FrameLayout(this);
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(getIdRange()+i);
            bulletin_item_LinearLayout.addView(frameLayouts[i],params);
        }

        Bulletin_Template[] items = new Bulletin_Template[data.size()];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = Bulletin_Template.newInstanceOf(data.get(i));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),items[i])
                    .commit();

        }*/
    }

    public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.ViewHolder>
    {
        private static final String TAG = "BulletinRecyclerAdapter";

        public ArrayList<Bulletin_Info> bulletin_infos;
        private ArrayList<Bulletin_Info> copy;
        private Context context;

        public BulletinRecyclerAdapter(Context context, ArrayList<Bulletin_Info> bulletin_infos) {
            this.bulletin_infos = bulletin_infos;
            this.context = context;
            Log.d(TAG, this.bulletin_infos.toString());
            copy = new ArrayList<>(this.bulletin_infos);
            Log.d(TAG, copy.toString());
        }

        public void notifyDataCopyChanged()
        {
            copy = new ArrayList<>(bulletin_infos);
            this.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.template__bulletin_display, parent, false)
                    ;
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Bulletin_Info info = copy.get(position);
            holder.titleText.setText(info.getTitle());
            holder.bodyText.setText(info.getBodyText());
            Helper.setTimeText_toView(holder.dateText, info.getTime());
        }

        @Override
        public int getItemCount() {
            return copy.size();
        }

        public void filterItems()
        {
            copy = new ArrayList<>(bulletin_infos);
            Log.d(TAG,seniors_active + "" + colleges_active + athletics_active + others_active + reference_active + events_active);
            if(seniors_active || colleges_active || athletics_active || others_active || reference_active || events_active)
                for(int i = copy.size() -1; i >= 0; i--)
                {

                    if(!seniors_active && copy.get(i).getType() == Type.SENIORS)
                        copy.remove(i);
                    else if(!colleges_active && copy.get(i).getType() == Type.COLLEGES)
                        copy.remove(i);
                    else if(!athletics_active && copy.get(i).getType() == Type.ATHLETICS)
                        copy.remove(i);
                    else if(!others_active && copy.get(i).getType() == Type.OTHERS)
                        copy.remove(i);
                    else if(!reference_active && copy.get(i).getType() == Type.REFERENCE)
                        copy.remove(i);
                    else if(!events_active && copy.get(i).getType() == Type.EVENTS)
                        copy.remove(i);
                }
            Log.d(TAG, copy.toString());
            this.notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView titleText, bodyText, dateText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.bulletin_template_TitleText);
                bodyText = itemView.findViewById(R.id.bulletin_template_BodyText);
                dateText = itemView.findViewById(R.id.bulletin_template_DateText);
            }
        }
    }

    private int getIdRange()
    {
        return 2000000;
    }

    /*private Bulletin_Info[] getData()
    {
        return new Bulletin_Info[]
                {
                    new Bulletin_Info("Science Bowl Tryouts:","4/24/2020", "Short answer written test on AP\n" +
                            "Bio, AP Chem, AP Physics, Earth & Space Sciences, Statistics, Math Analysis, Calculus. Candidates\n" +
                            "ideally should have taken an AP class and show subject mastery. No need to know all subjects. Team\n" +
                            "members typically show content mastery of a specific subject, not all of the subjects. Check out\n" +
                            "the link to watch a regional match, finals round. For questions email cmynster@ausd.net\n", Type.SENIORS),
                    new Bulletin_Info("Arcadia's Got Talent- AEF Video Contest","3/25/2020", "- Open until 3/25/2020. Click on the link to access further\n" +
                            "details.", Type.EVENTS),
                    new Bulletin_Info("Blind Date with a Book is Back!","2/5/2012","Come to the library to check out a surprise book. Read and review\n" +
                            "the book, and you can win prizes! With over 50 excellent books wrapped up for a delicious\n" +
                            "surprise, you're bound to find something you love!", Type.EVENTS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.ATHLETICS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.EVENTS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.ATHLETICS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.COLLEGES),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.SENIORS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.REFERENCE),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.OTHERS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.REFERENCE),
                    new Bulletin_Info("Title1","4/6/2938","sampleText", Type.ATHLETICS),



                };
    }*/
/*
    public void onSeniorsClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.SENIORS))
        {
            seniors_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_seniors);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_seniors));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            seniors_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_seniors);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_seniors));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.SENIORS);
        filterItems();
    }

    public void onEventsClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.EVENTS))
        {
            events_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_events);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_events));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            events_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_events);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_events));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.EVENTS);
        filterItems();
    }

    public void onCollegesClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.COLLEGES))
        {
            colleges_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_colleges);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_colleges));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            colleges_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_colleges);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_colleges));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.COLLEGES);
        filterItems();
    }

    public void onReferenceClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.REFERENCE))
        {
            reference_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_reference);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_reference));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            reference_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_reference);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_reference));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.REFERENCE);
        filterItems();
    }

    public void onAthleticsClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.ATHLETICS))
        {
            athletics_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_athletics);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_athletics));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            athletics_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_athletics);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_athletics));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.ATHLETICS);
        filterItems();
    }

    public void onOthersClick(View view)
    {
        if(!is_Type_Layout_Active(BulletinType.OTHERS))
        {
            others_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_circle_filled));
            ImageView inner = findViewById(R.id.bulletin_others);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_others));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        else
        {
            others_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_others);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_others));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF,null));
        }
        switch_Type_Layout_Active(BulletinType.OTHERS);
        filterItems();
    }*/

    public void filterItems()
    {
        for(int i = 0; i < data.length; i++)
        {
            if(is_Type_Layout_Active(data[i].getType()))
            {
                if(frameLayouts[i].getVisibility() != View.VISIBLE)
                    frameLayouts[i].setVisibility(View.VISIBLE);
                    //expand(frameLayouts[i]);
            }
            else
            {
                if (frameLayouts[i].getVisibility() == View.VISIBLE)
                    //collapse(frameLayouts[i]);
                    frameLayouts[i].setVisibility(View.GONE);
            }

            //frameLayouts[i].invalidate();
        }

        boolean isallfalse = false;
        for(boolean b: is_active)
            isallfalse = isallfalse||b;

        if(!isallfalse)
        {
            for(FrameLayout i: frameLayouts)
                if(i.getVisibility() != View.VISIBLE)
                    //expand(i);
                    i.setVisibility(View.VISIBLE);
        }
    }


}
