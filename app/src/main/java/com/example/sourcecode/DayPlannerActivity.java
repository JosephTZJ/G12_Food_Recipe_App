package com.example.sourcecode;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sourcecode.Adapters.SQLiteAdapter2;
import com.example.sourcecode.Helpers.MyDataModel;

import java.util.ArrayList;
import java.util.List;

public class DayPlannerActivity extends AppCompatActivity {

    private SQLiteAdapter2 mySQLiteAdapter;
    private SQLiteAdapter2 sqliteAdapter = new SQLiteAdapter2(this);

    private LinearLayout linearview;
    private RecyclerView recyclerView;
    //private MyRecipeAdapter adapter;
    private List<MyDataModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_planner);

        Intent intent = getIntent();
        String thedate = intent.getStringExtra("thedate");
        System.out.println("thedate: " + thedate);

        TextView tv_dayplannertitle = findViewById(R.id.tv_dayplannertitle);
        tv_dayplannertitle.setText(thedate);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayPlannerActivity.this, NewRecipeActivity.class);
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton refreshButton = findViewById(R.id.imageButton2);
        refreshButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(DayPlannerAcitivity.this, DayPlannerAcitivity.class);
                startActivity(getIntent());
            }
        });

        ImageButton backButton = findViewById(R.id.ibt_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayPlannerActivity.this, PlannerActivity.class);
                startActivity(intent);
            }
        });

        // Initialize LinearLayout
        linearview = findViewById(R.id.linearview);

        // Initialize data list
        dataList = new ArrayList<>();

        // Initialize and open SQLiteAdapter for reading
        sqliteAdapter.openToRead();

        Cursor cursor = sqliteAdapter.queryAllByDate(thedate);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String recipeTitle = cursor.getString(cursor.getColumnIndexOrThrow("RecipeTitle"));
                String ingredient = cursor.getString(cursor.getColumnIndexOrThrow("Ingredient"));
                String step = cursor.getString(cursor.getColumnIndexOrThrow("Step"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("Date"));

                MyDataModel dataModel = new MyDataModel(recipeTitle, ingredient, step, date);
                dataList.add(dataModel);

                addCardView(dataModel);
            } while (cursor.moveToNext());
        } else {
            // Handle case when no data is found
            TextView tv_norecord = findViewById(R.id.tv_norecord);
            tv_norecord.setText("You haven't add any recipe here yet");
            //Toast.makeText(this, "No data found for the selected date.", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }

        sqliteAdapter.close();

    }

    private void addCardView(MyDataModel dataModel) {
        // Inflate the CardView layout
        View cardView = getLayoutInflater().inflate(R.layout.card_item_layout, linearview, false);

        // Populate the CardView with data
        TextView recipeTitleTextView = cardView.findViewById(R.id.recipeTitleTextView);
        TextView ingredientTextView = cardView.findViewById(R.id.ingredientTextView);
        TextView stepTextView = cardView.findViewById(R.id.stepTextView);
        //TextView dateTextView = cardView.findViewById(R.id.dateTextView);
        ImageButton deleteButton = cardView.findViewById(R.id.bt_deleteRecipe);

        recipeTitleTextView.setText(dataModel.getRecipeTitle());
        ingredientTextView.setText(dataModel.getIngredient());
        stepTextView.setText(dataModel.getStep());
        //dateTextView.setText(dataModel.getDate());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(recipeTitleTextView.getText().toString(),
                        ingredientTextView.getText().toString(),
                        stepTextView.getText().toString(),
                        dataModel.getDate());
            }
        });

        // Add the CardView to the container
        linearview.addView(cardView);
    }

    private void showDeleteConfirmationDialog(String recipeTitle, String ingredient, String step, String date) {
        // Create and show the AlertDialog to confirm deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(DayPlannerActivity.this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed deletion, perform the deletion action
                if (sqliteAdapter != null) {
                    sqliteAdapter.openToWrite();
                    sqliteAdapter.delete(recipeTitle, ingredient, step, date);
                    sqliteAdapter.close();
                    Toast.makeText(getApplicationContext(), "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where sqLiteAdapter is null
                    Toast.makeText(getApplicationContext(), "sqliteAdapter is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the deletion action, do nothing
            }
        });
        builder.show();
    }

}