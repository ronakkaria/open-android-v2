package com.citrus.citrususer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;

import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;
import com.citrus.mobile.User;

public class BindtoPrepaid {
	Activity activity;
	
	String email, mobile, password;
	
	PrepaidOauth oauth;
	
	public BindtoPrepaid(Activity activity, String email, String mobile, String password) {
		this.activity = activity;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
		oauth = new PrepaidOauth(activity, email, password);
	}
	
	public String setPassword() {
		JSONObject headers = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONObject response = new JSONObject();
        
        try {
        	OauthToken token = new OauthToken(activity, User.SIGNIN_TOKEN);
        	JSONObject jsontoken = token.getuserToken();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            if (jsontoken != null) {
                headers.put("Authorization", "Bearer " + jsontoken.getString("access_token"));
            }
            else {
            	return "Signin Token not found - Link user first";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
		
        RandomPassword pwd = new RandomPassword();
        
        String random_pass = pwd.generate(this.email, this.mobile);
        
        try {
			userJson.put("old", random_pass);
			
	        userJson.put("new", password);
	        
	        RESTclient restclient = new RESTclient("password", Config.getEnv(), userJson, headers);
	        
	        response = restclient.makePutrequest();
	        
	        if (response == null) {
				return createprepaidAccount();
			}
			else {
				return response.toString();
			}

	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        return "could not update password";
	}
	
	private String createprepaidAccount() {
		String result = oauth.create();
		
		if (TextUtils.equals(result, "prepaid token received")) {
			return getbalance();
		}
		
	    return result;
	}
	
	private String getbalance() {
		String result = oauth.getbalance();
		
		if (TextUtils.equals(result, "prepaid user created")) {
			return getPrepaidCookie();
		}
		
		return result;
	}
	
	private String getPrepaidCookie() {
		String result = oauth.getsetCookie();
		return result;
	}
	
	
}
