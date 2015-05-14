package com.avisit.vijayam.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 5/9/2015.
 */
public class User {
    private String registrationId;
    private String contentProviderId;
    private String email;
    private String password;

    public User(){

    }

    public User(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getContentProviderId() {
        return contentProviderId;
    }

    public void setContentProviderId(String contentProviderId) {
        this.contentProviderId = contentProviderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toJson() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("registrationId", this.getRegistrationId());
            jsonObject.put("contentProviderId", this.getContentProviderId());
            jsonObject.put("email", this.getEmail());
            jsonObject.put("password", this.getPassword());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
