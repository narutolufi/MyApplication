package com.rnkj.rain.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francis on 2015/10/13.
 */
public class User implements IEntity<User> {

    private String name;

    private long loginDate;

    private long supportDate;

    private long createdDate;

    private String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }

    public long getSupportDate() {
        return supportDate;
    }

    public void setSupportDate(long supportDate) {
        this.supportDate = supportDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String str){
        if(TextUtils.isEmpty(str)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(str);
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public User parseJson(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.optString("id",null));
        this.setName(jsonObject.optString("name",null));
        this.setCreatedDate(jsonObject.optLong("createdDate",0));
        this.setLoginDate(jsonObject.optLong("loginDate",0));
        this.setSupportDate(jsonObject.optLong("supportDate",0));
        return this;
    }
}
