/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.citrus.sample;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.card.Card;
import com.citrus.mobile.Callback;
import com.citrus.mobile.Month;
import com.citrus.mobile.Year;
import com.citrus.netbank.Bank;
import com.citrus.payment.Bill;
import com.citrus.payment.PG;
import com.citrus.payment.UserDetails;
import com.citrus.sdk.CitrusActivity;
import com.citrus.sdk.Constants;
import com.citrus.sdk.PaymentParams;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.NetbankingOption;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.payment.PaymentType;

public class PaymentPage extends Activity {
    public static final String BILL_URL = "https://salty-plateau-1529.herokuapp.com/billGenerator.sandbox.php?";

    Button cardpayment, tokenpayment, bankpay, walletpay, signin, getbalance;

    JSONObject customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        cardpayment = (Button) this.findViewById(R.id.cardpayment);

        tokenpayment = (Button) this.findViewById(R.id.tokenpayment);

        bankpay = (Button) this.findViewById(R.id.bankpay);

        walletpay = (Button) this.findViewById(R.id.walletpay);

        signin = (Button) this.findViewById(R.id.signin);

        getbalance = (Button) this.findViewById(R.id.getbalance);

        customer = new JSONObject();

        cardpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amount amount = new Amount(2.5);
                PaymentType paymentType = new PaymentType.PGPayment(amount, BILL_URL + "amount=" + amount.getValue());
                DebitCardOption debitCardOption = new DebitCardOption("My Debit Card", "4111111111111111", "123", Month.APR, Year._2016);

                PaymentParams paymentParams = PaymentParams.builder(amount, paymentType, debitCardOption)
                        .environment(PaymentParams.Environment.SANDBOX)
                        .build();
                startCitrusActivity(paymentParams);
            }
        });

        tokenpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount amount = new Amount(2.5);
                PaymentType paymentType = new PaymentType.PGPayment(amount, BILL_URL + "amount=" + amount.getValue());
                CreditCardOption creditCardOption = new CreditCardOption("f1b2508e360c345285d7917d4f4eb112", "123");

                PaymentParams paymentParams = PaymentParams.builder(amount, paymentType, creditCardOption)
                        .environment(PaymentParams.Environment.SANDBOX)
                        .build();
                startCitrusActivity(paymentParams);
            }
        });

        bankpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount amount = new Amount(2.5);
                PaymentType paymentType = new PaymentType.PGPayment(amount, BILL_URL + "amount=" + amount.getValue());
                NetbankingOption netbankingOption = new NetbankingOption("ICICI Bank", "CID001");

                PaymentParams paymentParams = PaymentParams.builder(amount, paymentType, netbankingOption)
                        .environment(PaymentParams.Environment.SANDBOX)
                        .build();
                startCitrusActivity(paymentParams);
            }
        });
    }

    private void startCitrusActivity(PaymentParams paymentParams) {
        Intent intent = new Intent(PaymentPage.this, CitrusActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_PAYMENT_PARAMS, paymentParams);
        startActivityForResult(intent, Constants.RESULT_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TransactionResponse transactionResponse = data.getParcelableExtra(Constants.INTENT_EXTRA_TRANSACTION_RESPONSE);
        if (transactionResponse != null) {
            Log.d("Citrus", "transactionResponse :: " + transactionResponse.toString());
        }
    }
}