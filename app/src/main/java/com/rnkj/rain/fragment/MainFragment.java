package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Machine;
import com.rnkj.rain.bean.MachineDetail;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.T;
import com.rnkj.rain.view.PieChart;
import com.rnkj.rain.view.SwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by francis on 2015/10/18.
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.btn_shot)
    Button btn_shot;
    @Bind(R.id.btn_accessory1)
    Button btn_accessory1;
    @Bind(R.id.btn_accessory2)
    Button btn_accessory2;
    @Bind(R.id.btn_running)
    Button btn_running;
    @Bind(R.id.btn_pasue)
    Button btn_pause;

    @Bind(R.id.img_machine_status)
    ImageView img_machine_status;
    @Bind(R.id.txt_machine_status)
    TextView txt_machine_status;

    @Bind(R.id.img_wifi_status)
    ImageView img_wifi_status;
    @Bind(R.id.txt_wifi_status)
    TextView txt_wifi_status;



    @Bind(R.id.img_temperature)
    ImageView img_temperature;
    @Bind(R.id.temperature_text)
    TextView temperature_text;

    @Bind(R.id.machine_current)
    TextView machine_current;
    @Bind(R.id.machine_voltage)
    TextView machine_voltage;
    @Bind(R.id.machine_pass)
    TextView machine_pass;


    @Bind(R.id.txt_progress)
    TextView txt_progress;
    @Bind(R.id.txt_time)
    TextView txt_time;
    @Bind(R.id.txt_speed)
    TextView txt_speed;
    @Bind(R.id.txt_position)
    TextView txt_position;
    @Bind(R.id.txt_area)
    TextView txt_area;
    @Bind(R.id.txt_times)
    TextView txt_times;
//    @Bind(R.id.speedviewset)
//    TextView speedviewset;


    @Bind(R.id.mode_switchview)
    SwitchView mode_switchview;

    @Bind(R.id.pieChartView)
    PieChart pieChartView;

    @Bind(R.id.img_refresh)
    ImageView img_refresh;

    @Bind(R.id.edit_text_speedviewset)
    EditText edit_text_speedviewset;

    @Bind(R.id.btn_speed_cancel)
    Button btn_speed_cancel;

    @Bind(R.id.btn_speed_confrim)
    Button btn_speed_confrim;


    private String machine_id;
    private MachineDetail machineDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine_id = getActivity().getIntent().getStringExtra("machine_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        mode_switchview.switching(false);
    }

    private void initData(String machine_id){
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).machinenDetailRequest(new ResponseAction() {
            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }
        },machine_id);
    }


    @OnClick(R.id.btn_speed_cancel)
    public void btn_speed_cancel(){
        edit_text_speedviewset.setText(machineDetail.getSpeed() + "");
    }

    @OnClick(R.id.btn_speed_confrim)
    public void btn_speed_confrim(){
        String speed_str = edit_text_speedviewset.getText().toString();
        if(TextUtils.isEmpty(speed_str)){
            return;
        }
        int int_speed = Integer.parseInt(speed_str);
        if(int_speed < 0 || int_speed > 100){
            T.showShort(getActivity(),"填写的设置错误，速度的在0~100");
            return;
        }
        setting_speed(int_speed);
    }

    private void setting_speed(int int_speed){
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).machineSpeedRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity) getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail) entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity) getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        }, machine_id, int_speed);
    }


    @OnClick(R.id.img_refresh)
    public void img_refresh(){
        initData(machine_id);
    }

    @OnClick(R.id.btn_shot)
    public void btn_shot(){
        String endgunSwitch = null;
        if(machineDetail.getEndgunSwitch().equalsIgnoreCase("ON")){
            endgunSwitch = "OFF";
        }else{
            endgunSwitch = "ON";
        }
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).endgunRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        },machine_id,endgunSwitch);
    }

    @OnClick(R.id.btn_accessory1)
    public void btn_accessory1(){
        String accessory1 = null;
        if(machineDetail.getAccessory1Switch().equalsIgnoreCase("ON")){
            accessory1 = "OFF";
        }else{
            accessory1 = "ON";
        }
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).accessory1Request(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        }, machine_id, accessory1);
    }

    @OnClick(R.id.btn_accessory2)
    public void btn_accessory2(){
        String accessory2 = null;
        if(machineDetail.getAccessory2Switch().equalsIgnoreCase("ON")){
            accessory2 = "OFF";
        }else{
            accessory2 = "ON";
        }
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).accessory2Request(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        }, machine_id, accessory2);
    }

    @OnClick(R.id.btn_running)
    public void btn_start(){
        String start = null;
        if(machineDetail.getStartSwitch().equalsIgnoreCase("ON")){
            start = "OFF";
        }else{
            start = "ON";
        }
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).machineStartRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        }, machine_id, start);
    }

    @OnClick(R.id.btn_pasue)
    public void btn_pause(){
        String pause = null;
        if(machineDetail.getPauseSwitch().equalsIgnoreCase("ON")){
            pause = "OFF";
        }else{
            pause = "ON";
        }
        ((BaseActivity)getActivity()).showDialogLoading();
        Dao.instance(getActivity()).machinePauseRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                machineDetail = (MachineDetail)entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)getActivity()).dismissDialogLoading();
                        resetView();
                    }
                });
            }
        }, machine_id, pause);
    }


    private void resetView(){
        if(machineDetail.getWorkingStatus().equalsIgnoreCase("NORMAL")){
            img_machine_status.setBackgroundResource(R.drawable.normal);
            txt_machine_status.setText(getResources().getString(R.string.machine_status_normal));
        }else if(machineDetail.getWorkingStatus().equalsIgnoreCase("TO_BE_INSPECTED")){
            img_machine_status.setBackgroundResource(R.drawable.to_be_inspected);
            txt_machine_status.setText(getResources().getString(R.string.machine_status_inspected));
        }else{
            img_machine_status.setBackgroundResource(R.drawable.error);
            txt_machine_status.setText(getResources().getString(R.string.machine_status_broken));
        }

        if(machineDetail.getNetworkStatus().equalsIgnoreCase("ONLINE")){
            img_wifi_status.setBackgroundResource(R.drawable.online);
            txt_wifi_status.setText(getResources().getString(R.string.wifi_status_online));
        }else{
            img_wifi_status.setBackgroundResource(R.drawable.offline);
            txt_wifi_status.setText(getResources().getString(R.string.wifi_status_outline));
        }
        temperature_text.setText(String.format(getActivity().getResources().getString(R.string.mac_temperature),machineDetail.getTemperature()));

        txt_progress.setText(String.format(getActivity().getResources().getString(R.string.string_progress),machineDetail.getPlan_progress() + "%"));
        int cost = machineDetail.getPlan_timeCost();
        int count = cost / 60;
        int remainder = cost % 60;
        String time = null;
        if(count <= 0){
            time = remainder + "分";
        }else{
            time = count + "时" + remainder + "分";
        }
        txt_time.setText(String.format(getActivity().getResources().getString(R.string.string_time),time));
        txt_speed.setText(String.format(getActivity().getResources().getString(R.string.string_speed),machineDetail.getSpeed()));
        txt_position.setText(String.format(getActivity().getResources().getString(R.string.string_position),machineDetail.getPosition()));
        txt_area.setText(String.format(getActivity().getResources().getString(R.string.string_area),machineDetail.getPlan_area_start(),machineDetail.getPlan_area_end()));
        txt_times.setText(String.format(getActivity().getResources().getString(R.string.string_times),machineDetail.getPlan_completedTimess(),machineDetail.getPlan_times()));

        machine_current.setText(String.format(getActivity().getResources().getString(R.string.mac_current),machineDetail.getCurrent()));
        machine_pass.setText(String.format(getActivity().getResources().getString(R.string.mac_pass),machineDetail.getPressure()));
        machine_voltage.setText(String.format(getActivity().getResources().getString(R.string.mac_voltage),machineDetail.getVoltage()));

        int direction = PieChart.DIRECTION_CLOCKWISE;
        if(machineDetail.getDirection().equalsIgnoreCase("CLOCKWISE")){
            direction = PieChart.DIRECTION_CLOCKWISE;
        }else{
            direction = PieChart.DIRECTION_ANTICLOCKWISE;
        }
        pieChartView.setCirAngleInfo((float)machineDetail.getPlan_area_start(),(float)machineDetail.getPlan_area_end(),(float)machineDetail.getPosition(),direction);
        edit_text_speedviewset.setText(machineDetail.getSpeed()+"");

        if(machineDetail.getPump().equalsIgnoreCase("ON")){
            mode_switchview.switching(SwitchView.TYPE_LEFT);
        }else{
            mode_switchview.switching(SwitchView.TYPE_RIGHT);
        }
        if(machineDetail.getEndgunSwitch().equalsIgnoreCase("ON")){
            btn_shot.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_shot.setEnabled(true);
        }else{
            btn_shot.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_shot.setEnabled(false);
        }

        if(machineDetail.getAccessory1Switch().equalsIgnoreCase("ON")){
            btn_accessory1.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_accessory1.setEnabled(true);
        }else{
            btn_accessory1.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_accessory1.setEnabled(false);
        }

        if(machineDetail.getAccessory2Switch().equalsIgnoreCase("ON")){
            btn_accessory2.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_accessory2.setEnabled(true);
        }else{
            btn_accessory2.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_accessory2.setEnabled(false);
        }

        if(machineDetail.getStartSwitch().equalsIgnoreCase("ON")){
            btn_running.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_running.setEnabled(true);
        }else{
            btn_running.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_running.setEnabled(false);
        }

        if(machineDetail.getPauseSwitch().equalsIgnoreCase("ON")){
            btn_pause.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_pause.setEnabled(true);
        }else{
            btn_pause.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_pause.setEnabled(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initData(machine_id);
    }
}
