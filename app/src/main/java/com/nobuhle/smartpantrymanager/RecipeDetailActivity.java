package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private SmartPantryDatabase database;

    private TextView tvDetailRecipeName;
    private TextView tvDetailInstructions;
    private LinearLayout ingredientsContainer;

    private Button btnTryRecipe;
    private Button btnViewIngredients;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );

        database =
                new SmartPantryDatabase(this);

        tvDetailRecipeName =
                findViewById(
                        R.id.tvDetailRecipeName
                );

        tvDetailInstructions =
                findViewById(
                        R.id.tvDetailInstructions
                );

        ingredientsContainer =
                findViewById(
                        R.id.ingredientsContainer
                );

        btnTryRecipe =
                findViewById(
                        R.id.btnTryRecipe
                );

        btnViewIngredients =
                findViewById(
                        R.id.btnViewIngredients
                );

        btnBack =
                findViewById(
                        R.id.btnBack
                );

        // Get recipe information from the previous screen
        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        String recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        String recipeInstructions =
                getIntent().getStringExtra(
                        "recipe_instructions"
                );

        // Display recipe name
        if (recipeName != null) {

            tvDetailRecipeName.setText(
                    recipeName
            );
        }

        // Display instructions
        if (recipeInstructions != null) {

            tvDetailInstructions.setText(
                    recipeInstructions
            );
        }

        // Load recipe ingredients
        loadRecipeIngredients(recipeId);

        // Try Recipe button
        btnTryRecipe.setOnClickListener(v -> {

            android.widget.Toast.makeText(
                    RecipeDetailActivity.this,
                    "Recipe selected: " + recipeName,
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });

        // View Ingredients button
        btnViewIngredients.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            RecipeDetailActivity.this,
                            RecipeIngredientsActivity.class
                    );

            intent.putExtra(
                    "recipe_id",
                    recipeId
            );

            intent.putExtra(
                    "recipe_name",
                    recipeName
            );

            startActivity(intent);
        });

        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRecipeIngredients(int recipeId) {

        if (recipeId == -1) {
            return;
        }

        List<String> ingredients =
                database.getRecipeIngredients(
                        recipeId
                );

        for (String ingredient : ingredients) {

            TextView ingredientText =
                    new TextView(this);

            ingredientText.setText(
                    "• " + ingredient
            );

            ingredientText.setTextSize(16);

            ingredientText.setPadding(
                    0,
                    6,
                    0,
                    6
            );

            ingredientsContainer.addView(
                    ingredientText
            );
        }
    }
}