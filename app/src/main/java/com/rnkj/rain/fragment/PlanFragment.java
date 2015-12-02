package com.rnkj.rain.fragment;

import android.content.Intent;
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
import com.rnkj.rain.ui.PlanAddAcitivy;
import com.rnkj.rain.utils.SpUtil;
import com.rnkj.rain.view.OrderPlanPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by francis on 2015/10/18.
 */
public class PlanFragment extends BaseFragment {


    public static final int PLAN_ORDER_BY_DATE = 1;
    public static final int PLAN_ORDER_BY_COST = 2;

    public static int current_order_type = PLAN_ORDER_BY_DATE;


    @Bind(R.id.id_plan_current)
    TextView plan_current;
    @Bind(R.id.id_plan_history)
    TextView plan_history;

    @Bind(R.id.id_add_plan)
    ImageView add_plan;
    @Bind(R.id.id_plan_orderby)
    ImageView plan_orderby;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private OrderPlanPopWindow orderPlanPopWindow;
    private DoOrderPlanInterface doOrderPlanInterface;


    private CurrentPlans currentPlansFragment;
    private HistoryPlans historyPlansFragment;
    List<BaseFragment> fs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_run,null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        fs = new ArrayList<BaseFragment>();
        currentPlansFragment=new CurrentPlans();
        historyPlansFragment=new HistoryPlans();
        fs.add(currentPlansFragment);
        fs.add(historyPlansFragment);
        FragmentManager fm = getChildFragmentManager();
        PlanPagerAdapter adapter = new PlanPagerAdapter(fm, fs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        plan_history.setOnClickListener(new planTabClick());
        plan_current.setOnClickListener(new planTabClick());
        initViewPager();
        doOrderPlanInterface = new DoOrderPlanInterface();
    }

    private void initViewPager() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                resetTabView();
                switch (position) {
                    case 0:
                        plan_current.setTextColor(getResources().getColor(R.color.plan_txt_pressed));
                        break;
                    case 1:
                        plan_history.setTextColor(getResources().getColor(R.color.plan_txt_pressed));
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
                case R.id.id_plan_current:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.id_plan_history:
                    viewPager.setCurrentItem(1);
                    break;
            }
        }
    }

    private void resetTabView(){
        plan_current.setTextColor(getResources().getColor(R.color.plan_txt_normal));
        plan_history.setTextColor(getResources().getColor(R.color.plan_txt_normal));
    }

    @OnClick(R.id.id_add_plan)
    public void addPlanClick(){
        Intent intent = new Intent(getActivity(), PlanAddAcitivy.class);
        intent.putExtra("machine_id",getActivity().getIntent().getSerializableExtra("machine_id"));
        intent.putExtra("machine_name", getActivity().getIntent().getSerializableExtra("machine_name"));
        startActivityForResult(intent, 99);
    }

    @OnClick(R.id.id_plan_orderby)
    public void orderPlanClick(){
        if(orderPlanPopWindow == null){
            orderPlanPopWindow = new OrderPlanPopWindow(getActivity());
            orderPlanPopWindow.setDoOrderPlanInterface(doOrderPlanInterface);
        }
        orderPlanPopWindow.showPopupWindow(plan_orderby);
    }


    class DoOrderPlanInterface implements OrderPlanPopWindow.doOrderPlanInterface{
        @Override
        public void doOrder(int orderBy) {
            if(viewPager.getCurrentItem() == 0){
                if(currentPlansFragment != null){
                    currentPlansFragment.doCallback(orderBy);
                }
            }else{
                if(historyPlansFragment != null){
                    historyPlansFragment.doCallback(orderBy);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99 && resultCode == 99){
            if(doOrderPlanInterface != null){
                doOrderPlanInterface.doOrder(PlanFragment.PLAN_ORDER_BY_DATE);
            }
        }
    }
}
