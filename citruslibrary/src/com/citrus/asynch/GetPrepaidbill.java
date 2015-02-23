package com.citrus.asynch;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;
import com.citrus.mobile.User;

import android.app.Activity;
import android.os.AsyncTask;

public class GetPrepaidbill extends AsyncTask<String, Void, String> {
	Activity activity;
	Callback callback;
	
	JSONObject headers, params, response = null;
	
	public GetPrepaidbill(Activity activity, Callback callback) {
		this.activity = activity;
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		headers = new JSONObject();
		OauthToken token = new OauthToken(activity, User.PREPAID_TOKEN);
		try {
			JSONObject tokenjson = token.getuserToken();
			String access_token = null;
			if (tokenjson != null) {
				access_token = tokenjson.getString("access_token");
			}
			else {
				return "Prepaid Oauth Token is missing - did you sign in the user?";
			}
			
			try {
	            headers.put("Authorization", "Bearer " + access_token);
	            headers.put("Content-Type", "application/x-www-form-urlencoded");
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
		} catch (JSONException e) {
			e.printStackTrace();
			return "Prepaid Oauth Token is missing - did you sign in the user?";
		}
		
		try {
			this.params = new JSONObject();
			this.params.put("amount", params[0]);
			this.params.put("currency", "INR");
			this.params.put("redirect", params[1]);

		} catch (JSONException e) {
			e.printStackTrace();
			return "Prepaid bill parameters are missing";
		}
		
		RESTclient restClient = new RESTclient("prepaidbill", Config.getEnv(), this.params, headers);
		
		try {
			response = restClient.makePostrequest();
		} catch (IOException e) {
			e.printStackTrace();
			return "IO Exception - check if internet is working!";
		}
		
		return response.toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (response != null) {
			callback.onTaskexecuted(response.toString(), "");
		}
		else {
			callback.onTaskexecuted("", result);
		}
	}

}
