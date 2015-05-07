package com.avisit.vijayam.dao;

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
            close();
        }
        return appParamMap;
    }
}
