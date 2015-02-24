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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WebPage extends Activity {
    WebView webView;
    
    BroadcastReceiver receiver;
    
    Button submit;
    
    String js, otp;
    
    TextView otpview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        
        String url = getIntent().getStringExtra("url");

        webView = (WebView) this.findViewById(R.id.webview);
        
        submit = (Button) this.findViewById(R.id.submitButton);
        
        otpview = (TextView) this.findViewById(R.id.otpText);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JsInterface(), "CitrusResponse");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        
        webView.loadUrl(url);

    }
   
    private class JsInterface {

        @JavascriptInterface
        public void pgResponse(String response) {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        }
    }

}