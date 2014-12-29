package com.citrus.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.asynch.Binduser;
import com.citrus.asynch.GetWallet;
import com.citrus.asynch.Savecard;
import com.citrus.card.Card;
import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citruspay.sampleapp.R;

public class MainActivity extends Activity {

    Button bind, savecard, getWallet, paybutton;

    Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        DebugLogConfig.enable();

        callback = new Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                showToast(success, error);
            }
        };

        bind = (Button) this.findViewById(R.id.bind);

        savecard = (Button) this.findViewById(R.id.savecard);

        getWallet = (Button) this.findViewById(R.id.getWallet);

        paybutton = (Button) this.findViewById(R.id.paybutton);

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Binduser(MainActivity.this, callback).execute("tester46@gmail.com", "9020184710");
            }
        });

        savecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = new Card("4242424242424242", "12", "20", "123", "Bruce Wayne", "debit");

                new Savecard(MainActivity.this, callback).execute(card);
            }
        });

        getWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetWallet(MainActivity.this, callback).execute();
            }
        });

        paybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PaymentPage.class);
                startActivity(intent);
            }
        });
    }


    private void init() {

        Config.setEnv("production"); //replace it with production when you are ready

//        Config.setupSignupId("test-signup");
//        Config.setupSignupSecret("c78ec84e389814a05d3ae46546d16d2e");
//
//        Config.setSigninId("test-signin");
//        Config.setSigninSecret("52f7e15efd4208cf5345dd554443fd99");

        Config.setupSignupId("tinqrin-mobile-signup");
        Config.setupSignupSecret("cd871c73c0624d0f10a96db4fcb2b99d");

        Config.setSigninId("tinqrin-mobile-signedin");
        Config.setSigninSecret("1ef04e72eceb83138e2538e5fc3c4f8e");
    }

    private void showToast(String message, String error) {
        if (!TextUtils.isEmpty(message))
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        if (!TextUtils.isEmpty(error))
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

}