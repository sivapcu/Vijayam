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
import com.avisit.vijayam.dao.CourseDao;
import com.avisit.vijayam.dao.QuestionDao;
import com.avisit.vijayam.dao.TopicDao;
import com.avisit.vijayam.model.Course;
import com.avisit.vijayam.model.Question;
import com.avisit.vijayam.model.Topic;
import com.avisit.vijayam.service.HttpServiceHandler;
import com.avisit.vijayam.util.AndroidUtils;
import com.avisit.vijayam.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupActivity extends ActionBarActivity {
    private EditText emailEditText;
    private EditText passEditText;
    /*private EditText instituteEditText;*/
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
        /*instituteEditText = (EditText) findViewById(R.id.instituteCode);*/
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

        /*final String instituteCode = instituteEditText.getText().toString();
        if (!isValid(instituteCode, 6)) {
            instituteEditText.setError("Invalid Institute Code");
            validationFlag = false;
        }*/

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
            } else{
                registerWithServer();
            }
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

            /**
             * Sends the registration ID to the server over HTTP, so it can use GCM/HTTP or CCS to send
             * messages to your app.
             */
            private void sendRegistrationIdToServer() throws Exception {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("registrationId", registrationId));
                String contentProviderId = new AppParamDao(SetupActivity.this).getAppParamValue("contentProviderId");
                /*params.add(new BasicNameValuePair("contentProviderId", instituteEditText.getText().toString()));*/
                params.add(new BasicNameValuePair("contentProviderId", contentProviderId));
                params.add(new BasicNameValuePair("email", emailEditText.getText().toString()));
                params.add(new BasicNameValuePair("password", passEditText.getText().toString()));

                HttpServiceHandler httpServiceHandler = new HttpServiceHandler();
                String response1 = httpServiceHandler.makeServiceCall(Constants.USER_REGISTER_URL, HttpServiceHandler.POST, params);
                if ("SUCCESS".equals(response1)) {
                    new AppParamDao(context).update(Constants.REGISTRATION_FLAG, "REGISTERED");
                    errorMsg = "Registration Successful";
                    publishProgress("Successfully registered with the server");
                } else {
                    new AppParamDao(context).update(Constants.REGISTRATION_FLAG, "FAILED");
                    errorMsg = response1;
                }

                if(errorMsg.equals("Registration Successful")){
                    publishProgress("Fetching data from server");
                    String response2 = httpServiceHandler.makeServiceCall(Constants.COURSES_BY_CONTENT_PROVIDER, HttpServiceHandler.POST, params);
                    List<Course> courseList = new ObjectMapper().readValue(response2, new TypeReference<ArrayList<Course>>(){});
                    publishProgress("Persisting data to the local database");
                    for(Course course : courseList){
                        if(new CourseDao(context).insertIfUpdateFails(course)){
//                            List<NameValuePair> topicParam = new ArrayList<NameValuePair>();
                            for(Topic topic : course.getTopicList()){
                                if(new TopicDao(context).insertIfUpdateFails(topic)){
                                    /*topicParam.clear();
                                    topicParam.add(new BasicNameValuePair("topicId", Integer.toString(topic.getId())));
                                    //String response3 = httpServiceHandler.makeServiceCall(Constants.QUES_BY_TOPIC, HttpServiceHandler.POST, topicParam);
                                    List<Question> questionList = new ObjectMapper().readValue(response3, new TypeReference<ArrayList<Question>>() {});
                                    for(Question question : questionList) {
                                        new QuestionDao(context).insertIfUpdateFails(question, topic.getId());
                                    }*/
                                    for(Question question : topic.getQuestions()){
                                        new QuestionDao(context).insertIfUpdateFails(question, topic.getId());
                                    }
                                }
                            }
                        }
                    }
                }
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
        /*new AppParamDao(context).update(Constants.CONTENT_PROVIDER_ID, instituteEditText.getText().toString());*/
        new AppParamDao(context).update(Constants.USER_EMAIL, emailEditText.getText().toString());
        new AppParamDao(context).update(Constants.USER_PASSWORD, passEditText.getText().toString());
        new AppParamDao(context).update(Constants.REGISTERED_VERSION, Integer.toString(AndroidUtils.getAppVersionCode(context)));
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