package com.citrus.asynch;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.citrususer.ResetPassword;
import com.citrus.mobile.Callback;

public class ForgotPass extends AsyncTask<Void, Void, String>{
	Callback callback;	
	String email;
	
	JSONObject headers, params, response;
	
	Activity activity;
	
	public ForgotPass(Activity activity, String email, Callback callback) {
		this.email = email;
		this.callback = callback;
		this.activity = activity;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		ResetPassword reset = new ResetPassword(activity, email);
		String results = reset.sendresetEmail();
		return results;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		callback.onTaskexecuted(result, "");
	}
}
