package com.citrus.asynch;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.citrususer.LoginUser;
import com.citrus.mobile.Callback;

public class SignIn extends AsyncTask<String, Void, String> {
	Activity activity;
	Callback callback;
	
	public SignIn(Activity activity, Callback callback) {
		this.activity = activity;
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String email = params[0];
		String password = params[1];
		LoginUser login = new LoginUser(activity, email, password);
		String result = login.login();		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onTaskexecuted(result, "");
	}
}
