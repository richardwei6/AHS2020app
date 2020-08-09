package com.example.ahsapptest3.Setting_Activities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.R;

public class Credits_Activity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_credits_layout);

        ImageView backButton = findViewById(R.id.credits_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout outerLayout = findViewById(R.id.credits_outerLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) outerLayout.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();
    }
}
