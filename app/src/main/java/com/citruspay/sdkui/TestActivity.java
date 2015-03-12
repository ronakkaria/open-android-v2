package com.citruspay.sdkui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.citrus.mobile.Config;


public class TestActivity extends ActionBarActivity {

    private static final String BILL_URL = "http://192.168.1.5:8080/billGenerator.orig.jsp";// host your bill url here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();

        Intent intent = new Intent(TestActivity.this, MainActivity.class);
        CitrusPaymentParams paymentParams = new CitrusPaymentParams();
        paymentParams.billUrl = BILL_URL;
        paymentParams.merchantName = "Greenseer";
        paymentParams.transactionAmount = 100.0;
        paymentParams.vanity = "NativeSDK";
//        paymentParams.colorPrimary = "#F9A323";
//        paymentParams.colorPrimaryDark = "#E7961D";

        paymentParams.colorPrimary = "#F9A323";
        paymentParams.colorPrimaryDark = "#9575CD";
        paymentParams.accentColor = "#64FFDA";

        CitrusUser user = new CitrusUser("shardullavekar@gmail.com", "1234567890", "Developer", "Citrus", null);
        paymentParams.user = user;
        intent.putExtra(Constants.INTENT_EXTRA_PAYMENT_PARAMS, paymentParams);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void init() {
        Config.setEnv("production"); // replace it with production when you are ready

        Config.setupSignupId("test-signup");
        Config.setupSignupSecret("c78ec84e389814a05d3ae46546d16d2e");

        Config.setSigninId("test-signin");
        Config.setSigninSecret("52f7e15efd4208cf5345dd554443fd99");
    }
}
