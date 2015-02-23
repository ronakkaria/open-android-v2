package com.citrus.asynch;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.citrususer.BindtoPrepaid;
import com.citrus.mobile.Callback;

public class SetPassword extends AsyncTask<String, Void, String> {
	Activity activity;
	
	Callback callback;
	
	String email, mobile, password;
		
	public SetPassword(Activity activity, Callback callback) {
		this.activity = activity;
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		email = params[0];
		
		mobile = params[1];
		
		password = params[2];
		
		BindtoPrepaid pwd = new BindtoPrepaid(activity, email, mobile, password);
		
		String result = pwd.setPassword();
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onTaskexecuted(result, "");
	}
}
