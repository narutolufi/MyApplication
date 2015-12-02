package com.rnkj.rain.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by francis on 2015/10/19.
 */
public class MachineDetail implements IEntity<MachineDetail> {

    private String name;//:"喷灌机1",//机器名
    private String model;//:"1.0",//机器型号
    private String workingStatus;//"workingStatus":"NORMAL",//工作状态
    private String networkStatus;//:"ONLINE",//网络状态
    private double temperature;//:23.1,//温度
    private double current;//:13.0,//电流，单位A
    private double voltage;//:380.0,//电压，单位V
    private double pressure;//":0.5,//水压，单位MPa
    private double position;//:180.0,//机器位置，单位°（度）
    private String direction;//:"CLOCKWISE",//运行方向，CLOCKWISE正（顺时针），COUNTER反（逆时针）
    private String plan_description;//        "plan":{//计划，不为空表示机器处于自动模式，为空表示处手动模式
    private double plan_area_start;
    private double plan_area_end;
    private int plan_times;
    private long plan_startDate;
    private long plan_endDate;
    private String plan_status;

    private int plan_progress;
    private int plan_timeCost;
    private int plan_completedTimess;
    private long plan_creaedDate;
    private String pump;
    private int speed;

    private String accessory1Switch;
    private String accessory2Switch;
    private String startSwitch;
    private String pauseSwitch;
    private long supportDate;
    private long createdDate;
    private String id;
    private boolean pumpAuto;
    private boolean speedAuto;
    private boolean endgunAuto;
    private boolean accessory1Auto;
    private boolean accessory2Auto;
    private String endgunSwitch;
    private boolean planOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlan_description() {
        return plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }

    public double getPlan_area_start() {
        return plan_area_start;
    }

    public void setPlan_area_start(double plan_area_start) {
        this.plan_area_start = plan_area_start;
    }

    public double getPlan_area_end() {
        return plan_area_end;
    }

    public void setPlan_area_end(double plan_area_end) {
        this.plan_area_end = plan_area_end;
    }

    public int getPlan_times() {
        return plan_times;
    }

    public void setPlan_times(int plan_times) {
        this.plan_times = plan_times;
    }

    public long getPlan_startDate() {
        return plan_startDate;
    }

    public void setPlan_startDate(long plan_startDate) {
        this.plan_startDate = plan_startDate;
    }

    public long getPlan_endDate() {
        return plan_endDate;
    }

    public void setPlan_endDate(long plan_endDate) {
        this.plan_endDate = plan_endDate;
    }

    public String getPlan_status() {
        return plan_status;
    }

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public int getPlan_progress() {
        return plan_progress;
    }

    public void setPlan_progress(int plan_progress) {
        this.plan_progress = plan_progress;
    }

    public int getPlan_timeCost() {
        return plan_timeCost;
    }

    public void setPlan_timeCost(int plan_timeCost) {
        this.plan_timeCost = plan_timeCost;
    }

    public int getPlan_completedTimess() {
        return plan_completedTimess;
    }

    public void setPlan_completedTimess(int plan_completedTimess) {
        this.plan_completedTimess = plan_completedTimess;
    }

    public long getPlan_creaedDate() {
        return plan_creaedDate;
    }

    public void setPlan_creaedDate(long plan_creaedDate) {
        this.plan_creaedDate = plan_creaedDate;
    }

    public String getPump() {
        return pump;
    }

    public void setPump(String pump) {
        this.pump = pump;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getAccessory1Switch() {
        return accessory1Switch;
    }

    public void setAccessory1Switch(String accessory1Switch) {
        this.accessory1Switch = accessory1Switch;
    }

    public String getAccessory2Switch() {
        return accessory2Switch;
    }

    public void setAccessory2Switch(String accessory2Switch) {
        this.accessory2Switch = accessory2Switch;
    }

    public String getStartSwitch() {
        return startSwitch;
    }

    public void setStartSwitch(String startSwitch) {
        this.startSwitch = startSwitch;
    }

    public String getPauseSwitch() {
        return pauseSwitch;
    }

    public void setPauseSwitch(String pauseSwitch) {
        this.pauseSwitch = pauseSwitch;
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

    public boolean isPumpAuto() {
        return pumpAuto;
    }

    public void setPumpAuto(boolean pumpAuto) {
        this.pumpAuto = pumpAuto;
    }

    public boolean isSpeedAuto() {
        return speedAuto;
    }

    public void setSpeedAuto(boolean speedAuto) {
        this.speedAuto = speedAuto;
    }

    public boolean isEndgunAuto() {
        return endgunAuto;
    }

    public void setEndgunAuto(boolean endgunAuto) {
        this.endgunAuto = endgunAuto;
    }

    public boolean isAccessory1Auto() {
        return accessory1Auto;
    }

    public void setAccessory1Auto(boolean accessory1Auto) {
        this.accessory1Auto = accessory1Auto;
    }

    public boolean isAccessory2Auto() {
        return accessory2Auto;
    }

    public void setAccessory2Auto(boolean accessory2Auto) {
        this.accessory2Auto = accessory2Auto;
    }

    public String getEndgunSwitch() {
        return endgunSwitch;
    }

    public void setEndgunSwitch(String endgunSwitch) {
        this.endgunSwitch = endgunSwitch;
    }

    public boolean isPlanOn() {
        return planOn;
    }

    public void setPlanOn(boolean planOn) {
        this.planOn = planOn;
    }


    public MachineDetail(JSONObject jsonObject){
        try{
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MachineDetail parseJson(JSONObject jsonObject) throws JSONException {
        setName(jsonObject.optString("name",null));
        setModel(jsonObject.optString("model",null));
        //      "workingStatus":"NORMAL",//工作状态
        setWorkingStatus(jsonObject.optString("workingStatus",null));
        setNetworkStatus(jsonObject.optString("networkStatus",null));
        setTemperature(jsonObject.optDouble("temperature",0.0)); //       "temperature":23.1,//温度
        setCurrent(jsonObject.optDouble("current",0.0)); //       "current":13.0,//电流，单位A
        setVoltage(jsonObject.optDouble("voltage",0.0));//"voltage":380.0,//电压，单位V
        //"pressure":0.5,//水压，单位MPa
        setPressure(jsonObject.optDouble("pressure",0.0));
        //"position":180.0,//机器位置，单位°（度）
        setPosition(jsonObject.optDouble("position",0.0));
        //"direction":"CLOCKWISE",//运行方向，CLOCKWISE正（顺时针），COUNTER反（逆时针）
        setDirection(jsonObject.optString("direction",null));

        JSONObject plan_jsonobject = jsonObject.getJSONObject("plan");
        setPlan_description(plan_jsonobject.optString("description",null));
        JSONObject area_jsonobject = plan_jsonobject.getJSONObject("area");
        setPlan_area_start(area_jsonobject.optDouble("start",0.0));
        setPlan_area_end(area_jsonobject.optDouble("end",0.0));
        setPlan_times(plan_jsonobject.optInt("times",0));
        setPlan_startDate(plan_jsonobject.optLong("startDate",0));
        setPlan_endDate(plan_jsonobject.optLong("endDate",0));
        setPlan_status(plan_jsonobject.optString("status",null));
        setPlan_progress(plan_jsonobject.optInt("progress",0));
        setPlan_timeCost(plan_jsonobject.optInt("timeCost",0));
        setPlan_completedTimess(plan_jsonobject.optInt("completedTimess",0));
        setPlan_creaedDate(plan_jsonobject.optLong("creaedDate",0));
        //"pump":"ON",//水泵（模式）标识，ON表示开，OFF表示关
        setPump(jsonObject.optString("pump",null));
        setSpeed(jsonObject.optInt("speed",0));
        //"accessory1Switch":"ON",//附件1标识
        setAccessory1Switch(jsonObject.optString("accessory1Switch",null));
        //"accessory2Switch":"OFF",//附件2标识
        setAccessory2Switch(jsonObject.optString("accessory2Switch",null));
        //"startSwitch":"ON",//运行标识
        setStartSwitch(jsonObject.optString("startSwitch",null));
        //"pauseSwitch":"OFF",//暂停标识
        setPauseSwitch(jsonObject.optString("pauseSwitch",null));
        setSupportDate(jsonObject.optLong("supportDate",0));
        setCreatedDate(jsonObject.optLong("createdDate",0));
        setId(jsonObject.optString("id",null));
        setPumpAuto(jsonObject.optBoolean("pumpAuto",false));
        setSpeedAuto(jsonObject.optBoolean("speedAuto",false));
        setEndgunAuto(jsonObject.optBoolean("endgunAuto",false));
        setAccessory1Auto(jsonObject.optBoolean("accessory1Auto",false));
        setAccessory2Auto(jsonObject.optBoolean("accessory2Auto",false));
        setPlanOn(jsonObject.optBoolean("planOn",false));
        setEndgunSwitch(jsonObject.optString("endgunSwitch",null));
        return this;
    }
}
