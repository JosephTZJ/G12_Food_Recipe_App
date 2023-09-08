package com.example.sourcecode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.sourcecode.Adapters.IngredientsAdapter;
import com.example.sourcecode.Adapters.SQLiteAdapter;

public class RecipeDetailsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView tv_recipename;
    ImageView recipe_image;
    RecyclerView recycler_ingredients;
    LinearLayout stepslayout;

    IngredientsAdapter adapter;

    private SQLiteAdapter mySQLiteAdapter;

    private Toolbar toolbar;

    private boolean isFavorite = false; // Keep track of favorite status

    String Id;
    String recipe_title;
    List<String> recipe_ingredients;
    List<String> recipe_steps;
    String recipeImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //to find all the views
        findViews();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        String recipeId = intent.getStringExtra("recipeId");


        // shows recipe details
        if (response != null || recipeId != null) {
            try {

                JSONObject targetRecipe = findRecipe(response, recipeId);

                Id = recipeId;


                if (targetRecipe != null) {

                    String title = targetRecipe.getString("recipe_name");
                    String ingredients = targetRecipe.getString("ingredients");
                    String steps = targetRecipe.getString("steps") ;
                    String imageURL = targetRecipe.getString("imageURL");

                    recipe_title = title;
                    recipeImageURL = imageURL;

                    tv_recipename.setText(title);


                    //for card view of ingredients
                    try {
                        JSONArray ingredientsArray = new JSONArray(ingredients);
                        List<String> ingredientList = new ArrayList<>();

                        for (int i = 0; i < ingredientsArray.length(); i++) {
                            String ingredient = ingredientsArray.getString(i);
                            ingredientList.add(ingredient);
                        }

                        recipe_ingredients = ingredientList;
                        recycler_ingredients.setHasFixedSize(true);
                        recycler_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this,LinearLayoutManager.VERTICAL, false));

                        adapter = new IngredientsAdapter(ingredientList);
                        recycler_ingredients.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //output recipe steps
                    JSONArray stepsArray = new JSONArray(steps);
                    List<String> stepsList = new ArrayList<>();
                    StringBuilder formattedSteps = new StringBuilder();

                    for (int i = 0; i < stepsArray.length(); i++) {
                        JSONObject stepObject = stepsArray.getJSONObject(i);
                        String instruction = stepObject.getString("instruction");
                        stepsList.add(instruction);

                        formattedSteps.append(i + 1).append(". ").append(instruction).append("\n\n");
                    }

                    recipe_steps = stepsList;

                    TextView stepTextView = new TextView(this);

                    stepTextView.setPadding(8,8,8,8);
                    stepTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    stepTextView.setTextSize(18);
                    stepTextView.setText(formattedSteps.toString());
                    stepslayout.addView(stepTextView);

                    //get image from supabase
                    Picasso.get()
                            .load(imageURL)
                            .into(recipe_image);


                } else {

                    tv_recipename.setText("Recipe not found.");
                    tv_recipename.setTextSize(18);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            tv_recipename.setText("No data received.");
            tv_recipename.setTextSize(18);
        }

    }

    private void findViews() {
        tv_recipename = findViewById(R.id.tv_recipename);
        recipe_image = findViewById(R.id.recipe_image);
        recycler_ingredients = findViewById(R.id.recycler_ingredients);

        stepslayout = findViewById(R.id.layout_steps);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_top_menu, menu);

        MenuItem favoriteMenuItem = menu.findItem(R.id.action_favourite);

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        // Check if the recipe is a favorite and set the icon accordingly
        if (mySQLiteAdapter.checkIsFavourite(Id)) {
            favoriteMenuItem.setIcon(R.drawable.ic_baseline_favorite_24); // Set the filled favorite icon
            isFavorite = true;
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_baseline_favorite_border_24); // Set the unfilled favorite icon
            isFavorite = false;
        }
        mySQLiteAdapter.close();


        return true;
    }

    // when download, back and favorite button clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String title = recipe_title;
        String imageURL = recipeImageURL;
        List<String> ingredients = recipe_ingredients;
        List<String> steps = recipe_steps;

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");


        switch (id) {
            case R.id.action_back:
                onBackPressed();
                return true;
            case R.id.action_favourite:

                isFavorite = !isFavorite;
                updateFavoriteMenuItemIcon(item);


                addToFavourites(isFavorite, title, imageURL, ingredients, steps, recipeId);
                Toast.makeText(getApplicationContext(), "Added to Fav " , Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_download:

                addToDownloads(title, imageURL, ingredients, steps, recipeId); //add to sql fav database
                Toast.makeText(getApplicationContext(), "Downloaded " , Toast.LENGTH_SHORT).show();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //update the fav icon
    private void updateFavoriteMenuItemIcon(MenuItem favoriteMenuItem) {

        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_baseline_favorite_24); // Set the favorite icon
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_baseline_favorite_border_24); // Set the not-favorite icon
        }
    }


    private void addToFavourites(boolean isFavorite, String title, String imageURL, List<String> ingredients, List<String> steps, String recipeId) {

        // write the favourited recipe into db
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();


        if(isFavorite){     // add recipe to db

            mySQLiteAdapter.insert_fav_recipe_id(recipeId);
            mySQLiteAdapter.close();

        }
        else{
            mySQLiteAdapter.remove_fav_recipe_id(recipeId);
            Toast.makeText(getApplicationContext(), "Removed from Fav " , Toast.LENGTH_SHORT).show();

        }
        mySQLiteAdapter.close();

    }

    private boolean ingredientsAdded = false;

    // Save the ingredients to the SQLiteAdapter
    private void addToList(String title, List<String> ingredients) {

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        // Insert the shopping cart item with the title and ingredients
        long result = mySQLiteAdapter.insert_ingredient(title, ingredients);

        mySQLiteAdapter.close();

        if (result != -1) {
            Toast.makeText(this, "Ingredients added to shopping list", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add ingredients to shopping list", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToList(View view) {

        if (!ingredientsAdded) {
            addToList(recipe_title, recipe_ingredients);
            ingredientsAdded = true;
        }
    }

    private void addToDownloads(String title, String imageURL, List<String> ingredients, List<String> steps, String recipeId) {

        // write the favourited recipe into db
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        mySQLiteAdapter.insert_recipe( title, imageURL, recipeId, ingredients, steps);
        mySQLiteAdapter.close();

    }

    private JSONObject findRecipe(String response, String recipeId) throws JSONException {

        JSONArray jsonArray = new JSONArray(response);
        JSONObject targetRecipe = null;

        // Find the recipe with the specified recipeId
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String id = jsonObject.getString("id");

            if (recipeId.trim().equals(id.trim())) {
                targetRecipe = jsonObject;
                break; // Stop searching once the target recipe is found
            }
        }

        return targetRecipe;
    }

}