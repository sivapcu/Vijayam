package com.avisit.vijayam.activities;

import android.util.Log;
import android.widget.ProgressBar;

import com.avisit.vijayam.dao.DatabaseSetupManager;

/**
 * Created by User on 5/13/2015.
 */
public class DatabaseSetupListener implements DatabaseSetupManager.Listener {
    private static final String TAG = DatabaseSetupListener.class.getSimpleName();

    @Override
    public void progress(int completed, int total) {
        Log.i(TAG, "*********Progress*************** Completed : "+completed +"total :"+total);
    }

    @Override
    public void complete(boolean success, Exception result) {
        Log.i(TAG, "*********Completed*************** status : "+success);
        Log.i(TAG, "*********Complete Exception *************** Exception  : "+result);
    }
}
