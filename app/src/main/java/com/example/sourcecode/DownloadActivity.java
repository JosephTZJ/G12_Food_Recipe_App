package com.example.sourcecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.sourcecode.Adapters.DownloadsAdapter;
import com.example.sourcecode.Adapters.SQLiteAdapter;
import com.example.sourcecode.Helpers.Recipe;

public class DownloadActivity extends AppCompatActivity {


    private SQLiteAdapter mySQLiteAdapter;

    RecyclerView recycler_recipes;
    DownloadsAdapter adapter;
    Intent intent;
    BottomNavigationView bottomNavigationView, topNavigationMenu;
    Button btn_delete;

    // Declare SharedPreferences variable
    private SharedPreferences sharedPreferences, sharedPreferences2;

    // SharedPreferences key for storing selected item index
    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";

    private static final String PREF_SELECTED_TOP_NAV_ITEM_INDEX = "selected_top_nav_item_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("my_prefs2", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        recycler_recipes = findViewById(R.id.recycler_downloads);
        topNavigationMenu = findViewById(R.id.topNavigationView);
        btn_delete = findViewById(R.id.btn_delete);

        intent = new Intent(DownloadActivity.this, RecipeDetailsActivity.class); // Initialize the Intent

        int selectedIndex = getSelectedNavItemIndex(0);
        bottomNavigationView.getMenu().getItem(selectedIndex).setChecked(true);

        int selectedIndex2 = getSelectedTopNavItemIndex(0);
        topNavigationMenu.getMenu().getItem(selectedIndex2).setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    Intent intent = new Intent(DownloadActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.favourite:
                    saveSelectedNavItemIndex(1);

                    return true;

                case R.id.cart:
                    saveSelectedNavItemIndex(2);
                    Intent intent2 = new Intent(DownloadActivity.this, ShoppingCart.class);
                    startActivity(intent2);
                    return true;

                case R.id.calendar:
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

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();


        List<Recipe> downloadRecipes = mySQLiteAdapter.getAllDownloadRecipes();
        adapter = new DownloadsAdapter(downloadRecipes, this);
        mySQLiteAdapter.close();


        // Set the adapter to the RecyclerView
        recycler_recipes.setHasFixedSize(true);
        recycler_recipes.setLayoutManager(new LinearLayoutManager(DownloadActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_recipes.setAdapter(adapter);

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

    // Deletes Recipe from the database
    public void deleteDownloadedRecipe(String id) {

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();
        mySQLiteAdapter.remove_fav_recipe_from_id(id);

        mySQLiteAdapter.close();
    }
}