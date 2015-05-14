package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.avisit.vijayam.util.AndroidUtils;
import com.avisit.vijayam.util.Constants;

public class DataBaseHelper extends SQLiteOpenHelper {
    protected Context context;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context to use to open or create the database
     */
    public DataBaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, AndroidUtils.getAppVersionCode(context));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getSimpleName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1:
                //version 1 to version 2 updates here
                //version1to2(db);
                // we want both updates, so no break statement here...
            case 2:
                //version 2 to version 3 updates here
                //version2to3(db);
        }
    }
}