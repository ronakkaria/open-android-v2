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
package com.citrus.mobile;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

/**
 * Created by shardul on 18/11/14.
 */
public class User {
    private Activity activity;

    //private SharedPreferences sharedPreferences;

    private String base_url;

    public User(Activity activity) {
        this.activity = activity;
        base_url = Config.getEnv();
    }

    private boolean getSignupToken(final String email, final String mobile) {
        JSONObject response = new JSONObject();


        JSONObject userJson = new JSONObject();

        try {
            userJson.put("client_id", Config.getSignupId());

            userJson.put("client_secret", Config.getSignupSecret());

            userJson.put("grant_type", "implicit");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RESTclient restclient = new RESTclient("signup",base_url, userJson, headers);

        try {
            response = restclient.makePostrequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.has("access_token")) {
            return signupuser(email, mobile, response);
        }
        else {
            return false;
        }

    }

    public boolean binduser(final String email, final String mobile) {
        return getSignupToken(email, mobile);
    }

    private boolean signupuser(final String email, String mobile, JSONObject token) {

        JSONObject signupJson = new JSONObject();

        JSONObject response = new JSONObject();

        try {
            signupJson.put("email", email);

            signupJson.put("mobile", mobile);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Authorization", "Bearer " + token.getString("access_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RESTclient restclient = new RESTclient("bind", base_url, signupJson, headers);

        try {
            response = restclient.makePostrequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.has("username")) {
            return signinUser(email);
        }
        else {
            return false;
        }
    }

    private boolean signinUser(String email) {
        JSONObject response = new JSONObject();

        JSONObject userJson = new JSONObject();

        try {
            userJson.put("client_id", Config.getSigninId());

            userJson.put("client_secret", Config.getSigninSecret());

            userJson.put("grant_type", "username");

            userJson.put("username", email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RESTclient restclient = new RESTclient("signin", base_url, userJson, headers);

        try {
            response = restclient.makePostrequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.has("access_token")) {
            OauthToken token = new OauthToken(activity);
            return token.createToken(response);
        }
        else {
            return false;
        }

    }
    
    
    public final static boolean logoutUser(Activity activity) {
		OauthToken token = new OauthToken(activity);
		return token.clearToken();
	}
	
	public final static boolean isUserLoggedIn(Activity activity) {
		OauthToken token = new OauthToken(activity);
		if(token.getuserToken() == null)
			return false;
		else
			return true;
	}
    
    
}