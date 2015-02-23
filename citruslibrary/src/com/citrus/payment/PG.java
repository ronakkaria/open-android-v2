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


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.citrus.asynch.MakePayment;
import com.citrus.card.Card;
import com.citrus.cash.Prepaid;
import com.citrus.mobile.Callback;
import com.citrus.netbank.Bank;

public class PG {
    private Card card;
    private Bill bill;
    private UserDetails userDetails;
    private Callback callback;

    private JSONObject payment;

    private Bank bank;
    
    private Prepaid prepaid;

    private String paymenttype;
    
    private JSONObject customParameters = null;

    ArrayList<String> mylist = new ArrayList<String>();


    public PG(Card card, Bill bill, UserDetails userDetails) {
        this.card = card;
        this.bill = bill;
        this.userDetails = userDetails;

        if (TextUtils.isEmpty(card.getCardNumber())) {
            paymenttype = "cardtoken";
        }
        else {
            paymenttype = "card";
        }
    }

    public PG(Bank bank, Bill bill, UserDetails userDetails) {
        this.bank = bank;
        this.bill = bill;
        this.userDetails = userDetails;
        paymenttype = "netbank";
    }
    
    public PG(Prepaid prepaid, Bill bill, UserDetails userDetails) {
         this.bill = bill;
         this.userDetails = userDetails;
         this.prepaid = prepaid;
         paymenttype = "prepaid";
    }

    public void charge(Callback callback) {
        this.callback = callback;

        validate();

    }

    
    public void setCustomParameters(JSONObject customParameters)
    {
    	this.customParameters = customParameters;
    }

    private void validate() {

        if (TextUtils.equals(paymenttype.toString(), "card") || TextUtils.equals(paymenttype.toString(), "cardtoken")) {
            if (TextUtils.isEmpty(card.getCardNumber()) && TextUtils.isEmpty(card.getcardToken())) {
                callback.onTaskexecuted("","Invalid Card or Card token!");
                return;
            }

            if (!TextUtils.isEmpty(card.getCardNumber())) {
                if (!card.validateCard()) {
                    callback.onTaskexecuted("","Invalid Card!");
                    return;
                }
            }
        }



        String access_key = bill.getAccess_key();
        String txn_id = bill.getTxnId();
        String signature = bill.getSignature();
        String returnUrl = bill.getReturnurl();

        String email = userDetails.getEmail();
        String mobile = userDetails.getMobile();
        String firstname = userDetails.getFirstname();
        String lastname = userDetails.getLastname();

        mylist.add(access_key);
        mylist.add(txn_id);
        mylist.add(signature);
        mylist.add(returnUrl);
        mylist.add(email);
        mylist.add(mobile);
        mylist.add(firstname);
        mylist.add(lastname);

        checkifnull();

        formjson();
    }

    private void checkifnull() {
        for (String param : mylist) {
            if (TextUtils.isEmpty(param)) {
                callback.onTaskexecuted("", "Bill or userdetails can not contain empty parameters");
                return;
            }
        }
    }

    private void formjson() {
        JSONObject paymentToken = new JSONObject();
        JSONObject paymentmode;
        if (TextUtils.equals(paymenttype.toString(), "card")){

            paymentmode = new JSONObject();
            try {
                paymentmode.put("cvv", card.getCvvNumber());
                paymentmode.put("holder", card.getCardHolderName());
                paymentmode.put("number", card.getCardNumber());
                paymentmode.put("scheme", card.getCardType());
                paymentmode.put("type", card.getCrdr());
                paymentmode.put("expiry", card.getExpiryMonth() + "/" + card.getExpiryYear());

                paymentToken.put("type","paymentOptionToken");
                paymentToken.put("paymentMode", paymentmode);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onTaskexecuted("", "Problem forming payment Json");
                return;
            }

        }
        
        else if(TextUtils.equals(paymenttype.toString(), "prepaid")) {
        	paymentmode = new JSONObject();
            try {
                paymentmode.put("cvv", "000");
                paymentmode.put("holder", prepaid.getUserEmail());
                paymentmode.put("number", "1234561234561234");
                paymentmode.put("scheme", "CPAY");
                paymentmode.put("type", "prepaid");
                paymentmode.put("expiry", "04/2030");

                paymentToken.put("type","paymentOptionToken");
                paymentToken.put("paymentMode", paymentmode);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onTaskexecuted("", "Problem forming payment Json");
                return;
            }
        }
        
        else if (TextUtils.equals(paymenttype.toString(), "cardtoken")) {
            try {
                paymentToken.put("type","paymentOptionIdToken");
                paymentToken.put("id", card.getcardToken());
                paymentToken.put("cvv", card.getCvvNumber());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            try {
                paymentmode = new JSONObject();
                paymentmode.put("type", "netbanking");
                paymentmode.put("code", bank.getCidnumber());
                paymentToken.put("type", "paymentOptionToken");
                paymentToken.put("paymentMode", paymentmode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject userdetails = new JSONObject();

        JSONObject address = new JSONObject();

        try {
            address.put("state", userDetails.getState());
            address.put("street1", userDetails.getStreet1());
            address.put("street2", userDetails.getStreet2());
            address.put("city", userDetails.getCity());
            address.put("country", userDetails.getCountry());
            address.put("zip", userDetails.getZip());
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onTaskexecuted("", "Problem forming in address Json");
            return;
        }

        try {
            userdetails.put("email", userDetails.getEmail());
            userdetails.put("mobileNo", userDetails.getMobile());
            userdetails.put("firstName", userDetails.getFirstname());
            userdetails.put("lastName", userDetails.getLastname());
            userdetails.put("address", address);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onTaskexecuted("", "Problem forming in userdetails Json");
            return;
        }

        payment = new JSONObject();

        try {
            payment.put("returnUrl", bill.getReturnurl());
            payment.put("amount", bill.getAmount());
            payment.put("merchantAccessKey", bill.getAccess_key());
            
            if(customParameters!=null)
            {
            	payment.put("customParameters", customParameters);
            }
    
            
            payment.put("paymentToken", paymentToken);
            payment.put("merchantTxnId", bill.getTxnId());
            payment.put("requestSignature", bill.getSignature());
            payment.put("userDetails", userdetails);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onTaskexecuted("", "Problem forming in userdetails Json");
            return;
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Content-Type", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new MakePayment(payment, headers, callback).execute();

    }
}
