package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class TopicDao extends DataBaseHelper {
    private static final String TAG = TopicDao.class.getSimpleName();
    public TopicDao(Context context) {
        super(context);
    }


    public List<Topic> fetchTopics(int courseId) {
        openReadableDataBase();
        List<Topic> topicList = new ArrayList<Topic>();
        Cursor cursor = null;
        try{
            cursor = myDataBase.rawQuery("SELECT topic_id, topic_name FROM Topic WHERE course_id = ? ORDER BY sort_order", new String[]{Integer.toString(courseId)});
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Topic topic = new Topic();
                        topic.setTopicId(cursor.getInt(cursor.getColumnIndex("topic_id")));
                        topic.setTopicName(cursor.getString(cursor.getColumnIndex("topic_name")));
                        topicList.add(topic);
                    }while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return topicList;
    }


}
