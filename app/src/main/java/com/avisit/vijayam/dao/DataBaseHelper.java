package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.avisit.vijayam.util.VersionUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_COURSE = "CREATE TABLE Course (_id INTEGER PRIMARY KEY NOT NULL, course_name TEXT, sort_order NUMERIC, image_name TEXT );";
    private static final String CREATE_TABLE_TOPIC = "CREATE TABLE Topic (_id INTEGER PRIMARY KEY NOT NULL, course_id NUMERIC, topic_name TEXT, sort_order NUMERIC);";
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE Question (_id INTEGER PRIMARY KEY NOT NULL, topic_id NUMERIC, question_text TEXT, sort_order NUMERIC, review_flag TEXT, win_flag TEXT);";
    private static final String CREATE_TABLE_OPTION = "CREATE TABLE Option(option_id INTEGER NOT NULL, question_id NUMERIC, option_text TEXT, correct_flag TEXT, marked_flag TEXT, PRIMARY KEY(question_id, option_id));";
    private static final String CREATE_TABLE_QUESTION_IMAGE = "CREATE TABLE QuestionImage (image_id INTEGER NOT NULL, question_id NUMERIC, image_name TEXT, PRIMARY KEY(question_id, image_id));";
    private static final String CREATE_TABLE_AppParam = "CREATE TABLE AppParam (key TEXT PRIMARY KEY NOT NULL, value TEXT);";

    private static final String DB_NAME = "vijayam.db";

    protected Context context;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context to use to open or create the database
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VersionUtils.getAppVersionCode(context));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
        initializeAppParamTable(db);
        initializeData(db);
        //version3to4(db);
    }

    private void initializeData(SQLiteDatabase db) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(new HttpGet("http://192.168.43.243:8080/vijayam/rest/courses/all"));
                    int statusCode = response.getStatusLine().getStatusCode();
                    String content;
                    String errorMsg;
                    if (statusCode == 200) {
                        HttpEntity httpEntity = response.getEntity();
                        content = EntityUtils.toString(httpEntity);
                        if(content!=null && !content.isEmpty()){
                            JSONArray jsonArray = new JSONArray(content);
                            for(int i = 0; i < jsonArray.length(); i++){
                                Object obj = jsonArray.get(i);

                            }
                        }
                    } else {
                        errorMsg = EntityUtils.toString(response.getEntity());
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getSimpleName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1:
                //version 1 to version 2 updates here
                version1to2(db);
                // we want both updates, so no break statement here...
            case 2:
                //version 2 to version 3 updates here
                version2to3(db);
        }
    }

    private void createAllTables(SQLiteDatabase db) {
        dropTables(db);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_OPTION);
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_QUESTION_IMAGE);
        db.execSQL(CREATE_TABLE_TOPIC);
        db.execSQL(CREATE_TABLE_AppParam);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Course");
        db.execSQL("DROP TABLE IF EXISTS Topic");
        db.execSQL("DROP TABLE IF EXISTS Question");
        db.execSQL("DROP TABLE IF EXISTS Option");
        db.execSQL("DROP TABLE IF EXISTS QuestionImage");
        db.execSQL("DROP TABLE IF EXISTS AppParam");
    }

    private void initializeAppParamTable(SQLiteDatabase db) {
        db.execSQL("INSERT INTO AppParam VALUES('contentProviderId','vijayam');");
        db.execSQL("INSERT INTO AppParam VALUES('courseCount',0);");
        db.execSQL("INSERT INTO AppParam VALUES('courseCompleted',0);");
        db.execSQL("INSERT INTO AppParam VALUES('topicCount',0);");
        db.execSQL("INSERT INTO AppParam VALUES('topicCompleted',0);");
        db.execSQL("INSERT INTO AppParam VALUES('questionCount',0);");
        db.execSQL("INSERT INTO AppParam VALUES('questionCompleted',0);");
    }

    private void version1to2(SQLiteDatabase db) {
        //no database updates
    }

    private void version2to3(SQLiteDatabase db) {
        createAllTables(db);
        initializeAppParamTable(db);
    }
}