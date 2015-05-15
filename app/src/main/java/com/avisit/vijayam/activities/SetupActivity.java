package com.avisit.vijayam.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avisit.vijayam.R;
import com.avisit.vijayam.dao.AppParamDao;
import com.avisit.vijayam.model.User;
import com.avisit.vijayam.util.AndroidUtils;
import com.avisit.vijayam.util.Constants;
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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupActivity extends ActionBarActivity {
    private EditText emailEditText;
    private EditText passEditText;
    private EditText instituteEditText;
    private Context context;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = SetupActivity.class.getSimpleName();
    String registrationId;
    String errorMsg;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "447980964198";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        customizeActionBar();
        this.context = getApplicationContext();
        instituteEditText = (EditText) findViewById(R.id.instituteCode);
        emailEditText = (EditText) findViewById(R.id.email);
        passEditText = (EditText) findViewById(R.id.password);

        if(!AndroidUtils.isNetworkAvailable(context)){
            showNoConnectionDialog(this);
        }
    }

    private void customizeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.mipmap.vijayam_ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void showNoConnectionDialog(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_connection);
        builder.setTitle(R.string.no_connection_title);
        builder.setIcon(R.mipmap.vijayam_ic_launcher);
        builder.setPositiveButton(R.string.settings_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void registerLater(View view) {
        new AppParamDao(getApplicationContext()).update(Constants.REGISTRATION_FLAG, "LATER");
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        boolean validationFlag = true;

        final String instituteCode = instituteEditText.getText().toString();
        if (!isValid(instituteCode, 6)) {
            instituteEditText.setError("Invalid Institute Code");
            validationFlag = false;
        }

        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            emailEditText.setError("Invalid Email");
            validationFlag = false;
        }

        final String pass = passEditText.getText().toString();
        if (!isValid(pass, 6)) {
            passEditText.setError("Invalid Password. Must be minimum of 6 char");
            validationFlag = false;
        }

        if(validationFlag){
            if(!AndroidUtils.isNetworkAvailable(context)){
                showNoConnectionDialog(this);
            }
            registerWithServer();
        }
    }

    private void registerWithServer() {
        new AsyncTask<Void, String, Exception>() {
            ProgressDialog progressDialog = new ProgressDialog(SetupActivity.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Registration for content updates");
                progressDialog.show();
            }

            @Override
            protected Exception doInBackground(Void... params) {
                GoogleCloudMessaging gcm = null;

                try {
                    publishProgress("Verifying presence of Google Play Services");
                    if(checkPlayServices()){
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    if(gcm!=null){
                        publishProgress("Generating Device Id for content update notifications");
                        registrationId = gcm.register(SENDER_ID);
                        Log.i(TAG, "User registered, registration ID=" + registrationId);
                        publishProgress("Registering Device for content update notifications");
                        sendRegistrationIdToServer();
                    }
                } catch (IOException e) {
                    return e;
                } catch (Exception e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception exception) {
                if ("Registration Successful".equals(errorMsg)) {
                    storeRegistrationId();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
                if(exception == null) {
                    Toast.makeText(SetupActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SetupActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected void onProgressUpdate(String... values) {
                progressDialog.setMessage(values[0]);
            }
        }.execute(null, null, null);
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

    private void storeRegistrationId() {
        new AppParamDao(context).update(Constants.DEVICE_REG_ID, registrationId);
        new AppParamDao(context).update(Constants.CONTENT_PROVIDER_ID, instituteEditText.getText().toString());
        new AppParamDao(context).update(Constants.USER_EMAIL, emailEditText.getText().toString());
        new AppParamDao(context).update(Constants.USER_PASSWORD, passEditText.getText().toString());
        new AppParamDao(context).update(Constants.REGISTERED_VERSION, Integer.toString(AndroidUtils.getAppVersionCode(context)));
    }

    /**
     * Sends the registration ID to the server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app.
     */
    private void sendRegistrationIdToServer() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost registerUserRequest = new HttpPost("http://192.168.43.243:8080/vijayam/rest/user/register");
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 5000);
        HttpConnectionParams.setSoTimeout(httpclient.getParams(), 5000);

        User user = new User(registrationId);
        user.setContentProviderId(instituteEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setPassword(passEditText.getText().toString());

        try {
            StringEntity entity = new StringEntity(user.toJson());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            registerUserRequest.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(registerUserRequest);
            if (httpResponse != null) {
                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                if (response.toString().equals("SUCCESS")) {
                    new AppParamDao(context).update(Constants.REGISTRATION_FLAG, "REGISTERED");
                    errorMsg = "Registration Successful";
                } else {
                    new AppParamDao(context).update(Constants.REGISTRATION_FLAG, "FAILED");
                    errorMsg = response.toString();
                }
                in.close();
            }

            if(errorMsg.equals("Registration Successful")){
                HttpPost syncDataRequest = new HttpPost("http://192.168.43.243:8080/vijayam/rest/courses/contentProvider/"+user.getContentProviderId());
                //todo write code sync data
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Email validation
     * @param email input string
     * @return true if input is a valid email pattern
     */
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     *
     * @param pass input string
     * @param minimumCharacters minimum length required for the string
     * @return true if valid, else return false
     */
    private boolean isValid(String pass, int minimumCharacters) {
        return pass != null && pass.length() >= minimumCharacters;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}