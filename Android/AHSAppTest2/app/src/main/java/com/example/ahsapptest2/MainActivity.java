package com.example.ahsapptest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ahsapptest2.Home_Page_Code.Home_Page_Fragment;

public class MainActivity extends AppCompatActivity {

    /*private static final int NUM_PAGES = 5;
    private ViewPager ausdNewsPager;
    private ViewPager sportsPager;*/

    ImageButton home_button, bulletin_button, notifications_button, settings_button;
    ImageButton[] nav_buttons;
    Fragment currentFrag;
    //@SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        home_button = findViewById(R.id.home_button);
        bulletin_button = findViewById(R.id.pinned_button);
        notifications_button = findViewById(R.id.notif_button);
        settings_button = findViewById(R.id.settings_button);

        nav_buttons = new ImageButton[] {
                home_button,
                bulletin_button,
                notifications_button,
                settings_button
        };

        currentFrag = getSupportFragmentManager().findFragmentById(R.id.home_fragment);


        //bulletin_button.setColorFilter(R.color.DarkLogoRed);
        /*

        // viewpager
        // dots - https://github.com/ongakuer/CircleIndicator

        ausdNewsPager = (ViewPager) findViewById(R.id.ausdNewsPager);
        ausdNewsPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        CircleIndicator ausdDots = (CircleIndicator) findViewById(R.id.ausdindicator);
        ausdDots.setViewPager(ausdNewsPager);

        sportsPager = (ViewPager) findViewById(R.id.sportsPager);
        sportsPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        CircleIndicator sportsDots = (CircleIndicator) findViewById(R.id.sportsindicator);
        sportsDots.setViewPager(sportsPager);
    }
    @Override
    public void onBackPressed() {
        if (ausdNewsPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            ausdNewsPager.setCurrentItem(ausdNewsPager.getCurrentItem() - 1);
        }
        if (sportsPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            sportsPager.setCurrentItem(sportsPager.getCurrentItem() - 1);
        }
    }

    public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            /*switch (position) {
                case 0:
                    return new Article_MainPage();
            }
            return null;*'/
            return new Article_MainPage();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        */

    }
    public void goToHome(View view)
    {
        if(!(currentFrag instanceof Home_Page_Fragment)) { //if we decide to animate the changes, we wouldn't want to animate it if the user goes to the same page
            for (ImageButton button : nav_buttons) {
                if (button.equals(home_button))
                    button.setColorFilter(ContextCompat.getColor(this, R.color.DarkLogoRed_791314__Everywhere));
                else
                    button.setColorFilter(ContextCompat.getColor(this, R.color.Gray_D6D6D6__Home_Bulletin));
            }
            Home_Page_Fragment newFrag = new Home_Page_Fragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(currentFrag.getId(), newFrag)
                    .addToBackStack(null)//Lets user use the back button to go back
                    .commit();
            currentFrag = newFrag;
        }
    }
    public void goToBulletin(View view)
    {
        if(!(currentFrag instanceof Bulletin_Page_Fragment)) {
            for (ImageButton button : nav_buttons) {
                if (button.equals(bulletin_button))
                    button.setColorFilter(ContextCompat.getColor(this, R.color.DarkLogoRed_791314__Everywhere));
                else
                    button.setColorFilter(ContextCompat.getColor(this, R.color.Gray_D6D6D6__Home_Bulletin));
            }
            Bulletin_Page_Fragment newFrag = new Bulletin_Page_Fragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(currentFrag.getId(), newFrag)
                    .addToBackStack(null)//Lets user use the back button to go back
                    .commit();
            currentFrag = newFrag;
        }
    }
    public void goToNotifications(View view)
    {
        for(ImageButton button: nav_buttons) {
            if (button.equals(notifications_button))
                button.setColorFilter(ContextCompat.getColor(this, R.color.DarkLogoRed_791314__Everywhere));
            else
                button.setColorFilter(ContextCompat.getColor(this, R.color.Gray_D6D6D6__Home_Bulletin));
        }
    }
    public void goToSettings(View view)
    {
        for(ImageButton button: nav_buttons) {
            if (button.equals(settings_button))
                button.setColorFilter(ContextCompat.getColor(this, R.color.DarkLogoRed_791314__Everywhere));
            else
                button.setColorFilter(ContextCompat.getColor(this, R.color.Gray_D6D6D6__Home_Bulletin));
        }
    }
    //TODO: fix the colors right above
    //TODO: fix colors.xml and sort out that mess before it gets even bigger
    //TODO: kind of make bulletin page
    //TODO: padding for main buttons

    public void onBulletinSeniorsClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onSeniorsClick(view);
    }

    public void onBulletinEventsClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onEventsClick(view);
    }

    public void onBulletinCollegeClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onCollegeClick(view);
    }

    public void onBulletinReferenceClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onReferenceClick(view);
    }

    public void onBulletinAthleticsClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onAthleticsClick(view);
    }

    public void onBulletinOtherClick(View view){
        if (currentFrag instanceof Bulletin_Page_Fragment)
            ((Bulletin_Page_Fragment) currentFrag).onOtherClick(view);
    }
}

