package com.rnkj.rain.bean;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public interface IEntity<T> extends Serializable {
    T parseJson(JSONObject jsonObject) throws JSONException;
}
