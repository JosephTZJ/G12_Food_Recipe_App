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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
    ProgressDialog dialog;
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


//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()){
//
//
//                case R.id.home:
//                    Intent intent_home = new Intent(RecipeDetailsActivity.this, MainActivity.class);
//                    startActivity(intent_home);
//                    return true;
//
//                case R.id.favourite:
//                    Intent intent_fav = new Intent(RecipeDetailsActivity.this, FavouriteActivity.class);
//                    startActivity(intent_fav);
//                    return true;
//
//                case R.id.cart:
//                    return true;
//
//                case R.id.calendar:
//                    return true;
//
//                case R.id.settings:
//                    return true;
//
//            }
//            return false;
//        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        String recipeId = intent.getStringExtra("recipeId");
//        System.out.println("Response3: " + response);
//        System.out.println("recipeId: " + recipeId);


        if (response != null || recipeId != null) {
            try {

                JSONObject targetRecipe = findRecipe(response, recipeId);

                System.out.println("\n"+targetRecipe+"\n");

                Id = recipeId;


                if (targetRecipe != null) {

                    String title = targetRecipe.getString("recipe_name");
                    String ingredients = targetRecipe.getString("ingredients");
                    String steps = targetRecipe.getString("steps") ;
                    String imageURL = targetRecipe.getString("imageURL");


//                    System.out.println("title: "+ title);
//                    System.out.println("ingredients: "+ ingredients);
//                    System.out.println("steps: "+ steps);

                    recipe_title = title;
                    recipeImageURL = imageURL;

                    tv_recipename.setText(title);   //recipe title


                    //for card view of ingredients
                    try {
                        JSONArray ingredientsArray = new JSONArray(ingredients);
                        List<String> ingredientList = new ArrayList<>();

                        for (int i = 0; i < ingredientsArray.length(); i++) {
                            String ingredient = ingredientsArray.getString(i);
                            ingredientList.add(ingredient);
//                            System.out.println("\n"+ingredientsArray.length()+"\n");
//                            System.out.println("\n"+ingredient+"\n");
//                            System.out.println("\ningredientsList"+ingredientList+"\n");

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

//                        System.out.println("\nstepsList"+stepsList+"\n");
//                        System.out.println("\n"+stepsArray.length()+"\n");
//                        System.out.println("\n"+instruction+"\n");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_back:
                // Handle the back action here
                onBackPressed(); // This will go back to the previous activity
                return true;
            case R.id.action_favourite:

                isFavorite = !isFavorite;  // toggle fav status on click
                updateFavoriteMenuItemIcon(item); // Update the menu item icon

                String title = recipe_title; // Get recipe title
                String imageURL = recipeImageURL; // Get image URL, you need to implement this part
                List<String> ingredients = recipe_ingredients; // Get ingredients, you need to implement this part
                List<String> steps = recipe_steps; // Get steps, you need to implement this part

                Intent intent = getIntent();
                String recipeId = intent.getStringExtra("recipeId");

                addToFavourites(isFavorite, title, imageURL, ingredients, steps, recipeId); //add to sql fav database

                return true;

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

//        mySQLiteAdapter.deleteAll();

        System.out.println("Entering add to fav function");

        System.out.println("the isfav is " + isFavorite);


        if(isFavorite){     // add recipe to db

            System.out.println("Adding to favorites and writing to DB");
            mySQLiteAdapter.insert_recipe( title, imageURL, recipeId, ingredients, steps);
            mySQLiteAdapter.insert_fav_recipe_id(recipeId);
            mySQLiteAdapter.close();

        }
        else{
            System.out.println("Removing from favorites and deleting from DB");

            mySQLiteAdapter.remove_fav_recipe_id(recipeId);
            mySQLiteAdapter.remove_fav_recipe_from_id(recipeId);


        }
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