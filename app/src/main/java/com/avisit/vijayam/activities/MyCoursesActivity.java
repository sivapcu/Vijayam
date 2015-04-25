package com.avisit.vijayam.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avisit.vijayam.R;
import com.avisit.vijayam.adapters.CourseListViewAdapter;
import com.avisit.vijayam.dao.CourseDao;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.util.VijayamApplication;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = MyCoursesActivity.class.getSimpleName();
    private List<Course> courseList = new ArrayList<Course>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        listView = (ListView) findViewById(R.id.coursesList);
        courseList = new CourseDao(this).fetchMyCourses();
        CourseListViewAdapter courseAdapter = new CourseListViewAdapter(this, courseList);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(this);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((VijayamApplication) getApplication()).setSelectedCourse(courseList.get(position));
        Intent intent = new Intent(parent.getContext(), TopicsActivity.class);
        startActivity(intent);
        MyCoursesActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
