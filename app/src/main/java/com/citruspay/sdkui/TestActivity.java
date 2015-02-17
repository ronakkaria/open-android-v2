package com.citruspay.sdkui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.VolleyError;
import com.citrus.interfaces.InitListener;
import com.citrus.mobile.Config;


public class TestActivity extends ActionBarActivity {

    InitListener initListener;
    ProgressDialog mProgressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initListener();
        init();
    }

    private void init() {
        Config.setEnv("sandbox"); // replace it with production when you are ready

        Config.setupSignupId("test-signup");
        Config.setupSignupSecret("c78ec84e389814a05d3ae46546d16d2e");

        Config.setSigninId("test-signin");
        Config.setSigninSecret("52f7e15efd4208cf5345dd554443fd99");
        Config.setVanity("NativeSDK");
        Config.setEmailID("developercitrus@gmail.com");
        Config.setMobileNo("8692862420");
        Config.initUser(getApplicationContext(),initListener);

        mProgressDialog = new ProgressDialog(this);
        showDialog("Loading the data", false);
    }

    private void initListener() {
        initListener = new InitListener() {
            @Override
            public void onSuccess(String response) {

                dismissDialog();

                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onBindFailed(String response) {

            }

            @Override
            public void onWalletLoadFailed(String response) {

            }

            @Override
            public void onNetBankingListFailed(VolleyError error) {

            }

            @Override
            public void onError(Exception e) {

            }

        };
    }

    private void showDialog(String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
