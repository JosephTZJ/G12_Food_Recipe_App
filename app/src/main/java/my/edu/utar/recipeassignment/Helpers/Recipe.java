package my.edu.utar.recipeassignment.Helpers;

import java.util.List;

public class Recipe {
    private String id;
    private String title;
    private String imageURL;
    private List<String> ingredients;
    private List<String> instructions;

    // Constructors, getters, and setters

    public Recipe() {
        // Default constructor
    }

    public Recipe(String id, String title, String imageURL, List<String> ingredients, List<String> instructions) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}
