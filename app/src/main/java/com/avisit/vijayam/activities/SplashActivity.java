package com.avisit.vijayam.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.avisit.vijayam.R;
import com.avisit.vijayam.dao.AppParamDao;
import com.avisit.vijayam.model.Device;
import com.avisit.vijayam.util.Constants;
import com.avisit.vijayam.util.VersionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplashActivity extends ActionBarActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int SPLASH_SCREEN_TIME = 500;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "447980964198";
    GoogleCloudMessaging gcm;
    Context context;
    String registrationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = getApplicationContext();

        registrationId = getRegistrationId(context);
        if (registrationId.isEmpty()) {
            if(checkPlayServices()){
                gcm = GoogleCloudMessaging.getInstance(this);
                registerInBackground();
            }else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN_TIME);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = new AppParamDao(this).getAppParamValue(Constants.DEVICE_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = new AppParamDao(this).getAppParamValue(Constants.REGISTERED_VERSION, 0);
        int currentVersion = VersionUtils.getAppVersionCode(context);
        if (registeredVersion != currentVersion) {
            // TODO verify the below commented logic or this can be addressed with the gcm xmpp server implementation
            //deleteRegIdFromServer(registrationId);
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Sends the registration ID to the server over HTTP, so it can delete this unused registration Id
     * @param oldRegistrationId Device_Registration_Id to be deleted
     */
    private void deleteRegIdFromServer(String oldRegistrationId) {
        new AsyncTask<String, Void, Void>() {
            ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);

            @Override
            protected Void doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.43.243:8080/vijayam/rest/user/deRegisterDevice");
                Device device = new Device(params[0]);
                try {
                    StringEntity entity = new StringEntity( device.toJson());
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    httppost.setEntity(entity);
                    httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
                return null;
            }
        }.execute(oldRegistrationId, null, null);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID in the database
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Verifying device registration for content updates");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    registrationId = gcm.register(SENDER_ID);
                    Log.i(TAG, "Device registered, registration ID=" + registrationId);
                    storeRegistrationId(context, registrationId);
                    sendRegistrationIdToServer();
                } catch (Exception e) {
                    Log.e(TAG, "Error :" + e.getMessage());
                    // TODO Retry device registration on failure
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String registrationId) {
        new AppParamDao(context).update(Constants.DEVICE_REG_ID, registrationId);
        new AppParamDao(context).update(Constants.REGISTERED_VERSION, Integer.toString(VersionUtils.getAppVersionCode(context)));
    }

    /**
     * Sends the registration ID to the server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app.
     */
    private void sendRegistrationIdToServer() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://192.168.43.243:8080/vijayam/rest/user/registerDevice");
        Device device = new Device(registrationId);
        try {
            StringEntity entity = new StringEntity(device.toJson());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httppost);
            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                if (response.toString().equals("SUCCESS")) {
                    new AppParamDao(context).update("registeredWithServer", "1");
                } else {
                    new AppParamDao(context).update("registeredWithServer", "0");
                    throw new Exception("Failed to Register device");
                }
                in.close();
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
