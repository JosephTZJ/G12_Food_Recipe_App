package my.edu.utar.recipeassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import my.edu.utar.recipeassignment.Adapters.DownloadsAdapter;
import my.edu.utar.recipeassignment.Adapters.SQLiteAdapter;
import my.edu.utar.recipeassignment.Helpers.Recipe;

public class DownloadActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView, topNavigationMenu;

    private SQLiteAdapter mySQLiteAdapter;

    RecyclerView recycler_recipes;

    DownloadsAdapter adapter;

    Intent intent;

    // Declare SharedPreferences variable
    private SharedPreferences sharedPreferences, sharedPreferences2;

    // SharedPreferences key for storing selected item index
    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";

    private static final String PREF_SELECTED_TOP_NAV_ITEM_INDEX = "selected_top_nav_item_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        CardView cardView_fav = findViewById(R.id.download_list_container);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        sharedPreferences2 = getSharedPreferences("my_prefs2", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recycler_recipes = findViewById(R.id.recycler_downloads);
        topNavigationMenu = findViewById(R.id.topNavigationView);

        intent = new Intent(DownloadActivity.this, RecipeDetailsActivity.class); // Initialize the Intent

        int selectedIndex = getSelectedNavItemIndex(0);

        bottomNavigationView.getMenu().getItem(selectedIndex).setChecked(true);

        int selectedIndex2 = getSelectedTopNavItemIndex(0);

        topNavigationMenu.getMenu().getItem(selectedIndex2).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    Intent intent = new Intent(DownloadActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.favourite:
                    saveSelectedNavItemIndex(1);
                    saveSelectedTopNavItemIndex(0); // Set the top navigation menu selection to 0

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

        topNavigationMenu.setOnNavigationItemSelectedListener(item->
        {
            switch(item.getItemId()){
                case R.id.fav_mn:
                    saveSelectedTopNavItemIndex(0);
                    Intent intent = new Intent(DownloadActivity.this, FavouriteActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.download_mn:
                    saveSelectedTopNavItemIndex(1);

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

        List<Recipe> downloadRecipes = mySQLiteAdapter.getAllFavouriteRecipes();
        System.out.println("downloaded recipe:" + downloadRecipes);


        adapter = new DownloadsAdapter(downloadRecipes, DownloadActivity.this);


        mySQLiteAdapter.close();

        // Create an instance of the adapter and pass the data
//        adapter = new FavouritesAdapter(recipeTitles, recipeImageURLs, recipeIds,FavouriteActivity.this);

        // Set the adapter to the RecyclerView
        recycler_recipes.setHasFixedSize(true);
        recycler_recipes.setLayoutManager(new LinearLayoutManager(DownloadActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_recipes.setAdapter(adapter);



    }

//    public String getResponseFromDatabase() {
//
//        System.out.println("key: " + getString(R.string.SUPABASE_KEY));
//
//        try {
//            URL url = new URL("https://fsgrlullvgbtzvatcujr.supabase.co/rest/v1/Recipe?"
//            );
//            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
//            hc.setRequestProperty("apikey", getString(R.string.SUPABASE_KEY));
//            hc.setRequestProperty("Authorization", "Bearer " + getString(R.string.SUPABASE_KEY));
//
//            int responseCode = hc.getResponseCode();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(hc.getInputStream()));
//                String line;
//                StringBuilder response = new StringBuilder();
//
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//                reader.close();
//
//                String result = response.toString();
//
//                System.out.println("Response: " + result);
//
//
//                return result;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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