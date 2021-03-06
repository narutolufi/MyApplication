package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rnkj.rain.R;
import com.rnkj.rain.adapter.SpeedAreaAdapter;
import com.rnkj.rain.bean.Area;
import com.rnkj.rain.bean.Speed;
import com.rnkj.rain.utils.AreaPickerDLUtil;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by francis on 2015/12/1.
 */
public class SpeedAreaFragment extends BaseFragment {

    @Bind(R.id.listview)
    ListView speedAreaListView;

    private List<Area> mAreaList;
    private SpeedAreaAdapter speedAreaAdapter;

    private boolean isCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_speedarea,null);
        ButterKnife.bind(this, view);
        initView();
        isCreated = true;
        return view;
    }

    private void initView(){
        if(SpeedFragment.speed == null){
            return;
        }
        mAreaList = SpeedFragment.speed.getAreaList();
        Log.i("francis", "SpeedAreaFragment--->size----->" + mAreaList.size());
        speedAreaAdapter = new SpeedAreaAdapter(getActivity(),(LinkedList)mAreaList);
        speedAreaListView.setAdapter(speedAreaAdapter);
        speedAreaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("francis","position----->" + position);
                AreaPickerDLUtil areaPickerDLUtil = new AreaPickerDLUtil(getActivity(),SpeedFragment.speed.getAreaList().get(position),position);
                areaPickerDLUtil.areaPicKDialog();
                areaPickerDLUtil.setAreaCallback(new AreaPickerDLUtil.SettingsAreaCallback() {
                    @Override
                    public void onCallback() {
                        speedAreaAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void resetView(){
        if(speedAreaAdapter != null){
            speedAreaAdapter.notifyDataSetChanged();
        }else if(isCreated){
            initView();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
    }
}
