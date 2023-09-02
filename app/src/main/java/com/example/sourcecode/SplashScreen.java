package com.example.sourcecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getSupportActionBar().hide();

        decorView.setSystemUiVisibility(uiOptions);

        ImageView iv1 = findViewById(R.id.splash_image1);
        ImageView iv2 = findViewById(R.id.splash_image2);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.side_slide);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv1.setVisibility(View.INVISIBLE);
                iv2.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                iv2.startAnimation(animation2);
            }
        });

        iv1.startAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }
}
