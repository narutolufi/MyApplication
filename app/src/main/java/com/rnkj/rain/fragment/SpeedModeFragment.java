package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rnkj.rain.R;
import com.rnkj.rain.adapter.SpeedAreaAdapter;
import com.rnkj.rain.adapter.SpeedModeAdapter;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.view.SwitchView;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by francis on 2015/12/1.
 */
public class SpeedModeFragment extends BaseFragment {

    @Bind(R.id.listview)
    ListView speedModeListView;

    private SpeedModeAdapter speedModeAdapter;

    private List<Area> mAreaList;

    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_speedmode,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        if(mAreaList == null){
            mAreaList = new LinkedList<Area>();
        }
        speedModeAdapter = new SpeedModeAdapter(getActivity(),(LinkedList)mAreaList);
        speedModeAdapter.setOnSpeedModeSwitchListener(new SpeedModeAdapter.OnSpeedModeSwitchListener() {
            @Override
            public void onCheck(int position, Area area, SwitchView sv, boolean checkLeft, boolean checkRight) {

            }
        });
        speedModeListView.setAdapter(speedModeAdapter);
    }


    public void setData(List<Area> areas){
        this.mAreaList = areas;
        speedModeAdapter.notifyDataSetChanged();
    }
}
