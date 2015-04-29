package com.citrus.sdk.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.classes.Amount;

/**
 * Created by salil on 29/4/15.
 */
public class PaymentResponse extends CitrusResponse implements Parcelable {
    protected String transactionId = null;
    protected Amount transactionAmount = null;
    protected Amount balanceAmount = null;
    protected CitrusUser user = null;
    protected String merchantName = null;
    protected String date = null;

    PaymentResponse() {
        super();
    }

    public PaymentResponse(String message, Status status, String transactionId, Amount transactionAmount, Amount balanceAmount, CitrusUser user) {
        super(message, status);

        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
        this.balanceAmount = balanceAmount;
        this.user = user;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Amount getTransactionAmount() {
        return transactionAmount;
    }

    public Amount getBalanceAmount() {
        return balanceAmount;
    }

    public CitrusUser getUser() {
        return user;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getDate() {
        return date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transactionId);
        dest.writeParcelable(this.transactionAmount, 0);
        dest.writeParcelable(this.balanceAmount, 0);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.merchantName);
        dest.writeString(this.date);
        dest.writeString(this.message);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    private PaymentResponse(Parcel in) {
        this.transactionId = in.readString();
        this.transactionAmount = in.readParcelable(Amount.class.getClassLoader());
        this.balanceAmount = in.readParcelable(Amount.class.getClassLoader());
        this.user = in.readParcelable(CitrusUser.class.getClassLoader());
        this.merchantName = in.readString();
        this.date = in.readString();
        this.message = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : Status.values()[tmpStatus];
    }

    public static final Parcelable.Creator<PaymentResponse> CREATOR = new Parcelable.Creator<PaymentResponse>() {
        public PaymentResponse createFromParcel(Parcel source) {
            return new PaymentResponse(source);
        }

        public PaymentResponse[] newArray(int size) {
            return new PaymentResponse[size];
        }
    };
}
