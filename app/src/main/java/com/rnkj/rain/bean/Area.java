package com.rnkj.rain.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Area implements IEntity<Area> {

    private int start;

    private int end;

    private int speed;

    private String pump;


    public Area(JSONObject jsonObject){
        try {
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getPump() {
        return pump;
    }

    public void setPump(String pump) {
        this.pump = pump;
    }

    @Override
    public Area parseJson(JSONObject jsonObject) throws JSONException {
        this.setStart(jsonObject.optInt("start", 0));
        this.setEnd(jsonObject.optInt("end", 0));
        this.setSpeed(jsonObject.optInt("speed", 0));
        this.setPump(jsonObject.optString("pump", null));
        return this;
    }
}
