package com.example.sourcecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import com.example.sourcecode.Adapters.SQLiteAdapter;
import com.example.sourcecode.Adapters.ShoppingCartAdapter;

public class ShoppingCart extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private boolean isIngredientsVisible = false;
    private SQLiteAdapter mySQLiteAdapter;
    private SharedPreferences sharedPreferences;
    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.ingredientRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        intent = new Intent(ShoppingCart.this, RecipeDetailsActivity.class); // Initialize the Intent

        int selectedIndex = getSelectedNavItemIndex(0);

        bottomNavigationView.getMenu().getItem(selectedIndex).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    Intent intent_home = new Intent(ShoppingCart.this, HomeActivity.class);
                    startActivity(intent_home);
                    return true;

                case R.id.favourite:
                    saveSelectedNavItemIndex(1);
                    Intent intent_fav = new Intent(ShoppingCart.this, FavouriteActivity.class);
                    startActivity(intent_fav);
                    return true;

                case R.id.cart:
                    //saveSelectedNavItemIndex(2);
                    return true;

                case R.id.calendar:
                    saveSelectedNavItemIndex(3);
                    Intent intent_planner = new Intent(ShoppingCart.this, PlannerActivity.class);
                    startActivity(intent_planner);
                    return true;

            }
            return false;
        });

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // Initialize the SQLiteAdapter
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        Map<String, List<String>> shoppingList = mySQLiteAdapter.getShoppingListData();

        System.out.println("Result:" + shoppingList);

        adapter = new ShoppingCartAdapter(this, shoppingList,mySQLiteAdapter);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }else{
            System.out.println("recyclerView is null.");
        }

        System.out.println("shoppingCart.java: " + shoppingList);
    }

    private void saveSelectedNavItemIndex(int selectedIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SELECTED_NAV_ITEM_INDEX, selectedIndex);
        editor.apply();
    }

    // Function to retrieve the selected item index from SharedPreferences
    private int getSelectedNavItemIndex(int defaultValue) {
        return sharedPreferences.getInt(PREF_SELECTED_NAV_ITEM_INDEX, defaultValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the SQLiteAdapter
        mySQLiteAdapter.close();
    }
}
