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

package com.citrus.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.citrus.asynch.GetBill;
import com.citrus.card.Card;
import com.citrus.card.TextUtils;
import com.citrus.library.R;
import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citrus.payment.Bill;
import com.citrus.payment.PG;
import com.citrus.payment.UserDetails;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.payment.PaymentType;

import org.json.JSONException;
import org.json.JSONObject;

public class CitrusActivity extends Activity {

    private WebView mPaymentWebview = null;
    private String mUrl = null;
    private Context mContext = this;
    private ProgressDialog mProgressDialog = null;
    private PaymentParams mPaymentParams = null;
    private PaymentType mPaymentType = null;
    private PaymentOption mPaymentOption = null;
    private String mTransactionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citrus);

        mProgressDialog = new ProgressDialog(mContext);
        mUrl = getIntent().getStringExtra(Constants.INTENT_EXTRA_RETURN_URL);
        mPaymentParams = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_PAYMENT_PARAMS);
        mPaymentType = mPaymentParams.getPaymentType();
        mPaymentOption = mPaymentParams.getPaymentOption();

        mPaymentWebview = (WebView) findViewById(R.id.payment_webview);
        mPaymentWebview.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*
            This setting is required to enable redirection of urls from https to http or vice-versa.
            This redirection is blocked by default from Lollipop (Android 21).
             */
            mPaymentWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mPaymentWebview.addJavascriptInterface(new JsInterface(), Constants.JS_INTERFACE_NAME);

        mPaymentWebview.setWebChromeClient(new WebChromeClient());

        mPaymentWebview.setWebViewClient(new CitrusWebClient());

        fetchBill();
    }

    private void fetchBill() {
        showDialog("Processing payment, please wait...", false);

        String billUrl = mPaymentType.getUrl();

        new GetBill(billUrl, new Callback() {
            @Override
            public void onTaskexecuted(String bill, String error) {
                Log.d("Citrus", "Bill ::: " + bill + " ERROR :: " + error);

                dismissDialog();
                if (!android.text.TextUtils.isEmpty(error)) {
                    Toast.makeText(CitrusActivity.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    proceedToPayment(bill);
                }
            }
        }).execute();
    }

    private void proceedToPayment(String billJSON) {
        showDialog("Processing payment, please wait...", false);

        final CitrusUser citrusUser = mPaymentParams.getUser();
        UserDetails userDetails = new UserDetails(CitrusUser.toJSONObject(citrusUser));
        Bill bill = new Bill(billJSON);
        mTransactionId = bill.getTxnId();

        PG paymentgateway = new PG(mPaymentOption, bill, userDetails);

        paymentgateway.charge(new Callback() {
            @Override
            public void onTaskexecuted(String success, String error) {
                processresponse(success, error);
                dismissDialog();
            }
        });
    }

    private void processresponse(String response, String error) {

        if (!android.text.TextUtils.isEmpty(response)) {
            try {

                JSONObject redirect = new JSONObject(response);
                if (!android.text.TextUtils.isEmpty(redirect.getString("redirectUrl"))) {

                    mPaymentWebview.loadUrl(redirect.getString("redirectUrl"));
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }

    }

    private void showDialog(String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.setCanceledOnTouchOutside(false);
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


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                TransactionResponse transactionResponse = new TransactionResponse(TransactionResponse.TransactionStatus.FAIL, "Cancelled By User", mTransactionId);
                sendResult(transactionResponse);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        // Set other dialog properties
        builder.setMessage("Do you want to cancel the transaction?")
                .setTitle("Cancel Transaction?");
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void sendResult(TransactionResponse transactionResponse) {
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_EXTRA_TRANSACTION_RESPONSE, transactionResponse);
        setResult(Constants.RESULT_CODE_PAYMENT, intent);
        finish();
    }

    /**
     * Handle all the Webview loading in custom webview client.
     */
    private class CitrusWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Let this webview handle all the urls loaded inside. Return false to denote that.
            view.loadUrl(url);

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // Display the message.
            showDialog("Processing your payment. Please do not refresh the page.", true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // Dismiss the progress/message dialog.
            dismissDialog();
        }
    }

    /**
     * This class will be loaded as JSInterface and the methods of this class will be called from
     * the javascript loaded inside webview.
     * <p/>
     * Handle the payment response and take actions accordingly.
     */
    private class JsInterface {

        @JavascriptInterface
        public void pgResponse(String response) {
            TransactionResponse transactionResponse = TransactionResponse.fromJSON(response);
            sendResult(transactionResponse);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPaymentWebview != null) {
            mPaymentWebview.stopLoading();
            mPaymentWebview.destroy();
        }
        mPaymentWebview = null;
        mUrl = null;
    }
}
