package com.nobuhle.smartpantrymanager;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    /**
     * Checks whether the pantry contains everything
     * required for a recipe.
     */
    public static boolean isRecipeMatch(
            List<String> recipeIngredients,
            List<PantryItem> pantryItems) {

        return getMatchMessage(
                recipeIngredients,
                pantryItems
        ).equals("MATCH");
    }

    /**
     * Returns a message explaining whether the recipe
     * can be made with the current pantry.
     */
    public static String getMatchMessage(
            List<String> recipeIngredients,
            List<PantryItem> pantryItems) {

        if (recipeIngredients == null ||
                recipeIngredients.isEmpty()) {

            return "No ingredients were found for this recipe.";
        }

        if (pantryItems == null ||
                pantryItems.isEmpty()) {

            return "Your pantry is empty.";
        }

        // Check every ingredient required by the recipe
        for (String recipeIngredient : recipeIngredients) {

            IngredientRequirement requirement =
                    parseIngredient(recipeIngredient);

            if (requirement == null) {

                return "Unable to read one of the recipe ingredients.";
            }

            boolean ingredientNameFound = false;
            boolean unitFound = false;

            double availableQuantity = 0;

            // Compare with every pantry item
            for (PantryItem pantryItem : pantryItems) {

                if (!namesMatch(
                        requirement.name,
                        pantryItem.getName())) {

                    continue;
                }

                ingredientNameFound = true;

                if (!unitsMatch(
                        requirement.unit,
                        pantryItem.getUnit())) {

                    continue;
                }

                unitFound = true;

                // Add quantities from duplicate pantry records
                availableQuantity +=
                        pantryItem.getQuantity();
            }

            // Ingredient does not exist
            if (!ingredientNameFound) {

                return "Missing ingredient: "
                        + requirement.name
                        + ".";
            }

            // Ingredient exists but units differ
            if (!unitFound) {

                return "Unit mismatch for "
                        + requirement.name
                        + ". Recipe requires "
                        + requirement.unit
                        + ".";
            }

            // Ingredient exists but quantity is insufficient
            if (availableQuantity <
                    requirement.quantity) {

                return "Not enough "
                        + requirement.name
                        + ". You have "
                        + formatQuantity(availableQuantity)
                        + " "
                        + requirement.unit
                        + " but need "
                        + formatQuantity(requirement.quantity)
                        + " "
                        + requirement.unit
                        + ".";
            }
        }

        // All required ingredients are available
        return "MATCH";
    }

    /**
     * Matches ingredient names while handling
     * common singular/plural differences.
     *
     * Examples:
     *
     * chicken = chickens
     * onion = onions
     * tomato = tomatoes
     * potato = potatoes
     * berry = berries
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

        // Exact match
        if (recipe.equals(pantry)) {

            return true;
        }

        // -----------------------------------------
        // Basic "s" plural handling
        // -----------------------------------------

        if (recipe.endsWith("s") &&
                recipe.length() > 1 &&
                recipe.substring(
                        0,
                        recipe.length() - 1
                ).equals(pantry)) {

            return true;
        }

        if (pantry.endsWith("s") &&
                pantry.length() > 1 &&
                pantry.substring(
                        0,
                        pantry.length() - 1
                ).equals(recipe)) {

            return true;
        }

        // -----------------------------------------
        // "es" plural handling
        // tomato = tomatoes
        // potato = potatoes
        // -----------------------------------------

        if (recipe.endsWith("es") &&
                recipe.length() > 2 &&
                recipe.substring(
                        0,
                        recipe.length() - 2
                ).equals(pantry)) {

            return true;
        }

        if (pantry.endsWith("es") &&
                pantry.length() > 2 &&
                pantry.substring(
                        0,
                        pantry.length() - 2
                ).equals(recipe)) {

            return true;
        }

        // -----------------------------------------
        // "ies" plural handling
        // berry = berries
        // -----------------------------------------

        if (recipe.endsWith("ies") &&
                recipe.length() > 3 &&
                recipe.substring(
                                0,
                                recipe.length() - 3
                        )
                        .concat("y")
                        .equals(pantry)) {

            return true;
        }

        if (pantry.endsWith("ies") &&
                pantry.length() > 3 &&
                pantry.substring(
                                0,
                                pantry.length() - 3
                        )
                        .concat("y")
                        .equals(recipe)) {

            return true;
        }

        return false;
    }

    /**
     * Normalises ingredient names.
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
     * Formats quantities without unnecessary .0
     */
    private static String formatQuantity(
            double quantity) {

        if (quantity ==
                Math.floor(quantity)) {

            return String.valueOf(
                    (int) quantity
            );
        }

        return String.valueOf(
                quantity
        );
    }

    /**
     * Converts a recipe ingredient string such as:
     *
     * chicken - 500.0 g
     *
     * into its name, quantity and unit.
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
                text.split(
                        "\\s*-\\s*",
                        2
                );

        if (parts.length != 2) {

            return null;
        }

        String name =
                parts[0].trim();

        String quantityAndUnit =
                parts[1].trim();

        String[] quantityParts =
                quantityAndUnit.split(
                        "\\s+",
                        2
                );

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
     * Stores a recipe ingredient requirement.
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