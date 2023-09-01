package com.example.sourcecode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;

    private static final int homePage = R.id.home_page;
    private static final int favouritesPage = R.id.favourites_page;
    private static final int profilePage = R.id.profile_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getSupportActionBar().hide();

        decorView.setSystemUiVisibility(uiOptions);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.setQueryHint("Search Food Recipes");

        recyclerView = findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new MyThread().start();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();
                    switch (itemId) {
                        case homePage:
                            showToast("Home Page Selected");
                            return true;
                        case favouritesPage:
                            showToast("Favourites Page Selected");
                            return true;
                        case profilePage:
                            showToast("Profile Page Selected");
                            return true;
                    }
                    return false;
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                List<Recipe> recipes = (List<Recipe>) msg.obj;

                if (recipes != null) {
                    recipeAdapter = new RecipeAdapter(recipes);
                    recyclerView.setAdapter(recipeAdapter);
                } else {
                    showToast("Failed to fetch recipes.");
                }
            }
            return true;
        }
    });

    private class MyThread extends Thread {

        @Override
        public void run() {
            try {
                URL url = new URL("https://fsgrlullvgbtzvatcujr.supabase.co/rest/v1/Recipe");
                HttpURLConnection hc = (HttpURLConnection) url.openConnection();
                hc.setRequestProperty("apikey", getString(R.string.SUPABASE_KEY));
                hc.setRequestProperty("Authorization", "Bearer " + getString(R.string.SUPABASE_KEY));

                int responseCode = hc.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    List<Recipe> recipes = parseRecipes(response.toString());

                    Message message = new Message();
                    message.what = 1;
                    message.obj = recipes;
                    handler.sendMessage(message);
                } else {
                    Log.i("HTTP response failed: ", String.valueOf(hc.getResponseCode()));
                }

                hc.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Recipe> parseRecipes(String json) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String recipeName = jsonObject.getString("recipe_name");
            String imageURL = jsonObject.getString("imageURL");

            Recipe recipe = new Recipe(recipeName, imageURL);
            recipes.add(recipe);
        }

        return recipes;
    }
}
