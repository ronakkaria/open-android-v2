package com.citrus.asynch;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.citrus.mobile.Callback;
import com.citrus.mobile.RESTclient;

public class PaymentOptions extends AsyncTask<Void, Void, String>{
	
	Callback callback;
	
	String vanity;
	
	public PaymentOptions(Callback callback, String vanity) {
		this.callback = callback;
		this.vanity = vanity;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		JSONObject header, param, response;
		
		header = new JSONObject();
		
		try {
			header.put("Content-Type", "application/x-www-form-urlencoded");
		} catch(JSONException e) {
			return "Could not add headers";
		}
		
		param = new JSONObject();
		try {
		param.put("vanity", vanity);
		} catch(JSONException e) {
			return "Could not add vanity url";
		}
		
		RESTclient restClient = new RESTclient("paymentoptions", com.citrus.mobile.Config.getEnv(), param, header);
	
		try {
			response = restClient.makePostrequest();
		} catch (IOException e) {
			e.printStackTrace();
			return "Check your internet connection!";
		}
		
		return response.toString();
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onTaskexecuted(result, "");
	}
}
