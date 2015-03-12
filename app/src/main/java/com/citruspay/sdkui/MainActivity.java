package com.citruspay.sdkui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.citrus.asynch.InitSDK;
import com.citrus.card.Card;
import com.citrus.interfaces.InitListener;
import com.citrus.mobile.Callback;
import com.citrus.mobile.Config;
import com.citrus.netbank.Bank;
import com.citrus.payment.Bill;
import com.citrus.payment.PG;
import com.citrus.payment.UserDetails;
import com.citrus.sdkui.CardOption;
import com.citrus.sdkui.NetbankingOption;
import com.citrus.sdkui.PaymentOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends ActionBarActivity implements OnPaymentOptionSelectedListener, InitListener {

    private String mUserEmail = null;
    private String mUserMobile = null;
    private String mMerchantVanity = null;
    private String mMerchantName = null;
    private String mMerchantBillUrl = null;
    private double mTransactionAmount = 0.0;
    private ProgressDialog mProgressDialog = null;
    private FragmentManager mFragmentManager = null;
    private CitrusPaymentParams mPaymentParams = null;
    private String mColorPrimary = null;
    private String mColorPrimaryDark = null;
    private ActionBar mActionBar = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mPaymentParams = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_PAYMENT_PARAMS);

        // Get required details from intent.
        if (mPaymentParams != null) {
            mTransactionAmount = mPaymentParams.transactionAmount;

            CitrusUser user = mPaymentParams.user;
            if (user != null) {
                mUserEmail = user.getEmailId();
                mUserMobile = user.getMobileNo();
            }

            mMerchantVanity = mPaymentParams.vanity;
            mMerchantBillUrl = mPaymentParams.billUrl;
            mMerchantName = mPaymentParams.merchantName;

            mColorPrimary = mPaymentParams.colorPrimary;
            mColorPrimaryDark = mPaymentParams.colorPrimaryDark;

            // Set primary color
            if (mColorPrimary != null && mActionBar != null) {
                mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(mColorPrimary)));
            }

            // Set action bar color. Available only on android version Lollipop or higher.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mColorPrimaryDark != null) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(mColorPrimaryDark));
            }

            if (mMerchantName != null) {
                setTitle(mMerchantName + "\t \t " + mTransactionAmount);
            }
        }

        Config.setVanity(mMerchantVanity);

        mProgressDialog = new ProgressDialog(this);
        mFragmentManager = getSupportFragmentManager();

        new InitSDK(this, this, mUserEmail, mUserMobile);

        showDialog("Initializing....", true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dismissDialog();
        mProgressDialog = null;
        mFragmentManager = null;
        mUserEmail = null;
        mUserMobile = null;
        mColorPrimaryDark= null;
        mColorPrimary = null;
        mMerchantVanity = null;
        mMerchantBillUrl = null;
        mPaymentParams = null;
    }

    @Override
    public void onOptionSelected(final PaymentOption paymentOption) {

        if (paymentOption instanceof CardOption) {
            final CardOption cardOption = (CardOption) paymentOption;

            new GetBill(mMerchantBillUrl, mTransactionAmount, new Callback() {
                @Override
                public void onTaskexecuted(String billString, String error) {
                    if (TextUtils.isEmpty(error)) {
                        Bill bill = new Bill(billString);
                        Card card = null;

                        if (!TextUtils.isEmpty(cardOption.getToken())) {
                            // TODO Take the CVV instead of hardcoded value.
                            card = new Card(cardOption.getToken(), "123");
                        } else {
                            card = new Card(cardOption.getCardNumber(), cardOption.getCardExpiryMonth(), cardOption.getCardExpiryYear(), cardOption.getCardCVV(), cardOption.getCardHolderName(), cardOption.getCardType());
                        }

                        UserDetails userDetails = new UserDetails(getCustomer());

                        PG paymentGateway = new PG(card, bill, userDetails);

                        paymentGateway.charge(new Callback() {
                            @Override
                            public void onTaskexecuted(String success, String error) {
                                processresponse(success, error);
                            }
                        });
                    }
                }
            }).execute();
        } else if (paymentOption instanceof NetbankingOption) {
            new GetBill(mMerchantBillUrl, mTransactionAmount, new Callback() {
                @Override
                public void onTaskexecuted(String billString, String error) {
                    Bill bill = new Bill(billString);

                    Bank netbank = new Bank(((NetbankingOption) paymentOption).getBankCID());

                    // TODO Make token payment for bank

                    UserDetails userDetails = new UserDetails(getCustomer());

                    PG paymentgateway = new PG(netbank, bill, userDetails);

                    paymentgateway.charge(new Callback() {
                        @Override
                        public void onTaskexecuted(String success, String error) {
                            processresponse(success, error);
                        }
                    });
                }
            }).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.REQUEST_CODE_PAYMENT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String response = data.getStringExtra(Utils.INTENT_EXTRA_PAYMENT_RESPONSE);
                JSONObject jsonObject = null;
                CitrusTransactionResponse transactionResponse = null;
                try {
                    jsonObject = new JSONObject(response);
                    transactionResponse = CitrusTransactionResponse.fromJSONObject(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null) {
                    transactionResponse = new CitrusTransactionResponse();
                    transactionResponse.setJsonResponse(response);
                }

                Log.d("SDKUI", "Transaction Response : " + transactionResponse.toString());
            }
        }
    }

    private void processresponse(String response, String error) {

        if (!TextUtils.isEmpty(response)) {
            try {

                JSONObject redirect = new JSONObject(response);
                Intent i = new Intent(MainActivity.this, CitrusPaymentActivity.class);

                if (!TextUtils.isEmpty(redirect.getString("redirectUrl"))) {

                    i.putExtra(Utils.INTENT_EXTRA_PAYMENT_URL, redirect.getString("redirectUrl"));
                    startActivityForResult(i, Utils.REQUEST_CODE_PAYMENT_ACTIVITY);
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }

    }


    private List<PaymentOption> getCitrusWalletForUser() {
        List<PaymentOption> citrusWallet = Config.getCitrusWallet();


        return citrusWallet;
    }

    private JSONObject getCustomer() {

        JSONObject customer = null;

		/*
         * All the below mentioned parameters are mandatory - missing anyone of them may create errors Do not change the
		 * key in the json below - only change the values
		 */

        try {
            customer = new JSONObject();
            customer.put("firstName", "Tester");
            customer.put("lastName", "Citrus");
            customer.put("email", "tester@gmail.com");
            customer.put("mobileNo", "9170164284");
            customer.put("street1", "streetone");
            customer.put("street2", "streettwo");
            customer.put("city", "Mumbai");
            customer.put("state", "Maharashtra");
            customer.put("country", "India");
            customer.put("zip", "400052");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public void onSuccess(String response) {

        Log.i("citrus", " success callback ::: " + response);

        dismissDialog();

//        setTitle(mMerchantName);

        mPaymentParams.netbankingOptionList = Config.getBankList();
        mPaymentParams.userSavedOptionList = Config.getCitrusWallet();

        mFragmentManager.beginTransaction()
                .add(R.id.container, PaymentOptionsFragment.newInstance(mPaymentParams))
                .commit();

        dismissDialog();
    }

    @Override
    public void onBindFailed(String response) {
        Log.i("citrus", "onBindFailed");
    }

    @Override
    public void onWalletLoadFailed(String response) {
        Log.i("citrus", "onWalletLoadFailed");
    }

    @Override
    public void onNetBankingListFailed(VolleyError error) {
        Log.i("citrus", "onNetBankingListFailed");
    }

    @Override
    public void onError(Exception e) {
        Log.i("citrus", "onError");
    }

    private void showDialog(String message, boolean cancelable) {
        if (mProgressDialog != null) {
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
