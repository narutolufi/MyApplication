package com.rnkj.rain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chanven.commonpulltorefresh.PtrClassicFrameLayout;
import com.chanven.commonpulltorefresh.PtrDefaultHandler;
import com.chanven.commonpulltorefresh.PtrFrameLayout;
import com.chanven.commonpulltorefresh.loadmore.OnLoadMoreListener;
import com.rnkj.rain.MainApplication;
import com.rnkj.rain.R;
import com.rnkj.rain.activity.ActivityTack;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.adapter.MachineAdapter;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Machine;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.SpUtil;
import com.rnkj.rain.utils.T;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by francis on 2015/10/13.
 */
public class ListActivity extends BaseActivity {

    @Bind(R.id.tv_username)
    TextView userName;
    @Bind(R.id.tv_userid)
    TextView userId;

    @Bind(R.id.listview)
    ListView machine_list;

    @Bind(R.id.view_about)
    RelativeLayout view_about;
    @Bind(R.id.view_logout)
    RelativeLayout view_logout;

    @Bind(R.id.list_view_frame)
    PtrClassicFrameLayout ptrClassicFrameLayout;

    private int current_page = 0;//当前分页，从0开始

    public static final int LOGOUT_REQUEST_CODE = 99;

    public static final int MAIN_REQUEST_CODE =100;

    private LinkedList<Machine> machineLinkedList;
    private MachineAdapter machineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
        userName.setText(SpUtil.getInstance(this).getName());
        userId.setText(String.format(getResources().getString(R.string.user_name), SpUtil.getInstance(this).getUserName()));

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
    }

    @OnClick(R.id.view_logout)
    public void logout() {
        this.showDialogLoading();
        Dao.instance(this).logoutRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListActivity.this.showShort(getResources().getString(R.string.netword_error));
                        ListActivity.this.dismissDialogLoading();
                    }
                });
                return;
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListActivity.this.dismissDialogLoading();
                        setResult(LOGOUT_REQUEST_CODE);
                        finish();
                    }
                });
            }
        });
    }

    @OnClick(R.id.view_about)
    public void aboutServer() {
        startActivity(AboutActivity.class);
    }

    private void getMachineList() {
        current_page = 0;
        Dao.instance(this).machinenListRequest(new ResponseAction() {
            @Override
            public void onSuccess(List<IEntity> entities) {
                super.onSuccess(entities);
                if (machineLinkedList == null) {
                    machineLinkedList = new LinkedList<Machine>();
                } else {
                    machineLinkedList.clear();
                }
                machineLinkedList.addAll((LinkedList) entities);
                ListActivity.this.runOnUiThread(new Runnable() {
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
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.refreshComplete();
                    }
                });

            }
        }, 0, MainApplication.LENGTH);
    }


    private void getMoreMachineList() {
        current_page = current_page + 1;
        Dao.instance(this).machinenListRequest(new ResponseAction() {
            @Override
            public void onSuccess(List<IEntity> entities) {
                super.onSuccess(entities);
                if (machineLinkedList == null) {
                    machineLinkedList = new LinkedList<Machine>();
                } else {
                    machineLinkedList.addAll((LinkedList) entities);
                }
                ListActivity.this.runOnUiThread(new Runnable() {
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
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                });
            }
        }, current_page * MainApplication.LENGTH, MainApplication.LENGTH);
    }


    private void initAdapter() {
        if (machineAdapter == null) {
            machineAdapter = new MachineAdapter(this, machineLinkedList);
        }
        machine_list.setAdapter(machineAdapter);
        machineAdapter.setOnMachineDetailClick(new MachineAdapter.OnMachineDetailClick() {
            @Override
            public void onClick(Machine machine) {
                Intent intent = new Intent(ListActivity.this, IndexActivity.class);
                intent.putExtra("machine_id", machine.getId());
                intent.putExtra("machine_name", machine.getName());
                ListActivity.this.startActivityForResult(intent,MAIN_REQUEST_CODE);
            }
        });

        machineAdapter.setOnMachineSettingClick(new MachineAdapter.OnMachineSettingClick() {
            @Override
            public void onClick(Machine machine) {
                T.show(ListActivity.this, machine.toString(), 3000);
            }
        });
    }


    private void reflushAdapter() {
        if (machineAdapter != null) {
            machineAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityTack.getInstanse().exit(this);
        }
        return true;
    }
}
