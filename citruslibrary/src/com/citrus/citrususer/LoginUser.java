package com.citrus.citrususer;

import org.json.JSONObject;

import android.app.Activity;

public class LoginUser {
	Activity activity;
	String email, password;
	
	public LoginUser(Activity activity, String email, String password) {
		this.activity = activity;
		this.email = email;
		this.password = password;
	}
	
	public JSONObject login() {
		PrepaidOauth oauth = new PrepaidOauth(activity, email, password);
		
		JSONObject result = oauth.create();
		
		if (result.has("status")) {
			result = oauth.getsetCookie();
		}
		
		return result;
	}
	
}
