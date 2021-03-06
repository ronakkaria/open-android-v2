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

package com.citrus.asynch;

import android.app.Activity;
import android.os.AsyncTask;

import com.citrus.mobile.Callback;
import com.citrus.mobile.User;

/**
 * Created by shardul on 20/11/14.
 */
public class Binduser extends AsyncTask<String, Void, Void> {

    boolean binderesult;
    Activity activity;
    Callback callback;
    User user;

    public Binduser(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... params) {
        user = new User(activity);
        binderesult = user.binduser(params[0], params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (binderesult) {
            callback.onTaskexecuted("User Bound Successfully!", "");
        }
        else {
            callback.onTaskexecuted("", "Could not bind user - check oauth details");
        }
    }
}
