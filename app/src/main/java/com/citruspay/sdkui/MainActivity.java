package com.citruspay.sdkui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.citrus.card.Card;
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
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, OnPaymentOptionSelectedListener {

    private static final String BILL_URL = "http://192.168.1.5:8080/billGenerator.orig.jsp";// host your bill url here
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private double mTransactionAmount = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mTransactionAmount = getIntent().getDoubleExtra("MERCHANT_TRANSACTION_AMOUNT", 2.0);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onOptionSelected(final PaymentOption paymentOption) {

        if (paymentOption instanceof CardOption) {
            final CardOption cardOption = (CardOption) paymentOption;

            new GetBill(BILL_URL, mTransactionAmount, new Callback() {
                @Override
                public void onTaskexecuted(String billString, String error) {
                    if (TextUtils.isEmpty(error)) {
                        Bill bill = new Bill(billString);

                        Card card = new Card(cardOption.getCardNumber(), cardOption.getCardExpiryMonth(), cardOption.getCardExpiryYear(), cardOption.getCardCVV(), cardOption.getCardHolderName(), cardOption.getCardType());

                        UserDetails userDetails = new UserDetails(getCustomer());

                        PG paymentgateway = new PG(card, bill, userDetails);

                        paymentgateway.charge(new Callback() {
                            @Override
                            public void onTaskexecuted(String success, String error) {
                                processresponse(success, error);
                            }
                        });
                    }
                }
            }).execute();
        } else if (paymentOption instanceof NetbankingOption) {
            new GetBill(BILL_URL, mTransactionAmount, new Callback() {
                @Override
                public void onTaskexecuted(String billString, String error) {
                    Bill bill = new Bill(billString);

                    Bank netbank = new Bank(((NetbankingOption) paymentOption).getBankCID());

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CitrusWalletFragment.newInstance(getCitrusWalletForUser());
                case 1:
                    return CardPaymentFragment.newInstance();
                case 2:
                    return NetbankingPaymentFragment.newInstance(Config.getBankList());
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
