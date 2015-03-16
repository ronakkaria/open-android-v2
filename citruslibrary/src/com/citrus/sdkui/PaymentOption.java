package com.citrus.sdkui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created by salil on 13/2/15.
 */
public abstract class PaymentOption implements Parcelable {
    /**
     * Following variables will be used in case of tokenized payments and mostly internally.
     * Hence no public constructor with these variables is required. If required create a constructor
     * with default access modifier so as to avoid confusion for the merchant developer.
     */
    protected String name = null; // Denotes the friendly name for the payment option.
    protected String token = null; // Denotes the token for the payment option.
    protected boolean savePaymentOption = false;

    protected PaymentOption() {
    }

    /**
     * @param name  - User friendly name of the payment option. e.g. Debit Card (4242) or Net Banking - ICICI Bank
     * @param token - Token for payment option, used for tokenized payment.
     */
    protected PaymentOption(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public static PaymentOption fromJSONObject(JSONObject walletObject) {

        PaymentOption paymentOption = null;
        String type = walletObject.optString("type");
        String name = walletObject.optString("name");
        String token = walletObject.optString("token");
        String cardHolderName = walletObject.optString("owner");
        String expiry = walletObject.optString("expiryDate");
        String cardNumber = walletObject.optString("number");
        String cardScheme = walletObject.optString("scheme");

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(token)) {
            if (android.text.TextUtils.equals("credit", type)) {
                paymentOption = new CreditCardOption(name, token, cardHolderName, cardNumber, cardScheme, expiry);
            } else if (android.text.TextUtils.equals("debit", type)) {
                paymentOption = new DebitCardOption(name, token, cardHolderName, cardNumber, cardScheme, expiry);
            } else {
                String bankName = walletObject.optString("bank");
                paymentOption = new NetbankingOption(name, token, bankName);
            }
        }

        return paymentOption;
    }

    public void setSavePaymentOption(boolean savePaymentOption) {
        this.savePaymentOption = savePaymentOption;
    }

    public boolean isSavePaymentOption() {
        return savePaymentOption;
    }

    public abstract Drawable getOptionIcon(Context context);

    @Override
    public String toString() {
        return "PaymentOption{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.token);
    }

    private PaymentOption(Parcel in) {
        this.name = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<PaymentOption> CREATOR = new Parcelable.Creator<PaymentOption>() {
        public PaymentOption createFromParcel(Parcel source) {
            return null;
        }

        public PaymentOption[] newArray(int size) {
            return new PaymentOption[size];
        }
    };
}
