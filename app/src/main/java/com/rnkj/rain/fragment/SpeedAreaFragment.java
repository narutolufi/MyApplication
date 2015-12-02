package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rnkj.rain.R;
import com.rnkj.rain.bean.Area;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_speedarea,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView(){

    }
}
