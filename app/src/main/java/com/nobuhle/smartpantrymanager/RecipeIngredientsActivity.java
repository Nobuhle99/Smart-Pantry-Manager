package com.nobuhle.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeIngredientsActivity extends AppCompatActivity {

    private SmartPantryDatabase database;

    private TextView tvIngredientsRecipeName;
    private LinearLayout recipeIngredientsContainer;
    private Button btnBackIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_ingredients
        );

        database =
                new SmartPantryDatabase(this);

        tvIngredientsRecipeName =
                findViewById(
                        R.id.tvIngredientsRecipeName
                );

        recipeIngredientsContainer =
                findViewById(
                        R.id.recipeIngredientsContainer
                );

        btnBackIngredients =
                findViewById(
                        R.id.btnBackIngredients
                );

        // Get the selected recipe ID
        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        // Get the selected recipe name
        String recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        // Display recipe name
        if (recipeName != null) {

            tvIngredientsRecipeName.setText(
                    recipeName
            );
        }

        // Load ingredients
        loadIngredients(recipeId);

        // Back button
        btnBackIngredients.setOnClickListener(
                v -> finish()
        );
    }

    private void loadIngredients(int recipeId) {

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

            ingredientText.setTextSize(17);

            ingredientText.setPadding(
                    0,
                    10,
                    0,
                    10
            );

            recipeIngredientsContainer.addView(
                    ingredientText
            );
        }
    }
}