package com.citrus.user;

import com.citrus.mobile.OauthToken;

import android.app.Activity;

public class User {
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
