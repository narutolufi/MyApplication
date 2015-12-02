package com.rnkj.rain.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rnkj.rain.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/1.
 */
public class SpeedModeFragment extends BaseFragment {

    @Bind(R.id.listview)
    ListView speedModeListView;

    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_speedmode,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView(){

    }
}
