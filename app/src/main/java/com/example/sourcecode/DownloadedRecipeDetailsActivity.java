package com.example.sourcecode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.sourcecode.Adapters.IngredientsAdapter;
import com.example.sourcecode.Adapters.SQLiteAdapter;
import com.example.sourcecode.Helpers.Recipe;

public class DownloadedRecipeDetailsActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private Recipe recipe;
    RecyclerView recycler_ingredients;
    List<String> recipe_ingredients;
    IngredientsAdapter adapter;

    TextView tv_recipename;
    LinearLayout stepslayout;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_recipe_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tv_recipename = findViewById(R.id.tv_recipename);
        stepslayout = findViewById(R.id.layout_steps);
        recycler_ingredients = findViewById(R.id.recycler_ingredients);

        // Initialize the SQLiteAdapter
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");

        // Retrieve recipe data based on the recipe ID
        Recipe recipe = mySQLiteAdapter.findRecipeByRecipeId(recipeId);

        // Close the database connection when you're done
        mySQLiteAdapter.close();

        String title = recipe.getTitle();
        List<String> ingredients = recipe.getIngredients();
        List<String> steps = recipe.getInstructions();

        tv_recipename.setText(title);

        try {
            JSONArray ingredientsArray = new JSONArray(ingredients);
            List<String> ingredientList = new ArrayList<>();

            for (int i = 0; i < ingredientsArray.length(); i++) {
                String ingredient = ingredientsArray.getString(i);
                ingredientList.add(ingredient);
            }

            recipe_ingredients = ingredientList;

            recycler_ingredients.setHasFixedSize(true);
            recycler_ingredients.setLayoutManager(new LinearLayoutManager(DownloadedRecipeDetailsActivity.this,LinearLayoutManager.VERTICAL, false));

            adapter = new IngredientsAdapter(ingredientList);
            recycler_ingredients.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //output recipe steps
        try {
            JSONArray stepsArray = new JSONArray(steps);
            StringBuilder formattedSteps = new StringBuilder();

            for (int i = 0; i < stepsArray.length(); i++) {
                String instruction = stepsArray.getString(i);
                formattedSteps.append(i + 1).append(". ").append(instruction).append("\n\n");
            }

            TextView stepTextView = new TextView(this);
            stepTextView.setPadding(8, 8, 8, 8);
            stepTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            stepTextView.setTextSize(18);
            stepTextView.setText(formattedSteps.toString());
            stepslayout.addView(stepTextView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public Recipe parseRecipeFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        String id = jsonObject.optString("id");
        String title = jsonObject.optString("title");
        String imageURL = jsonObject.optString("imageURL");

        List<String> ingredients = new ArrayList<>();
        JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
        for (int i = 0; i < ingredientsArray.length(); i++) {
            ingredients.add(ingredientsArray.getString(i));
        }

        List<String> instructions = new ArrayList<>();
        JSONArray instructionsArray = jsonObject.getJSONArray("instructions");
        for (int i = 0; i < instructionsArray.length(); i++) {
            instructions.add(instructionsArray.getString(i));
        }

        return new Recipe(id, title, imageURL, ingredients, instructions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.download_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_back:
                // Handle the back action here
                onBackPressed(); // This will go back to the previous activity
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}