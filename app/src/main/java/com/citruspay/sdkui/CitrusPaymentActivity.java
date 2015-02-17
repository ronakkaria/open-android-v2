package com.citruspay.sdkui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class CitrusPaymentActivity extends ActionBarActivity {

    private WebView mWebviewPayment = null;
    private String mUrl = null;
    private String mJSIntefaceName = null;
    private ProgressDialog mProgressDialog = null;
    private String mTitle = null;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citrus_payment);

        mTitle = getIntent().getStringExtra(Utils.INTENT_EXTRA_PAYMENT_ACTIVITY_TITLE);
        mUrl = getIntent().getStringExtra(Utils.INTENT_EXTRA_PAYMENT_URL);
        mJSIntefaceName = getIntent().getStringExtra(Utils.INTENT_EXTRA_JSINTERFACE);
        if (TextUtils.isEmpty(mJSIntefaceName)) {
            mJSIntefaceName = "CitrusResponse";
        }

        mProgressDialog = new ProgressDialog(mContext);

        mWebviewPayment = (WebView) findViewById(R.id.webViewPayment);
        mWebviewPayment.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /*
            This setting is required to enable redirection of urls from https to http or vice-versa.
            This redirection is blocked by default from Lollipop (Android 21).
             */
            mWebviewPayment.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebviewPayment.addJavascriptInterface(new JsInterface(), mJSIntefaceName);
        mWebviewPayment.setWebViewClient(new CitrusWebClient());
        // Load the bank's or card payment url
        mWebviewPayment.loadUrl(mUrl);

        // Set the title
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
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
            showDialog("Processing your payment. Please do not refresh the page.", false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // Dismiss the progress/message dialog.
            dismissDialog();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
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
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra(Utils.INTENT_EXTRA_PAYMENT_RESPONSE, response);
            setResult(RESULT_OK, intent);

            finish();

        }
    }
}
