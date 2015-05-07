package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.avisit.vijayam.util.VersionUtils;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_COURSE = "CREATE TABLE Course (_id INTEGER PRIMARY KEY NOT NULL, course_name TEXT, sort_order NUMERIC, image_name TEXT );";
    //private static final String CREATE_TABLE_OPTION = "CREATE TABLE Option (_id INTEGER PRIMARY KEY NOT NULL, question_id NUMERIC, option_text TEXT, correct_flag TEXT, marked_flag TEXT);";
    private static final String CREATE_TABLE_OPTION = "CREATE TABLE Option(option_id INTEGER NOT NULL, question_id NUMERIC, option_text TEXT, correct_flag TEXT, marked_flag TEXT, PRIMARY KEY(question_id, option_id));";
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE Question (_id INTEGER PRIMARY KEY NOT NULL, topic_id NUMERIC, question_text TEXT, sort_order NUMERIC, review_flag TEXT, win_flag TEXT);";
    private static final String CREATE_TABLE_QUESTION_IMAGE = "CREATE TABLE QuestionImage (_id INTEGER PRIMARY KEY NOT NULL, question_id NUMERIC, image_name TEXT);";
    private static final String CREATE_TABLE_TOPIC = "CREATE TABLE Topic (_id INTEGER PRIMARY KEY NOT NULL, course_id NUMERIC, topic_name TEXT, sort_order NUMERIC);";
    private static final String CREATE_TABLE_TOPIC_QUESTION = "CREATE TABLE TopicQuestionMap (_id INTEGER PRIMARY KEY NOT NULL, topic_id NUMERIC, question_id NUMERIC);";
    private static final String CREATE_TABLE_APP_PARAM = "CREATE TABLE app_param (_id INTEGER PRIMARY KEY NOT NULL, key TEXT, value TEXT);";

    private static final String insertIntoCourse = "INSERT INTO Course(_id, course_name, sort_order, image_name) VALUES";
    private static final String insertIntoTopic = "INSERT INTO Topic(_id, course_id, topic_name, sort_order) VALUES";
    private static final String insertIntoQuestion = "INSERT INTO Question(_id, topic_id, question_text, sort_order) VALUES";
    private static final String insertIntoOption = "INSERT INTO Option(option_id, question_id, option_text, correct_flag) VALUES";

    private static final String DB_NAME = "vijayam.db";

    protected Context context;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context to use to open or create the database
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VersionUtils.getVersionCode(context));
        this.context = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
        initializeAppParamTable(db);
        //initializeData(db);
        //version3to4(db);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
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
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_OPTION);
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_QUESTION_IMAGE);
        db.execSQL(CREATE_TABLE_TOPIC);
        db.execSQL(CREATE_TABLE_TOPIC_QUESTION);
        db.execSQL(CREATE_TABLE_APP_PARAM);
    }

    private void initializeAppParamTable(SQLiteDatabase db) {
        db.execSQL("INSERT INTO app_param VALUES(1, 'contentProviderId','vijayam');");
        db.execSQL("INSERT INTO app_param VALUES(2, 'courseCount',0);");
        db.execSQL("INSERT INTO app_param VALUES(3, 'courseCompleted',0);");
        db.execSQL("INSERT INTO app_param VALUES(4, 'topicCount',0);");
        db.execSQL("INSERT INTO app_param VALUES(5, 'topicCompleted',0);");
        db.execSQL("INSERT INTO app_param VALUES(6, 'questionCount',0);");
        db.execSQL("INSERT INTO app_param VALUES(7, 'questionCompleted',0);");
    }

    private void initializeData(SQLiteDatabase db) {

    }

    private void version1to2(SQLiteDatabase db) {
        //no database updates
    }

    private void version2to3(SQLiteDatabase db) {
        createAllTables(db);
        initializeAppParamTable(db);
    }
}