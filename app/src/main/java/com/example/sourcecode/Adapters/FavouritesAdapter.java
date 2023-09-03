package com.example.sourcecode.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


import com.example.sourcecode.FavouriteActivity;
import com.example.sourcecode.R;
import com.example.sourcecode.RecipeDetailsActivity;

public class FavouritesAdapter  extends RecyclerView.Adapter<FavouritesViewHolder> {


    //private List<String> recipes;
    private FavouriteActivity favouriteActivity;
    private SQLiteAdapter mySQLiteAdapter;

    private List<String> favoriteRecipes;


    public FavouritesAdapter(List<String> favoriteRecipes, FavouriteActivity favouriteActivity) {
        this.favouriteActivity = favouriteActivity;
        this.favoriteRecipes = favoriteRecipes;

    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_favourite_recipe, viewGroup, false);
        return new FavouritesViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder favouritesViewHolder, int i) {


//        Recipe recipe = recipes.get(i);

//        favouritesViewHolder.fav_title.setText(recipe.getTitle());
//        Picasso.get().load(recipe.getImageURL()).into(favouritesViewHolder.fav_imageView);
//        favouritesViewHolder.fav_id.setText(recipe.getId());

//        mySQLiteAdapter = new SQLiteAdapter(this);
//        mySQLiteAdapter.openToRead();

        List<String> ids = favoriteRecipes;

        System.out.println("the ids in adapter:" + ids);

        String id = ids.get(i);
        System.out.println("the id in adapter:" + id);


        // Open the connection and fetch response from the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run: ");

                String response = favouriteActivity.getRecipeFromDatabase(id);
                if (response != null) {
                    try {
                        // Parse the response JSON
                        JSONObject jsonResponse = new JSONObject(response);

                        // Extract the specific data you need from the JSON
                        String title = jsonResponse.getString("recipe_name"); // Change "recipe_name" to the actual key in your JSON
                        String imageURL = jsonResponse.getString("imageURL"); // Change "imageURL" to the actual key in your JSON
                        String recipeId = jsonResponse.getString("id"); // Change "id" to the actual key in your JSON

                        // Update the UI on the main thread
                        favouritesViewHolder.itemView.post(new Runnable() {
                            @Override
                            public void run() {
                                // Set the extracted data to your ViewHolder views
                                favouritesViewHolder.fav_title.setText(title);
                                Picasso.get().load(imageURL).into(favouritesViewHolder.fav_imageView);
                                favouritesViewHolder.fav_id.setText(recipeId);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle error if response is null
                    // You can show a toast or dialog indicating the error
                }
            }
        }).start();


        // Set click listener for the item view
        favouritesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Item clicked! Recipe ID: " + recipe.getId(), Toast.LENGTH_SHORT).show();
//                System.out.println("recipeId: " + recipe.getId());
//                // Handle item click here
//                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
//                intent.putExtra("recipeId", recipe.getId());
//                v.getContext().startActivity(intent);

                // Open the connection and fetch response from the database
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("run: ");

                        String response = favouriteActivity.getResponseFromDatabase();

                        System.out.println("the response in adapter:" + response);
                        if (response != null) {

                            // Start the RecipeDetailsActivity and pass the response and recipe ID
                            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
                            intent.putExtra("response", response);
                            intent.putExtra("recipeId", id);
                            v.getContext().startActivity(intent);
                        } else {
                            // Handle error if response is null
                            // You can show a toast or dialog indicating the error
                        }
                    }
                }).start();
            }
        });
    }


    @Override
    public int getItemCount() {
        return favoriteRecipes.size();
    }
}


class FavouritesViewHolder extends RecyclerView.ViewHolder {
    TextView fav_title;
    ImageView fav_imageView;
    TextView fav_id;

    public FavouritesViewHolder(@NonNull View itemView) {
        super(itemView);

        fav_title = itemView.findViewById(R.id.fav_title);
        fav_imageView = itemView.findViewById(R.id.fav_imageView);
        fav_id = itemView.findViewById(R.id.invisible_id_fav);
    }

}
