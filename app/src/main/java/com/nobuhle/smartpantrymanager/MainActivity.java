package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private PantryAdapter pantryAdapter;
    private SmartPantryDatabase database;

    private LinearLayout layoutPantryHeader;
    private TextView tvPantryArrow;

    private boolean pantryExpanded = false;

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

        Button btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        Button btnViewRecipes =
                findViewById(R.id.btnViewRecipes);

        layoutPantryHeader =
                findViewById(R.id.layoutPantryHeader);

        tvPantryArrow =
                findViewById(R.id.tvPantryArrow);

        // Add Ingredient button
        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // View Recipes button
        btnViewRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RecipeListActivity.class
            );

            startActivity(intent);
        });

        // Pantry dropdown
        layoutPantryHeader.setOnClickListener(v -> {

            pantryExpanded = !pantryExpanded;

            if (pantryExpanded) {

                recyclerViewPantry.setVisibility(View.VISIBLE);
                tvPantryArrow.setText("▲");

            } else {

                recyclerViewPantry.setVisibility(View.GONE);
                tvPantryArrow.setText("▼");
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