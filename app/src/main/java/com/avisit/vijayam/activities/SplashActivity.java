package com.avisit.vijayam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.avisit.vijayam.R;

public class SplashActivity extends ActionBarActivity {
    // Constant Value for Splash Screen time visibility
    private static int SPLASH_SCREEN_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // Create object of Handler class and call method postDelayed to make
        // Splash Screen visible for SPLASH_SCREEN_TIME
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASH_SCREEN_TIME);
    }
}
