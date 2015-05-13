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
import android.util.Log;

import com.citrus.citrususer.RandomPassword;
import com.citrus.mobile.Config;
import com.citrus.mobile.OAuth2GrantType;
import com.citrus.mobile.OauthToken;
import com.citrus.pojo.AccessTokenPOJO;
import com.citrus.pojo.BindPOJO;
import com.citrus.retrofit.API;
import com.citrus.retrofit.RetroFitClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.payment.CardOption;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.PaymentBill;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import com.orhanobut.logger.Logger;

import java.util.List;

import com.citrus.sdk.Callback;

import org.json.JSONException;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.citrus.sdk.response.CitrusResponse.*;

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


    public static final String SIGNIN_TOKEN = "signin_token";

    public static final String SIGNUP_TOKEN = "signup_token";

    public static final String PREPAID_TOKEN = "prepaid_token";


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

    private API retrofitClient;

    private CitrusClient(Context context) {
        mContext = context;
        RetroFitClient.initRetroFitClient(environment.toString());
        retrofitClient = RetroFitClient.getCitrusRetroFitClient();
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
                    instance = new CitrusClient(context);
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
    public synchronized void linkUser(final String emailId, final String mobileNo, Callback<CitrusResponse> callback) {
        // TODO: Implemenation is remaining, need to change the response type as well.

        final Callback<CitrusResponse> callbackApp = callback;
        retrofitClient.getSignUpToken(Config.getSignupId(), Config.getSignupSecret(), OAuth2GrantType.implicit.toString(), new retrofit.Callback<AccessTokenPOJO>() {
            @Override
            public void success(AccessTokenPOJO accessTokenPOJO, Response response) {
                Logger.d("accessTokenPOJO " + accessTokenPOJO.getJSON().toString());

                if (accessTokenPOJO.getAccessToken() != null) {
                    OauthToken signuptoken = new OauthToken(mContext, SIGNUP_TOKEN);
                    signuptoken.createToken(accessTokenPOJO.getJSON()); //Oauth Token received
                    String header = "Bearer " + accessTokenPOJO.getAccessToken();

                    retrofitClient.getBindResponse(header, emailId, mobileNo, new retrofit.Callback<BindPOJO>() {
                        @Override
                        public void success(BindPOJO bindPOJO, Response response) {
                            Logger.d("BIND RESPONSE " + bindPOJO.getUsername());

                            if (bindPOJO.getUsername() != null) {
                                retrofitClient.getSignInToken(Config.getSigninId(), Config.getSigninSecret(), bindPOJO.getUsername(), OAuth2GrantType.username.toString(), new retrofit.Callback<AccessTokenPOJO>() {
                                    @Override
                                    public void success(AccessTokenPOJO accessTokenPOJO, Response response) {
                                        Logger.d("SIGNIN accessToken" + accessTokenPOJO.getJSON().toString());
                                        if (accessTokenPOJO.getAccessToken() != null) {
                                            OauthToken token = new OauthToken(mContext, SIGNIN_TOKEN);
                                            token.createToken(accessTokenPOJO.getJSON());

                                            RandomPassword pwd = new RandomPassword();

                                            String random_pass = pwd.generate(emailId, mobileNo);

                                            retrofitClient.getSignInWithPasswordResponse(Config.getSigninId(), Config.getSigninSecret(), emailId, random_pass, OAuth2GrantType.password.toString(), new retrofit.Callback<AccessTokenPOJO>() {
                                                @Override
                                                public void success(AccessTokenPOJO accessTokenPOJO, Response response) {
                                                    Logger.d("SET PWD RESPONSE" + accessTokenPOJO.getJSON().toString());
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    Logger.d("SETPWD ERROR **" + error.getMessage());
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        callbackApp.error(new CitrusError(error.getMessage(), Status.FAILED));
                                    }
                                });
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });

                }

            }

            @Override
            public void failure(RetrofitError error) {
                callbackApp.error(new CitrusError(error.getMessage(), Status.FAILED));
            }
        });


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

        retroFitClient.getWallet("Bearer 3fba6c48-2fcf-4b9e-9dc7-9ba6692fb6cf", new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement element, Response response) {
                Log.d("Citrus", "asString : " + element.getAsString() + " toString : " + element.toString());

//                ArrayList<PaymentOption> walletList = new ArrayList<>();
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray paymentOptions = jsonObject.optJSONArray("paymentOptions");
//
//                    if (paymentOptions != null) {
//                        for (int i = 0; i < paymentOptions.length(); i++) {
//                            PaymentOption option = PaymentOption.fromJSONObject(paymentOptions.getJSONObject(i));
//                            walletList.add(option);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


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
                            callback.error(new CitrusError("Error while fetching merchant payment options", Status.FAILED));
                        }
                    } else {
                        callback.error(new CitrusError("Invlid json received for merchant payment options", Status.FAILED));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.error(new CitrusError(error.getMessage(), Status.FAILED));
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
