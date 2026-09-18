package com.nobuhle.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSuggestedRecipes;
    private TextView tvNoSuggestedRecipes;
    private SmartPantryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        database =
                new SmartPantryDatabase(this);

        recyclerViewSuggestedRecipes =
                findViewById(
                        R.id.recyclerViewSuggestedRecipes
                );

        tvNoSuggestedRecipes =
                findViewById(
                        R.id.tvNoSuggestedRecipes
                );

        Button btnBack =
                findViewById(
                        R.id.btnBack
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        recyclerViewSuggestedRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null) {
            loadSuggestedRecipes();
        }
    }

    private void loadSuggestedRecipes() {

        // Get all ingredients currently in the pantry
        List<PantryItem> pantryItems =
                database.getAllPantryItems();

        // Get all available recipes
        List<Recipe> allRecipes =
                database.getAllRecipes();

        // Store only recipes that match the pantry
        List<Recipe> suggestedRecipes =
                new ArrayList<>();

        // Check every recipe
        for (Recipe recipe : allRecipes) {

            List<String> recipeIngredients =
                    database.getRecipeIngredients(
                            recipe.getId()
                    );

            boolean matches =
                    RecipeMatcher.isRecipeMatch(
                            recipeIngredients,
                            pantryItems
                    );

            if (matches) {

                suggestedRecipes.add(
                        recipe
                );
            }
        }

        // Display matching recipes
        if (suggestedRecipes.isEmpty()) {

            recyclerViewSuggestedRecipes.setVisibility(
                    RecyclerView.GONE
            );

            tvNoSuggestedRecipes.setVisibility(
                    TextView.VISIBLE
            );

        } else {

            recyclerViewSuggestedRecipes.setVisibility(
                    RecyclerView.VISIBLE
            );

            tvNoSuggestedRecipes.setVisibility(
                    TextView.GONE
            );

            RecipeAdapter recipeAdapter =
                    new RecipeAdapter(
                            suggestedRecipes
                    );

            recyclerViewSuggestedRecipes.setAdapter(
                    recipeAdapter
            );
        }
    }
}