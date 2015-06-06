package com.avisit.vijayam.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 4/28/2015.
 */
public class AppParamDao extends DataBaseHelper {
    private static final String TAG = AppParamDao.class.getSimpleName();

    public AppParamDao(Context context) {
        super(context);
    }

    public long insert(String key, String value){
        long rowId = 0;
        ContentValues values = new ContentValues();
        values.put("key", key);
        values.put("value", value);
        SQLiteDatabase myDataBase = getWritableDatabase();
        try{
            rowId = myDataBase.insert("app_param", null, values);
        } catch(SQLiteException se){
            Log.e(TAG, "Could not persist the question");
        } finally{
            myDataBase.close();
        }
        return rowId;
    }

    public void update(String key, String value) {
        SQLiteDatabase myDataBase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("value", value);

        if (myDataBase.update("app_param", cv, "key = ?", new String[]{key}) == 0) {
            ContentValues cv2 = new ContentValues();
            cv2.put("key", key);
            cv2.put("value", value);
            myDataBase.insert("app_param", null, cv2);
        }
        myDataBase.close();
    }

    public Map<String, String> fetchAppParamMap() {
        Map<String, String> appParamMap = new HashMap<String, String>();
        SQLiteDatabase myDataBase = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT key, value FROM app_param", null);
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    do {
                        appParamMap.put(cursor.getString(cursor.getColumnIndex("key")), cursor.getString(cursor.getColumnIndex("value")));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            myDataBase.close();
        }
        return appParamMap;
    }

    public String getAppParamValue(String key) {
        String value = null;
        SQLiteDatabase myDataBase = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT value FROM app_param WHERE key = ?", new String[]{key});
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    value = cursor.getString(cursor.getColumnIndex("value"));
                }
            }
        } catch (SQLiteException se ) {
            Log.e(TAG, "Could not open and query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            myDataBase.close();
        }
        return value;
    }

    public String getAppParamValue(String key, String defaultValue) {
        String value = getAppParamValue(key);
        if( value == null){
            value = defaultValue;
        }
        return value;
    }

    public int getAppParamValue(String key, int defaultValue) {
        String value = getAppParamValue(key);
        if( value == null){
            value = Integer.toString(defaultValue);
        }
        return Integer.valueOf(value);
    }
}
