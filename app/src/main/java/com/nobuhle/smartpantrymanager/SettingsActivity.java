package com.nobuhle.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryNotifications;

    private SharedPreferences preferences;

    private static final String PREFS_NAME =
            "SmartPantrySettings";

    private static final String EXPIRY_NOTIFICATIONS_KEY =
            "expiry_notifications_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_settings
        );

        // Get the expiry notification switch
        switchExpiryNotifications =
                findViewById(
                        R.id.switchExpiryNotifications
                );

        // Get saved settings
        preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        // Load the saved expiry notification setting
        boolean expiryNotificationsEnabled =
                preferences.getBoolean(
                        EXPIRY_NOTIFICATIONS_KEY,
                        false
                );

        switchExpiryNotifications.setChecked(
                expiryNotificationsEnabled
        );

        // Save the setting whenever the switch changes
        switchExpiryNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    EXPIRY_NOTIFICATIONS_KEY,
                                    isChecked
                            )
                            .apply();
                }
        );

        // Back button
        Button btnBack =
                findViewById(
                        R.id.btnBack
                );

        btnBack.setOnClickListener(
                v -> finish()
        );
    }
}