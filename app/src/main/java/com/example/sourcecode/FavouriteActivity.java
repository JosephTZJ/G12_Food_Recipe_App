package com.example.sourcecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.sourcecode.Adapters.FavouritesAdapter;
import com.example.sourcecode.Adapters.SQLiteAdapter;

public class FavouriteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView,topNavigationMenu;

    private SQLiteAdapter mySQLiteAdapter;

    RecyclerView recycler_recipes;

    FavouritesAdapter adapter;

    Intent intent;

    // Declare SharedPreferences variable
    private SharedPreferences sharedPreferences, sharedPreferences2;

    // SharedPreferences key for storing selected item index
    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";

    private static final String PREF_SELECTED_TOP_NAV_ITEM_INDEX = "selected_top_nav_item_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        sharedPreferences2 = getSharedPreferences("my_prefs2", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recycler_recipes = findViewById(R.id.recycler_favourites);
        topNavigationMenu = findViewById(R.id.topNavigationView);


        intent = new Intent(FavouriteActivity.this, RecipeDetailsActivity.class); // Initialize the Intent

        int selectedIndex = getSelectedNavItemIndex(0);

        bottomNavigationView.getMenu().getItem(selectedIndex).setChecked(true);

        int selectedIndex2 = getSelectedTopNavItemIndex(0);
        topNavigationMenu.getMenu().getItem(selectedIndex2).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    Intent intentMain = new Intent(FavouriteActivity.this, HomeActivity.class);
                    startActivity(intentMain);
                    return true;

                case R.id.favourite:
                    saveSelectedNavItemIndex(1);
                    return true;

                case R.id.cart:
                    saveSelectedNavItemIndex(2);
                    Intent intent2 = new Intent(FavouriteActivity.this, ShoppingCart.class);
                    startActivity(intent2);
                    return true;

                case R.id.calendar:
                    return true;


            }
            return false;
        });

        topNavigationMenu.setSelectedItemId(R.id.fav_mn);
        topNavigationMenu.setOnNavigationItemSelectedListener(item->
        {
            switch(item.getItemId()){
                case R.id.fav_mn:
                    saveSelectedTopNavItemIndex(0);

                    return true;
                case R.id.download_mn:
                    saveSelectedTopNavItemIndex(1);
                    Intent intent = new Intent(FavouriteActivity.this, DownloadActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        });


        // Initialize the SQLiteAdapter
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        List<String> favoriteRecipes = mySQLiteAdapter.getAllFavouriteRecipesId();

        adapter = new FavouritesAdapter(favoriteRecipes, FavouriteActivity.this);
        mySQLiteAdapter.close();


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

    public String getRecipeFromDatabase(String id)  {

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
                    response.append(line); // Append the entire line, including newline character
                }
                reader.close();


                // Now parse the entire response as a JSON array or object
                JSONArray jsonArray = new JSONArray(response.toString());

                // Process the JSON array or object as needed
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String recipeId = jsonObject.optString("id");


                    System.out.println("recipeId in favactivity: " + recipeId);
                    System.out.println("id in favactivity: " + id);


                    // Check if the 'id' matches the provided 'id' and append to response if needed
                    if (recipeId.trim().equals(id.trim())) {
                        System.out.println("id in equal: " + recipeId);

                        return jsonObject.toString();
                    }
                }

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveSelectedNavItemIndex(int selectedIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SELECTED_NAV_ITEM_INDEX, selectedIndex);
        editor.apply();
    }

    private void saveSelectedTopNavItemIndex(int selectedIndex) {
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putInt(PREF_SELECTED_TOP_NAV_ITEM_INDEX, selectedIndex);
        editor.apply();
    }
    // Function to retrieve the selected item index from SharedPreferences
    private int getSelectedNavItemIndex(int defaultValue) {
        return sharedPreferences.getInt(PREF_SELECTED_NAV_ITEM_INDEX, defaultValue);

    }
    private int getSelectedTopNavItemIndex(int defaultValue) {
        return sharedPreferences2.getInt(PREF_SELECTED_TOP_NAV_ITEM_INDEX, defaultValue);

    }

}