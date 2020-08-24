package com.hsappdev.ahs.Setting_Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.widget.SwitchCompat;

import com.hsappdev.ahs.Misc.FullScreenActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.Settings;

public class Notif_Settings_Activity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_notif_layout);

        ImageView backButton = findViewById(R.id.notif_settings_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SwitchCompat
                asb_switch = findViewById(R.id.notif_settings_asb_switch),
                district_switch = findViewById(R.id.notif_settings_district_switch),
                general_switch = findViewById(R.id.notif_settings_general_switch),
                bulletin_switch = findViewById(R.id.notif_settings_bulletin_switch);

        final Settings settings = new Settings(getApplicationContext());

        asb_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.ASB, isChecked, true);
            }
        });
        asb_switch
                .setChecked(settings.getNotifSetting(Settings.NotifOption.ASB));

        district_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.DISTRICT, isChecked, true);
            }
        });
        district_switch
                .setChecked(settings.getNotifSetting(Settings.NotifOption.DISTRICT));

        general_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.GENERAL, isChecked, true);
            }
        });
        general_switch
                .setChecked(settings.getNotifSetting(Settings.NotifOption.GENERAL));

        bulletin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.BULLETIN, isChecked, true);
            }
        });
        bulletin_switch
                .setChecked(settings.getNotifSetting(Settings.NotifOption.BULLETIN));
    }
}
