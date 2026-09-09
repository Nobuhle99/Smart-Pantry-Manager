package com.nobuhle.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SmartPantryDatabase extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";

    public static final String PANTRY_ID = "id";
    public static final String PANTRY_NAME = "name";
    public static final String PANTRY_QUANTITY = "quantity";
    public static final String PANTRY_UNIT = "unit";
    public static final String PANTRY_EXPIRY_DATE = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String RECIPE_INGREDIENT_ID = "id";
    public static final String RECIPE_INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String RECIPE_INGREDIENT_NAME = "ingredient_name";
    public static final String RECIPE_INGREDIENT_QUANTITY = "required_quantity";
    public static final String RECIPE_INGREDIENT_UNIT = "unit";

    public SmartPantryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create pantry table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PANTRY_NAME + " TEXT NOT NULL, " +
                PANTRY_QUANTITY + " REAL NOT NULL, " +
                PANTRY_UNIT + " TEXT NOT NULL, " +
                PANTRY_EXPIRY_DATE + " TEXT" +
                ")";

        db.execSQL(createPantryTable);

        // Create recipes table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPE_NAME + " TEXT NOT NULL, " +
                RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                ")";

        db.execSQL(createRecipesTable);

        // Create recipe ingredients table
        String createRecipeIngredientsTable = "CREATE TABLE " +
                TABLE_RECIPE_INGREDIENTS + " (" +
                RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPE_INGREDIENT_RECIPE_ID + " INTEGER NOT NULL, " +
                RECIPE_INGREDIENT_NAME + " TEXT NOT NULL, " +
                RECIPE_INGREDIENT_QUANTITY + " REAL NOT NULL, " +
                RECIPE_INGREDIENT_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + RECIPE_INGREDIENT_RECIPE_ID + ") " +
                "REFERENCES " + TABLE_RECIPES + "(" + RECIPE_ID + ")" +
                ")";

        db.execSQL(createRecipeIngredientsTable);
    }

    // INSERT pantry item
    public long insertPantryItem(PantryItem item, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PANTRY_NAME, item.getName());
        values.put(PANTRY_QUANTITY, item.getQuantity());
        values.put(PANTRY_UNIT, item.getUnit());

        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            values.put(PANTRY_EXPIRY_DATE, expiryDate);
        } else {
            values.putNull(PANTRY_EXPIRY_DATE);
        }

        long result = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return result;
    }

    // UPDATE pantry item
    public int updatePantryItem(PantryItem item, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PANTRY_NAME, item.getName());
        values.put(PANTRY_QUANTITY, item.getQuantity());
        values.put(PANTRY_UNIT, item.getUnit());

        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            values.put(PANTRY_EXPIRY_DATE, expiryDate);
        } else {
            values.putNull(PANTRY_EXPIRY_DATE);
        }

        int result = db.update(
                TABLE_PANTRY,
                values,
                PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return result;
    }

    // DELETE pantry item
    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_PANTRY,
                PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }

    // SELECT all pantry items
    public List<PantryItem> getAllPantryItems() {

        List<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                PANTRY_ID + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(PANTRY_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(PANTRY_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(PANTRY_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(PANTRY_UNIT)
                );

                PantryItem item = new PantryItem(
                        name,
                        quantity,
                        unit
                );

                item.setId(id);

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPE_INGREDIENTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_PANTRY
        );

        onCreate(db);
    }
}