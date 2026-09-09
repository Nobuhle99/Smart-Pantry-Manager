package com.nobuhle.smartpantrymanager;

public class Recipe {

    private int id;
    private String name;
    private String instructions;

    // Empty constructor
    public Recipe() {
    }

    // Constructor without ID
    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    // Constructor with ID
    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}