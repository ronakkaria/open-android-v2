package com.citrus.cash;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;
import com.citrus.mobile.User;

public class Prepaid {
	
	private String emailId, base_url;
	
	private Callback callback;
	
	private Activity activity;
	
	private JSONObject response;
	
	public Prepaid(String email) {
		this.emailId = email;
		this.base_url = Config.getEnv();
	}
	
	public String getUserEmail() {
		return emailId;
	}
	
	public void getBalance(Activity activity, Callback callback) {
		this.activity = activity;
		
		this.callback = callback;
		
        new GetBalance().execute();
   
	}
	
	private class GetBalance extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			OauthToken token = new OauthToken(activity, User.SIGNIN_TOKEN);
	        String access_token = null;

	        try {
	            access_token = token.getuserToken().getString("access_token");
	        } catch (JSONException e) {
	            e.printStackTrace();
	            callback.onTaskexecuted("", "User not bound - Please bind the user");
	        }
	        
	        JSONObject headers = new JSONObject();

	        try {
	            headers.put("Authorization", "Bearer " + access_token);
	        } catch (JSONException e) {
	            e.printStackTrace();
	            callback.onTaskexecuted("", "Access Token Not Found");
	            
	        }
	        
	        RESTclient resTclient = new RESTclient("balance",base_url, null, headers);
	        
	        
			try {
				response = resTclient.makegetRequest();
			} catch (JSONException e) {
				e.printStackTrace();
				callback.onTaskexecuted("", "Unable to get User Balance");
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (response != null) {
				callback.onTaskexecuted(response.toString(), "");
			}
			else {
				callback.onTaskexecuted("", "Unable to get Response from the server");
			}
			
		}
		
	}
}
