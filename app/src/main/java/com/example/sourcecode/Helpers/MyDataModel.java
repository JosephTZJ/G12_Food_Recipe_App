package com.example.sourcecode.Helpers;

public class MyDataModel {
    private long id;
    private String recipeTitle;
    private String ingredient;
    private String step;
    private String date;

    // Constructors (you can have multiple constructors as needed)

    public MyDataModel() {
    }

    public MyDataModel(String recipeTitle, String ingredient, String step, String date) {
        this.recipeTitle = recipeTitle;
        this.ingredient = ingredient;
        this.step = step;
        this.date = date;
    }

    // Getter and setter methods for each field

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
