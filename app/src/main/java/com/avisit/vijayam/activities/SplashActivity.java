package com.avisit.vijayam.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

import com.avisit.vijayam.R;
import com.avisit.vijayam.dao.AppParamDao;
import com.avisit.vijayam.dao.DatabaseSetupManager;
import com.avisit.vijayam.util.Constants;
import com.avisit.vijayam.util.VijayamApplication;

public class SplashActivity extends ActionBarActivity implements DatabaseSetupManager.Listener {
    Context context;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = getApplicationContext();

        ((VijayamApplication)getApplication()).getDatabaseSetupManager().setListener(this);
        DatabaseSetupManager.State state = ((VijayamApplication)getApplication()).getDatabaseSetupManager().getState();
        if(state == DatabaseSetupManager.State.READY){
            startNextActivity();
        }
    }

    private void startNextActivity() {
        String userRegistrationStatus = new AppParamDao(context).getAppParamValue(Constants.REGISTRATION_FLAG, "");
        if(userRegistrationStatus.equals("REGISTERED") || userRegistrationStatus.equals("LATER")){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void progress(int completed, int total) {
        // this wont work because of a bug in horizontal progress bar
        // for time being change the progress bar to indeterminate = true
        progressBar.setMax(total);
        progressBar.setProgress(completed);
    }

    @Override
    public void complete(boolean success, Exception result) {
        startNextActivity();
    }

}
