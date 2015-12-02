package com.rnkj.rain.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.DateUtils;
import com.rnkj.rain.utils.SpUtil;
import com.rnkj.rain.view.PieChart;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by francis on 2015/11/3.
 */
public class PlanDetailActivity extends BaseActivity {


    @Bind(R.id.id_plan_detail_startdate)
    TextView plan_detail_startdate;
    @Bind(R.id.id_plan_detail_area)
    TextView plan_detail_area;
    @Bind(R.id.id_plan_detail_times)
    TextView plan_detail_times;
    @Bind(R.id.id_plan_detail_submittime)
    TextView plan_detail_submittime;
    @Bind(R.id.id_plan_detail_expecttime)
    TextView plan_detail_expecttime;
    @Bind(R.id.id_plan_detail_timecost)
    TextView plan_detail_timecost;
    @Bind(R.id.id_plan_detail_progress)
    TextView plan_detail_progress;

    @Bind(R.id.btn_running)
    Button btn_running;
    @Bind(R.id.btn_pasue)
    Button btn_pasue;

    @Bind(R.id.txt_machinename)
    TextView txt_machinename;
    @Bind(R.id.index_username)
    TextView index_username;

    @Bind(R.id.pieChartView)
    PieChart pieChartView;

    @Bind(R.id.id_view_index_username)
    LinearLayout view_index_username;

    private String machineId;
    private String machineName, user_name;
    private String planId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        machineId = getIntent().getStringExtra("machine_id");
        machineName = getIntent().getStringExtra("machine_name");
        planId = getIntent().getStringExtra("plan_id");
        user_name = SpUtil.getInstance(this).getUserName();
        txt_machinename.setText(machineName);
        index_username.setText(user_name);
        initData();
    }


    private void initData() {
        Dao.instance(this).planDetailRequest(new ResponseAction() {
            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                final Plan plan = (Plan) entity;
                PlanDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setView(plan);
                    }
                });
            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                showShort(getResources().getString(R.string.netword_error));
            }
        }, machineId, planId);
    }


    private void setView(Plan plan) {
        plan_detail_startdate.setText(DateUtils.getDay(plan.getStartDate()));
        plan_detail_area.setText(String.format(getResources().getString(R.string.str_area_mark), plan.getArea_start()) + "~" + String.format(getResources().getString(R.string.str_area_mark), plan.getArea_end()));
        plan_detail_times.setText(plan.getTimes() + "次");
        plan_detail_submittime.setText(DateUtils.getDay(plan.getCreaedDate()));
        plan_detail_expecttime.setText(DateUtils.getMillon(plan.getEndDate() - plan.getStartDate()));
        plan_detail_timecost.setText(DateUtils.getMillon(plan.getTimeCost()));
        plan_detail_progress.setText(plan.getProgress() + "%");
        pieChartView.setCirAngleInfo(plan.getArea_start(), plan.getArea_end(), 100f, PieChart.DIRECTION_CLOCKWISE);
        if (plan.getStatus().equalsIgnoreCase("RUNNING")) {
            btn_running.setEnabled(false);
            btn_running.setBackgroundColor(getResources().getColor(R.color.status_stop));
            btn_pasue.setBackgroundColor(getResources().getColor(R.color.status_press));
        } else {
            btn_pasue.setEnabled(false);
            btn_running.setBackgroundColor(getResources().getColor(R.color.status_press));
            btn_pasue.setBackgroundColor(getResources().getColor(R.color.status_stop));
        }
    }


    @OnClick(R.id.btn_running)
    public void onRunningClick() {

    }

    @OnClick(R.id.btn_pasue)
    public void onPasueClick() {

    }

    @OnClick(R.id.id_view_index_username)
    public void onViewUserNameClick(){
        setResult(ListActivity.MAIN_REQUEST_CODE);
        finish();
    }
}
