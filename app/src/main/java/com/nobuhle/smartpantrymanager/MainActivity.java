package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private SmartPantryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        database = new SmartPantryDatabase(this);

        recyclerViewPantry =
                findViewById(R.id.recyclerViewPantry);

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Back button
        Button btnBackWelcome =
                findViewById(R.id.btnBackWelcome);

        btnBackWelcome.setOnClickListener(v -> finish());

        // Add Ingredient button
        Button btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // View Recipes button
        Button btnViewRecipes =
                findViewById(R.id.btnViewRecipes);

        btnViewRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RecipeListActivity.class
            );

            startActivity(intent);
        });

        // Pantry section title
        TextView tvPantrySection =
                findViewById(R.id.tvPantrySection);

        recyclerViewPantry.setVisibility(
                RecyclerView.GONE
        );

        tvPantrySection.setOnClickListener(v -> {

            if (recyclerViewPantry.getVisibility()
                    == RecyclerView.GONE) {

                recyclerViewPantry.setVisibility(
                        RecyclerView.VISIBLE
                );

            } else {

                recyclerViewPantry.setVisibility(
                        RecyclerView.GONE
                );
            }
        });

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
                new PantryAdapter(pantryItems);

        recyclerViewPantry.setAdapter(
                pantryAdapter
        );
    }
}