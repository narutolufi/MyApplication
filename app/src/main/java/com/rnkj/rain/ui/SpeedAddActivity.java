package com.rnkj.rain.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.callback.OnAreaCallback;
import com.rnkj.rain.fragment.SpeedFragment;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.AreaPickerDLUtil;
import com.rnkj.rain.utils.SpUtil;
import com.rnkj.rain.view.SwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by francis on 2015/12/13.
 */
public class SpeedAddActivity extends BaseActivity {

    @Bind(R.id.speed_area_spinner)
    Spinner speedAreaSpinner;

    @Bind(R.id.txt_machinename)
    TextView txt_machinename;
    @Bind(R.id.index_username)
    TextView index_username;

    @Bind(R.id.id_speed_add_sudu)
    EditText speed_add_sudu;
    @Bind(R.id.id_speed_area_start)
    EditText speed_area_start;
    @Bind(R.id.id_speed_area_end)
    EditText speed_area_end;
    @Bind(R.id.id_mode_switch)
    SwitchView mode_switchView;

    @Bind(R.id.id_btn_add_speed)
    Button btn_add_speed;
    @Bind(R.id.id_btn_cancel_speed)
    Button btn_cancel_speed;


    private Area selectedArea;

    private int selectedPos = 0;

    private AreaPickerDLUtil areaPickerDLUtil;

    private String machine_id,machine_name,user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_add);
        initView();
    }

    private void initView(){
        getSupportActionBar().hide();
        machine_id = (String) getIntent().getSerializableExtra("machine_id");
        machine_name = (String) getIntent().getSerializableExtra("machine_name");
        user_name = SpUtil.getInstance(this).getUserName();
        txt_machinename.setText(machine_name);
        index_username.setText(user_name);
        speed_add_sudu.setOnClickListener(new DateTimePickerOnClickListener());
        speed_area_start.setOnClickListener(new DateTimePickerOnClickListener());
        speed_area_end.setOnClickListener(new DateTimePickerOnClickListener());
        List<String> mItems = new ArrayList<String>();
        for(int i = 0; i < SpeedFragment.speed.getAreaList().size(); i++){
            String temp = "区域 " + (i+1) + " : " + String.format(getResources().getString(R.string.str_area_mark),SpeedFragment.speed.getAreaList().get(i).getStart()) + " ~~ " + String.format(getResources().getString(R.string.str_area_mark), SpeedFragment.speed.getAreaList().get(i).getEnd());
            mItems.add(temp);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedAreaSpinner .setAdapter(adapter);
        speedAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                selectedArea = SpeedFragment.speed.getAreaList().get(pos);
                selectedPos = pos;
                resetView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedArea = SpeedFragment.speed.getAreaList().get(0);
                selectedPos = 0;
                resetView();
            }
        });
    }

    private void resetView(){
        speed_add_sudu.setText(selectedArea.getSpeed()+"");
        speed_area_start.setText(String.format(getResources().getString(R.string.str_area_mark), selectedArea.getStart()));
        speed_area_end.setText(String.format(getResources().getString(R.string.str_area_mark),selectedArea.getEnd()));
        if(selectedArea.getPump().equalsIgnoreCase("ON")){
            mode_switchView.switching(SwitchView.TYPE_LEFT);
        }else{
            mode_switchView.switching(SwitchView.TYPE_RIGHT);
        }
        mode_switchView.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onCheck(SwitchView sv, boolean checkLeft, boolean checkRight) {
                if(checkLeft){
                    SpeedFragment.speed.getAreaList().get(selectedPos).setPump("ON");
                }else{
                    SpeedFragment.speed.getAreaList().get(selectedPos).setPump("OFF");
                }
            }
        });
    }


    @OnClick(R.id.id_btn_add_speed)
    public void addSuccess(){
        Dao.instance(this).getSetSpeedsRequest(new ResponseAction() {
            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
            }
        },machine_id,SpeedFragment.speed);
    }

    @OnClick(R.id.id_btn_cancel_speed)
    public void addCancel(){
        finish();
    }

    class DateTimePickerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_speed_add_sudu:
                    areaPickerDLUtil = new AreaPickerDLUtil(SpeedAddActivity.this,selectedArea);
                    areaPickerDLUtil.areaPicKDialogForSpeed(new OnAreaCallback() {
                        @Override
                        public void OnSettingArea(int startArea, int endArea) {

                        }

                        @Override
                        public void OnSettingSpeed(int speed) {
                            speed_add_sudu.setText(speed + "");
                            SpeedFragment.speed.getAreaList().get(selectedPos).setSpeed(speed);
                        }
                    });
                    break;
                case R.id.id_speed_area_start:
                case R.id.id_speed_area_end:
                    areaPickerDLUtil = new AreaPickerDLUtil(SpeedAddActivity.this,selectedArea);
                    areaPickerDLUtil.areaPicKDialogForAdd(new OnAreaCallback() {
                        @Override
                        public void OnSettingArea(int startArea, int endArea) {
                            speed_area_start.setText(startArea + "");
                            speed_area_end.setText(endArea + "");
                            SpeedFragment.speed.getAreaList().get(selectedPos).setStart(startArea);
                            SpeedFragment.speed.getAreaList().get(selectedPos).setEnd(endArea);
                        }

                        @Override
                        public void OnSettingSpeed(int speed) {

                        }
                    });
                    break;
            }
        }
    }

}
