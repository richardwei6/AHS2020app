package com.example.ahsapptest3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class BulletinActivity extends AppCompatActivity {

    private ImageButton home_btn, bulletin_btn, bookmarks_btn, settings_btn;
    FrameLayout[] frameLayouts;
    private ImageView seniors_toggle, events_toggle, colleges_toggle, athletics_toggle,reference_toggle,others_toggle;
    private ImageButton[] nav_btns;
    private Bulletin_Info [] data;
    private boolean[] is_active = new boolean[6];

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

        home_btn = findViewById(R.id.home_button);
        bulletin_btn = findViewById(R.id.bulletin_button);
        bulletin_btn.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
        bookmarks_btn = findViewById(R.id.bookmarks_button);
        settings_btn = findViewById(R.id.settings_button);

        nav_btns = new ImageButton[]
                {
                        home_btn,bulletin_btn,bookmarks_btn,settings_btn
                };
        final FrameLayout navBar= findViewById(R.id.nav_bar_FrameLayout);
        final ScrollView scrollView = findViewById(R.id.bulletin__scrollView);
        final int scrollAnimBuffer = 4;

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY() > y + scrollAnimBuffer) // scroll down, 2 is the buffer
                {
                    slideDown(navBar);
                    is_nav_bar_up = false;
                }
                else if (scrollView.getScrollY() < y - scrollAnimBuffer)
                {
                    slideUp(navBar);
                    is_nav_bar_up = true;
                }
                y= scrollView.getScrollY();
            }
        });

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
        params.setMargins(margin,margin,margin,0);
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            seniors_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_seniors);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_seniors));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            events_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_events);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_events));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            colleges_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_colleges);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_colleges));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            reference_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_reference);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_reference));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            athletics_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_athletics);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_athletics));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
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
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.White_FFFFFF__HOME_BULLETIN,null));
        }
        else
        {
            others_toggle.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_red_ring));
            ImageView inner = findViewById(R.id.bulletin_others);
            inner.setImageDrawable(getResources().getDrawable(R.drawable.bulletin_others));
            inner.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.Crimson_7F2F3C__HOME_BULLETIN,null));
        }
        switch_Type_Layout_Active(BulletinType.OTHERS);
        filterItems();
    }

    public void filterItems()
    {
        for(int i = 0; i < data.length; i++)
        {
            if(is_Type_Layout_Active(data[i].getType()))
                frameLayouts[i].setVisibility(View.VISIBLE);
            else
                frameLayouts[i].setVisibility(View.GONE);
            frameLayouts[i].invalidate();
        }

        boolean isallfalse = false;
        for(boolean b: is_active)
            isallfalse = isallfalse||b;

        if(!isallfalse)
        {
            for(FrameLayout i: frameLayouts)
                i.setVisibility(View.VISIBLE);
        }
    }

    public void goToHome (View view)
    {
        Intent myIntent = new Intent(BulletinActivity.this,MainActivity.class);
        BulletinActivity.this.startActivity(myIntent);
    }

    public void goToBulletin (View view)
    {

    }

    public void goToBookmarks(View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(bookmarks_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
            else
                i.clearColorFilter();
        }
    }

    public void goToSettings(View view)
    {
        for(ImageButton i: nav_btns)
        {
            if(i.equals(settings_btn))
                i.setColorFilter(ContextCompat.getColor(this,R.color.LightGray_F2F2F3__HOME));
            else
                i.clearColorFilter();
        }
    }

    boolean is_nav_bar_up = true;
    // slide the view from below itself to the current position
    public void slideUp(View view){
        if(!is_nav_bar_up)
        {
            view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }
    }


    // slide the view from its current position to below itself
    public void slideDown(View view){
        if(is_nav_bar_up)
        {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }

    }
}
