package com.nobuhle.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText edtIngredientName;
    private EditText edtQuantity;
    private EditText edtUnit;
    private EditText edtExpiryDate;
    private Button btnSaveIngredient;
    private Button btnBack;

    private SmartPantryDatabase database;

    // Used to determine whether we are adding or editing
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_edit
        );

        // Connect Java variables to XML fields
        edtIngredientName =
                findViewById(
                        R.id.edtIngredientName
                );

        edtQuantity =
                findViewById(
                        R.id.edtQuantity
                );

        edtUnit =
                findViewById(
                        R.id.edtUnit
                );

        edtExpiryDate =
                findViewById(
                        R.id.edtExpiryDate
                );

        btnSaveIngredient =
                findViewById(
                        R.id.btnSaveIngredient
                );

        btnBack =
                findViewById(
                        R.id.btnBack
                );

        // Create database object
        database =
                new SmartPantryDatabase(this);

        // Check whether this screen was opened for editing
        if (getIntent().hasExtra("ingredient_id")) {

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String name =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double quantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String unit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            // Put existing information into the fields
            edtIngredientName.setText(name);

            edtQuantity.setText(
                    String.valueOf(quantity)
            );

            edtUnit.setText(unit);

            // Change button text
            btnSaveIngredient.setText(
                    "Update Ingredient"
            );
        }

        // Save or update when button is clicked
        btnSaveIngredient.setOnClickListener(
                v -> saveIngredient()
        );

        // Back without saving
        btnBack.setOnClickListener(
                v -> finish()
        );
    }

    private void saveIngredient() {

        String name =
                edtIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                edtQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                edtUnit
                        .getText()
                        .toString()
                        .trim();

        String expiryDate =
                edtExpiryDate
                        .getText()
                        .toString()
                        .trim();

        // Basic validation
        if (name.isEmpty()) {

            edtIngredientName.setError(
                    "Enter an ingredient name"
            );

            edtIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            edtQuantity.setError(
                    "Enter a quantity"
            );

            edtQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {

            edtUnit.setError(
                    "Enter a unit"
            );

            edtUnit.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            edtQuantity.setError(
                    "Enter a valid quantity"
            );

            edtQuantity.requestFocus();
            return;
        }

        // Make sure quantity is positive
        if (quantity <= 0) {

            edtQuantity.setError(
                    "Quantity must be greater than 0"
            );

            edtQuantity.requestFocus();
            return;
        }

        // Create PantryItem
        PantryItem pantryItem =
                new PantryItem(
                        name,
                        quantity,
                        unit
                );

        // =====================================================
        // EDIT MODE
        // =====================================================

        if (ingredientId != -1) {

            pantryItem.setId(
                    ingredientId
            );

            int result =
                    database.updatePantryItem(
                            pantryItem,
                            expiryDate
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }

            return;
        }

        // =====================================================
        // ADD MODE
        // =====================================================

        /*
         * Check whether this ingredient already exists
         * in the pantry with the same unit.
         *
         * Example:
         *
         * Existing:
         * Chicken - 300 g
         *
         * Adding:
         * Chicken - 200 g
         *
         * Result:
         * Chicken - 500 g
         */

        List<PantryItem> existingItems =
                database.getAllPantryItems();

        PantryItem matchingItem = null;

        for (PantryItem existingItem : existingItems) {

            String existingName =
                    existingItem
                            .getName()
                            .trim();

            String existingUnit =
                    existingItem
                            .getUnit()
                            .trim();

            // Compare ingredient names without
            // considering upper/lower case
            boolean sameName =
                    existingName.equalsIgnoreCase(
                            name
                    );

            // Units must match
            boolean sameUnit =
                    existingUnit.equalsIgnoreCase(
                            unit
                    );

            if (sameName && sameUnit) {

                matchingItem =
                        existingItem;

                break;
            }
        }

        // =====================================================
        // DUPLICATE FOUND
        // =====================================================

        if (matchingItem != null) {

            double newQuantity =
                    matchingItem.getQuantity()
                            + quantity;

            matchingItem.setQuantity(
                    newQuantity
            );

            int result =
                    database.updatePantryItem(
                            matchingItem,
                            expiryDate
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        name + " quantity updated to "
                                + newQuantity
                                + " "
                                + unit,
                        Toast.LENGTH_LONG
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update existing ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            // =================================================
            // NEW INGREDIENT
            // =================================================

            long result =
                    database.insertPantryItem(
                            pantryItem,
                            expiryDate
                    );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully!",
                        Toast.LENGTH_SHORT
                ).show();


                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to save ingredient.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}