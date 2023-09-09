package com.example.sourcecode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sourcecode.Adapters.SQLiteAdapter2;

public class NewRecipeActivity extends AppCompatActivity {

    private SQLiteAdapter2 mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        Intent intent = getIntent();
        String thedate = intent.getStringExtra("thedate");
        System.out.println("thedate: " + thedate);

        EditText et_recipeTitle = findViewById(R.id.et_recipeTitle);
        EditText et_ingredient = findViewById(R.id.et_ingredient);
        EditText et_step = findViewById(R.id.et_step);

        mySQLiteAdapter = new SQLiteAdapter2(this);

        Button btAdd = findViewById(R.id.bt_addRecipe);
        btAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String recipeTitle = et_recipeTitle.getText().toString();
                String ingredient = et_ingredient.getText().toString();
                String step = et_step.getText().toString();

                // Create or open the database

                //mySQLiteAdapter.deleteAll();

                // Insert data into the table
                //mySQLiteAdapter.insert("Kimchi Fried Rice", "Kimchi, Rice", "Step 1: put oil, Step 2: put rice and kimchi", thedate);
                if(recipeTitle.isEmpty() || ingredient.isEmpty() || step.isEmpty() ){
                    Toast.makeText( getApplicationContext(), "Please fill in the details", Toast.LENGTH_SHORT).show();
                } else {
                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.insert(recipeTitle, ingredient, step, thedate);
                    mySQLiteAdapter.close();
                    mySQLiteAdapter.openToRead();
                    mySQLiteAdapter.close();

                    finish();
//                    Intent intent = new Intent(NewRecipeActivity.this, DayPlannerAcitivity.class);
//                    startActivity(intent);
                    Toast.makeText( getApplicationContext(), "New Recipe Added Successfully!", Toast.LENGTH_SHORT).show();

                }

//                Intent intent = new Intent(NewRecipeActivity.this, DayPlannerAcitivity.class);
//                startActivity(intent);

            }
        });


        mySQLiteAdapter.openToRead();
//        String contentRead = mySQLiteAdapter.queueAll();
//
        mySQLiteAdapter.close();
//
//        listContent.setText(contentRead);
    }
}