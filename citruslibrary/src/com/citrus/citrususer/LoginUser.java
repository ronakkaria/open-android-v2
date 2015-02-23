package com.citrus.citrususer;

import android.app.Activity;
import android.text.TextUtils;

public class LoginUser {
	Activity activity;
	String email, password;
	
	public LoginUser(Activity activity, String email, String password) {
		this.activity = activity;
		this.email = email;
		this.password = password;
	}
	
	public String login() {
		PrepaidOauth oauth = new PrepaidOauth(activity, email, password);
		
		String result = oauth.create();
		
		if (TextUtils.equals(result, "prepaid token received")) {
			result = oauth.getsetCookie();
		}
		
		return result;
	}
}
