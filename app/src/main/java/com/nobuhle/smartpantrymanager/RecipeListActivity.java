package com.nobuhle.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private SmartPantryDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);

        database =
                new SmartPantryDatabase(this);

        // Back button
        Button btnBack =
                findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Recipe RecyclerView
        recyclerViewRecipes =
                findViewById(R.id.recyclerViewRecipes);

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadRecipes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    private void loadRecipes() {

        executor.execute(() -> {
            List<Recipe> recipes =
                    database.getAllRecipes();

            runOnUiThread(() -> {
                recipeAdapter =
                        new RecipeAdapter(recipes);

                recyclerViewRecipes.setAdapter(
                        recipeAdapter
                );
            });
        });
    }
}
