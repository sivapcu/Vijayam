package com.avisit.vijayam.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avisit.vijayam.R;

public class DashboardActivity extends ActionBarActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);

        RelativeLayout browse = (RelativeLayout)findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout resume = (RelativeLayout)findViewById(R.id.resumeLastSession);
        resume.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Yet to be implemented!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        RelativeLayout search = (RelativeLayout)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Yet to be implemented!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        RelativeLayout tests = (RelativeLayout)findViewById(R.id.tests);
        tests.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Yet to be implemented!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
