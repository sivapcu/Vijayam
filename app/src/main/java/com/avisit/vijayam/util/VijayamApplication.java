package com.avisit.vijayam.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.avisit.vijayam.dao.DatabaseSetupManager;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.model.Topic;


/**
 * Created by User on 4/15/2015.
 */
public class VijayamApplication extends Application {
    private AppState appState;
    private DatabaseSetupManager databaseSetupManager;

    public AppState getAppState() {
        return appState;
    }

    public void setAppState(AppState appState) {
        this.appState = appState;
    }

    public DatabaseSetupManager getDatabaseSetupManager() {
        return databaseSetupManager;
    }

    public void setDatabaseSetupManager(DatabaseSetupManager databaseSetupManager) {
        this.databaseSetupManager = databaseSetupManager;
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.databaseSetupManager = new DatabaseSetupManager(getApplicationContext(), Constants.DB_ASSET_FILE_NAME, 14000, Constants.DB_NAME);
        this.appState = new AppState();
    }

}
