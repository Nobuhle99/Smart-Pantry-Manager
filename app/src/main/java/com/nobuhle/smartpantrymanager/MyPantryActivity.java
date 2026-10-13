package com.nobuhle.smartpantrymanager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private SmartPantryDatabase database;

    private SharedPreferences preferences;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final String PREFS_NAME =
            "SmartPantrySettings";

    private static final String EXPIRY_NOTIFICATIONS_KEY =
            "expiry_notifications_enabled";

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE =
            1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_my_pantry
        );

        database =
                new SmartPantryDatabase(this);

        preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        ExpiryNotificationHelper.createNotificationChannel(
                this
        );

        recyclerViewPantry =
                findViewById(
                        R.id.recyclerViewPantry
                );

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Add Ingredient button
        Button btnAddIngredient =
                findViewById(
                        R.id.btnAddIngredient
                );

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MyPantryActivity.this,
                            AddEditIngredientActivity.class
                    );

            startActivity(intent);
        });

        // Suggested Recipes button
        Button btnSuggestedRecipes =
                findViewById(
                        R.id.btnSuggestedRecipes
                );

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MyPantryActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });

        // Back button
        Button btnBack =
                findViewById(
                        R.id.btnBack
                );

        btnBack.setOnClickListener(v -> finish());

        loadPantryItems();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null) {
            loadPantryItems();
            checkExpiryNotifications();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    private void loadPantryItems() {

        executor.execute(() -> {
            List<PantryItem> pantryItems =
                    database.getAllPantryItems();

            runOnUiThread(() -> {
                pantryAdapter =
                        new PantryAdapter(
                                pantryItems
                        );

                recyclerViewPantry.setAdapter(
                        pantryAdapter
                );
            });
        });
    }

    private void checkExpiryNotifications() {

        boolean notificationsEnabled =
                preferences.getBoolean(
                        EXPIRY_NOTIFICATIONS_KEY,
                        false
                );

        if (!notificationsEnabled) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );

                return;
            }
        }

        executor.execute(() -> {
            List<ExpiringIngredient> expiringIngredients =
                    database.getExpiringIngredients(3);

            for (ExpiringIngredient ingredient :
                    expiringIngredients) {

                ExpiryNotificationHelper.showExpiryNotification(
                        MyPantryActivity.this,
                        ingredient.getName(),
                        ingredient.getDaysRemaining()
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkExpiryNotifications();
            }
        }
    }
}
