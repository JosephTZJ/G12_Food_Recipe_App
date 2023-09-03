package com.example.sourcecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    BottomNavigationView bottomNavigationView;
    SearchView searchView;

    Intent intent;

    private SharedPreferences sharedPreferences;

    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";

    private List<String> recipeResponses = new ArrayList<>();

    private RecipeAdapter adapter;
    private JSONArray originalJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        intent = new Intent(HomeActivity.this, RecipeDetailsActivity.class);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

        adapter = new RecipeAdapter(new JSONArray());
        recyclerView.setAdapter(adapter);

        Handler mHandler = new Handler();
        MyThread connectingThread = new MyThread(mHandler);
        connectingThread.start();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    return true;
                case R.id.favourite:
                    saveSelectedNavItemIndex(1);
                    Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
                    startActivity(intent);
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

        searchView = findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.setQueryHint("Search Food Recipes");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRecipes(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecipes(newText.toLowerCase());
                return true;
            }
        });
    }

    private void filterRecipes(String query) {
        if (query.isEmpty()) {
            adapter.updateData(originalJsonArray);
        } else {
            JSONArray filteredArray = new JSONArray();
            for (int i = 0; i < originalJsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = originalJsonArray.getJSONObject(i);
                    String recipeName = jsonObject.getString("recipe_name").toLowerCase();
                    if (recipeName.contains(query)) {
                        filteredArray.put(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.updateData(filteredArray);
        }
    }

    private class MyThread extends Thread {
        private Handler mHandler;

        public MyThread(Handler handler) {
            this.mHandler = handler;
        }

        public void run() {
            try {
                URL url = new URL("https://fsgrlullvgbtzvatcujr.supabase.co/rest/v1/Recipe");
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
                    recipeResponses.clear();
                    recipeResponses.add(result);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                originalJsonArray = jsonArray;
                                adapter.updateData(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Log.i("HTTP response failed: ", String.valueOf(hc.getResponseCode()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSelectedNavItemIndex(int selectedIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SELECTED_NAV_ITEM_INDEX, selectedIndex);
        editor.apply();
    }

    private int getSelectedNavItemIndex(int defaultValue) {
        return sharedPreferences.getInt(PREF_SELECTED_NAV_ITEM_INDEX, defaultValue);
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        private JSONArray jsonArray;

        public RecipeAdapter(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        public void updateData(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
            notifyDataSetChanged();
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recipe_cardview, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeViewHolder holder, int position) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(position);
                String recipeName = jsonObject.getString("recipe_name");
                String recipeId = jsonObject.getString("id");
                String recipeImageUrl = jsonObject.getString("imageURL");

                holder.title.setText(recipeName);

                Picasso.get()
                        .load(recipeImageUrl)
                        .into(holder.recipeImage);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("recipeId", recipeId);
                        intent.putExtra("response", recipeResponses.get(0));

                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            cardView = itemView.findViewById(R.id.recipe_card_view);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}