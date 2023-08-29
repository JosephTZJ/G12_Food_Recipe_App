package my.edu.utar.recipeassignment.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import my.edu.utar.recipeassignment.R;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{
    private List<String> ingredients;

    public IngredientsAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
        System.out.println(ingredients+"\n");
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_recipe_ingredients, viewGroup, false);
        return new IngredientsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int i) {

        String ingredient = ingredients.get(i);
        ingredientsViewHolder.card_ingredient.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}

class IngredientsViewHolder extends RecyclerView.ViewHolder{
    TextView card_ingredient;

    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        card_ingredient = itemView.findViewById(R.id.card_ingredient);
    }
}
