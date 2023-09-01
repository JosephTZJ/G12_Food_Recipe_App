package my.edu.utar.recipeassignment;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import my.edu.utar.recipeassignment.Adapters.FavouritesAdapter;
import my.edu.utar.recipeassignment.Adapters.IngredientsAdapter;
import my.edu.utar.recipeassignment.Adapters.SQLiteAdapter;
import my.edu.utar.recipeassignment.Helpers.Recipe;

public class FavouriteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private SQLiteAdapter mySQLiteAdapter;

    RecyclerView recycler_recipes;

    FavouritesAdapter adapter;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        CardView cardView_fav = findViewById(R.id.fav_list_container);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recycler_recipes = findViewById(R.id.recycler_favourites);

        intent = new Intent(FavouriteActivity.this, RecipeDetailsActivity.class); // Initialize the Intent


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    Intent intent = new Intent(FavouriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.favourite:
                    return true;

                case R.id.cart:
                    return true;

                case R.id.calendar:
                    return true;

                case R.id.settings:
                    return true;

            }
            return false;
        });


        // Initialize the SQLiteAdapter
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        // Retrieve the data you need from the database
//        List<String> recipeTitles = mySQLiteAdapter.findRecipeTitle();
//        List<String> recipeImageURLs = mySQLiteAdapter.findRecipeImage();
//        List<String> recipeIds = mySQLiteAdapter.findRecipeId();

        List<Recipe> favoriteRecipes = mySQLiteAdapter.getAllFavouriteRecipes();

        adapter = new FavouritesAdapter(favoriteRecipes, FavouriteActivity.this);


        mySQLiteAdapter.close();

        // Create an instance of the adapter and pass the data
//        adapter = new FavouritesAdapter(recipeTitles, recipeImageURLs, recipeIds,FavouriteActivity.this);

        // Set the adapter to the RecyclerView
        recycler_recipes.setHasFixedSize(true);
        recycler_recipes.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_recipes.setAdapter(adapter);






    }

    public String getResponseFromDatabase() {

        System.out.println("key: " + getString(R.string.SUPABASE_KEY));

        try {
            URL url = new URL("https://fsgrlullvgbtzvatcujr.supabase.co/rest/v1/Recipe?"
            );
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setRequestProperty("apikey", getString(R.string.SUPABASE_KEY));
            hc.setRequestProperty("Authorization", "Bearer " + getString(R.string.SUPABASE_KEY));

            int responseCode = hc.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String result = response.toString();

                System.out.println("Response: " + result);


                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}