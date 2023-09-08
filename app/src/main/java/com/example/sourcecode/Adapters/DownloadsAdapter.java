package com.example.sourcecode.Adapters;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sourcecode.DownloadActivity;
import com.example.sourcecode.DownloadedRecipeDetailsActivity;
import com.example.sourcecode.Helpers.Recipe;
import com.example.sourcecode.R;

import java.util.List;


public class DownloadsAdapter  extends RecyclerView.Adapter<DownloadsViewHolder> {


    private List<Recipe> recipes;
    private DownloadActivity downloadActivity;


    public DownloadsAdapter(List<Recipe> recipes, DownloadActivity downloadActivity) {
        this.recipes = recipes;
        this.downloadActivity = downloadActivity;

    }

    @NonNull
    @Override
    public DownloadsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_download_recipe, viewGroup, false);
        return new DownloadsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadsViewHolder downloadsViewHolder, int i) {

        Recipe recipe = recipes.get(i);

        downloadsViewHolder.download_title.setText(recipe.getTitle());
        downloadsViewHolder.download_id.setText(recipe.getId());

        // Set click listener for the item view
        downloadsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DownloadedRecipeDetailsActivity.class);
                intent.putExtra("recipeId", recipe.getId());
                v.getContext().startActivity(intent);

            }
        });

        downloadsViewHolder.btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                downloadActivity.deleteDownloadedRecipe(recipe.getId());
                System.out.println(recipe.getId() + "deleted");
                Toast.makeText(v.getContext(), "Recipe Deleted ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), DownloadActivity.class);
                v.getContext().startActivity(intent);
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



