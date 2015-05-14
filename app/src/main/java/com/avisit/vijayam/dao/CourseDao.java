package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avisit.vijayam.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class CourseDao extends DataBaseHelper {
    private static final String TAG = CourseDao.class.getSimpleName();
    public CourseDao(Context context){
        super(context);
    }

    public List<Course> fetchMyCourses() {
        SQLiteDatabase myDataBase = getReadableDatabase();
        List<Course> courseList = new ArrayList<Course>();
        Cursor cursor = null;
        try{
            cursor = myDataBase.rawQuery("SELECT id, name, imageName FROM course ORDER BY sortOrder",  null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Course course = new Course();
                        course.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        course.setName(cursor.getString(cursor.getColumnIndex("name")));
                        course.setImageName(cursor.getString(cursor.getColumnIndex("imageName")));
                        courseList.add(course);
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
        return courseList;
    }

    public long insert(Course course) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", course.getId());
        contentValues.put("name", course.getName());
        contentValues.put("description", course.getDescription());
        contentValues.put("imageName", course.getImageName());
        contentValues.put("enabledFlag", course.isEnabledFlag());
        contentValues.put("contentProviderId", course.getContentProviderId());
        contentValues.put("sortOrder", course.getSortOrder());
        long rowId = myDataBase.insert("course", null, contentValues);
        myDataBase.close();
        return rowId;
    }
}
