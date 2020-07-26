package com.example.ahsapptest3.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahsapptest3.R;

public class Notifications_Settings_Activity extends AppCompatActivity {

    private static final String
            NOTIF_SETTING = "notif settings";

    private static final String
            GENERAL_SETTING = "1",
            SPORTS_SETTING = "2",
            ASB_SETTING = "3",
            DISTRICT_SETTING = "4",
            BULLETIN_SETTING = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_notif_layout);

        ImageView backButton = findViewById(R.id.notif_settings_header_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Switch
                general_switch = findViewById(R.id.notif_settings_general_switch),
                sports_switch = findViewById(R.id.notif_settings_sports_switch),
                asb_switch = findViewById(R.id.notif_settings_asb_switch),
                district_switch = findViewById(R.id.notif_settings_district_switch),
                bulletin_switch = findViewById(R.id.notif_settings_bulletin_switch);

        SharedPreferences sharedPrefs = getSharedPreferences(NOTIF_SETTING,MODE_PRIVATE);

        general_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(NOTIF_SETTING, MODE_PRIVATE).edit();
                editor.putBoolean(GENERAL_SETTING,isChecked).apply();
            }
        });
        general_switch.setChecked(sharedPrefs.getBoolean(GENERAL_SETTING,true));

        sports_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(NOTIF_SETTING, MODE_PRIVATE).edit();
                editor.putBoolean(SPORTS_SETTING,isChecked).apply();
            }
        });
        sports_switch.setChecked(sharedPrefs.getBoolean(SPORTS_SETTING,true));

        asb_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(NOTIF_SETTING, MODE_PRIVATE).edit();
                editor.putBoolean(ASB_SETTING,isChecked).apply();
            }
        });
        asb_switch.setChecked(sharedPrefs.getBoolean(ASB_SETTING,true));

        district_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(NOTIF_SETTING, MODE_PRIVATE).edit();
                editor.putBoolean(DISTRICT_SETTING,isChecked).apply();
            }
        });
        district_switch.setChecked(sharedPrefs.getBoolean(DISTRICT_SETTING,true));

        bulletin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(NOTIF_SETTING, MODE_PRIVATE).edit();
                editor.putBoolean(BULLETIN_SETTING,isChecked).apply();
            }
        });
        bulletin_switch.setChecked(sharedPrefs.getBoolean(BULLETIN_SETTING,true));
    }
}
