package com.example.ahsapptest3.Helper_Code;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreenActivity extends AppCompatActivity {
    private static final String TAG = "FullScreenActivity";
    protected void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        hideSystemUI();
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideSystemUI();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        hideSystemUI();
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "config changed");
        /*hideSystemUI();*/
    }


}
