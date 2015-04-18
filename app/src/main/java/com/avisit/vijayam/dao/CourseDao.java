package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avisit.vijayam.activities.MyCoursesActivity;
import com.avisit.vijayam.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/17/2015.
 */
public class CourseDao extends DataBaseHelper {
    private static final String TAG = CourseDao.class.getSimpleName();
    public CourseDao(Context context) {
        super(context);
    }


    public List<Course> fetchMyCourses() {
        openReadableDataBase();
        List<Course> courseList = new ArrayList<Course>();
        Cursor cursor = null;
        try{
            cursor = myDataBase.rawQuery("SELECT course_name, course_id, image_name FROM Course ORDER BY sort_order",  null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        Course course = new Course();
                        course.setCourseId(cursor.getInt(cursor.getColumnIndex("course_id")));
                        course.setCourseName(cursor.getString(cursor.getColumnIndex("course_name")));
                        course.setImageName(cursor.getString(cursor.getColumnIndex("image_name")));
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
}
