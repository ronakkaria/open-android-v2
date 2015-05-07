package com.citrus.sdk.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.citrus.sdk.classes.Amount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by salil on 7/5/15.
 */
public class PaymentBill implements Parcelable {

    Amount amount = null;
    String requestSignature = null;
    String merchantTransactionId = null;
    String merchantAccessKey = null;
    String returnUrl = null;
    String notifyUrl = null;
    Map<String, String> customParametersMap = null;

    public PaymentBill(Amount amount, String requestSignature, String merchantTransactionId,
                       String merchantAccessKey, String returnUrl) {
        this.amount = amount;
        this.requestSignature = requestSignature;
        this.merchantTransactionId = merchantTransactionId;
        this.merchantAccessKey = merchantAccessKey;
        this.returnUrl = returnUrl;
    }

    public PaymentBill(Amount amount, String requestSignature, String merchantTransactionId,
                       String merchantAccessKey, String returnUrl, String notifyUrl,
                       Map<String, String> customParametersMap) {
        this.amount = amount;
        this.requestSignature = requestSignature;
        this.merchantTransactionId = merchantTransactionId;
        this.merchantAccessKey = merchantAccessKey;
        this.returnUrl = returnUrl;
        this.notifyUrl = notifyUrl;
        this.customParametersMap = customParametersMap;
    }

    private PaymentBill() {

    }

    public Amount getAmount() {
        return amount;
    }

    public String getRequestSignature() {
        return requestSignature;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public String getMerchantAccessKey() {
        return merchantAccessKey;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public Map<String, String> getCustomParametersMap() {
        return customParametersMap;
    }

    public static PaymentBill fromJSONObject(JSONObject billObject) {
        PaymentBill paymentBill = null;

        if (billObject != null) {
            Amount amount = null;
            String requestSignature = null;
            String merchantTransactionId = null;
            String merchantAccessKey = null;
            String returnUrl = null;
            String notifyUrl = null;
            Map<String, String> customParametersMap = null;


            amount = Amount.fromJSONObject(billObject.optJSONObject("amount"));
            requestSignature = billObject.optString("requestSignature");
            merchantTransactionId = billObject.optString("merchantTxnId");
            merchantAccessKey = billObject.optString("merchantAccessKey");
            returnUrl = billObject.optString("returnUrl");
            notifyUrl = billObject.optString("notifyUrl");

            JSONObject customParamsObject = billObject.optJSONObject("customParameters");
            if (customParamsObject != null) {
//                customParamsObject.
            }

            if (amount != null && requestSignature != null && returnUrl != null
                    && merchantAccessKey != null && merchantTransactionId != null) {

                paymentBill = new PaymentBill(amount, requestSignature, merchantTransactionId,
                        merchantAccessKey, returnUrl, notifyUrl, customParametersMap);
            }
        }

        return paymentBill;
    }

    public static JSONObject toJSONObject(PaymentBill paymentBill) {
        JSONObject billObject = null;

        if (paymentBill != null) {
            Amount amount = paymentBill.getAmount();
            String merchantAccessKey = paymentBill.getMerchantAccessKey();
            String merchantTransactionId = paymentBill.getMerchantTransactionId();
            String requestSignature = paymentBill.getRequestSignature();
            String returnUrl = paymentBill.getReturnUrl();
            String notifyUrl = paymentBill.getNotifyUrl();

            if (amount != null && requestSignature != null && merchantAccessKey != null
                    && merchantTransactionId != null && returnUrl != null) {

                try {
                    billObject = new JSONObject();
                    billObject.put("amount", amount.toJSONObject(amount));
                    billObject.put("merchantTxnId", merchantTransactionId);
                    billObject.put("merchantAccessKey", merchantAccessKey);
                    billObject.put("requestSignature", requestSignature);
                    billObject.put("returnUrl", returnUrl);
                    billObject.put("notifyUrl", notifyUrl);
//                    billObject.put("customParameters")

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return billObject;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.amount, 0);
        dest.writeString(this.requestSignature);
        dest.writeString(this.merchantTransactionId);
        dest.writeString(this.merchantAccessKey);
        dest.writeString(this.returnUrl);
        dest.writeString(this.notifyUrl);
//        dest.writeMap(this.customParametersMap);
    }

    private PaymentBill(Parcel in) {
        this.amount = in.readParcelable(Amount.class.getClassLoader());
        this.requestSignature = in.readString();
        this.merchantTransactionId = in.readString();
        this.merchantAccessKey = in.readString();
        this.returnUrl = in.readString();
        this.notifyUrl = in.readString();
//        this.customParametersMap = in.readMap(customParametersMap, String.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentBill> CREATOR = new Parcelable.Creator<PaymentBill>() {
        public PaymentBill createFromParcel(Parcel source) {
            return new PaymentBill(source);
        }

        public PaymentBill[] newArray(int size) {
            return new PaymentBill[size];
        }
    };
}
