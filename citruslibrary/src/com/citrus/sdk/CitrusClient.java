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
import android.widget.Toast;

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
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.MerchantPaymentOption;
import com.citrus.sdk.payment.PaymentBill;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.response.PaymentResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.citrus.sdk.response.CitrusResponse.*;

/**
 * Created by salil on 11/5/15.
 */
public class CitrusClient {

    public enum Environment {
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
    private static CitrusClient instance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private MerchantPaymentOption merchantPaymentOption = null;

    private API retrofitClient;

    private CitrusClient(Context context) {
        mContext = context;

        initRetrofitClient();
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
        RetroFitClient.initRetroFitClient(environment);
        retrofitClient = RetroFitClient.getCitrusRetroFitClient();
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
     * @param emailId  - emailId of the user
     * @param mobileNo - mobileNo of the user
     * @param callback - callback
     */
    public synchronized void linkUser(final String emailId, final String mobileNo, Callback<CitrusResponse> callback) {
        // TODO: Implemenation is remaining, need to change the response type as well.

        if (validate()) {

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
     * Returns whether the user is loggedin or not.
     *
     * @return returns true if the user is logged in.
     */
    public synchronized boolean isUserLoggedIn() {
        return !TextUtils.isEmpty(getAccessToken());
    }

    /**
     * Get the user saved payment options.
     *
     * @param callback - callback
     */
    public synchronized void getWallet(final Callback<List<PaymentOption>> callback) {
        /*
         * Get the saved payment options of the user.
         */
        if (validate()) {
            if (!isUserLoggedIn()) {
                showToast(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN);
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN, Status.FAILED));

                return;
            }

            retrofitClient.getWallet(getAccessToken(), new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement element, Response response) {
                    if (element != null) {
                        ArrayList<PaymentOption> walletList = new ArrayList<>();
                        try {

                            JSONObject jsonObject = new JSONObject(element.toString());
                            JSONArray paymentOptions = jsonObject.optJSONArray("paymentOptions");

                            if (paymentOptions != null) {
                                for (int i = 0; i < paymentOptions.length(); i++) {
                                    PaymentOption option = PaymentOption.fromJSONObject(paymentOptions.getJSONObject(i));

                                    // Check whether the merchant supports the user's payment option and then only add this payment option.
                                    if (merchantPaymentOption != null) {
                                        Set<CardOption.CardScheme> creditCardSchemeSet = merchantPaymentOption.getCreditCardSchemeSet();
                                        Set<CardOption.CardScheme> debitCardSchemeSet = merchantPaymentOption.getDebitCardSchemeSet();

                                        if (option instanceof CreditCardOption && creditCardSchemeSet != null &&
                                                creditCardSchemeSet.contains(((CreditCardOption) option).getCardScheme())) {
                                            walletList.add(option);
                                        } else if (option instanceof DebitCardOption && debitCardSchemeSet != null &&
                                                debitCardSchemeSet.contains(((DebitCardOption) option).getCardScheme())) {
                                            walletList.add(option);
                                        }
//                                        else if (option instanceof CreditCardOption && cardSchemeSet != null &&
//                                                cardSchemeSet.contains(((CreditCardOption) option).getCardScheme())) {
//                                            walletList.add(option);
//                                        }
                                    } else {
                                        // If the merchant payment options are not found, save all the options.
                                        walletList.add(option);
                                    }

                                }
                            }

                            sendResponse(callback, walletList);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_INVALID_JSON, Status.FAILED));
                        }
                    } else {
                        sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_INVALID_JSON, Status.FAILED));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    sendError(callback, new CitrusError(error.getMessage(), Status.FAILED));
                }
            });
        }
    }

    /**
     * Get the balance of the user.
     *
     * @param callback
     */
    public synchronized void getBalance(final Callback<Amount> callback) {
        if (validate()) {
            if (!isUserLoggedIn()) {
                showToast(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN);
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN, Status.FAILED));

                return;
            }

            retrofitClient.getBalance(getAccessToken(), new retrofit.Callback<Amount>() {
                @Override
                public void success(Amount amount, Response response) {
                    sendResponse(callback, amount);
                }

                @Override
                public void failure(RetrofitError error) {
                    sendError(callback, error);
                }
            });
        }
    }

    /**
     * Save the paymentOption.
     *
     * @param paymentOption - PaymentOption to be saved.
     * @param callback
     */
    public synchronized void savePaymentOption(PaymentOption paymentOption, final Callback<CitrusResponse> callback) {
        if (validate()) {
            if (!isUserLoggedIn()) {
                showToast(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN);
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN, Status.FAILED));

                return;
            }
        }
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
     * @param amount   - Amount to be sent
     * @param toUser   - The user detalis. Enter emailId if send by email or mobileNo if send by mobile.
     * @param message  - Optional message
     * @param callback - Callback
     */
    public synchronized void sendMoney(Amount amount, CitrusUser toUser, String message, final Callback<PaymentResponse> callback) {
        if (validate()) {
            if (!isUserLoggedIn()) {
                showToast(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN);
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_USER_NOT_LOGGED_IN, Status.FAILED));

                return;
            }

            if (amount == null || TextUtils.isEmpty(amount.getValue())) {
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_BLANK_AMOUNT, Status.FAILED));
                return;
            }

            if (toUser == null || (TextUtils.isEmpty(toUser.getEmailId()) && TextUtils.isEmpty(toUser.getMobileNo()))) {
                sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_BLANK_EMAIL_ID_MOBILE_NO, Status.FAILED));
                return;
            }

            retrofit.Callback<PaymentResponse> callbackSendMoney = new retrofit.Callback<PaymentResponse>() {
                @Override
                public void success(PaymentResponse paymentResponse, Response response) {
                    sendResponse(callback, paymentResponse);
                }

                @Override
                public void failure(RetrofitError error) {
                    sendError(callback, error);
                }
            };

            if (!TextUtils.isEmpty(toUser.getEmailId())) {
                retrofitClient.sendMoneyByEmail(getAccessToken(), amount.getValue(), amount.getCurrency(), message, toUser.getEmailId(), callbackSendMoney);
            } else {
                long mobileNo = com.citrus.card.TextUtils.isValidMobileNumber(toUser.getMobileNo());
                if (mobileNo != -1) {
                    retrofitClient.sendMoneyByEmail(getAccessToken(), amount.getValue(), amount.getCurrency(), message, String.valueOf(mobileNo), callbackSendMoney);
                } else {
                    sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_INVALID_MOBILE_NO, Status.FAILED));
                }
            }
        }
    }

//    public synchronized void getPrepaidToken()

    public synchronized void getMerchantPaymentOptions(final Callback<MerchantPaymentOption> callback) {
        if (validate()) {

            retrofitClient.getMerchantPaymentOptions(vanity, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement element, Response response) {

                    MerchantPaymentOption merchantPaymentOption = null;

                    if (element.isJsonObject()) {
                        JsonObject paymentOptionObj = element.getAsJsonObject();
                        if (paymentOptionObj != null) {
                            merchantPaymentOption = MerchantPaymentOption.getMerchantPaymentOptions(paymentOptionObj);

                            saveMerchantPaymentOptions(merchantPaymentOption);

                            sendResponse(callback, merchantPaymentOption);

                        } else {
                            sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_FAILED_MERCHANT_PAYMENT_OPTIONS, Status.FAILED));
                        }
                    } else {
                        sendError(callback, new CitrusError(ResponseMessages.ERROR_MESSAGE_INVALID_JSON, Status.FAILED));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    sendError(callback, error);
                }
            });
        }
    }


    // Public APIS end

    private synchronized boolean validate() {
        if (!TextUtils.isEmpty(signinId) && !TextUtils.isEmpty(signinSecret)
                && !TextUtils.isEmpty(signupId) && !TextUtils.isEmpty(signupSecret)
                && !TextUtils.isEmpty(vanity)) {
            return true;
        } else {
            throw new IllegalArgumentException(ResponseMessages.ERROR_MESSAGE_BLANK_CONFIG_PARAMS);
        }
    }

    /**
     * Returns the access token of the currently logged in user.
     *
     * @return
     */
    private synchronized String getAccessToken() {
        // TODO: Return the current loggedin user token
//        return "Bearer 3fba6c48-2fcf-4b9e-9dc7-9ba6692fb6cf";
        return "Bearer 92d3a132-c881-4c5c-b1d5-02ccb4cb94b4";
    }

    private <T> void sendResponse(Callback callback, T t) {
        if (callback != null) {
            callback.success(t);
        }
    }

    private void sendError(Callback callback, CitrusError error) {
        if (callback != null) {
            callback.error(error);
        }
    }

    private void sendError(Callback callback, RetrofitError error) {
        if (callback != null) {
            callback.error(new CitrusError(error.getMessage(), Status.FAILED));
        }
    }

    private void saveMerchantPaymentOptions(MerchantPaymentOption merchantPaymentOption) {
        this.merchantPaymentOption = merchantPaymentOption;

        // TODO Save these values in DB
    }

    private void showToast(String message) {
        Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Getters and setters.


    public String getSigninId() {
        return signinId;
    }

    public void setSigninId(String signinId) {
        this.signinId = signinId;
    }

    public String getSigninSecret() {
        return signinSecret;
    }

    public void setSigninSecret(String signinSecret) {
        this.signinSecret = signinSecret;
    }

    public String getSignupId() {
        return signupId;
    }

    public void setSignupId(String signupId) {
        this.signupId = signupId;
    }

    public String getSignupSecret() {
        return signupSecret;
    }

    public void setSignupSecret(String signupSecret) {
        this.signupSecret = signupSecret;
    }

    public String getVanity() {
        return vanity;
    }

    public void setVanity(String vanity) {
        this.vanity = vanity;
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
    }

    public Amount getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Amount balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public MerchantPaymentOption getMerchantPaymentOption() {
        return merchantPaymentOption;
    }

    public void setMerchantPaymentOption(MerchantPaymentOption merchantPaymentOption) {
        this.merchantPaymentOption = merchantPaymentOption;
    }
}
