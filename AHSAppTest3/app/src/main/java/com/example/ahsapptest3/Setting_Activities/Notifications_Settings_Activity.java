package com.example.ahsapptest3.Setting_Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.R;
import com.example.ahsapptest3.Settings;

public class Notifications_Settings_Activity extends FullScreenActivity {

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

        final Switch
                general_switch = findViewById(R.id.notif_settings_general_switch),
                sports_switch = findViewById(R.id.notif_settings_sports_switch),
                asb_switch = findViewById(R.id.notif_settings_asb_switch),
                district_switch = findViewById(R.id.notif_settings_district_switch),
                bulletin_switch = findViewById(R.id.notif_settings_bulletin_switch);

        final Settings settings = new Settings(getApplicationContext(), new Settings.OnNotifOptionChanged() {
            @Override
            public void onOptionChanged(Settings.NotifOption option, boolean currentValue) {
                switch(option) {
                    case GENERAL:
                        general_switch.setChecked(currentValue);
                        break;
                    case ASB:
                        asb_switch.setChecked(currentValue);
                        break;
                    case SPORTS:
                        sports_switch.setChecked(currentValue);
                        break;
                    case DISTRICT:
                        district_switch.setChecked(currentValue);
                        break;
                    case BULLETIN:
                        bulletin_switch.setChecked(currentValue);
                        break;
                }
            }
        });

        general_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.GENERAL, isChecked, true);
            }
        });
        general_switch
                .setChecked(settings.isNotifSettingsSelected(Settings.NotifOption.GENERAL));

        sports_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.SPORTS, isChecked, true);
            }
        });
        sports_switch
                .setChecked(settings.isNotifSettingsSelected(Settings.NotifOption.SPORTS));

        asb_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.ASB, isChecked, true);
            }
        });
        asb_switch
                .setChecked(settings.isNotifSettingsSelected(Settings.NotifOption.ASB));

        district_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.DISTRICT, isChecked, true);
            }
        });
        district_switch
                .setChecked(settings.isNotifSettingsSelected(Settings.NotifOption.DISTRICT));

        bulletin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.updateNotifSettings(Settings.NotifOption.BULLETIN, isChecked, true);
            }
        });
        bulletin_switch
                .setChecked(settings.isNotifSettingsSelected(Settings.NotifOption.BULLETIN));
    }
}
