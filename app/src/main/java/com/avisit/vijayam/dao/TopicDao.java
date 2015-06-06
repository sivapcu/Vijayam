package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avisit.vijayam.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class TopicDao extends DataBaseHelper {
    private static final String TAG = TopicDao.class.getSimpleName();
    public TopicDao(Context context){
        super(context);
    }


    public List<Topic> fetchTopics(int courseId) {
        SQLiteDatabase myDataBase = getReadableDatabase();
        List<Topic> topicList = new ArrayList<Topic>();
        Cursor cursor = null;
        try{
            cursor = myDataBase.rawQuery("SELECT id, name FROM topic WHERE courseId = ? ORDER BY sortOrder", new String[]{Integer.toString(courseId)});
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Topic topic = new Topic();
                        topic.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        topic.setName(cursor.getString(cursor.getColumnIndex("name")));
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
            myDataBase.close();
        }
        return topicList;
    }


    public void insertTopicList(List<Topic> topicList) {
        if(topicList!=null && !topicList.isEmpty()){
            for(Topic topic : topicList){
                insert(topic);
            }
        }
    }

    public long insert(Topic topic) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        long rowId;
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", topic.getId());
            contentValues.put("name", topic.getName());
            contentValues.put("description", topic.getDescription());
            contentValues.put("enabledFlag", topic.isEnabledFlag());
            contentValues.put("sortOrder", topic.getSortOrder());
            contentValues.put("courseId", topic.getCourseId());
            rowId = myDataBase.insert("topic", null, contentValues);
        } finally {
            myDataBase.close();
        }
        return rowId;
    }

    public boolean insertIfUpdateFails(Topic topic) {
        boolean successFlag = false;
        SQLiteDatabase myDataBase = getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("name", topic.getName());
            cv.put("description", topic.getDescription());
            cv.put("enabledFlag", topic.isEnabledFlag() ? 1 : 0);
            cv.put("sortOrder", topic.getSortOrder());
            cv.put("courseId", topic.getCourseId());
            int rowsUpdated = myDataBase.update("topic", cv, "id = ?", new String[]{Integer.toString(topic.getId())});
            if (rowsUpdated != 0) {
                successFlag = true;
            } else {
                cv.put("id", topic.getId());
                successFlag = myDataBase.insert("topic", null, cv)!= -1;
            }
        } finally {
            myDataBase.close();
        }
        return successFlag;
    }
}
