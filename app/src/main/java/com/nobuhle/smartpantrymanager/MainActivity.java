package com.nobuhle.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_main
        );

        // Back to Welcome
        Button btnBackWelcome =
                findViewById(
                        R.id.btnBackWelcome
                );

        btnBackWelcome.setOnClickListener(
                v -> finish()
        );

        // Add Ingredient
        Button btnAddIngredient =
                findViewById(
                        R.id.btnAddIngredient
                );

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class
                    );

            startActivity(intent);
        });

        // My Pantry
        Button btnMyPantry =
                findViewById(
                        R.id.btnMyPantry
                );

        btnMyPantry.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            MyPantryActivity.class
                    );

            startActivity(intent);
        });

        // View Recipes
        Button btnViewRecipes =
                findViewById(
                        R.id.btnViewRecipes
                );

        btnViewRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            RecipeListActivity.class
                    );

            startActivity(intent);
        });

        // Suggested Recipes
        Button btnSuggestedRecipes =
                findViewById(
                        R.id.btnSuggestedRecipes
                );

        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });
    }
}