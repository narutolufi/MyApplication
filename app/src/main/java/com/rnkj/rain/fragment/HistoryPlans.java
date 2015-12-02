package com.rnkj.rain.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chanven.commonpulltorefresh.PtrClassicFrameLayout;
import com.chanven.commonpulltorefresh.PtrDefaultHandler;
import com.chanven.commonpulltorefresh.PtrFrameLayout;
import com.chanven.commonpulltorefresh.loadmore.OnLoadMoreListener;
import com.rnkj.rain.MainApplication;
import com.rnkj.rain.R;
import com.rnkj.rain.adapter.PlanHistoryAdapter;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.callback.FragmentCallback;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.ui.PlanDetailActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by francis on 2015/11/1.
 */
public class HistoryPlans extends BaseFragment implements FragmentCallback{

    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.list_view_frame)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    private Context mContext;
    private int current_page = 0;
    private String machineId;
    private String machineName;

    private LinkedList<Plan> planLinkedList;
    private PlanHistoryAdapter planAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_historyplan,null);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView(){
        machineId = getActivity().getIntent().getStringExtra("machine_id");
        machineName = getActivity().getIntent().getStringExtra("machine_name");
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMachineList();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                getMoreMachineList();
            }
        });
        ptrClassicFrameLayout.autoRefresh(true);
        listView.setOnItemClickListener(new historyPlanOnItemClick());
    }


    private void getMachineList(){
        current_page = 0;
        Dao.instance(mContext).planHistoryListRequest(new ResponseAction() {
            @Override
            public void onSuccess(List<IEntity> entities) {
                super.onSuccess(entities);
                if (planLinkedList == null) {
                    planLinkedList = new LinkedList<Plan>();
                } else {
                    planLinkedList.clear();
                }
                planLinkedList.addAll((LinkedList) entities);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter();
                        ptrClassicFrameLayout.refreshComplete();
                        ptrClassicFrameLayout.setLoadMoreEnable(true);
                    }
                });
            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.refreshComplete();
                    }
                });

            }
        }, 0, MainApplication.LENGTH, machineId,PlanFragment.current_order_type);
    }


    private void getMoreMachineList(){
        current_page = current_page + 1;
        Dao.instance(mContext).planHistoryListRequest(new ResponseAction() {
            @Override
            public void onSuccess(List<IEntity> entities) {
                super.onSuccess(entities);
                if (planLinkedList == null) {
                    planLinkedList = new LinkedList<Plan>();
                } else {
                    planLinkedList.addAll((LinkedList) entities);
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        reflushAdapter();
                    }
                });
            }

            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                });
            }
        }, current_page * MainApplication.LENGTH, MainApplication.LENGTH, machineId,PlanFragment.current_order_type);
    }


    private void initAdapter(){
        if(planAdapter == null){
            planAdapter = new PlanHistoryAdapter(mContext,planLinkedList);
        }
        listView.setAdapter(planAdapter);
    }

    private void reflushAdapter(){
        if(planAdapter != null){
            planAdapter.notifyDataSetChanged();
        }
    }


    class historyPlanOnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, PlanDetailActivity.class);
            intent.putExtra("machine_id",machineId);
            intent.putExtra("machine_name",machineName);
            startActivity(intent);
        }
    }

    @Override
    public void doCallback(int action_code) {
        Log.i("francis", "action_code---->" + action_code);
        switch (action_code){
            case PlanFragment.PLAN_ORDER_BY_COST:
                PlanFragment.current_order_type = PlanFragment.PLAN_ORDER_BY_COST;
                break;
            case PlanFragment.PLAN_ORDER_BY_DATE:
                PlanFragment.current_order_type = PlanFragment.PLAN_ORDER_BY_DATE;
                break;
        }
        getMachineList();
    }
}
