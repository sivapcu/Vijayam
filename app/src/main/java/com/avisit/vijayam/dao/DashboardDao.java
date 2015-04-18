package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.avisit.vijayam.model.Dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/15/2015.
 */
public class DashboardDao extends DataBaseHelper {
    private static final String TAG = DashboardDao.class.getSimpleName();
    public DashboardDao(Context context) {
        super(context);
    }

    public List<Dashboard> fetchDashboardItems() {
        openReadableDataBase();
        List<Dashboard> dashboardList = new ArrayList<Dashboard>();
        Cursor cursor = null;
        try {
            cursor = myDataBase.rawQuery("SELECT item_id, item_name, image_name FROM dashboard ORDER BY item_id", null);
            if (cursor != null ) {
                if(cursor.moveToFirst()) {
                    do {
                        Dashboard dashboard = new Dashboard();
                        dashboard.setItemId(cursor.getInt(cursor.getColumnIndex("item_id")));
                        dashboard.setItemName(cursor.getString(cursor.getColumnIndex("item_name")));
                        dashboard.setImageName(cursor.getString(cursor.getColumnIndex("image_name")));
                        dashboardList.add(dashboard);
                    }while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            System.out.println("Could not Open and Query the database");
        } finally {
            if(cursor!=null){
                cursor.close();
            }
            close();
        }
        return dashboardList;
    }
}
