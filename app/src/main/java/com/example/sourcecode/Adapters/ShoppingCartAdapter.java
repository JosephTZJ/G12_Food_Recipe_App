package com.example.sourcecode.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.sourcecode.R;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {


    private Context context;
    private Map<String, List<String>> shoppingList;

    public ShoppingCartAdapter(Context context, Map<String, List<String>> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_shopping_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Convert the map entries to a list
        List<Map.Entry<String, List<String>>> entries = new ArrayList<>(shoppingList.entrySet());

        Map.Entry<String, List<String>> entry = entries.get(position);
        String recipeTitle = entry.getKey();
        List<String> ingredients = entry.getValue();

        holder.textViewTitle.setText(recipeTitle);

        // Set up RecyclerView for ingredients
        IngredientsAdapter ingredientAdapter = new IngredientsAdapter(ingredients);
        holder.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        RecyclerView ingredientRecyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.shopping_cart_title);
            ingredientRecyclerView = itemView.findViewById(R.id.ingredientRecyclerView);

        }
    }
}
