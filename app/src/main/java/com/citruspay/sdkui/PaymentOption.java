package com.citruspay.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public abstract class PaymentOption {
    /**
     * Following variables will be used in case of tokenized payments and mostly internally.
     * Hence no public constructor with these variables is required. If required create a constructor
     * with default access modifier so as to avoid confusion for the merchant developer.
     */
    protected String name = null; // Denotes the friendly name for the payment option.
    protected String token = null; // Denotes the token for the payment option.

    protected PaymentOption() {
    }

    /**
     *
     * @param name - User friendly name of the payment option. e.g. Debit Card (4242) or Net Banking - ICICI Bank
     * @param token - Token for payment option, used for tokenized payment.
     */
    PaymentOption(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
