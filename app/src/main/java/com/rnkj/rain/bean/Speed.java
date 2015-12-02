package com.rnkj.rain.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by francis on 2015/12/2.
 */
public class Speed implements IEntity<Speed>{

    private String autoSpeed;

    private String autoPump;

    private List<Area> areaList;

    public Speed(JSONObject jsonObject){
        try {
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAutoSpeed() {
        return autoSpeed;
    }

    public void setAutoSpeed(String autoSpeed) {
        this.autoSpeed = autoSpeed;
    }

    public String getAutoPump() {
        return autoPump;
    }

    public void setAutoPump(String autoPump) {
        this.autoPump = autoPump;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Override
    public Speed parseJson(JSONObject jsonObject) throws JSONException {
        this.setAutoSpeed(jsonObject.optString("autoSpeed",null));
        this.setAutoPump(jsonObject.optString("autoPump",null));
        return this;
    }
}
