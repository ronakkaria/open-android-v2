package com.citrus.citrususer;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;
import com.citrus.mobile.User;

import android.app.Activity;

public class ResetPassword {
	private Activity activity;
	
	private String emailid;
	
	public ResetPassword(Activity activity, String email) {
		this.activity = activity;
		this.emailid = email;
	}
	
	public String sendresetEmail() {
		OauthToken signuptoken = new OauthToken(activity, User.SIGNUP_TOKEN);
		
		JSONObject jsonToken = signuptoken.getuserToken();
		
		JSONObject response, params = null;
		
		if (jsonToken != null) {
			
			JSONObject headers = new JSONObject();
			
			try {
				headers.put("Authorization", "Bearer " + jsonToken.getString("access_token"));
			} catch (JSONException e) {
				e.printStackTrace();
				return "Could not reset Password - signup token missing";
			}
			
			try {
				params = new JSONObject().put("username", emailid);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			RESTclient restClient = new RESTclient("resetpassword", Config.getEnv(), params, headers);
			
			try {
				response = restClient.makePostrequest();
			} catch (IOException e) {
				e.printStackTrace();
				return "Check your internet connection";
			}
			
			if (response == null) {
				return "Reset Password Email sent on the mail Id";
			}
			else {
				return response.toString();
			}
			
		}
		else {
			return "User is not bound - bind the user first";
		}
	}
}
