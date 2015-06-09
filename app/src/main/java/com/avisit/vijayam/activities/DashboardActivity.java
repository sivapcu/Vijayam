package com.avisit.vijayam.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.avisit.vijayam.dao.CourseDao;
import com.avisit.vijayam.dao.QuestionDao;
import com.avisit.vijayam.dao.TopicDao;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.model.Topic;
import com.avisit.vijayam.service.HttpServiceHandler;
import com.avisit.vijayam.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends ActionBarActivity {
    boolean doubleBackToExitPressedOnce = false;
    String errorMsg = "";
    private int courseCount;

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
        courseCount = new CourseDao(this).fetchTotalCourseCount();
        totalCourseCount.setText(Integer.toString(courseCount));

        TextView compTopicCount = (TextView) findViewById(R.id.compTopicCount);
        compTopicCount.setText(new AppParamDao(this).getAppParamValue(Constants.TOPIC_COMPLETED, defaultValue));

        TextView totalTopicCount = (TextView) findViewById(R.id.totalTopicCount);
        totalTopicCount.setText(Integer.toString(new TopicDao(this).fetchTotalTopicCount()));

        TextView compQuesCount = (TextView) findViewById(R.id.compQuesCount);
        compQuesCount.setText(new AppParamDao(this).getAppParamValue(Constants.QUESTION_COMPLETED, defaultValue));

        TextView totalQuesCount = (TextView) findViewById(R.id.totalQuesCount);
        totalQuesCount.setText(Integer.toString(new QuestionDao(this).fetchTotalQuestionCount()));
    }

    public void browseCourses(View view){
        if(courseCount > 0){
            Intent intent = new Intent(getApplicationContext(), MyCoursesActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(DashboardActivity.this, "No Courses available yet. Contact Admin", Toast.LENGTH_SHORT).show();
        }
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
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registerMenuItem = menu.findItem(R.id.register);
        MenuItem syncMenuItem = menu.findItem(R.id.sync);
        if("REGISTERED".equals(new AppParamDao(getApplicationContext()).getAppParamValue(Constants.REGISTRATION_FLAG))){
            registerMenuItem.setEnabled(false);
        } else {
            syncMenuItem.setEnabled(false);
        }
        super.onPrepareOptionsMenu(menu);
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
            finish();
            return true;
        } else if (id == R.id.sync) {
            syncWithServer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void syncWithServer() {
        new AsyncTask<Void, String, Exception>() {
            ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Fetching content updates from server");
                progressDialog.show();
            }

            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    String contentProviderId = new AppParamDao(DashboardActivity.this).getAppParamValue(Constants.CONTENT_PROVIDER_ID);
                    String email = new AppParamDao(DashboardActivity.this).getAppParamValue(Constants.USER_EMAIL);
                    String password = new AppParamDao(DashboardActivity.this).getAppParamValue(Constants.USER_PASSWORD);
                    if(email!=null && !email.isEmpty() && password!=null && !password.isEmpty()){
                        List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                        httpParams.add(new BasicNameValuePair("contentProviderId", contentProviderId));
                        httpParams.add(new BasicNameValuePair("email", email));
                        httpParams.add(new BasicNameValuePair("password", password));
                        HttpServiceHandler httpServiceHandler = new HttpServiceHandler();
                        String response2 = httpServiceHandler.makeServiceCall(Constants.COURSES_BY_CONTENT_PROVIDER, HttpServiceHandler.POST, httpParams);
                        List<Course> courseList = new ObjectMapper().readValue(response2, new TypeReference<ArrayList<Course>>(){});
                        publishProgress("Persisting data to the local database");
                        for(Course course : courseList){
                            if(new CourseDao(DashboardActivity.this).insertIfUpdateFails(course)){
                                for(Topic topic : course.getTopicList()){
                                    if(new TopicDao(DashboardActivity.this).insertIfUpdateFails(topic)){
                                        for(Question question : topic.getQuestions()){
                                            new QuestionDao(DashboardActivity.this).insertIfUpdateFails(question, topic.getId());
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        throw new Exception("Not yet registered. Register before trying to sync.");
                    }

                } catch (IOException e) {
                    return e;
                } catch (Exception e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                progressDialog.dismiss();

                if(exception == null) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(DashboardActivity.this, "Sync Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(DashboardActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void onProgressUpdate(String... values) {
                progressDialog.setMessage(values[0]);
            }
        }.execute(null, null, null);
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
