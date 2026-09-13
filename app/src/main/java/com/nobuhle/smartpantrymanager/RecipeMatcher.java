package com.nobuhle.smartpantrymanager;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    /**
     * Checks whether the pantry contains everything
     * required for a recipe.
     *
     * @param recipeIngredients ingredients required by the recipe
     * @param pantryItems ingredients currently available in the pantry
     * @return true if all required ingredients are available
     */
    public static boolean isRecipeMatch(
            List<String> recipeIngredients,
            List<PantryItem> pantryItems) {

        if (recipeIngredients == null ||
                recipeIngredients.isEmpty()) {

            return false;
        }

        if (pantryItems == null ||
                pantryItems.isEmpty()) {

            return false;
        }

        // Check every ingredient required by the recipe
        for (String recipeIngredient : recipeIngredients) {

            IngredientRequirement requirement =
                    parseIngredient(recipeIngredient);

            if (requirement == null) {
                return false;
            }

            boolean ingredientMatched = false;

            // Compare with every pantry item
            for (PantryItem pantryItem : pantryItems) {

                if (!namesMatch(
                        requirement.name,
                        pantryItem.getName())) {

                    continue;
                }

                if (!unitsMatch(
                        requirement.unit,
                        pantryItem.getUnit())) {

                    continue;
                }

                // Pantry quantity must be equal to or greater
                // than the quantity required by the recipe
                if (pantryItem.getQuantity()
                        >= requirement.quantity) {

                    ingredientMatched = true;
                    break;
                }
            }

            // If even one ingredient is missing,
            // the recipe does not match.
            if (!ingredientMatched) {
                return false;
            }
        }

        // Every required ingredient was found
        return true;
    }

    /**
     * Handles basic singular/plural differences.
     *
     * Examples:
     * chicken = chickens
     * tomato = tomatoes
     * onion = onions
     */
    private static boolean namesMatch(
            String recipeName,
            String pantryName) {

        if (recipeName == null ||
                pantryName == null) {

            return false;
        }

        String recipe =
                normalizeName(recipeName);

        String pantry =
                normalizeName(pantryName);

        if (recipe.equals(pantry)) {
            return true;
        }

        // Basic plural handling
        if (recipe.endsWith("s") &&
                recipe.substring(
                        0,
                        recipe.length() - 1
                ).equals(pantry)) {

            return true;
        }

        if (pantry.endsWith("s") &&
                pantry.substring(
                        0,
                        pantry.length() - 1
                ).equals(recipe)) {

            return true;
        }

        // Basic "ies" plural handling
        if (recipe.endsWith("ies") &&
                recipe.substring(
                                0,
                                recipe.length() - 3
                        ).concat("y")
                        .equals(pantry)) {

            return true;
        }

        if (pantry.endsWith("ies") &&
                pantry.substring(
                                0,
                                pantry.length() - 3
                        ).concat("y")
                        .equals(recipe)) {

            return true;
        }

        return false;
    }

    /**
     * Normalises ingredient names for comparison.
     */
    private static String normalizeName(
            String name) {

        return name
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    /**
     * Compares units.
     *
     * At this stage we require the same unit.
     * Unit conversion can be added later if needed.
     */
    private static boolean unitsMatch(
            String recipeUnit,
            String pantryUnit) {

        if (recipeUnit == null ||
                pantryUnit == null) {

            return false;
        }

        return recipeUnit
                .trim()
                .equalsIgnoreCase(
                        pantryUnit.trim()
                );
    }

    /**
     * Converts a recipe ingredient string such as:
     *
     * chicken - 500.0 g
     *
     * into:
     *
     * name = chicken
     * quantity = 500.0
     * unit = g
     */
    private static IngredientRequirement parseIngredient(
            String ingredientText) {

        if (ingredientText == null ||
                ingredientText.trim().isEmpty()) {

            return null;
        }

        String text =
                ingredientText.trim();

        String[] parts =
                text.split("\\s*-\\s*", 2);

        if (parts.length != 2) {
            return null;
        }

        String name =
                parts[0].trim();

        String quantityAndUnit =
                parts[1].trim();

        String[] quantityParts =
                quantityAndUnit.split("\\s+", 2);

        if (quantityParts.length != 2) {
            return null;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityParts[0]
                    );

        } catch (NumberFormatException e) {

            return null;
        }

        String unit =
                quantityParts[1].trim();

        if (name.isEmpty() ||
                unit.isEmpty()) {

            return null;
        }

        return new IngredientRequirement(
                name,
                quantity,
                unit
        );
    }

    /**
     * Small internal class used to hold
     * a recipe ingredient requirement.
     */
    private static class IngredientRequirement {

        String name;
        double quantity;
        String unit;

        IngredientRequirement(
                String name,
                double quantity,
                String unit) {

            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }
}