package com.citruspay.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public class NetbankingOption implements PaymentOption {

    private String bankName = null;
    private String bankCID = null;

    public NetbankingOption(String bankName, String bankCID) {
        this.bankName = bankName;
        this.bankCID = bankCID;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankCID() {
        return bankCID;
    }
}
