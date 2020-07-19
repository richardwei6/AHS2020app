package com.example.ahsapptest3.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ahsapptest3.Bulletin;
import com.example.ahsapptest3.News;
import com.example.ahsapptest3.Navigation;
import com.example.ahsapptest3.R;
import com.example.ahsapptest3.Saved;
import com.example.ahsapptest3.Tester;

public class SettingsActivity extends AppCompatActivity implements Navigation {

    private static final String FONT_SETTING = "font_setting";
    private static final String FONT_SIZE = "1";

    public static int font_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        SharedPreferences sharedPrefs = getSharedPreferences(FONT_SETTING,MODE_PRIVATE);

        SeekBar font_SeekBar = findViewById(R.id.settings_FontSize_SeekBar);
        font_SeekBar.setProgress(sharedPrefs.getInt(FONT_SIZE,20));

        font_size = SeekBarProgress_ToFontSize(font_SeekBar.getProgress());
        final TextView fontText = findViewById(R.id.settings_FontSize_Text);
        String fontStr = "Font Size: \t" + font_size;
        fontText.setText(fontStr);

        font_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = getSharedPreferences(FONT_SETTING, MODE_PRIVATE).edit();
                editor.putInt(FONT_SIZE, progress).apply();
                font_size = SeekBarProgress_ToFontSize(progress);
                String fontStr = "Font Size: \t" + font_size;
                fontText.setText(fontStr);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        LinearLayout notifLayout = findViewById(R.id.settings_notif_LinearLayout);
        notifLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, Notifications_Settings_Activity.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });

        LinearLayout creditsLayout = findViewById(R.id.settings_credits_LinearLayout);
        creditsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, Credits_Activity.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });

        LinearLayout termsLayout = findViewById(R.id.settings_terms_LinearLayout);
        termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, Terms_And_Agreements_Activity.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });

        Button goToTester = findViewById(R.id.settings_tester_button);
        goToTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, Tester.class);
                SettingsActivity.this.startActivity(myIntent);
            }
        });
    }

    public static int SeekBarProgress_ToFontSize(int seekBar_progress)
    {
        return (int)(26*(seekBar_progress/4.0) + 10);
    }

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(SettingsActivity.this, News.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(SettingsActivity.this, Bulletin.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(SettingsActivity.this, Saved.class);
        SettingsActivity.this.startActivity(myIntent);
    }

    @Override
    public void goToSettings() {

    }

    @Override
    public int getScrollingViewId() {
        return R.id.settings_ScrollView;
    }
}
