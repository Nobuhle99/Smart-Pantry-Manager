package com.nobuhle.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    private SmartPantryDatabase database;

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

    private void loadRecipes() {

        List<Recipe> recipes =
                database.getAllRecipes();

        recipeAdapter =
                new RecipeAdapter(recipes);

        recyclerViewRecipes.setAdapter(
                recipeAdapter
        );
    }
}