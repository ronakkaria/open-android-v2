package com.citruspay.sdkui;

import android.text.TextUtils;

/**
 * Created by salil on 13/2/15.
 */
public abstract class CardOption implements PaymentOption {

    public static enum CardType {
        DEBIT {
            public String getCardType() {
                return "debit";
            }
        }, CREDIT {
            public String getCardType() {
                return "credit";
            }
        };

        public abstract String getCardType();
    }

    private String cardHolderName = null;
    private String cardNumber = null;
    private String cardCVV = null;
    private String cardExpiry = null;
    private String cardExpiryMonth = null;
    private String cardExpiryYear = null;

    public CardOption(String cardHolderName, String cardNumber, String cardCVV, String cardExpiry) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardExpiry = cardExpiry;

        if (!TextUtils.isEmpty(cardExpiry)) {
            String[] strArr = cardExpiry.split("/");
            if (strArr != null && strArr.length == 2) {
                cardExpiryMonth = strArr[0];
                cardExpiryYear = strArr[1];
            }
        }
    }

    public abstract String getCardType();


    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardExpiryYear() {
        return cardExpiryYear;
    }

    public String getCardExpiryMonth() {

        return cardExpiryMonth;
    }

    public String getCardCVV() {

        return cardCVV;
    }

    public String getCardNumber() {
        return cardNumber;
    }

}
