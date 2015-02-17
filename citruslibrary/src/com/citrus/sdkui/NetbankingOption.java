package com.citrus.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public class NetbankingOption extends PaymentOption {

    private String bankName = null;
    private String bankCID = null;

    public NetbankingOption(String bankName, String bankCID) {
        this.bankName = bankName;
        this.bankCID = bankCID;
    }

    NetbankingOption(String name, String token, String bankName) {
        super(name, token);
        this.bankName = bankName;
    }


    public String getBankName() {
        return bankName;
    }

    public String getBankCID() {
        return bankCID;
    }

    @Override
    public String toString() {
        return bankName;
    }
}
