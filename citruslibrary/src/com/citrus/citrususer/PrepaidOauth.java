package com.citrus.citrususer;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.citrus.cash.PersistentConfig;
import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;
import com.citrus.mobile.User;

public class PrepaidOauth {
	
	Activity activity;
	
	String email, password;
	
	String sessionCookie;
	CookieManager cookieManager; 
	
	public PrepaidOauth(Activity activity, String email, String password) {
		this.email = email;
		this.password = password;
		this.activity = activity;				
	}
	
	public PrepaidOauth(Activity activity) {
		this.activity = activity;		
	}
	
	public String create() {
		JSONObject headers = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONObject response = new JSONObject();
        
        try {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		try {
			userJson.put("client_id", Config.getSigninId());
			
	        userJson.put("client_secret", Config.getSigninSecret());
	        
	        userJson.put("grant_type", "password");
	        
	        userJson.put("username", this.email);
	        
	        userJson.put("password", this.password);
	        
	        RESTclient restclient = new RESTclient("signin", Config.getEnv(), userJson, headers);
	        
	        response = restclient.makePostrequest();
	        
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (response.has("access_token")) {
			OauthToken prepaid_token = new OauthToken(activity, User.PREPAID_TOKEN);
			prepaid_token.createToken(response);	
			return "prepaid token received";
		}
		else {
			return response.toString();
		}
	}
	
	public String getbalance() {
		JSONObject headers = new JSONObject();
		JSONObject response = new JSONObject();
		
		try {
			OauthToken prepaid_token = new OauthToken(activity, User.PREPAID_TOKEN);
			JSONObject token_getbalance = prepaid_token.getuserToken();
            headers.put("Authorization", "Bearer " + token_getbalance.getString("access_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		RESTclient restclient = new RESTclient("specialbalance", Config.getEnv(), null, headers);
        
        try {
			response = restclient.makegetRequest();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        if (response.has("value")) {        	
        	return "prepaid user created";
        } 
        else {
        	return "Could not create prepaid user - get balance error";
        }
	}
	
	public String getsetCookie() {
		JSONObject user = new JSONObject();
		JSONObject headers = new JSONObject();		
		JSONObject response = new JSONObject();
		
		try {
			user.put("email", email);
			user.put("password", password);
			user.put("rmcookie", "true");			
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			
		} catch (JSONException e) {
			return "email or password missing";
		}

		RESTclient restClient = new RESTclient("prepaid", Config.getEnv(), user, headers);
		try {
			response = restClient.makePostrequest();
		} catch (IOException e) {
			e.printStackTrace();
			return "Check your internet connection";
		}
		
		if (!response.equals(null)) {
			try {				
				CookieSyncManager.createInstance(activity);
				cookieManager = CookieManager.getInstance(); 
				
				PersistentConfig config = new PersistentConfig(activity.getApplicationContext());
				
				sessionCookie = config.getCookieString();
				
				if (sessionCookie != null) {
					cookieManager.removeSessionCookie();
				}
				
				sessionCookie = response.getString("Set-Cookie");
				
				config.setCookie(sessionCookie);
				
				return "User logged in with prepaid credentials!";
								
			} catch (JSONException e) {
				e.printStackTrace();
				return response.toString();
			}
		}
		else {
			return "Could not get Prepaid Cookie";
		}
	}
	
}
