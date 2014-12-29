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
import android.text.TextUtils;

import com.citrus.mobile.Callback;

class GetBill extends AsyncTask<Void, Void, String> {
    String billurl;
    Callback callback;
    double amount;

    public GetBill(String url, double amount, Callback callback) {
        billurl = url;
        this.callback = callback;
        this.amount = amount;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(billurl + "?amount=" + amount);
        HttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                if (jsonObject != null) {
                    return jsonObject.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (!TextUtils.isEmpty(response)) {
            callback.onTaskexecuted(response, "");
        } else {
            callback.onTaskexecuted("", "Could not get the bill");
        }
    }
}
