package com.rnkj.rain.bean;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created by francis on 2015/10/10.
 */
public class Message implements IEntity<Message> {

    private static final long serialVersionUID = 1L;

    public static final String ERROR_CODE = "400";

    public static final String ERROR_STATUS = "FAILED";
    public static final String SUCCESS_STATUS = "COMPLETED";

    private String message;
    private String status;
    private String object;


    public Message(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public Message(JSONObject jsonObject) {
        this.parseJson(jsonObject);
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return this.status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Message parseJson(JSONObject jsonObject) {
        this.setStatus(jsonObject.optString("status"));
        this.setMessage(jsonObject.optString("message"));
        this.setObject(jsonObject.optString("object"));
        return this;
    }

    public static boolean isSimilar(JSONObject jsonObject) {
        return !TextUtils.isEmpty(jsonObject.optString("code")) && !TextUtils.isEmpty(jsonObject.optString("message"));
    }
}
