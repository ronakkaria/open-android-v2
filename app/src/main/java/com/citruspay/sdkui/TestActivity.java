package com.citruspay.sdkui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.citrus.interfaces.InitListener;
import com.citrus.mobile.Config;


public class TestActivity extends ActionBarActivity {

    private static final String BILL_URL = "http://192.168.1.5:8080/billGenerator.orig.jsp";// host your bill url here

    InitListener initListener;
    ProgressDialog mProgressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();

        Intent intent = new Intent(TestActivity.this, MainActivity.class);
        intent.putExtra(Config.INTENT_EXTRA_USER_EMAIL, "developercitrus@gmail.com");
        intent.putExtra(Config.INTENT_EXTRA_USER_MOBILE, "1234567890");
        intent.putExtra(Config.INTENT_EXTRA_MERCHANT_BILL_URL, BILL_URL);
        intent.putExtra(Config.INTENT_EXTRA_MERCHANT_VANITY, "NativeSDK");
        intent.putExtra(Config.INTENT_EXTRA_MERCHANT_NAME, "Shopmatic Stores");

        startActivity(intent);
    }

    private void init() {
        Config.setEnv("production"); // replace it with production when you are ready

        Config.setupSignupId("test-signup");
        Config.setupSignupSecret("c78ec84e389814a05d3ae46546d16d2e");

        Config.setSigninId("test-signin");
        Config.setSigninSecret("52f7e15efd4208cf5345dd554443fd99");

        mProgressDialog = new ProgressDialog(this);
        showDialog("Loading the data", false);
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
