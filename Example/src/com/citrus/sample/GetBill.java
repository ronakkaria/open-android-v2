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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.citrus.mobile.Callback;

public class GetBill extends AsyncTask<Void, Void, Void> {
    String billurl;

    HttpResponse response;

    Callback callback;

    public GetBill(String url, Callback callback) {
        billurl = url;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(billurl);

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (response.getStatusLine().getStatusCode() == 200) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.onTaskexecuted(jsonObject.toString(), "");
        }
        else {
            callback.onTaskexecuted("", "Could not get the bill");
        }
    }
}
