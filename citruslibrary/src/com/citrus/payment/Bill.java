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
package com.citrus.payment;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class Bill {
    private String txnId, signature, access_key, returnurl, notifyurl = null;
    private JSONObject amount;

    public Bill(String bill) {
        JSONObject jsonBill = null;
        try {
            jsonBill = new JSONObject(bill);

            if (jsonBill != null) {

                this.txnId = jsonBill.getString("merchantTxnId");
                this.amount = jsonBill.getJSONObject("amount");
                this.signature = jsonBill.getString("requestSignature");
                this.access_key = jsonBill.getString("merchantAccessKey");
                this.returnurl = jsonBill.getString("returnUrl");

                if (jsonBill.has("notifyUrl")) {
                    this.notifyurl = jsonBill.getString("notifyUrl");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bill(String bill, String type) {
        JSONObject jsonBill = null;
        try {
            jsonBill = new JSONObject(bill);
            if (jsonBill != null) {

                this.txnId = jsonBill.getString("merchantTransactionId");
                this.amount = jsonBill.getJSONObject("amount");
                this.signature = jsonBill.getString("signature");
                this.access_key = jsonBill.getString("merchantAccessKey");
                this.returnurl = jsonBill.getString("returnUrl");

                if (jsonBill.has("notifyUrl")) {
                    this.notifyurl = jsonBill.getString("notifyUrl");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        if (!TextUtils.isEmpty(txnId) && amount != null && !TextUtils.isEmpty(signature)
                && !TextUtils.isEmpty(access_key) && !TextUtils.isEmpty(returnurl)) {

            return true;
        } else {
            return false;
        }
    }

    public String getTxnId() {
        return this.txnId;
    }

    public JSONObject getAmount() {
        return this.amount;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getAccess_key() {
        return this.access_key;
    }

    public String getReturnurl() {
        return this.returnurl;
    }

    public String getNotifyurl() {
        return this.notifyurl;
    }
}