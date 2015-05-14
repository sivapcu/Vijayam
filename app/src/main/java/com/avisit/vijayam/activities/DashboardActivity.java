package com.avisit.vijayam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avisit.vijayam.R;
import com.avisit.vijayam.dao.AppParamDao;
import com.avisit.vijayam.util.Constants;

public class DashboardActivity extends ActionBarActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        customizeActionBar();
        initDashboard();
    }

    private void customizeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void initDashboard() {
        String defaultValue = "0";
        TextView compCourseCount = (TextView) findViewById(R.id.compCourseCount);
        compCourseCount.setText(new AppParamDao(this).getAppParamValue(Constants.COURSE_COMPLETED, defaultValue));

        TextView totalCourseCount = (TextView) findViewById(R.id.totalCourseCount);
        totalCourseCount.setText(new AppParamDao(this).getAppParamValue(Constants.COURSE_COUNT, defaultValue));

        TextView compTopicCount = (TextView) findViewById(R.id.compTopicCount);
        compTopicCount.setText(new AppParamDao(this).getAppParamValue(Constants.TOPIC_COMPLETED, defaultValue));

        TextView totalTopicCount = (TextView) findViewById(R.id.totalTopicCount);
        totalTopicCount.setText(new AppParamDao(this).getAppParamValue(Constants.TOPIC_COUNT, defaultValue));

        TextView compQuesCount = (TextView) findViewById(R.id.compQuesCount);
        compQuesCount.setText(new AppParamDao(this).getAppParamValue(Constants.QUESTION_COMPLETED, defaultValue));

        TextView totalQuesCount = (TextView) findViewById(R.id.totalQuesCount);
        totalQuesCount.setText(new AppParamDao(this).getAppParamValue(Constants.QUESTION_COUNT, defaultValue));
    }

    public void browseCourses(View view){
        Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
        startActivity(intent);
    }

    public void resumeLastSession(View view){
        Toast.makeText(DashboardActivity.this, "Resume Last Session!", Toast.LENGTH_SHORT).show();
    }

    public void search(View view){
        Toast.makeText(DashboardActivity.this, "Search feature yet to be implemented!", Toast.LENGTH_SHORT).show();
    }

    public void mockExams(View view){
        Toast.makeText(DashboardActivity.this, "Mock Exams!", Toast.LENGTH_SHORT).show();
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
            finish();
            return true;
        } else if (id == R.id.register) {
            Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
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
        }, 1500);
    }
}
