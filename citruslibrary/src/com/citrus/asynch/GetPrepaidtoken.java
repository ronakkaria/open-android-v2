package com.citrus.asynch;

import org.json.JSONObject;


import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.citrus.mobile.Callback;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.User;

public class GetPrepaidtoken extends AsyncTask<Void, Void, String>{
	
	Activity activity;
	
	Callback callback;
	
	public GetPrepaidtoken(Activity activity, Callback callback) {
		this.activity = activity;
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		
		OauthToken token = new OauthToken(activity, User.PREPAID_TOKEN);
		
		JSONObject tokenjson = token.getuserToken();
		
		String prepaidToken = null;
		
		if (tokenjson != null) {
			prepaidToken = tokenjson.toString();
		}
				
		return prepaidToken;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (TextUtils.isEmpty(result)) {
			callback.onTaskexecuted("", "Prepaid Token Not Found - did you sign in the user?");
		}
		else {
			callback.onTaskexecuted(result, "");
		}		
	}
}
