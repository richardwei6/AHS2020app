package com.example.ahsapptest3.Setting_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.Bulletin_Activity;
import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Navigation;
import com.example.ahsapptest3.News_Activity;
import com.example.ahsapptest3.NotifBtn;
import com.example.ahsapptest3.Notif_Activity;
import com.example.ahsapptest3.R;
import com.example.ahsapptest3.Saved_Activity;
import com.example.ahsapptest3.Settings;
import com.example.ahsapptest3.Tester;

public class Settings_Activity extends FullScreenActivity implements Navigation, NotifBtn.Navigation, Settings.OnTextSizeOptionChanged{
    private static final String TAG = "Settings_Activity";
    private CheckBox notifCheckBox;
    /*private boolean changeSettings = true; */// tries to avoid infinite loop with onResume set checked and listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            notifCheckBox.setChecked(new Settings(getApplicationContext()).getAllNotifSetting());
            /*changeSettings = false;*/
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        /*if(getIntent().hasExtra("bundle")) {

        }*/

        final Settings settings = new Settings(getApplicationContext(), this);

        notifCheckBox = findViewById(R.id.settings_notif_checkBox);
        notifCheckBox.setChecked(settings.getAllNotifSetting());
        notifCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*Log.d(TAG, "Change settings values " + changeSettings);*/
                settings.updateAllSetting(isChecked, true);

            }
        });

        SeekBar font_SeekBar = findViewById(R.id.settings_FontSize_SeekBar);
        font_SeekBar.setProgress(Settings.getCurrentTextSizeOption().getNumCode());

        final TextView fontText = findViewById(R.id.settings_FontSize_Text);
        setFontSizeText(fontText);

        font_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.updateTextSize(Settings.TextSizeOption.getOptionFromNumCode(progress));
                setFontSizeText(fontText);
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
                Intent myIntent = new Intent(Settings_Activity.this, Notif_Settings_Activity.class);
                Settings_Activity.this.startActivityForResult(myIntent, 0);
            }
        });

        LinearLayout aboutLayout = findViewById(R.id.settings_about_LinearLayout);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings_Activity.this, About_Activity.class);
                Settings_Activity.this.startActivity(myIntent);
            }
        });

        LinearLayout termsLayout = findViewById(R.id.settings_terms_LinearLayout);
        termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings_Activity.this, Terms_Activity.class);
                Settings_Activity.this.startActivity(myIntent);
            }
        });

        Button goToTester = findViewById(R.id.settings_tester_button);
        goToTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings_Activity.this, Tester.class);
                Settings_Activity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void goToHome() {
        Intent myIntent = new Intent(Settings_Activity.this, News_Activity.class);
        Settings_Activity.this.startActivity(myIntent);
    }

    @Override
    public void goToBulletin() {
        Intent myIntent = new Intent(Settings_Activity.this, Bulletin_Activity.class);
        Settings_Activity.this.startActivity(myIntent);
    }

    @Override
    public void goToSaved() {
        Intent myIntent = new Intent(Settings_Activity.this, Saved_Activity.class);
        Settings_Activity.this.startActivity(myIntent);
    }

    @Override
    public void goToSettings() {

    }

    @Override
    public int getScrollingViewId() {
        return R.id.settings_ScrollView;
    }

    @Override
    public HighlightOption getHighlightOption() {
        return HighlightOption.SETTINGS;
    }

    @Override
    public void goToNotif() {
        Intent myIntent = new Intent(Settings_Activity.this, Notif_Activity.class);
        Settings_Activity.this.startActivity(myIntent);
    }

    @Override
    public void run() {
        Intent intent = getIntent();
        Bundle tempBundle = new Bundle();
        intent.putExtra("bundle", tempBundle);

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }

    public void setFontSizeText(TextView fontSizeTextView) {
        String start = "Font Size: \t";
        Spannable sizeText = new SpannableString(Settings.getCurrentTextSizeOption().getName());
        int colorResourceID;
        switch(Settings.getCurrentTextSizeOption()) {
            case SMALL:
                colorResourceID = R.color.GoldenYellow_E0C260;
                break;
            case DEFAULT:
                colorResourceID = R.color.AngryRed_GoldenYellow_Midpoint;
                break;
            case LARGE:
                colorResourceID = R.color.AngryRed_9F0C0C;
                break;
            case MASSIVE:
            default:
                colorResourceID = R.color.AngryRed_GoldenYellow_MidpointExtrapolate;
        }
        sizeText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, colorResourceID)), 0, sizeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        fontSizeTextView.setText(start);
        fontSizeTextView.append(sizeText);
    }
}
