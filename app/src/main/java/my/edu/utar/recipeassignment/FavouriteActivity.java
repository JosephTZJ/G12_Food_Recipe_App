package my.edu.utar.recipeassignment;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavouriteActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){


                case R.id.home:
                    Intent intent = new Intent(FavouriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.favourite:
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




    }
}