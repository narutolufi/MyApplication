package com.rnkj.rain.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by francis on 2014/4/17.
 */
public class SpUtil {
    private static final String PREF_NAME = "rain.preference";
    private static SpUtil instance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor spEditor;

    private SpUtil() {
    }

    public static SpUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SpUtil();
        }
        if (preferences == null && context != null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            spEditor = preferences.edit();
        }
        return instance;
    }

    public boolean setName(String name) {
        spEditor.putString("user.name", name);
        return spEditor.commit();
    }

    public String getName() {
        return preferences.getString("user.name", null);
    }

    public boolean setUserName(String username) {
        spEditor.putString("user.username", username);
        return spEditor.commit();
    }

    public String getUserName() {
        return preferences.getString("user.username", null);
    }

    public boolean setCookies(String cookie) {
        spEditor.putString("cookies", cookie);
        return spEditor.commit();
    }

    public String getCookies() {
        return preferences.getString("cookies", null);
    }
}
