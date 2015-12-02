package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.MachineDetail;
import com.rnkj.rain.bean.Speed;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.ui.IndexActivity;
import com.rnkj.rain.view.PieChart;
import com.rnkj.rain.view.SwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by francis on 2015/10/18.
 */
public class SpeedFragment extends BaseFragment {

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

    @Bind(R.id.id_view_add_speed)
    ImageView view_add_speed;

    @Bind(R.id.pieChartView)
    PieChart pieChartView;

    @Bind(R.id.img_refresh)
    ImageView img_refresh;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.id_tab_area)
    TextView tab_area;
    @Bind(R.id.id_tab_speed)
    TextView tab_speed;
    @Bind(R.id.id_tab_mode)
    TextView tab_mode;


    private SpeedAreaFragment speedAreaFragment;
    private SpeedSFragment speedSFragment;
    private SpeedModeFragment speedModeFragment;
    List<BaseFragment> fs;

    private String machine_id;
    private MachineDetail machineDetail;
    private Speed speed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine_id = getActivity().getIntent().getStringExtra("machine_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_speed,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        fs = new ArrayList<BaseFragment>();
        speedAreaFragment = new SpeedAreaFragment();
        speedSFragment = new SpeedSFragment();
        speedModeFragment = new SpeedModeFragment();

        fs.add(speedAreaFragment);
        fs.add(speedSFragment);
        fs.add(speedModeFragment);
        FragmentManager fm = getChildFragmentManager();
        PlanPagerAdapter adapter = new PlanPagerAdapter(fm, fs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        tab_area.setOnClickListener(new planTabClick());
        tab_speed.setOnClickListener(new planTabClick());
        tab_mode.setOnClickListener(new planTabClick());
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                resetTabView();
                switch (position) {
                    case 0:
                        tab_area.setTextColor(getResources().getColor(R.color.white));
                        tab_area.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        break;
                    case 1:
                        tab_speed.setTextColor(getResources().getColor(R.color.white));
                        tab_speed.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        break;
                    case 2:
                        tab_mode.setTextColor(getResources().getColor(R.color.white));
                        tab_mode.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        break;
                }
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }



    class PlanPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fs;
        public PlanPagerAdapter(FragmentManager fm, List<BaseFragment> fs) {
            super(fm);
            this.fs = fs;
        }
        @Override
        public Fragment getItem(int arg0) {
            return fs.get(arg0);
        }
        @Override
        public int getCount() {
            return fs.size();
        }
    }


    class planTabClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_tab_area:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.id_tab_speed:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.id_tab_mode:
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    }

    private void resetTabView(){
        tab_area.setTextColor(getResources().getColor(R.color.tab_pressed));
        tab_area.setBackgroundColor(getResources().getColor(R.color.plan_txt_normal));
        tab_speed.setTextColor(getResources().getColor(R.color.tab_pressed));
        tab_speed.setBackgroundColor(getResources().getColor(R.color.plan_txt_normal));
        tab_mode.setTextColor(getResources().getColor(R.color.tab_pressed));
        tab_mode.setBackgroundColor(getResources().getColor(R.color.plan_txt_normal));
    }

    private void initData(String machine_id){
        if(IndexActivity.mMachineDetail == null){
            ((BaseActivity)getActivity()).showDialogLoading();
            Dao.instance(getActivity()).machinenDetailRequest(new ResponseAction() {
                @Override
                public void onSuccess(IEntity entity) {
                    super.onSuccess(entity);
                    IndexActivity.mMachineDetail = (MachineDetail)entity;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getSpeedList();
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
        }else{
            getSpeedList();
        }
    }



    private void getSpeedList(){
        Dao.instance(getActivity()).getListSpeedsRequest(new ResponseAction() {
            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                speed = (Speed) entity;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity) getActivity()).dismissDialogLoading();
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
                        ((BaseActivity) getActivity()).dismissDialogLoading();
                    }
                });
            }
        }, machine_id);
    }





    @OnClick(R.id.img_refresh)
    public void img_refresh(){
        initData(machine_id);
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

        speedAreaFragment.setData(speed.getAreaList());
        speedSFragment.setData(speed.getAreaList());
        speedModeFragment.setData(speed.getAreaList());
    }


    @Override
    public void onResume() {
        super.onResume();
        initData(machine_id);
    }
}
