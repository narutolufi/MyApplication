package com.rnkj.rain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.fragment.BaseFragment;
import com.rnkj.rain.fragment.MainFragment;
import com.rnkj.rain.fragment.PlanFragment;
import com.rnkj.rain.fragment.ShotFragment;
import com.rnkj.rain.fragment.SpeedFragment;
import com.rnkj.rain.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by francis on 2015/10/18.
 */
public class IndexActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.main_button)
    Button main_button;
    @Bind(R.id.run_button)
    Button run_button;
    @Bind(R.id.speed_button)
    Button speed_button;
    @Bind(R.id.shot_button)
    Button shot_button;
    @Bind(R.id.index_username)
    TextView txt_username;

    @Bind(R.id.txt_machinename)
    TextView txt_machinename;

    private MainFragment mainFragment;
    private PlanFragment runfragment;
    private ShotFragment shotfragment;
    private SpeedFragment speedfragment;
    List<BaseFragment> fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        getSupportActionBar().hide();
        initViewPager();
        initData();
    }

    private void initData(){
        fs = new ArrayList<BaseFragment>();
        mainFragment=new MainFragment();
        runfragment=new PlanFragment();
        speedfragment=new SpeedFragment();
        shotfragment=new ShotFragment();
        fs.add(mainFragment);
        fs.add(runfragment);
        fs.add(speedfragment);
        fs.add(shotfragment);
        FragmentManager fm = getSupportFragmentManager();
        MainPagerAdapter adapter = new MainPagerAdapter(fm, fs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        String machine_name = getIntent().getStringExtra("machine_name");
        txt_machinename.setText(machine_name);
    }

    private void initViewPager() {
        txt_username.setText(SpUtil.getInstance(this).getUserName());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                resetTabView();
                switch (position) {
                    case 0:
                        main_button.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        main_button.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        run_button.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        run_button.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        speed_button.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        speed_button.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        shot_button.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
                        shot_button.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.main_button:
                viewPager.setCurrentItem(0);
                break;
            case R.id.run_button:
                viewPager.setCurrentItem(1);
                break;
            case R.id.speed_button:
                viewPager.setCurrentItem(2);
                break;
            case R.id.shot_button:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    class MainPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fs;
        public MainPagerAdapter(FragmentManager fm, List<BaseFragment> fs) {
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

    private void resetTabView(){
        main_button.setBackgroundColor(getResources().getColor(R.color.tab_normal));
        main_button.setTextColor(getResources().getColor(R.color.black));
        run_button.setBackgroundColor(getResources().getColor(R.color.tab_normal));
        run_button.setTextColor(getResources().getColor(R.color.black));
        speed_button.setBackgroundColor(getResources().getColor(R.color.tab_normal));
        speed_button.setTextColor(getResources().getColor(R.color.black));
        shot_button.setBackgroundColor(getResources().getColor(R.color.tab_normal));
        shot_button.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ListActivity.MAIN_REQUEST_CODE && resultCode == ListActivity.MAIN_REQUEST_CODE){
            finish();
        }
    }
}
