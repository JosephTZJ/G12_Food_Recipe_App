package com.example.sourcecode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PlannerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    // Declare SharedPreferences variable
    private SharedPreferences sharedPreferences;

    // SharedPreferences key for storing selected item index
    private static final String PREF_SELECTED_NAV_ITEM_INDEX = "selected_nav_item_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        //sharedPreferences2 = getSharedPreferences("my_prefs2", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        int selectedIndex = getSelectedNavItemIndex(0);
        bottomNavigationView.getMenu().getItem(selectedIndex).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    saveSelectedNavItemIndex(0);
                    Intent intent = new Intent(PlannerActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.favourite:
                    saveSelectedNavItemIndex(1);
                    Intent intent2 = new Intent(PlannerActivity.this, FavouriteActivity.class);
                    startActivity(intent2);
                    return true;

                case R.id.cart:
                    saveSelectedNavItemIndex(2);
                    Intent intent3 = new Intent(PlannerActivity.this, ShoppingCart.class);
                    startActivity(intent3);
                    return true;

                case R.id.calendar:
                    saveSelectedNavItemIndex(3);
                    return true;

            }
            return false;
        });

        Calendar calendar = Calendar.getInstance();

        // Set the calendar to the current date
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Find the first day of the week (Monday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        // Define a date format pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.ENGLISH);

        // Format the dates
        String[] day = new String[7];
        int[] month = new int[7];

        for (int i=0; i<6; i++){
            day[i] = dateFormat.format(calendar.getTime());
            month[i] = calendar.get(Calendar.MONTH) + 1;
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
        day[6] = dateFormat.format(calendar.getTime());
        month[6] = calendar.get(Calendar.MONTH) + 1;

        // Get the month names
        String[] monthname = new String[7];
        for (int i=0; i<7; i++){
            monthname[i] = getMonthName(month[i]);
        }

        // Print the results
        TextView tv_date = findViewById(R.id.tv_date);
        tv_date.setText(day[0] + " " + monthname[0] + " - " + day[6] + " " + monthname[6]);

        TextView tv_datemon = findViewById(R.id.tv_datemon);
        tv_datemon.setText(day[0] + " " + monthname[0]);
        TextView tv_datetue = findViewById(R.id.tv_datetue);
        tv_datetue.setText(day[1] + " " + monthname[1]);
        TextView tv_datewed = findViewById(R.id.tv_datewed);
        tv_datewed.setText(day[2] + " " + monthname[2]);
        TextView tv_datethu = findViewById(R.id.tv_datethu);
        tv_datethu.setText(day[3] + " " + monthname[3]);
        TextView tv_datefri = findViewById(R.id.tv_datefri);
        tv_datefri.setText(day[4] + " " + monthname[4]);
        TextView tv_datesat = findViewById(R.id.tv_datesat);
        tv_datesat.setText(day[5] + " " + monthname[5]);
        TextView tv_datesun = findViewById(R.id.tv_datesun);
        tv_datesun.setText(day[6] + " " + monthname[6]);

        ImageButton ibt_lastweek = findViewById(R.id.ibt_lastweek);
        ImageButton ibt_nextweek = findViewById(R.id.ibt_nextweek);

        ibt_lastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Find the first day of the week (Monday)
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.setFirstDayOfWeek(Calendar.MONDAY);

                calendar.add(Calendar.DATE, -7); // Move back one week

                // Define a date format pattern
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.ENGLISH);

                // Format the dates
                String[] day = new String[7];
                int[] month = new int[7];

                for (int i=0; i<6; i++){
                    day[i] = dateFormat.format(calendar.getTime());
                    month[i] = calendar.get(Calendar.MONTH) + 1;
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                day[6] = dateFormat.format(calendar.getTime());
                month[6] = calendar.get(Calendar.MONTH) + 1;

                // Get the month names
                String[] monthname = new String[7];
                for (int i=0; i<7; i++){
                    monthname[i] = getMonthName(month[i]);
                }

                // Print the results
                TextView tv_date = findViewById(R.id.tv_date);
                tv_date.setText(day[0] + " " + monthname[0] + " - " + day[6] + " " + monthname[6]);

                TextView tv_datemon = findViewById(R.id.tv_datemon);
                tv_datemon.setText(day[0] + " " + monthname[0]);
                TextView tv_datetue = findViewById(R.id.tv_datetue);
                tv_datetue.setText(day[1] + " " + monthname[1]);
                TextView tv_datewed = findViewById(R.id.tv_datewed);
                tv_datewed.setText(day[2] + " " + monthname[2]);
                TextView tv_datethu = findViewById(R.id.tv_datethu);
                tv_datethu.setText(day[3] + " " + monthname[3]);
                TextView tv_datefri = findViewById(R.id.tv_datefri);
                tv_datefri.setText(day[4] + " " + monthname[4]);
                TextView tv_datesat = findViewById(R.id.tv_datesat);
                tv_datesat.setText(day[5] + " " + monthname[5]);
                TextView tv_datesun = findViewById(R.id.tv_datesun);
                tv_datesun.setText(day[6] + " " + monthname[6]);

                ImageButton ibt_monday = findViewById(R.id.ibt_monday);
                ibt_monday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[0] + " " + monthname[0];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_tuesday = findViewById(R.id.ibt_tuesday);
                ibt_tuesday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[1] + " " + monthname[1];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_wednesday = findViewById(R.id.ibt_wednesday);
                ibt_wednesday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[2] + " " + monthname[2];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_thursday = findViewById(R.id.ibt_thursday);
                ibt_thursday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[3] + " " + monthname[3];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_friday = findViewById(R.id.ibt_friday);
                ibt_friday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[4] + " " + monthname[4];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_saturday = findViewById(R.id.ibt_saturday);
                ibt_saturday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[5] + " " + monthname[5];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_sunday = findViewById(R.id.ibt_sunday);
                ibt_sunday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[6] + " " + monthname[6];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });
            }
        });

        ibt_nextweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the first day of the week (Monday)
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.setFirstDayOfWeek(Calendar.MONDAY);

                calendar.add(Calendar.DATE, +7); // Move back one week

                // Define a date format pattern
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.ENGLISH);

                // Format the dates
                String[] day = new String[7];
                int[] month = new int[7];

                for (int i=0; i<6; i++){
                    day[i] = dateFormat.format(calendar.getTime());
                    month[i] = calendar.get(Calendar.MONTH) + 1;
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                day[6] = dateFormat.format(calendar.getTime());
                month[6] = calendar.get(Calendar.MONTH) + 1;

                // Get the month names
                String[] monthname = new String[7];
                for (int i=0; i<7; i++){
                    monthname[i] = getMonthName(month[i]);
                }

                // Print the results
                TextView tv_date = findViewById(R.id.tv_date);
                tv_date.setText(day[0] + " " + monthname[0] + " - " + day[6] + " " + monthname[6]);

                TextView tv_datemon = findViewById(R.id.tv_datemon);
                tv_datemon.setText(day[0] + " " + monthname[0]);
                TextView tv_datetue = findViewById(R.id.tv_datetue);
                tv_datetue.setText(day[1] + " " + monthname[1]);
                TextView tv_datewed = findViewById(R.id.tv_datewed);
                tv_datewed.setText(day[2] + " " + monthname[2]);
                TextView tv_datethu = findViewById(R.id.tv_datethu);
                tv_datethu.setText(day[3] + " " + monthname[3]);
                TextView tv_datefri = findViewById(R.id.tv_datefri);
                tv_datefri.setText(day[4] + " " + monthname[4]);
                TextView tv_datesat = findViewById(R.id.tv_datesat);
                tv_datesat.setText(day[5] + " " + monthname[5]);
                TextView tv_datesun = findViewById(R.id.tv_datesun);
                tv_datesun.setText(day[6] + " " + monthname[6]);

                ImageButton ibt_monday = findViewById(R.id.ibt_monday);
                ibt_monday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[0] + " " + monthname[0];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_tuesday = findViewById(R.id.ibt_tuesday);
                ibt_tuesday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[1] + " " + monthname[1];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_wednesday = findViewById(R.id.ibt_wednesday);
                ibt_wednesday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[2] + " " + monthname[2];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_thursday = findViewById(R.id.ibt_thursday);
                ibt_thursday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[3] + " " + monthname[3];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_friday = findViewById(R.id.ibt_friday);
                ibt_friday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[4] + " " + monthname[4];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_saturday = findViewById(R.id.ibt_saturday);
                ibt_saturday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[5] + " " + monthname[5];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });

                ImageButton ibt_sunday = findViewById(R.id.ibt_sunday);
                ibt_sunday.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                        String thedate = day[6] + " " + monthname[6];
                        intent.putExtra("thedate", thedate);
                        startActivity(intent);
                    }
                });
            }
        });

        for( int i=0; i<7; i++){
            System.out.println((i+1) + "th");
            System.out.println(day[i]);
            System.out.println(monthname[i]);
        }


        ImageButton ibt_monday = findViewById(R.id.ibt_monday);
        ibt_monday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[0] + " " + monthname[0];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_tuesday = findViewById(R.id.ibt_tuesday);
        ibt_tuesday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[1] + " " + monthname[1];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_wednesday = findViewById(R.id.ibt_wednesday);
        ibt_wednesday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[2] + " " + monthname[2];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_thursday = findViewById(R.id.ibt_thursday);
        ibt_thursday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[3] + " " + monthname[3];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_friday = findViewById(R.id.ibt_friday);
        ibt_friday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[4] + " " + monthname[4];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_saturday = findViewById(R.id.ibt_saturday);
        ibt_saturday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[5] + " " + monthname[5];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });

        ImageButton ibt_sunday = findViewById(R.id.ibt_sunday);
        ibt_sunday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlannerActivity.this, DayPlannerActivity.class);
                String thedate = day[6] + " " + monthname[6];
                intent.putExtra("thedate", thedate);
                startActivity(intent);
            }
        });
    }

    private void saveSelectedNavItemIndex(int selectedIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SELECTED_NAV_ITEM_INDEX, selectedIndex);
        editor.apply();
    }

    private int getSelectedNavItemIndex(int defaultValue) {
        return sharedPreferences.getInt(PREF_SELECTED_NAV_ITEM_INDEX, defaultValue);
    }

    private static String getMonthName(int month) {
        String[] monthNames = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "June",
                "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
        };
        return monthNames[month - 1];
    }
}