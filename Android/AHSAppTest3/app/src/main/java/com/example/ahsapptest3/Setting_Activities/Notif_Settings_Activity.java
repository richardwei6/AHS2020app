package com.example.ahsapptest3.Setting_Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.R;
import com.example.ahsapptest3.Settings;

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

        final Switch
                asb_switch = findViewById(R.id.notif_settings_asb_switch),
                district_switch = findViewById(R.id.notif_settings_district_switch),
                general_switch = findViewById(R.id.notif_settings_general_switch),
                bulletin_switch = findViewById(R.id.notif_settings_bulletin_switch);

        final Settings settings = new Settings(getApplicationContext(), new Settings.OnNotifOptionChanged() {
            @Override
            public void onOptionChanged(Settings.NotifOption option, boolean currentValue) {
                /*switch(option) {
<<<<<<< HEAD:AHSAppTest3/app/src/main/java/com/example/ahsapptest3/Setting_Activities/Notif_Settings_Activity.java

=======
                    case GENERAL:
                        general_switch.setChecked(currentValue);
                        break;
>>>>>>> 56bdcf7d8638e17e23ef945e9c16f0b288b6db4e:AHSAppTest3/app/src/main/java/com/example/ahsapptest3/Setting_Activities/Notifications_Settings_Activity.java
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
                }*/
            }
        });



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
