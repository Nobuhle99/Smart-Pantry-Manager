package com.nobuhle.smartpantrymanager;

public class ExpiringIngredient {

    private String name;
    private String expiryDate;
    private long daysRemaining;

    public ExpiringIngredient(
            String name,
            String expiryDate,
            long daysRemaining) {

        this.name = name;
        this.expiryDate = expiryDate;
        this.daysRemaining = daysRemaining;
    }

    public String getName() {
        return name;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }
}