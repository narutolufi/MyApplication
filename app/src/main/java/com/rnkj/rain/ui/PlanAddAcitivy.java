package com.rnkj.rain.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.dialogs.DialogUtil;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.DateTimePickDialogUtil;
import com.rnkj.rain.utils.DateUtils;
import com.rnkj.rain.utils.NumPickerDLUtil;
import com.rnkj.rain.utils.SpUtil;
import com.rnkj.rain.view.PieChart;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by francis on 2015/11/2.
 */
public class PlanAddAcitivy extends BaseActivity {

    @Bind(R.id.pieChartView)
    PieChart pieChartView;

    @Bind(R.id.txt_machinename)
    TextView txt_machinename;
    @Bind(R.id.index_username)
    TextView index_username;

    @Bind(R.id.id_plan_add_date)
    EditText plan_add_date;
    @Bind(R.id.id_plan_add_time)
    EditText plan_add_time;
    @Bind(R.id.id_plan_area_start)
    EditText plan_area_start;
    @Bind(R.id.id_plan_area_end)
    EditText plan_area_end;
    @Bind(R.id.id_plan_add_times)
    EditText plan_add_times;

    @Bind(R.id.id_btn_add_plan)
    Button btn_add_plan;
    @Bind(R.id.id_btn_cancel_plan)
    Button btn_cancel_plan;

    @Bind(R.id.id_view_index_username)
    LinearLayout view_index_username;


    private String machine_id, machine_name, user_name;
    private DateTimePickDialogUtil dateTimePickDialogUtil;
    private NumPickerDLUtil numPickerDLUtil;
    private Plan plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        machine_id = (String) getIntent().getSerializableExtra("machine_id");
        machine_name = (String) getIntent().getSerializableExtra("machine_name");
        user_name = SpUtil.getInstance(this).getUserName();
        txt_machinename.setText(machine_name);
        index_username.setText(user_name);
        plan_add_date.setOnClickListener(new DateTimePickerOnClickListener());
        plan_add_time.setOnClickListener(new DateTimePickerOnClickListener());
        plan_area_start.setOnClickListener(new DateTimePickerOnClickListener());
        plan_area_end.setOnClickListener(new DateTimePickerOnClickListener());
        plan_add_times.setOnClickListener(new DateTimePickerOnClickListener());
        dateTimePickDialogUtil = new DateTimePickDialogUtil(this);
        numPickerDLUtil = new NumPickerDLUtil(this);
        numPickerDLUtil.setAreaCallback(new SettingsAreaCallBack());
        resetView();
    }

    @OnClick(R.id.id_btn_add_plan)
    public void onAddPlanClick() {
        if (!verificationView()) {
            return;
        }
        Dao.instance(this).insertPlanRequest(new ResponseAction() {
            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                PlanAddAcitivy.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShort(getResources().getString(R.string.str_add_plan_success));
                        setResult(99);
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                PlanAddAcitivy.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShort(getResources().getString(R.string.str_add_plan_failure));
                        resetView();
                    }
                });
            }
        }, machine_id, plan);
    }

    @OnClick(R.id.id_btn_cancel_plan)
    public void onCancelPlanClick() {
        setResult(99);
        finish();
    }

    @OnClick(R.id.id_view_index_username)
    public void onViewUserNameClick(){
        setResult(ListActivity.MAIN_REQUEST_CODE);
        finish();
    }


    class DateTimePickerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_plan_add_date:
                    dateTimePickDialogUtil.datePicKDialog(plan_add_date);
                    break;
                case R.id.id_plan_add_time:
                    dateTimePickDialogUtil.timePicKDialog(plan_add_time);
                    break;
                case R.id.id_plan_area_start:
                    numPickerDLUtil.areaPicKDialog(plan_area_start, plan_area_end);
                    break;
                case R.id.id_plan_area_end:
                    numPickerDLUtil.areaPicKDialog(plan_area_start, plan_area_end);
                    break;
                case R.id.id_plan_add_times:
                    numPickerDLUtil.timesPicKDialog(plan_add_times);
                    break;
            }
        }
    }


    private void resetView() {
        plan_add_date.setText("");
        plan_add_time.setText("");
        plan_area_start.setText(String.format(getResources().getString(R.string.str_area_mark), "0"));
        plan_area_end.setText(String.format(getResources().getString(R.string.str_area_mark), "270"));
        pieChartView.setCirAngleInfo(0f, 270f, 150f, PieChart.DIRECTION_CLOCKWISE);
        plan_add_times.setText("");
    }

    private boolean verificationView() {
        String txt_plan_add_date = plan_add_date.getText().toString();
        String txt_plan_add_time = plan_add_time.getText().toString();
        String txt_plan_area_start = plan_area_start.getText().toString();
        String txt_plan_area_end = plan_area_end.getText().toString();
        String txt_plan_add_times = plan_add_times.getText().toString();
        if (TextUtils.isEmpty(txt_plan_add_date)) {
            showShort("请选择日期");
            return false;
        }
        if (TextUtils.isEmpty(txt_plan_add_time)) {
            showShort("请选择时间");
            return false;
        }
        if (TextUtils.isEmpty(txt_plan_area_start)) {
            showShort("请选择起点");
            return false;
        }
        if (TextUtils.isEmpty(txt_plan_area_end)) {
            showShort("请选择终点");
            return false;
        }
        if (TextUtils.isEmpty(txt_plan_add_times)) {
            showShort("请选择次数");
            return false;
        }
        plan = new Plan();
        plan.setDescription("");
        plan.setTimes(Integer.valueOf(txt_plan_add_times));
        plan.setArea_start(numPickerDLUtil.getAreaStart_value());
        plan.setArea_end(numPickerDLUtil.getAreaEnd_value());
        plan.setStartDate(dateTimePickDialogUtil.getMillSeconds());
        return true;
    }

    class SettingsAreaCallBack implements NumPickerDLUtil.SettingsAreaCallback {
        @Override
        public void onCallback(int start_area, int end_area) {
            pieChartView.setCirAngleInfo(start_area, end_area, 100, PieChart.DIRECTION_CLOCKWISE);
        }
    }
}
