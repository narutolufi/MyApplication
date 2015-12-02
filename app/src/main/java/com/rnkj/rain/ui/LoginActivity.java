package com.rnkj.rain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.rnkj.rain.R;
import com.rnkj.rain.activity.BaseActivity;
import com.rnkj.rain.bean.IEntity;
import com.rnkj.rain.bean.Message;
import com.rnkj.rain.bean.Plan;
import com.rnkj.rain.bean.User;
import com.rnkj.rain.request.Dao;
import com.rnkj.rain.request.results.ResponseAction;
import com.rnkj.rain.utils.SpUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by francis on 2015/10/10.
 */
public class LoginActivity extends BaseActivity {

    public static final int LOGIN_REQUEST_CODE = 1024;

    @Bind(R.id.et_userid)
    EditText userName_edit;
    @Bind(R.id.et_password)
    EditText password_edit;

    @Bind(R.id.btn_login)
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        getSupportActionBar().hide();
    }

    @OnClick(R.id.btn_login)
    public void doLogin() {
        String userName = userName_edit.getText().toString();
        String password = password_edit.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            LoginActivity.this.showShort(getResources().getString(R.string.name_pwd_null));
            return;
        }
        this.showDialogLoading();
        Dao.instance(this).loginRequest(new ResponseAction() {
            @Override
            public void onFailure(IEntity entity) {
                super.onFailure(entity);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.dismissDialogLoading();
                        LoginActivity.this.showShort(getResources().getString(R.string.netword_error));
                        return;
                    }
                });
            }

            @Override
            public void onSuccess(IEntity entity) {
                super.onSuccess(entity);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.this.dismissDialogLoading();
                    }
                });
                User user = (User) entity;
                SpUtil.getInstance(LoginActivity.this).setName(user.getName());
                SpUtil.getInstance(LoginActivity.this).setUserName(user.getId());
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                LoginActivity.this.startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        }, userName, password);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == ListActivity.LOGOUT_REQUEST_CODE) {
            userName_edit.setText("");
            password_edit.setText("");
        }
    }
}
