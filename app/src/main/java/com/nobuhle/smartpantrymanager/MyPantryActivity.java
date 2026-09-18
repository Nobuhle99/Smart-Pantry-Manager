package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyPantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private SmartPantryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_my_pantry
        );

        database =
                new SmartPantryDatabase(this);

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
        }
    }

    private void loadPantryItems() {

        List<PantryItem> pantryItems =
                database.getAllPantryItems();

        pantryAdapter =
                new PantryAdapter(
                        pantryItems
                );

        recyclerViewPantry.setAdapter(
                pantryAdapter
        );
    }
}