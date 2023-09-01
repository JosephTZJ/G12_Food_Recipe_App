package com.example.sourcecode;

public class Recipe {
    private String recipeName;
    private String imageURL;

    public Recipe(String recipeName, String imageURL) {
        this.recipeName = recipeName;
        this.imageURL = imageURL;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImageURL() {
        return imageURL;
    }
}
