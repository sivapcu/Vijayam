package com.avisit.vijayam.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private String DB_LOCATION = null;
    private static final String DB_NAME = "vijayamDb.sqlite";
    private final Context myContext;
    protected SQLiteDatabase myDataBase;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * @throws java.io.IOException
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_LOCATION = context.getFilesDir().getParentFile().getPath()+"/databases/";
        if(!isDataBaseExist()) {
            createDataBase();
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean isDataBaseExist(){
        SQLiteDatabase checkDB = null;
        String DB_PATH = DB_LOCATION + DB_NAME;
        try{
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }
        if(checkDB != null){
            checkDB.close();
            return true;
        }
        return false;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase(){
        // By calling this method an empty database will be created into the
        // default system path of your application so we are gonna be able to
        // overwrite that database with our database.
        this.getReadableDatabase();
        try {
            copyDataBase();
            openReadableDataBase();
            close();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * @throws java.io.IOException
     */
    private void copyDataBase() throws IOException{
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            //Open your local db as the input stream
            myInput = myContext.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_LOCATION + DB_NAME;
            //Open the empty db as the output stream
            myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw e;
        }
        //Close the streams
        if(myOutput!=null){
            myOutput.flush();
            myOutput.close();
        }
        if(myInput!=null){
            myInput.close();
        }
    }

    public void openReadableDataBase() throws SQLException {
        String DB_PATH = DB_LOCATION + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void openWritableDataBase() throws SQLException{
        String DB_PATH = DB_LOCATION + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

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

    }

    @Override
    public synchronized void close() {
        super.close();
        if (myDataBase != null) {
            myDataBase.close();
            myDataBase = null;
        }
    }
}