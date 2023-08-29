package my.edu.utar.recipeassignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.recipeassignment.Adapters.IngredientsAdapter;

public class RecipeDetailsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView tv_recipename;
    ImageView recipe_image;
    RecyclerView recycler_ingredients;
    ProgressDialog dialog;
    LinearLayout stepslayout;

    IngredientsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //to find all the views
        findViews();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    Intent intent_home = new Intent(RecipeDetailsActivity.this, MainActivity.class);
                    startActivity(intent_home);
                    return true;

                case R.id.favourite:
                    Intent intent_fav = new Intent(RecipeDetailsActivity.this, FavouriteActivity.class);
                    startActivity(intent_fav);
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

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        String recipeId = intent.getStringExtra("recipeId");
        System.out.println("Response3: " + response);
        System.out.println("recipeId: " + recipeId);


        if (response != null && recipeId != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject targetRecipe = null;

                // Find the recipe with the specified recipeId
                for (int i = 0; i < jsonArray.length(); i++) {

                    dialog = new ProgressDialog(this);
                    dialog.setTitle("Loading Details....");
                    dialog.show();


                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    System.out.println("id: "+ id);

                    if (id.equals(recipeId)) {
                        targetRecipe = jsonObject;
                        System.out.println("targetRecipe: " + targetRecipe.getString("recipe_name"));
                        break; // Stop searching once the target recipe is found
                    }
                }

                if (targetRecipe != null) {

                    dialog.dismiss();

                    String title = targetRecipe.getString("recipe_name");
                    String ingredients = targetRecipe.getString("ingredients");
                    String steps = targetRecipe.getString("steps") ;
                    String imageURL = targetRecipe.getString("imageURL");

                    System.out.println("title: "+ title);
                    System.out.println("ingredients: "+ ingredients);
                    System.out.println("steps: "+ steps);


                    tv_recipename.setText(title);   //recipe title


                    //for card view of ingredients
                    try {
                        JSONArray ingredientsArray = new JSONArray(ingredients);
                        List<String> ingredientList = new ArrayList<>();

                        for (int i = 0; i < ingredientsArray.length(); i++) {
                            String ingredient = ingredientsArray.getString(i);
                            ingredientList.add(ingredient);
                            System.out.println("\n"+ingredientsArray.length()+"\n");
                            System.out.println("\n"+ingredient+"\n");

                        }

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


                        System.out.println("\n"+stepsArray.length()+"\n");
                        System.out.println("\n"+instruction+"\n");
                    }

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
                    //load image using Glide
                    Picasso.get()
                            .load(imageURL)
                            .into(recipe_image);

                } else {
                    dialog.dismiss();

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
}