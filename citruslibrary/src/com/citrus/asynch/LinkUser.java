package com.citrus.asynch;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.citrususer.SignupUser;
import com.citrus.mobile.Callback;

public class LinkUser extends AsyncTask<String, Void, String>{
	private Activity activity;
	
	private Callback callback;
	
	private String signup_result;
	
	public LinkUser(Activity activity, Callback callback) {
		this.activity = activity;
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		SignupUser user = new SignupUser(activity);
		this.signup_result = user.register(params[0], params[1]);
		return signup_result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onTaskexecuted(result, "");
	}
}
