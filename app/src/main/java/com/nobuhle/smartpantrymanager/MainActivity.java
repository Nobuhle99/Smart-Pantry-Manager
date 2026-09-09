package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        // Add the starter recipes if the recipe table is empty
        database.seedRecipesIfEmpty();

        recyclerViewPantry =
                findViewById(R.id.recyclerViewPantry);

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        Button btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
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