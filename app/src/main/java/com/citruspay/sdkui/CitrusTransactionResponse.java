package com.citruspay.sdkui;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by salil on 16/2/15.
 */
public class CitrusTransactionResponse implements Parcelable {

    public static final Parcelable.Creator<CitrusTransactionResponse> CREATOR = new Parcelable.Creator<CitrusTransactionResponse>() {
        public CitrusTransactionResponse createFromParcel(Parcel source) {
            return new CitrusTransactionResponse(source);
        }

        public CitrusTransactionResponse[] newArray(int size) {
            return new CitrusTransactionResponse[size];
        }
    };
    private String amount = null;
    private String currency = null;
    private String message = null;
    private String responseCode = null;
    private TransactionStatus transactionStatus = null;
    private TransactionDetails transactionDetails = null;
    private CitrusUser citrusUser = null;
    private PaymentMode paymentMode = null;
    private String issuerCode = null;
    private String impsMobileNumber = null;
    private String impsMmid = null;
    private String authIdCode = null;
    private String signature = null;
    private boolean COD = false; // Cash On Delivery
    private Map<String, String> customParamsMap = null;
    private String jsonResponse = null;

    public CitrusTransactionResponse() {

    }

    public CitrusTransactionResponse(String amount, String currency, String message, String responseCode, TransactionStatus transactionStatus, TransactionDetails transactionDetails, CitrusUser citrusUser, PaymentMode paymentMode, String issuerCode, String impsMobileNumber, String impsMmid, String authIdCode, String signature, boolean COD, Map<String, String> customParamsMap) {
        this.amount = amount;
        this.currency = currency;
        this.message = message;
        this.responseCode = responseCode;
        this.transactionStatus = transactionStatus;
        this.transactionDetails = transactionDetails;
        this.citrusUser = citrusUser;
        this.paymentMode = paymentMode;
        this.issuerCode = issuerCode;
        this.impsMobileNumber = impsMobileNumber;
        this.impsMmid = impsMmid;
        this.authIdCode = authIdCode;
        this.signature = signature;
        this.COD = COD;
        this.customParamsMap = customParamsMap;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public CitrusUser getCitrusUser() {
        return citrusUser;
    }

    private CitrusTransactionResponse(Parcel in) {
        this.amount = in.readString();
        this.currency = in.readString();
        this.message = in.readString();
        this.responseCode = in.readString();
        int tmpTransactionStatus = in.readInt();
        this.transactionStatus = tmpTransactionStatus == -1 ? null : TransactionStatus.values()[tmpTransactionStatus];
        this.transactionDetails = in.readParcelable(TransactionDetails.class.getClassLoader());
        this.citrusUser = in.readParcelable(CitrusUser.class.getClassLoader());
        int tmpPaymentMode = in.readInt();
        this.paymentMode = tmpPaymentMode == -1 ? null : PaymentMode.values()[tmpPaymentMode];
        this.issuerCode = in.readString();
        this.impsMobileNumber = in.readString();
        this.impsMmid = in.readString();
        this.authIdCode = in.readString();
        this.signature = in.readString();
        this.COD = in.readByte() != 0;
//        this.customParamsMap = in.readParcelable(HashMap<String, String>.class.getClassLoader());
        this.jsonResponse = in.readString();
    }

    public static CitrusTransactionResponse fromJSONObject(JSONObject response) {
        CitrusTransactionResponse citrusTransactionResponse = null;

        if (response != null) {
            PaymentMode paymentMode = PaymentMode.getPaymentMode(response.optString("paymentMode"));
            TransactionStatus transactionStatus = TransactionStatus.getTransactionStatus(response.optString("TxStatus"));
            String currency = response.optString("currency");
            String amount = response.optString("amount");
            String responseCode = response.optString("pgRespCode");
            String message = response.optString("TxMsg");
            String isCOD = response.optString("isCOD");
            String signature = response.optString("signature");
            String issuerCode = response.optString("issuerCode");
            String impsMmid = response.optString("impsMmid");
            String impsMobileNumber = response.optString("impsMobileNumber");
            String authIdCode = response.optString("authIdCode");
            // TODO Need to parse custom parameters
            Map<String, String> customParamsMap = null;

            TransactionDetails transactionDetails = TransactionDetails.fromJSONObject(response);
            CitrusUser citrusUser = CitrusUser.fromJSONObject(response);
            boolean cod = "true".equalsIgnoreCase(isCOD) ? true : false;

            citrusTransactionResponse = new CitrusTransactionResponse(amount, currency, message, responseCode, transactionStatus, transactionDetails, citrusUser, paymentMode, issuerCode, impsMobileNumber, impsMmid, authIdCode, signature, cod, customParamsMap);
            citrusTransactionResponse.setJsonResponse(response.toString());

        }
        return citrusTransactionResponse;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMessage() {
        return message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public boolean isCOD() {
        return COD;
    }

    public String getImpsMobileNumber() {
        return impsMobileNumber;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public String getImpsMmid() {
        return impsMmid;
    }

    public String getAuthIdCode() {
        return authIdCode;
    }

    public String getSignature() {
        return signature;
    }

    public Map<String, String> getCustomParamsMap() {
        return customParamsMap;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String toString() {
        return "CitrusTransactionResponse{" +
                "amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", message='" + message + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", transactionDetails=" + transactionDetails +
                ", citrusUser=" + citrusUser +
                ", paymentMode=" + paymentMode +
                ", issuerCode='" + issuerCode + '\'' +
                ", impsMobileNumber='" + impsMobileNumber + '\'' +
                ", impsMmid='" + impsMmid + '\'' +
                ", authIdCode='" + authIdCode + '\'' +
                ", signature='" + signature + '\'' +
                ", COD=" + COD +
                ", customParamsMap=" + customParamsMap +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.message);
        dest.writeString(this.responseCode);
        dest.writeInt(this.transactionStatus == null ? -1 : this.transactionStatus.ordinal());
        dest.writeParcelable(this.transactionDetails, flags);
        dest.writeParcelable(this.citrusUser, 0);
        dest.writeInt(this.paymentMode == null ? -1 : this.paymentMode.ordinal());
        dest.writeString(this.issuerCode);
        dest.writeString(this.impsMobileNumber);
        dest.writeString(this.impsMmid);
        dest.writeString(this.authIdCode);
        dest.writeString(this.signature);
        dest.writeByte(COD ? (byte) 1 : (byte) 0);
//        dest.writeParcelable(this.customParamsMap, flags);
        dest.writeString(this.jsonResponse);
    }

    public static enum PaymentMode {
        NET_BANKING, CREDIT_CARD, DEBIT_CARD;

        public static PaymentMode getPaymentMode(String paymentMode) {
            PaymentMode mode = null;
            if (TextUtils.equals(paymentMode, "NET_BANKING")) {
                mode = NET_BANKING;
            } else if (TextUtils.equals(paymentMode, "CREDIT_CARD")) {
                mode = CREDIT_CARD;
            } else if (TextUtils.equals(paymentMode, "DEBIT_CARD")) {
                mode = DEBIT_CARD;
            }

            return mode;
        }
    }

    public static enum TransactionStatus {
        SUCCESS, FAIL;

        public static TransactionStatus getTransactionStatus(String transactionStatus) {
            TransactionStatus status = null;
            if (TextUtils.equals(transactionStatus, "STATUS")) {
                status = SUCCESS;
            } else if (TextUtils.equals(transactionStatus, "FAIL")) {
                status = FAIL;
            }

            return status;
        }
    }

    public static class TransactionDetails implements Parcelable {
        public static final Creator<TransactionDetails> CREATOR = new Creator<TransactionDetails>() {
            public TransactionDetails createFromParcel(Parcel source) {
                return new TransactionDetails(source);
            }

            public TransactionDetails[] newArray(int size) {
                return new TransactionDetails[size];
            }
        };
        private String transactionId = null;
        private String txRefNo = null;
        private String pgTxnNo = null;
        private String issuerRefNo = null;
        private String transactionGateway = null;
        private String transactionDateTime = null;

        public TransactionDetails(String transactionId, String txRefNo, String pgTxnNo, String issuerRefNo, String transactionGateway, String transactionDateTime) {
            this.transactionId = transactionId;
            this.txRefNo = txRefNo;
            this.pgTxnNo = pgTxnNo;
            this.issuerRefNo = issuerRefNo;
            this.transactionGateway = transactionGateway;
            this.transactionDateTime = transactionDateTime;
        }

        private TransactionDetails(Parcel in) {
            this.transactionId = in.readString();
            this.txRefNo = in.readString();
            this.pgTxnNo = in.readString();
            this.issuerRefNo = in.readString();
            this.transactionGateway = in.readString();
            this.transactionDateTime = in.readString();
        }

        public static TransactionDetails fromJSONObject(JSONObject response) {
            TransactionDetails transactionDetails = null;

            if (response != null) {
                String txRefNo = response.optString("TxRefNo");
                String pgTxnNo = response.optString("pgTxnNo");
                String issuerRefNo = response.optString("issuerRefNo");
                String txGateway = response.optString("TxGateway");
                String txnDateTime = response.optString("txnDateTime");
                String txId = response.optString("txId");

                transactionDetails = new TransactionDetails(txId, txRefNo, pgTxnNo, issuerRefNo, txGateway, txnDateTime);
            }

            return transactionDetails;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getTxRefNo() {
            return txRefNo;
        }

        public String getPgTxnNo() {
            return pgTxnNo;
        }

        public String getIssuerRefNo() {
            return issuerRefNo;
        }

        public String getTransactionGateway() {
            return transactionGateway;
        }

        public String getTransactionDateTime() {
            return transactionDateTime;
        }

        @Override
        public String toString() {
            return "TransactionDetails{" +
                    "transactionId='" + transactionId + '\'' +
                    ", txRefNo='" + txRefNo + '\'' +
                    ", pgTxnNo='" + pgTxnNo + '\'' +
                    ", issuerRefNo='" + issuerRefNo + '\'' +
                    ", transactionGateway='" + transactionGateway + '\'' +
                    ", transactionDateTime='" + transactionDateTime + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.transactionId);
            dest.writeString(this.txRefNo);
            dest.writeString(this.pgTxnNo);
            dest.writeString(this.issuerRefNo);
            dest.writeString(this.transactionGateway);
            dest.writeString(this.transactionDateTime);
        }
    }
}
