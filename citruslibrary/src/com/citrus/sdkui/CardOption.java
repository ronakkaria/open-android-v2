package com.citrus.sdkui;

import android.text.TextUtils;

/**
 * Created by salil on 13/2/15.
 */
public abstract class CardOption extends PaymentOption {

    private String cardHolderName = null;
    private String cardNumber = null;
    private String cardCVV = null;
    private String cardExpiry = null;
    private String cardExpiryMonth = null;
    private String cardExpiryYear = null;

    /**
     * @param cardHolderName - Name of the card holder.
     * @param cardNumber     - Card number
     * @param cardCVV        - CVV of the card. We do not store CVV at our end.
     * @param cardExpiry     - Expiry date in MM/YY format.
     */
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

    /**
     * This constructor will be used internally, mostly for the cases of tokenized payments.
     *
     * @param name           - User friendly name of the card. e.g. Debit Card (4242) or Credit Card (1234)
     * @param token          - Stored token for Card payment.
     * @param cardHolderName - Name of the card holder.
     * @param cardNumber     - Card number
     * @param cardExpiry     - Card expiry date. In MMYYYY format.
     */
    CardOption(String name, String token, String cardHolderName, String cardNumber, String cardExpiry) {
        super(name, token);
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;

        // The received expiry date is in MMYYYY format so take out expiry month and year which will be required for display purpose.
        if (!TextUtils.isEmpty(cardExpiry)) {
            this.cardExpiryMonth = TextUtils.substring(cardExpiry, 0, 2);
            this.cardExpiryYear = TextUtils.substring(cardExpiry, 2, cardExpiry.length());
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

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Denotes the type of the card. i.e. Credit or Debit.
     */
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

        /**
         * Get the type of the card in string, i.e. debit or credit.
         *
         * @return
         */
        public abstract String getCardType();
    }

}
