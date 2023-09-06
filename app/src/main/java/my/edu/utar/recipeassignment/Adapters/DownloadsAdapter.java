package my.edu.utar.recipeassignment.Adapters;

import static android.support.v4.content.res.TypedArrayUtils.getString;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import my.edu.utar.recipeassignment.DownloadActivity;
import my.edu.utar.recipeassignment.DownloadedRecipeDetailsActivity;
import my.edu.utar.recipeassignment.Helpers.Recipe;
import my.edu.utar.recipeassignment.R;
import my.edu.utar.recipeassignment.RecipeDetailsActivity;

public class DownloadsAdapter  extends RecyclerView.Adapter<DownloadsViewHolder> {

//    private List<String> titles;
//    private List<String> imageURLs;
//    private List<String> ids;

    private List<Recipe> recipes;
    private DownloadActivity downloadActivity;


    public DownloadsAdapter(List<Recipe> recipes, DownloadActivity downloadActivity) {
        this.recipes = recipes;
        this.downloadActivity = downloadActivity;

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
    public DownloadsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_download_recipe, viewGroup, false);
        return new DownloadsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadsViewHolder downloadsViewHolder, int i) {
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
        System.out.println("the recipe is " + recipe);

        downloadsViewHolder.download_title.setText(recipe.getTitle());
        downloadsViewHolder.download_id.setText(recipe.getId());

        // Set click listener for the item view
        downloadsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Item clicked! Recipe ID: " + recipe.getId(), Toast.LENGTH_SHORT).show();
                System.out.println("recipeId: " + recipe.getId());

                Intent intent = new Intent(v.getContext(), DownloadedRecipeDetailsActivity.class);
                intent.putExtra("recipeId", recipe.getId());
                v.getContext().startActivity(intent);

//                // Handle item click here
//                Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
//                intent.putExtra("recipeId", recipe.getId());
//                v.getContext().startActivity(intent);

                // Open the connection and fetch response from the database
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("run: ");

                        //String response = downloadActivity.getResponseFromDatabase();

//                        if (response != null) {
//
//                            // Start the RecipeDetailsActivity and pass the response and recipe ID
//                            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
//                            //intent.putExtra("response", response);
//                            intent.putExtra("recipeId", recipe.getId());
//                            v.getContext().startActivity(intent);
//                        } else {
//                            // Handle error if response is null
//                            // You can show a toast or dialog indicating the error
//                        }
                    }
                }).start();*/


            }
        });

        downloadsViewHolder.btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("deleting the item");
                System.out.println("the string id is" + recipe.getId().toString());
                downloadActivity.deleteDownloadedRecipe(recipe.getId());
                System.out.println(recipe.getId() + "deleted");
            }
        });


    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}


    class DownloadsViewHolder extends RecyclerView.ViewHolder {
        TextView download_title;
        TextView download_id;
        Button btn_delete; // Add a reference to the delete button here

        public DownloadsViewHolder(@NonNull View itemView) {
            super(itemView);

            download_title = itemView.findViewById(R.id.download_title);
            download_id = itemView.findViewById(R.id.invisible_id_downlaod);
            btn_delete = itemView.findViewById(R.id.btn_delete); // Initialize the delete button
        }
    }

