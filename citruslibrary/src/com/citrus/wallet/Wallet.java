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
package com.citrus.wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.citrus.card.Card;
import com.citrus.mobile.Config;
import com.citrus.mobile.OauthToken;
import com.citrus.mobile.RESTclient;

/**
 * Created by shardul on 19/11/14.
 */
public class Wallet {
    private Card card;

    public Wallet(Card card) {
        this.card = card;
        base_url = Config.getEnv();
    }

    private String base_url;

    public Wallet() {
        base_url = Config.getEnv();
    }

    public String saveCard(Activity activity) {

        OauthToken token = new OauthToken(activity);
        String access_token = null;

        try {
            access_token = token.getuserToken().getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JSONObject cardJson = new JSONObject();
        JSONObject cardDetails = new JSONObject();


        try {
            cardJson.put("type", "payment");
            cardDetails.put("owner", card.getCardHolderName());
            cardDetails.put("number", card.getCardNumber());
            cardDetails.put("scheme", card.getCardType());
            cardDetails.put("type", card.getCrdr().toLowerCase());
            cardDetails.put("expiryDate", card.getExpiryMonth() + "/" + card.getExpiryYear().substring(card.getExpiryYear().length()-2));

            JSONArray array = new JSONArray();
            array.put(cardDetails);

            cardJson.put("paymentOptions", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RESTclient resTclient = new RESTclient("wallet",base_url, null, headers);

        JSONObject response = resTclient.makePutrequest(cardJson);

        if (response == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("response", "card saved successfully!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        return response.toString();

    }

    public String getWallet(Activity activity) {

        OauthToken token = new OauthToken(activity);
        String access_token = null;

        try {
            access_token = token.getuserToken().getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject headers = new JSONObject();

        try {
            headers.put("Authorization", "Bearer " + access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RESTclient resTclient = new RESTclient("wallet", base_url, null, headers);

        JSONObject response = resTclient.makegetRequest();

        return response.toString();
    }
}
