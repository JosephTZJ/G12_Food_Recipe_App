package my.edu.utar.recipeassignment.Adapters;

import static android.support.v4.content.res.TypedArrayUtils.getString;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import my.edu.utar.recipeassignment.FavouriteActivity;
import my.edu.utar.recipeassignment.Helpers.Recipe;
import my.edu.utar.recipeassignment.R;
import my.edu.utar.recipeassignment.RecipeDetailsActivity;

public class FavouritesAdapter  extends RecyclerView.Adapter<FavouritesViewHolder>{

//    private List<String> titles;
//    private List<String> imageURLs;
//    private List<String> ids;

    private List<Recipe> recipes;
    private FavouriteActivity favouriteActivity;


    public FavouritesAdapter(List<Recipe> recipes, FavouriteActivity favouriteActivity) {
        this.recipes = recipes;
        this.favouriteActivity = favouriteActivity;

    }

//
//    public FavouritesAdapter(List<String> title, List<String> imageURL, List<String> recipeId, FavouriteActivity favouriteActivity) {
//        this.titles = title;
//        this.imageURLs = imageURL;
//        this.ids = recipeId;
//        this.favouriteActivity = favouriteActivity;
//        System.out.println(title+"\n");
//        System.out.println(imageURL+"\n");
//        System.out.println(recipeId+"\n");
//
//    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_favourite_recipe, viewGroup, false);
        return new FavouritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder favouritesViewHolder, int i) {
//
//        String title = titles.get(i);
//        String imageURL = imageURLs.get(i);
//        String recipeId = ids.get(i);
//        favouritesViewHolder.fav_title.setText(title);
//        favouritesViewHolder.fav_id.setText(recipeId);
//        Picasso.get().load(imageURL).into(favouritesViewHolder.fav_imageView);
//
//        // Set click listener for the item view
//        favouritesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle item click here
//                Toast.makeText(v.getContext(), "Item clicked! Recipe ID: " + recipeId, Toast.LENGTH_SHORT).show();
//                System.out.println("recipeId: " + recipeId);


                // Start the RecipeDetailsActivity and pass the recipe ID
//                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
//                intent.putExtra("response", response);
//                intent.putExtra("recipeId", recipeId);
//                System.out.println("recipeId: " + recipeId);
//                v.getContext().startActivity(intent);

//                // Open the connection and fetch response from the database
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("run: ");
//
//                        String response = favouriteActivity.getResponseFromDatabase();
//
//                        if (response != null) {
//
//                            // Start the RecipeDetailsActivity and pass the response and recipe ID
//                            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
//                            intent.putExtra("response", response);
//                            intent.putExtra("recipeId", recipeId);
//                            v.getContext().startActivity(intent);
//                        } else {
//                            // Handle error if response is null
//                            // You can show a toast or dialog indicating the error
//                        }
//                    }
//                }).start();
//            }
//        });


        Recipe recipe = recipes.get(i);

        favouritesViewHolder.fav_title.setText(recipe.getTitle());
        Picasso.get().load(recipe.getImageURL()).into(favouritesViewHolder.fav_imageView);
        favouritesViewHolder.fav_id.setText(recipe.getId());

        // Set click listener for the item view
        favouritesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Item clicked! Recipe ID: " + recipe.getId(), Toast.LENGTH_SHORT).show();
                System.out.println("recipeId: " + recipe.getId());
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

                        if (response != null) {

                            // Start the RecipeDetailsActivity and pass the response and recipe ID
                            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
                            intent.putExtra("response", response);
                            intent.putExtra("recipeId", recipe.getId());
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
        return recipes.size();
    }


}

class FavouritesViewHolder extends RecyclerView.ViewHolder{
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