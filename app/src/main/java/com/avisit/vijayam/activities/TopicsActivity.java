package com.avisit.vijayam.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avisit.vijayam.R;
import com.avisit.vijayam.adapters.CourseListViewAdapter;
import com.avisit.vijayam.adapters.TopicListViewAdapter;
import com.avisit.vijayam.dao.CourseDao;
import com.avisit.vijayam.dao.QuestionDao;
import com.avisit.vijayam.dao.TopicDao;
import com.avisit.vijayam.model.Topic;
import com.avisit.vijayam.util.VijayamApplication;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = TopicsActivity.class.getSimpleName();
    private List<Topic> topicList = new ArrayList<Topic>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        setTitle(((VijayamApplication)getApplication()).getSelectedCourse().getCourseName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        listView = (ListView) findViewById(R.id.coursesList);
        topicList = new TopicDao(this).fetchTopics(((VijayamApplication)getApplication()).getSelectedCourse().getCourseId());
        TopicListViewAdapter topicAdapter = new TopicListViewAdapter(this, topicList);
        listView.setAdapter(topicAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_my_courses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      /*  int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
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
        VijayamApplication application = (VijayamApplication) getApplication();
        Topic topic = topicList.get(position);
        application.setSelectedTopic(topic);
        application.setTotalQuestions(new QuestionDao(this).fetchTotalQuestionCount(topic.getTopicId()));
        application.setCurrentQuestionIndex(new QuestionDao(this).fetchLastSessionQuesId(topic.getTopicId()));
        Intent intent = new Intent(parent.getContext(), QuestionsActivity.class);
        parent.getContext().startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MyCoursesActivity.class));
        finish();
    }
}
