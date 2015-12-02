package com.rnkj.rain.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francis on 2015/10/13.
 */
public class Machine implements IEntity<Machine> {

    private String id;
    private int progress;//进度：0~100的整数，表示0%~100%
    private String startSwitch;//运行标识：OFF停止，ON运行
    private String networkStatus;//网络状态：OFFLINE,离线，ONLINE在线
    private String workingStatus;//工作状态：NORMAL正常，TO_BE_INSPECTED待检，BROKEN故障
    private String name;

    public static final String START_SWITCH_OFF = "OFF";
    public static final String START_SWITCH_ON = "ON";

    public static final String NETWORK_STATUS_OFFLINE = "OFFLINE";
    public static final String NETWORK_STATUS_ONLINE = "ONLINE";

    public static final String WORKING_STATUS_NORMAL = "NORMAL";
    public static final String WORKING_STATUS_TO_BE_INSPECTED = "TO_BE_INSPECTED";
    public static final String WORKING_STATUS_BROKEN = "BROKEN";


    public Machine(JSONObject jsonObject){
        try {
            parseJson(jsonObject);
        }catch (Exception e){
            return;
        }

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getStartSwitch() {
        return startSwitch;
    }

    public void setStartSwitch(String startSwitch) {
        this.startSwitch = startSwitch;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Machine parseJson(JSONObject jsonObject) throws JSONException {
        this.setId(jsonObject.optString("id", null));
        this.setName(jsonObject.optString("name", null));
        this.setNetworkStatus(jsonObject.optString("networkStatus", NETWORK_STATUS_OFFLINE));
        this.setStartSwitch(jsonObject.optString("startSwitch", START_SWITCH_OFF));
        this.setWorkingStatus(jsonObject.optString("workingStatus", WORKING_STATUS_BROKEN));
        this.setProgress(jsonObject.optInt("progress",0));
        return this;
    }


    @Override
    public String toString() {
        return "id--->" + id + "--name--->" + name + "--networkStatus-->" + networkStatus + "--startSwitch-->" + startSwitch;
    }
}
