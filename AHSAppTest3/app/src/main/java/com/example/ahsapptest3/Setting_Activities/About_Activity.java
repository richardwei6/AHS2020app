package com.example.ahsapptest3.Setting_Activities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ahsapptest3.Misc.FullScreenActivity;
import com.example.ahsapptest3.R;

public class About_Activity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_about_layout);

        ImageView backButton = findViewById(R.id.about_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout outerLayout = findViewById(R.id.about_outerLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) outerLayout.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }
}
