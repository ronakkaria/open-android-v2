/*
 *
 *    Copyright 2015 Citrus Payment Solutions Pvt. Ltd.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.citrus.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.citrus.retrofit.API;
import com.citrus.retrofit.RetroFitClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.PaymentBill;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by salil on 11/5/15.
 */
public class CitrusClient {

    public static enum Environment {
        SANDBOX {
            public String getBaseUrl() {
                return "https://sandboxadmin.citruspay.com";
            }
        }, PRODUCTION {
            public String getBaseUrl() {
                return "https://admin.citruspay.com";
            }
        };

        public abstract String getBaseUrl();
    }

    private String signinId;
    private String signinSecret;
    private String signupId;
    private String signupSecret;
    private String vanity;

    private String merchantName;
    private Environment environment = Environment.SANDBOX;
    private Amount balanceAmount;
    private static CitrusClient instance = null;
    private Context mContext = null;
    private SharedPreferences mSharedPreferences = null;
    private API retroFitClient = null;

    private CitrusClient() {
    }

    public void init(String signupId, String signupSecret, String signinId, String signinSecret, String vanity, Environment environment) {
        this.signupId = signupId;
        this.signupSecret = signupSecret;
        this.signinId = signinId;
        this.signinSecret = signinSecret;
        this.vanity = vanity;
        this.environment = environment;

        if (validate()) {
            initRetrofitClient();
        }
    }

    private void initRetrofitClient() {
        RetroFitClient.initRetroFitClient(environment.toString());
        retroFitClient = RetroFitClient.getCitrusRetroFitClient();
    }

    public static CitrusClient getInstance(Context context) {
        if (instance == null) {
            synchronized (CitrusClient.class) {
                if (instance == null) {
                    instance = new CitrusClient();
                }
            }
        }

        return instance;
    }

    // Public APIS start

    /**
     * This api will check whether the user is existing user or not. If the user is existing user,
     * then it will return the existing details, else it will create an account internally and
     * then call setPassword to set the password and activate the account.
     *
     * @param emailId
     * @param mobileNo
     * @param callback
     */
    public synchronized void linkUser(String emailId, String mobileNo, Callback<CitrusResponse> callback) {
        // TODO: Implemenation is remaining, need to change the response type as well.
    }

    /**
     * @param emailId
     * @param mobileNo
     * @param password
     * @param callback
     */
    public synchronized void signIn(String emailId, String mobileNo, String password, Callback<CitrusResponse> callback) {

    }

    /**
     * Signout the existing logged in user.
     */
    public synchronized void signOut(Callback<CitrusResponse> callback) {

    }

    /**
     * Set the user password.
     *
     * @param emailId
     * @param mobileNo
     * @param password
     * @param callback
     */
    public synchronized void setPassword(String emailId, String mobileNo, String password, Callback<CitrusResponse> callback) {

    }

    /**
     * Reset the user password. The password reset link will be sent to the user.
     *
     * @param emailId
     * @param mobileNo
     * @param callback
     */
    public synchronized void resetPassword(String emailId, String mobileNo, Callback<CitrusResponse> callback) {

    }

    /**
     * Get the user saved payment options.
     *
     * @param callback
     */
    public synchronized void getWallet(Callback<List<PaymentOption>> callback) {
        /*
         * Get the saved payment options of the user.
         */




    }

    /**
     * Get the balance of the user.
     *
     * @param callback
     */
    public synchronized void getBalance(Callback<Amount> callback) {

    }

    /**
     * Save the paymentOption.
     *
     * @param paymentOption - PaymentOption to be saved.
     * @param callback
     */
    public synchronized void savePaymentOption(PaymentOption paymentOption, Callback<CitrusResponse> callback) {

    }

    /**
     * Get the payment bill for the transaction.
     *
     * @param amount   - Transaction amount
     * @param callback
     */
    public synchronized void getBill(Amount amount, Callback<PaymentBill> callback) {

    }

    /**
     * Send money to your friend.
     *
     * @param toUser   - User to whom to send the money.
     * @param callback
     */
    public synchronized void sendMoney(CitrusUser toUser, Callback<CitrusResponse> callback) {

    }

//    public synchronized void getPrepaidToken()

    public synchronized void getMerchantPaymentOptions(final Callback<MerchantPaymentOption> callback) {
        if (validate()) {
            retroFitClient.getMerchantPaymentOptions(vanity, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement element, Response response) {

                    MerchantPaymentOption merchantPaymentOption = null;

                    if (element.isJsonObject()) {
                        JsonObject paymentOptionObj = element.getAsJsonObject();
                        if (paymentOptionObj != null) {
                            merchantPaymentOption = MerchantPaymentOption.getMerchantPaymentOptions(paymentOptionObj);

                            callback.success(merchantPaymentOption);

                        } else {
                            callback.error(new CitrusError("Error while fetching merchant payment options", CitrusResponse.Status.FAILED));
                        }
                    } else {
                        callback.error(new CitrusError("Invlid json received for merchant payment options", CitrusResponse.Status.FAILED));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.error(new CitrusError(error.getMessage(), CitrusResponse.Status.FAILED));
                }
            });
        }
    }


    // Public APIS end

    private boolean validate() {
        if (!TextUtils.isEmpty(signinId) && !TextUtils.isEmpty(signinSecret)
                && !TextUtils.isEmpty(signupId) && !TextUtils.isEmpty(signupSecret)
                && !TextUtils.isEmpty(vanity)) {
            return true;
        } else {
            throw new IllegalArgumentException("Please make sure SignIn Id, SignIn Secret, SignUp Id, SignUp Secret & Vanity");
        }
    }

    // Getters and setters
    public String getSigninId() {
        return signinId;
    }

    public String getSigninSecret() {
        return signinSecret;
    }

    public String getSignupId() {
        return signupId;
    }

    public String getSignupSecret() {
        return signupSecret;
    }

    public String getVanity() {
        return vanity;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;

        initRetrofitClient();
    }

    public Amount getBalanceAmount() {
        return balanceAmount;
    }

}
