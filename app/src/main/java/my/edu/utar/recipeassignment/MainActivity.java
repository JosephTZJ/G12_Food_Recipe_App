package my.edu.utar.recipeassignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RecyclerView  recyclerView;
    BottomNavigationView bottomNavigationView;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardView = findViewById(R.id.random_list_container);
        intent = new Intent(MainActivity.this, RecipeDetailsActivity.class); // Initialize the Intent


        //start connection to db
        Handler mHandler = new Handler();
        MyThread connectingThread = new MyThread(mHandler);
        connectingThread.start();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {


                case R.id.home:
                    return true;

                case R.id.favourite:
                    Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                    startActivity(intent);
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


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click here
                Toast.makeText(MainActivity.this, "Card clicked!", Toast.LENGTH_SHORT).show();

                TextView id = findViewById(R.id.invisible_id);
                String recipeId = id.getText().toString();
                intent.putExtra("recipeId", recipeId);

                startActivity(intent);
            }

        });
    }

    private class MyThread extends Thread {
        private Handler mHandler;


        public MyThread(Handler handler) {
            this.mHandler = handler;
        }

        public void run(){
            try {


                URL url = new URL("https://fsgrlullvgbtzvatcujr.supabase.co/rest/v1/Recipe?"
                );
                HttpURLConnection hc = (HttpURLConnection) url.openConnection();
                hc.setRequestProperty("apikey", getString(R.string.SUPABASE_KEY));
                hc.setRequestProperty("Authorization", "Bearer " + getString(R.string.SUPABASE_KEY));

                int responseCode = hc.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String result = response.toString();
                    System.out.println("Response: " + result);
                    intent.putExtra("response", result);


                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String recipeName = jsonObject.getString("recipe_name");

                    String Id = jsonObject.getString("id");

                    Runnable uiUpdateRunnable = new Runnable() {
                        @Override
                        public void run() {
                            TextView title = findViewById(R.id.textView_title);
                            TextView stored_id = findViewById(R.id.invisible_id);
                            title.setText(recipeName);
                            stored_id.setText(Id);
                        }
                    };

                    mHandler.post(uiUpdateRunnable);


                } else {
                    Log.i("HTTP response failed: ", String.valueOf(hc.getResponseCode()));
                }

            }catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }


    }


}