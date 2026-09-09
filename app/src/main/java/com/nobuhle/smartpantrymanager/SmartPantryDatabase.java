package com.nobuhle.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SmartPantryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public SmartPantryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Pantry table
        db.execSQL(
                "CREATE TABLE pantry_items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT" +
                        ")"
        );

        // Recipe table
        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "instructions TEXT NOT NULL" +
                        ")"
        );

        // Recipe ingredients table
        db.execSQL(
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "required_quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id)" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry_items");

        onCreate(db);
    }

    // =========================================================
    // PANTRY CRUD METHODS
    // =========================================================

    // INSERT pantry item
    public long insertPantryItem(
            PantryItem item,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", expiryDate);

        return db.insert(
                "pantry_items",
                null,
                values
        );
    }

    // SELECT all pantry items
    public List<PantryItem> getAllPantryItems() {

        List<PantryItem> pantryItems =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit " +
                        "FROM pantry_items " +
                        "ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("quantity")
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        );

                PantryItem item =
                        new PantryItem(
                                id,
                                name,
                                quantity,
                                unit
                        );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return pantryItems;
    }

    // UPDATE pantry item
    public int updatePantryItem(
            PantryItem item,
            String expiryDate) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiry_date", expiryDate);

        return db.update(
                "pantry_items",
                values,
                "id = ?",
                new String[]{
                        String.valueOf(item.getId())
                }
        );
    }

    // DELETE pantry item
    public int deletePantryItem(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        return db.delete(
                "pantry_items",
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // =========================================================
    // RECIPE METHODS
    // =========================================================

    // INSERT recipe
    public long insertRecipe(Recipe recipe) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", recipe.getName());
        values.put(
                "instructions",
                recipe.getInstructions()
        );

        return db.insert(
                "recipes",
                null,
                values
        );
    }

    // SELECT all recipes
    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, instructions " +
                        "FROM recipes " +
                        "ORDER BY id ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                String instructions =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("instructions")
                        );

                Recipe recipe =
                        new Recipe(
                                id,
                                name,
                                instructions
                        );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return recipes;
    }

    // =========================================================
    // RECIPE INGREDIENT METHODS
    // =========================================================

    // INSERT recipe ingredient
    public long insertRecipeIngredient(
            long recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName);
        values.put(
                "required_quantity",
                requiredQuantity
        );
        values.put("unit", unit);

        return db.insert(
                "recipe_ingredients",
                null,
                values
        );
    }

    // SELECT ingredients for a specific recipe
    public List<String> getRecipeIngredients(long recipeId) {

        List<String> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingredient_name, required_quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipe_id = ? " +
                        "ORDER BY id ASC",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        if (cursor.moveToFirst()) {

            do {

                String ingredientName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "ingredient_name"
                                )
                        );

                double requiredQuantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        "required_quantity"
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        "unit"
                                )
                        );

                String ingredient =
                        ingredientName +
                                " - " +
                                requiredQuantity +
                                " " +
                                unit;

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    // =========================================================
    // SEED RECIPE DATA
    // =========================================================

    public void seedRecipesIfEmpty() {

        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {
            recipeCount = cursor.getInt(0);
        }

        cursor.close();

        // Do not insert recipes if they already exist
        if (recipeCount > 0) {
            return;
        }

        // 1. Chicken Pasta
        long recipeId = insertRecipe(
                new Recipe(
                        "Chicken Pasta",
                        "Cook the pasta. Cook the chicken and onion in a pan. " +
                                "Add the tomato and mix with the cooked pasta."
                )
        );

        insertRecipeIngredient(recipeId, "chicken", 500, "g");
        insertRecipeIngredient(recipeId, "pasta", 250, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");

        // 2. Spaghetti Bolognese
        recipeId = insertRecipe(
                new Recipe(
                        "Spaghetti Bolognese",
                        "Cook the spaghetti. Brown the beef with onion. " +
                                "Add tomatoes and simmer before serving with spaghetti."
                )
        );

        insertRecipeIngredient(recipeId, "spaghetti", 250, "g");
        insertRecipeIngredient(recipeId, "beef mince", 500, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");

        // 3. Omelette
        recipeId = insertRecipe(
                new Recipe(
                        "Omelette",
                        "Beat the eggs. Cook them in a pan and add onion, " +
                                "tomato and cheese."
                )
        );

        insertRecipeIngredient(recipeId, "eggs", 3, "whole");
        insertRecipeIngredient(recipeId, "onion", 0.5, "whole");
        insertRecipeIngredient(recipeId, "tomato", 1, "whole");
        insertRecipeIngredient(recipeId, "cheese", 50, "g");

        // 4. Chicken Curry
        recipeId = insertRecipe(
                new Recipe(
                        "Chicken Curry",
                        "Cook onion and chicken. Add curry seasoning and tomato. " +
                                "Simmer until the chicken is cooked and serve with rice."
                )
        );

        insertRecipeIngredient(recipeId, "chicken", 500, "g");
        insertRecipeIngredient(recipeId, "rice", 250, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");

        // 5. Fried Rice
        recipeId = insertRecipe(
                new Recipe(
                        "Fried Rice",
                        "Cook the rice. Stir-fry the onion and vegetables. " +
                                "Add eggs and cooked rice and stir together."
                )
        );

        insertRecipeIngredient(recipeId, "rice", 300, "g");
        insertRecipeIngredient(recipeId, "eggs", 2, "whole");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "mixed vegetables", 150, "g");

        // 6. Vegetable Stir Fry
        recipeId = insertRecipe(
                new Recipe(
                        "Vegetable Stir Fry",
                        "Cut the vegetables and stir-fry them in a hot pan. " +
                                "Cook until tender and serve."
                )
        );

        insertRecipeIngredient(recipeId, "mixed vegetables", 300, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "carrot", 2, "whole");

        // 7. Tuna Sandwich
        recipeId = insertRecipe(
                new Recipe(
                        "Tuna Sandwich",
                        "Mix the tuna with mayonnaise. Place the mixture " +
                                "between slices of bread with tomato and lettuce."
                )
        );

        insertRecipeIngredient(recipeId, "bread", 4, "slices");
        insertRecipeIngredient(recipeId, "tuna", 1, "can");
        insertRecipeIngredient(recipeId, "tomato", 1, "whole");
        insertRecipeIngredient(recipeId, "lettuce", 50, "g");

        // 8. Pancakes
        recipeId = insertRecipe(
                new Recipe(
                        "Pancakes",
                        "Mix flour, eggs and milk into a smooth batter. " +
                                "Cook small portions in a lightly greased pan."
                )
        );

        insertRecipeIngredient(recipeId, "flour", 250, "g");
        insertRecipeIngredient(recipeId, "eggs", 2, "whole");
        insertRecipeIngredient(recipeId, "milk", 300, "ml");

        // 9. Tomato Pasta
        recipeId = insertRecipe(
                new Recipe(
                        "Tomato Pasta",
                        "Cook the pasta. Prepare a tomato and onion sauce " +
                                "and mix it with the pasta."
                )
        );

        insertRecipeIngredient(recipeId, "pasta", 250, "g");
        insertRecipeIngredient(recipeId, "tomato", 3, "whole");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");

        // 10. Chicken and Rice
        recipeId = insertRecipe(
                new Recipe(
                        "Chicken and Rice",
                        "Cook the rice. Season and cook the chicken. " +
                                "Serve the chicken with the rice and vegetables."
                )
        );

        insertRecipeIngredient(recipeId, "chicken", 500, "g");
        insertRecipeIngredient(recipeId, "rice", 250, "g");
        insertRecipeIngredient(recipeId, "mixed vegetables", 150, "g");

        // 11. Beef Stir Fry
        recipeId = insertRecipe(
                new Recipe(
                        "Beef Stir Fry",
                        "Slice the beef and vegetables. Stir-fry the beef " +
                                "and vegetables together until cooked."
                )
        );

        insertRecipeIngredient(recipeId, "beef", 400, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "carrot", 2, "whole");
        insertRecipeIngredient(recipeId, "mixed vegetables", 150, "g");

        // 12. Potato Curry
        recipeId = insertRecipe(
                new Recipe(
                        "Potato Curry",
                        "Cook the onion and potatoes with curry seasoning. " +
                                "Add tomato and simmer until the potatoes are soft."
                )
        );

        insertRecipeIngredient(recipeId, "potato", 500, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");

        // 13. Egg Fried Rice
        recipeId = insertRecipe(
                new Recipe(
                        "Egg Fried Rice",
                        "Cook the rice. Scramble the eggs and stir-fry with " +
                                "the rice and onion."
                )
        );

        insertRecipeIngredient(recipeId, "rice", 300, "g");
        insertRecipeIngredient(recipeId, "eggs", 3, "whole");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");

        // 14. Macaroni and Cheese
        recipeId = insertRecipe(
                new Recipe(
                        "Macaroni and Cheese",
                        "Cook the macaroni. Prepare a cheese sauce using milk " +
                                "and cheese, then mix with the macaroni."
                )
        );

        insertRecipeIngredient(recipeId, "macaroni", 250, "g");
        insertRecipeIngredient(recipeId, "cheese", 150, "g");
        insertRecipeIngredient(recipeId, "milk", 300, "ml");

        // 15. Chicken Wrap
        recipeId = insertRecipe(
                new Recipe(
                        "Chicken Wrap",
                        "Cook the chicken. Place chicken, lettuce and tomato " +
                                "inside a wrap and roll it up."
                )
        );

        insertRecipeIngredient(recipeId, "chicken", 300, "g");
        insertRecipeIngredient(recipeId, "wrap", 4, "whole");
        insertRecipeIngredient(recipeId, "lettuce", 50, "g");
        insertRecipeIngredient(recipeId, "tomato", 1, "whole");

        // 16. Vegetable Soup
        recipeId = insertRecipe(
                new Recipe(
                        "Vegetable Soup",
                        "Chop the vegetables. Add them to a pot with water " +
                                "and cook until soft."
                )
        );

        insertRecipeIngredient(recipeId, "potato", 300, "g");
        insertRecipeIngredient(recipeId, "carrot", 2, "whole");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "mixed vegetables", 200, "g");

        // 17. Beef Pasta
        recipeId = insertRecipe(
                new Recipe(
                        "Beef Pasta",
                        "Cook the pasta. Brown the beef with onion and tomato. " +
                                "Mix the sauce with the pasta."
                )
        );

        insertRecipeIngredient(recipeId, "pasta", 250, "g");
        insertRecipeIngredient(recipeId, "beef mince", 400, "g");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");

        // 18. Breakfast Scramble
        recipeId = insertRecipe(
                new Recipe(
                        "Breakfast Scramble",
                        "Scramble the eggs with onion and tomato. " +
                                "Serve with bread."
                )
        );

        insertRecipeIngredient(recipeId, "eggs", 3, "whole");
        insertRecipeIngredient(recipeId, "bread", 2, "slices");
        insertRecipeIngredient(recipeId, "onion", 0.5, "whole");
        insertRecipeIngredient(recipeId, "tomato", 1, "whole");

        // 19. Chicken Soup
        recipeId = insertRecipe(
                new Recipe(
                        "Chicken Soup",
                        "Cook the chicken with onion, potato and carrot " +
                                "in water until everything is tender."
                )
        );

        insertRecipeIngredient(recipeId, "chicken", 400, "g");
        insertRecipeIngredient(recipeId, "potato", 300, "g");
        insertRecipeIngredient(recipeId, "carrot", 2, "whole");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");

        // 20. Rice and Beans
        recipeId = insertRecipe(
                new Recipe(
                        "Rice and Beans",
                        "Cook the rice. Heat the beans with onion and tomato. " +
                                "Serve the beans with the rice."
                )
        );

        insertRecipeIngredient(recipeId, "rice", 250, "g");
        insertRecipeIngredient(recipeId, "beans", 1, "can");
        insertRecipeIngredient(recipeId, "onion", 1, "whole");
        insertRecipeIngredient(recipeId, "tomato", 2, "whole");
    }
}