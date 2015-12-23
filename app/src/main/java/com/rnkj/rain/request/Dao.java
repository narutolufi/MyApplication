package com.rnkj.rain.request;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rnkj.rain.bean.Area;
import com.rnkj.rain.bean.Machine;
import com.rnkj.rain.bean.MachineDetail;
import com.rnkj.rain.bean.Message;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.bean.Speed;
import com.rnkj.rain.bean.User;
import com.rnkj.rain.fragment.PlanFragment;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.SpUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okio.BufferedSink;


/**
 * Created by francis on 2015/9/6.
 */
public class Dao {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://123.57.95.123:8080/webserver/spring/%s";

    private static final String LOGIN = "login";

    private static final String MODIFY_NAME = "security/username";

    private static final String MODIFY_PWD = "security/password";

    private static final String LOGOUT = "security/logout";

    private static final String MACHINE_LIST = "security/machinelist";

    private static final String MACHINE_DETAIL = "security/machine/get";

    private static final String MACHINE_MODIFY_NAME = "security/machine/machinename";

    private static final String MACHINE_MODIFY_DIRECTION = "security/machine/direction";

    private static final String PUMP_SETTINGS_DIRECTION = "security/machine/pump";

    private static final String MACHINE_SETTINGS_SPEED = "security/machine/speed";

    private static final String MACHINE_SETTINGS_ENDGUN = "security/machine/endgun";

    private static final String MACHINE_SETTINGS_ACCESSORY1 = "security/machine/accessory1";

    private static final String MACHINE_SETTINGS_ACCESSORY2 = "security/machine/accessory2";

    private static final String MACHINE_START = "security/machine/start";

    private static final String MACHINE_PAUSE = "security/machine/pause";

    private static final String MACHINE_PLAN_LIST = "security/machine/plan/list";

    private static final String MACHINE_PLAN_HISTORY_LIST = "security/machine/plan/history";

    private static final String MACHINE_INSERT_PLAN = "security/machine/plan/add";

    private static final String MACHINE_DETAIL_PLAN = "security/machine/plan/get";

    private static final String MACHINE_SPEED_LIST = "security/machine/speedstrategy/get";

    private static final String MACHINE_SPEED_SET = "security/machine/speedstrategy/set";

    private static Dao mInstance;

    private Context mContext;

    private String cookies;


    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    private Dao(Context context) {
        this.mContext = context;
    }

    public static Dao instance(Context context) {
        if (mInstance == null) {
            mInstance = new Dao(context);
        }
        return mInstance;
    }

    public void loginRequest(final ResponseAction responseAction,String username,String password){
        String base_url = String.format(BASE_URL,LOGIN);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?userid=");
        stringBuffer.append(username);
        stringBuffer.append("&password=");
        stringBuffer.append(password);
        Request  request = new Request.Builder().url(stringBuffer.toString()).build();
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new LoginCallBack(responseAction));
    }

    //处理登录
    private class LoginCallBack implements Callback {
        private ResponseAction responseAction;
        private LoginCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                Message message = new Message(new JSONObject(response.body().string()));
                if(message.getStatus().equalsIgnoreCase(Message.SUCCESS_STATUS)){
                    Log.i("francis","object--->" + message.getObject());
                    String[] arrStr = response.header("Set-Cookie").split(";");
                    SpUtil.getInstance(mContext).setCookies(arrStr[0]);
                    User user = new User(message.getObject());
                    responseAction.onSuccess(user);
                }else{
                    responseAction.onFailure(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void logoutRequest(final ResponseAction responseAction){
        String base_url = String.format(BASE_URL,LOGOUT);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        HttpUtils.asyncEnqueue(request,new LogoutCallBack(responseAction));
    }

    //处理登出
    private class LogoutCallBack implements Callback {
        private ResponseAction responseAction;
        private LogoutCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                Message message = new Message(new JSONObject(response.body().string()));
                if(message.getStatus().equalsIgnoreCase(Message.SUCCESS_STATUS)){
                    SpUtil.getInstance(mContext).setCookies(null);
                    responseAction.onSuccess(message);
                }else{
                    responseAction.onFailure(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void modifyUserNameRequest(final ResponseAction responseAction,String username){
        String base_url = String.format(BASE_URL,MODIFY_NAME);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?username=");
        stringBuffer.append(username);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        HttpUtils.asyncEnqueue(request,new ModifyUserNameCallBack(responseAction));
    }

    //处理用户名
    private class ModifyUserNameCallBack implements Callback {
        private ResponseAction responseAction;
        private ModifyUserNameCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                Message message = new Message(new JSONObject(response.body().string()));
                if(message.getStatus().equalsIgnoreCase(Message.SUCCESS_STATUS)){
                    responseAction.onSuccess(message);
                }else{
                    responseAction.onFailure(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void modifyPWDRequest(final ResponseAction responseAction,String password){
        String base_url = String.format(BASE_URL,MODIFY_PWD);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?password=");
        stringBuffer.append(password);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        HttpUtils.asyncEnqueue(request,new ModifyPWDCallBack(responseAction));
    }

    //处理用户名
    private class ModifyPWDCallBack implements Callback {
        private ResponseAction responseAction;
        private ModifyPWDCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                Message message = new Message(new JSONObject(response.body().string()));
                if(message.getStatus().equalsIgnoreCase(Message.SUCCESS_STATUS)){
                    responseAction.onSuccess(message);
                }else{
                    responseAction.onFailure(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void machinenListRequest(final ResponseAction responseAction,int start,int length){
        String base_url = String.format(BASE_URL,MACHINE_LIST);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?start=");
        stringBuffer.append(start);
        stringBuffer.append("&length=");
        stringBuffer.append(length);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new MachinenListCallBack(responseAction));
    }

    //处理机器列表
    private class MachinenListCallBack implements Callback {
        private ResponseAction responseAction;
        private MachinenListCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                List<Machine> machineList = null;
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null && jsonArray.length() > 0){
                    machineList = new LinkedList<Machine>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        Machine machine = new Machine(jsonArray.getJSONObject(i));
                        if(machine != null){
                            machineList.add(machine);
                        }
                    }
                    responseAction.onSuccess((LinkedList)machineList);
                }else{
                    responseAction.onFailure(new Message("length is zero",Message.ERROR_STATUS));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void machinenDetailRequest(final ResponseAction responseAction,String machine_id){
        String base_url = String.format(BASE_URL,MACHINE_DETAIL);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new MachinenDetailCallBack(responseAction));
    }

    //处理机器详情
    private class MachinenDetailCallBack implements Callback {
        private ResponseAction responseAction;
        private MachinenDetailCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject);
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void endgunRequest(final ResponseAction responseAction,String machine_id,String endgun_status){
        String base_url = String.format(BASE_URL,MACHINE_SETTINGS_ENDGUN);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&endgun=");
        stringBuffer.append(endgun_status);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new EndgunCallBack(responseAction));
    }

    //处理机器详情
    private class EndgunCallBack implements Callback {
        private ResponseAction responseAction;
        private EndgunCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void accessory1Request(final ResponseAction responseAction,String machine_id,String accessory1){
        String base_url = String.format(BASE_URL,MACHINE_SETTINGS_ACCESSORY1);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&accessory1=");
        stringBuffer.append(accessory1);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new Accessory1CallBack(responseAction));
    }

    //附件1开关
    private class Accessory1CallBack implements Callback {
        private ResponseAction responseAction;
        private Accessory1CallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                Log.i("francis","result---->" + result);
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }



    public void accessory2Request(final ResponseAction responseAction,String machine_id,String accessory2){
        String base_url = String.format(BASE_URL,MACHINE_SETTINGS_ACCESSORY2);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&accessory2=");
        stringBuffer.append(accessory2);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new Accessory2CallBack(responseAction));
    }

    //附件2开关
    private class Accessory2CallBack implements Callback {
        private ResponseAction responseAction;
        private Accessory2CallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }



    public void machineStartRequest(final ResponseAction responseAction,String machine_id,String start){
        String base_url = String.format(BASE_URL,MACHINE_START);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&start=");
        stringBuffer.append(start);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new MachineStartCallBack(responseAction));
    }

    //开始运行开关
    private class MachineStartCallBack implements Callback {
        private ResponseAction responseAction;
        private MachineStartCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }



    public void machinePauseRequest(final ResponseAction responseAction,String machine_id,String pause){
        String base_url = String.format(BASE_URL,MACHINE_START);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&pause=");
        stringBuffer.append(pause);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new MachinePauseCallBack(responseAction));
    }

    //开始运行开关
    private class MachinePauseCallBack implements Callback {
        private ResponseAction responseAction;

        private MachinePauseCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(), Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!response.isSuccessful() || response.code() != 200) {
                Message message = new Message("Request Failure", Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    responseAction.onFailure(new Message("null", Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(), Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void machineSpeedRequest(final ResponseAction responseAction,String machine_id,int int_speed){
        String base_url = String.format(BASE_URL,MACHINE_SETTINGS_SPEED);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&speed=");
        stringBuffer.append(int_speed);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new MachineSpeedCallBack(responseAction));
    }

    //设置速度
    private class MachineSpeedCallBack implements Callback {
        private ResponseAction responseAction;

        private MachineSpeedCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(), Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!response.isSuccessful() || response.code() != 200) {
                Message message = new Message("Request Failure", Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    responseAction.onFailure(new Message("null", Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                MachineDetail machineDetail = new MachineDetail(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(machineDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(), Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void planListRequest(final ResponseAction responseAction,int start,int length,String machineid,int order_type){
        String base_url = String.format(BASE_URL,MACHINE_PLAN_LIST);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machineid);
        stringBuffer.append("&start=");
        stringBuffer.append(start);
        stringBuffer.append("&length=");
        stringBuffer.append(length);
        switch (order_type){
            case PlanFragment.PLAN_ORDER_BY_DATE:
                stringBuffer.append("&order=");
                stringBuffer.append("created");
                break;
            case PlanFragment.PLAN_ORDER_BY_COST:
                stringBuffer.append("&order=");
                stringBuffer.append("started");
                break;
            default:
                stringBuffer.append("&order=");
                stringBuffer.append("created");
                break;
        }
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new PlanListCallBack(responseAction));
    }

    //处理机器列表
    private class PlanListCallBack implements Callback {
        private ResponseAction responseAction;
        private PlanListCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                List<Plan> machineList = null;
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null && jsonArray.length() > 0){
                    machineList = new LinkedList<Plan>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        Plan plan = new Plan(jsonArray.getJSONObject(i));
                        if(plan != null){
                            machineList.add(plan);
                        }
                    }
                    responseAction.onSuccess((LinkedList)machineList);
                }else{
                    responseAction.onFailure(new Message("length is zero",Message.ERROR_STATUS));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void planHistoryListRequest(final ResponseAction responseAction,int start,int length,String machineid,int order_type){
        String base_url = String.format(BASE_URL,MACHINE_PLAN_HISTORY_LIST);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machineid);
        stringBuffer.append("&start=");
        stringBuffer.append(start);
        stringBuffer.append("&length=");
        stringBuffer.append(length);
        switch (order_type){
            case PlanFragment.PLAN_ORDER_BY_DATE:
                stringBuffer.append("&order=");
                stringBuffer.append("created");
                break;
            case PlanFragment.PLAN_ORDER_BY_COST:
                stringBuffer.append("&order=");
                stringBuffer.append("started");
                break;
            default:
                stringBuffer.append("&order=");
                stringBuffer.append("created");
                break;
        }
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new PlanHistoryListCallBack(responseAction));
    }

    //处理机器列表
    private class PlanHistoryListCallBack implements Callback {
        private ResponseAction responseAction;
        private PlanHistoryListCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                List<Plan> machineList = null;
                String result = response.body().string();
                if(TextUtils.isEmpty(result)){
                    responseAction.onFailure(new Message("null",Message.ERROR_STATUS));
                    return;
                }
                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray != null && jsonArray.length() > 0){
                    machineList = new LinkedList<Plan>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        Plan plan = new Plan(jsonArray.getJSONObject(i));
                        if(plan != null){
                            machineList.add(plan);
                        }
                    }
                    responseAction.onSuccess((LinkedList)machineList);
                }else{
                    responseAction.onFailure(new Message("length is zero",Message.ERROR_STATUS));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    public void insertPlanRequest(final ResponseAction responseAction,String machineid,Plan plan){

        StringBuffer requestBodyJson = new StringBuffer();
        requestBodyJson.append("{");
        requestBodyJson.append("\"description\": ");
        requestBodyJson.append("\""+plan.getDescription()+"\"");
        requestBodyJson.append(",");
        requestBodyJson.append("\"area\": ");
        requestBodyJson.append("{\"start\":");
        requestBodyJson.append(plan.getArea_start() + ",");
        requestBodyJson.append("\"end\":");
        requestBodyJson.append(plan.getArea_end() + "},");
        requestBodyJson.append("\"times\": ");
        requestBodyJson.append(plan.getTimes());
        requestBodyJson.append(",");
        requestBodyJson.append("\"startDate\": ");
        requestBodyJson.append(plan.getStartDate());
        requestBodyJson.append("}");
        Log.i("francis","str---->" + requestBodyJson.toString());
        RequestBody jsonBody = RequestBody.create(JSON, requestBodyJson.toString());
        String base_url = String.format(BASE_URL,MACHINE_INSERT_PLAN);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machineid);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).post(jsonBody).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).post(jsonBody).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new InsertPlanCallBack(responseAction));
    }

    //处理机器列表
    private class InsertPlanCallBack implements Callback {
        private ResponseAction responseAction;
        private InsertPlanCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }
        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(),Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if(!response.isSuccessful() || response.code() != 200){
                Message message = new Message("Request Failure",Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                Message message = new Message(new JSONObject(response.body().string()));
                if(message.getStatus().equalsIgnoreCase(Message.SUCCESS_STATUS)){
                    responseAction.onSuccess(message);
                }else{
                    responseAction.onFailure(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(),Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void planDetailRequest(final ResponseAction responseAction,String machine_id,String plan_id){
        String base_url = String.format(BASE_URL,MACHINE_DETAIL_PLAN);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        stringBuffer.append("&planid=");
        stringBuffer.append(plan_id);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new PlanDetailCallBack(responseAction));
    }

    //计划详情
    private class PlanDetailCallBack implements Callback {
        private ResponseAction responseAction;

        private PlanDetailCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(), Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!response.isSuccessful() || response.code() != 200) {
                Message message = new Message("Request Failure", Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    responseAction.onFailure(new Message("null", Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                Plan planDetail = new Plan(jsonObject.getJSONObject("object"));
                responseAction.onSuccess(planDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(), Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }

    public void getListSpeedsRequest(final ResponseAction responseAction,String machine_id){
        String base_url = String.format(BASE_URL,MACHINE_SPEED_LIST);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new GetListSpeedsCallBack(responseAction));
    }

    //计划详情
    private class GetListSpeedsCallBack implements Callback {
        private ResponseAction responseAction;

        private GetListSpeedsCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(), Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!response.isSuccessful() || response.code() != 200) {
                Message message = new Message("Request Failure", Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    responseAction.onFailure(new Message("null", Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                Speed speed = new Speed(jsonObject);
                JSONArray areaListJsonArray = jsonObject.optJSONArray("areas");
                if(areaListJsonArray == null){
                    Message message = new Message("null", Message.ERROR_CODE);
                    responseAction.onFailure(message);
                    return;
                }
                List<Area> areas = new LinkedList<Area>();
                for (int i = 0; i < areaListJsonArray.length(); i++){
                    Area area = new Area(areaListJsonArray.getJSONObject(i));
                    areas.add(area);
                }
                speed.setAreaList(areas);
                responseAction.onSuccess(speed);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(), Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


    //添加Speed
    public void getSetSpeedsRequest(final ResponseAction responseAction,String machine_id,Speed speed){
        StringBuffer requestBodyJson = new StringBuffer();
        requestBodyJson.append("{");
        requestBodyJson.append("\"autoSpeed\": ");
        requestBodyJson.append("\""+speed.getAutoSpeed()+"\"");
        requestBodyJson.append(",");
        requestBodyJson.append("\"autoPump\": ");
        requestBodyJson.append("\""+speed.getAutoPump()+"\"");
        requestBodyJson.append(",");
        requestBodyJson.append("\"areas\":[");
        for (int i = 0; i < speed.getAreaList().size(); i++){
            if(i + 1 == speed.getAreaList().size()){
                requestBodyJson.append("{\"start\":");
                requestBodyJson.append(speed.getAreaList().get(i).getStart() + ",");
                requestBodyJson.append("\"end\":");
                requestBodyJson.append(speed.getAreaList().get(i).getEnd() + ",");
                requestBodyJson.append("\"speed\":");
                requestBodyJson.append(speed.getAreaList().get(i).getSpeed() + ",");
                requestBodyJson.append("\"pump\":");
                requestBodyJson.append("\""+speed.getAreaList().get(i).getPump()+"\"}]");
            }else{
                requestBodyJson.append("{\"start\":");
                requestBodyJson.append(speed.getAreaList().get(i).getStart() + ",");
                requestBodyJson.append("\"end\":");
                requestBodyJson.append(speed.getAreaList().get(i).getEnd() + ",");
                requestBodyJson.append("\"speed\":");
                requestBodyJson.append(speed.getAreaList().get(i).getSpeed() + ",");
                requestBodyJson.append("\"pump\":");
                requestBodyJson.append("\""+speed.getAreaList().get(i).getPump()+"\"},");
            }
        }
        requestBodyJson.append("}");
        Log.i("francis","str---->" + requestBodyJson.toString());
        RequestBody jsonBody = RequestBody.create(JSON, requestBodyJson.toString());
        String base_url = String.format(BASE_URL,MACHINE_SPEED_SET);
        StringBuffer stringBuffer = new StringBuffer(base_url);
        stringBuffer.append("?machineid=");
        stringBuffer.append(machine_id);
        Request request;
        if(!TextUtils.isEmpty(SpUtil.getInstance(mContext).getCookies())){
            request = new Request.Builder().url(stringBuffer.toString()).header("Cookie",SpUtil.getInstance(mContext).getCookies()).post(jsonBody).build();
        }else{
            request = new Request.Builder().url(stringBuffer.toString()).post(jsonBody).build();
        }
        Log.i("francis","url---->" + stringBuffer.toString());
        HttpUtils.asyncEnqueue(request,new GetSetSpeedsCallBack(responseAction));
    }

    //计划详情
    private class GetSetSpeedsCallBack implements Callback {
        private ResponseAction responseAction;

        private GetSetSpeedsCallBack(ResponseAction responseAction) {
            this.responseAction = responseAction;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Message message = new Message(e.getMessage(), Message.ERROR_CODE);
            responseAction.onFailure(message);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (!response.isSuccessful() || response.code() != 200) {
                Message message = new Message("Request Failure", Message.ERROR_CODE);
                responseAction.onFailure(message);
                return;
            }
            try {
                String result = response.body().string();
                Log.i("francis","result------>" + result);
                if (TextUtils.isEmpty(result)) {
                    responseAction.onFailure(new Message("null", Message.ERROR_STATUS));
                    return;
                }
                JSONObject jsonObject = new JSONObject(result);
                Speed speed = new Speed(jsonObject);
                JSONArray areaListJsonArray = jsonObject.optJSONArray("areas");
                if(areaListJsonArray == null){
                    Message message = new Message("null", Message.ERROR_CODE);
                    responseAction.onFailure(message);
                    return;
                }
                List<Area> areas = new LinkedList<Area>();
                for (int i = 0; i < areaListJsonArray.length(); i++){
                    Area area = new Area(areaListJsonArray.getJSONObject(i));
                    areas.add(area);
                }
                speed.setAreaList(areas);
                responseAction.onSuccess(speed);
            } catch (JSONException e) {
                e.printStackTrace();
                Message message = new Message(e.getMessage(), Message.ERROR_CODE);
                responseAction.onFailure(message);
            }
        }
    }


}
