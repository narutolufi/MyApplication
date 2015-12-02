package com.rnkj.rain.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francis on 2015/11/1.
 */
public class Plan implements IEntity<Plan>{

    public static final String PLAN_STATUS_RUNNING = "RUNNING";
    public static final String PLAN_STATUS_WAITING = "WAITING";
    public static final String PLAN_STATUSCONFLICT = "CONFLICT";


    public static final String PLAN_HISTORY_STATUS_COMPLETED = "COMPLETED";
    public static final String PLAN_HISTORY_STATUS_CANCELED = "FAILED";
    public static final String PLAN_HISTORY_STATUS_BROKEN = "CANCELLED";

    private String description;//描述

    private int area_start;//范围开始

    private int area_end;//范围结束

    private int times;//计划次数

    private long startDate;//开始时间

    private long endDate;//结束时间

    private String status;

    private int progress;

    private int timeCost;

    private int completedTimes;

    private long creaedDate;

    private int id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArea_start() {
        return area_start;
    }

    public void setArea_start(int area_start) {
        this.area_start = area_start;
    }

    public int getArea_end() {
        return area_end;
    }

    public void setArea_end(int area_end) {
        this.area_end = area_end;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(int timeCost) {
        this.timeCost = timeCost;
    }

    public int getCompletedTimes() {
        return completedTimes;
    }

    public void setCompletedTimes(int completedTimes) {
        this.completedTimes = completedTimes;
    }

    public long getCreaedDate() {
        return creaedDate;
    }

    public void setCreaedDate(long creaedDate) {
        this.creaedDate = creaedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plan(JSONObject jsonObject){
        try {
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Plan(){

    }


    @Override
    public Plan parseJson(JSONObject jsonObject) throws JSONException {
        setProgress(jsonObject.optInt("progress",0));
        JSONObject jsonObject1 = jsonObject.getJSONObject("area");
        setArea_start(jsonObject1.optInt("start",0));
        setArea_end(jsonObject1.optInt("end",0));
        setCompletedTimes(jsonObject.optInt("completedTimes",0));
        setDescription(jsonObject.optString("description",null));
        setTimes(jsonObject.optInt("times",0));
        setStartDate(jsonObject.optLong("startDate",0));
        setEndDate(jsonObject.optLong("endDate",0));
        setStatus(jsonObject.optString("status",null));
        setTimeCost(jsonObject.optInt("timeCost",0));
        setCreaedDate(jsonObject.optLong("creaedDate",0));
        setId(jsonObject.optInt("id",0));
        return this;
    }
}
