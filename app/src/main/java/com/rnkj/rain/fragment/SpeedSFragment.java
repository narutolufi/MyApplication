package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rnkj.rain.R;
import com.rnkj.rain.adapter.SpeedSAdapter;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.view.SwitchView;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by francis on 2015/12/1.
 */
public class SpeedSFragment extends BaseFragment {

    @Bind(R.id.listview)
    ListView speedSListView;

    @Bind(R.id.id_speed_switch)
    SwitchView speed_switch;

    private List<Area> mAreaList;
    private SpeedSAdapter speedSAdapter;

    private boolean isCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_speeds, null);
        ButterKnife.bind(this, view);
        initView();
        isCreated = true;
        return view;
    }

    private void initView() {
        if (SpeedFragment.speed == null) {
            return;
        }
        speed_switch.getLeftView().setText("开启");
        speed_switch.getRightView().setText("关闭");
        if (SpeedFragment.speed.getAutoSpeed().equalsIgnoreCase("ON")) {
            speed_switch.switching(SwitchView.TYPE_LEFT);
        } else {
            speed_switch.switching(SwitchView.TYPE_RIGHT);
        }
        speed_switch.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onCheck(SwitchView sv, boolean checkLeft, boolean checkRight) {
                if (checkLeft) {
                    SpeedFragment.speed.setAutoSpeed("ON");
                } else if (checkRight) {
                    SpeedFragment.speed.setAutoSpeed("OFF");
                }
            }
        });

        mAreaList = SpeedFragment.speed.getAreaList();
        Log.i("francis", "SpeedAreaFragment--->size----->" + mAreaList.size());
        speedSAdapter = new SpeedSAdapter(getActivity(), (LinkedList) mAreaList);
        speedSAdapter.setOnSpeedClick(new SpeedSAdapter.OnSpeedClick() {
            @Override
            public void onAdd(int position, Area area) {
                Log.i("francis", "setOnSpeedClick--->onAdd---->");
            }

            @Override
            public void onSubtract(int position, Area area) {
                Log.i("francis", "setOnSpeedClick--->onSubtract---->");
            }
        });
        speedSListView.setAdapter(speedSAdapter);
    }

    public void resetView() {
        if (speedSAdapter != null) {
            speedSAdapter.notifyDataSetChanged();
        } else if (isCreated) {
            initView();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
    }
}
