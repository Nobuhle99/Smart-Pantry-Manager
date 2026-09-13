package com.nobuhle.smartpantrymanager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );

        database =
                new SmartPantryDatabase(this);

        Button btnBack =
                findViewById(R.id.btnBack);

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

        btnBack.setOnClickListener(v -> finish());

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

        // Display recipe information
        tvDetailRecipeName.setText(
                recipeName
        );

        tvDetailInstructions.setText(
                recipeInstructions
        );

        // Load recipe ingredients
        loadRecipeIngredients(recipeId);
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