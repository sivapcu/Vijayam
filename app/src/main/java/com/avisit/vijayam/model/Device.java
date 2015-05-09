package com.avisit.vijayam.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 5/9/2015.
 */
public class Device {
    private String registrationId;

    public Device(){

    }

    public Device(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String toJson() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("registrationId", this.getRegistrationId());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
