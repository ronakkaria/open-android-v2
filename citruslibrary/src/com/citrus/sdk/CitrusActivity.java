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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citrus.library.R;

public class CitrusActivity extends Activity {

    private WebView mPaymentWebview = null;
    private String mUrl = null;
    private Context mContext = this;
    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citrus);

        mProgressDialog = new ProgressDialog(mContext);
        mUrl = getIntent().getStringExtra(Constants.INTENT_EXTRA_RETURN_URL);
        mPaymentWebview = (WebView) findViewById(R.id.payment_webview);
        mPaymentWebview.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*
            This setting is required to enable redirection of urls from https to http or vice-versa.
            This redirection is blocked by default from Lollipop (Android 21).
             */
            mPaymentWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mPaymentWebview.addJavascriptInterface(new JsInterface(), "CitrusResponse");

        mPaymentWebview.setWebChromeClient(new WebChromeClient());

        mPaymentWebview.setWebViewClient(new CitrusWebClient());
        // Load the bank's or card payment url
        mPaymentWebview.loadUrl(mUrl);
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
            //mListener.onTransactionComplete(transactionResponse);
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_EXTRA_TRANSACTION_RESPONSE,transactionResponse);
            setResult(Constants.RESULT_CODE_PAYMENT, intent);
            finish();
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
