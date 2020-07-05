package com.example.ahsapptest3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.ahsapptest3.Settings.SettingsActivity;

public class BulletinActivity extends AppCompatActivity implements Navigation{

    FrameLayout[] frameLayouts;
    private ImageView seniors_toggle, events_toggle, colleges_toggle, athletics_toggle,reference_toggle,others_toggle;

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
        return R.id.bulletin__scrollView;
    }

    enum BulletinType
    {
        SENIORS, EVENTS, COLLEGES, REFERENCE, ATHLETICS, OTHERS;
    }

    public boolean is_Type_Layout_Active(BulletinType type) //"Translates" enum to is_active array location
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

    public void switch_Type_Layout_Active(BulletinType type)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_layout);

        final ScrollView scrollView = findViewById(R.id.bulletin__scrollView);


        seniors_toggle = scrollView.findViewById(R.id.bulletin_seniors_toggle_image);
        events_toggle = scrollView.findViewById(R.id.bulletin_events_toggle_image);
        colleges_toggle = scrollView.findViewById(R.id.bulletin_colleges_toggle_image);
        reference_toggle = scrollView.findViewById(R.id.bulletin_reference_toggle_image);
        athletics_toggle = scrollView.findViewById(R.id.bulletin_athletics_toggle_image);
        others_toggle = scrollView.findViewById(R.id.bulletin_others_toggle_image);

        data = getData();
        frameLayouts = new FrameLayout[data.length];

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

        Bulletin_Template[] items = new Bulletin_Template[data.length];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = Bulletin_Template.newInstanceOf(data[i]);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),items[i])
                    .commit();

        }
    }

    private int getIdRange()
    {
        return 2000000;
    }

    private Bulletin_Info[] getData()
    {
        return new Bulletin_Info[]
                {
                    new Bulletin_Info("Science Bowl Tryouts:","4/24/2020", "Short answer written test on AP\n" +
                            "Bio, AP Chem, AP Physics, Earth & Space Sciences, Statistics, Math Analysis, Calculus. Candidates\n" +
                            "ideally should have taken an AP class and show subject mastery. No need to know all subjects. Team\n" +
                            "members typically show content mastery of a specific subject, not all of the subjects. Check out\n" +
                            "the link to watch a regional match, finals round. For questions email cmynster@ausd.net\n", BulletinType.SENIORS),
                    new Bulletin_Info("Arcadia's Got Talent- AEF Video Contest","3/25/2020", "- Open until 3/25/2020. Click on the link to access further\n" +
                            "details.", BulletinType.EVENTS),
                    new Bulletin_Info("Blind Date with a Book is Back!","","Come to the library to check out a surprise book. Read and review\n" +
                            "the book, and you can win prizes! With over 50 excellent books wrapped up for a delicious\n" +
                            "surprise, you're bound to find something you love!",BulletinType.EVENTS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.ATHLETICS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.EVENTS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.ATHLETICS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.COLLEGES),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.SENIORS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.REFERENCE),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.OTHERS),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.REFERENCE),
                    new Bulletin_Info("Title1","4/6/2938","sampleText",BulletinType.ATHLETICS),



                };
    }

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
    }

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
