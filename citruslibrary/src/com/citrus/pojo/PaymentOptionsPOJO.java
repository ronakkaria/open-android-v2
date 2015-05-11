package com.citrus.pojo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MANGESH KADAM on 5/6/2015.
 */

public class PaymentOptionsPOJO {

    @Expose
    private List<String> creditCard = new ArrayList<String>();
    @Expose
    private List<String> debitCard = new ArrayList<String>();
    @Expose
    private List<NetBankingPOJO> netBanking = new ArrayList<NetBankingPOJO>();

    /**
     *
     * @return
     * The creditCard
     */
    public List<String> getCreditCard() {
        return creditCard;
    }

    /**
     *
     * @param creditCard
     * The creditCard
     */
    public void setCreditCard(List<String> creditCard) {
        this.creditCard = creditCard;
    }

    /**
     *
     * @return
     * The debitCard
     */
    public List<String> getDebitCard() {
        return debitCard;
    }

    /**
     *
     * @param debitCard
     * The debitCard
     */
    public void setDebitCard(List<String> debitCard) {
        this.debitCard = debitCard;
    }

    /**
     *
     * @return
     * The netBanking
     */
    public List<NetBankingPOJO> getNetBanking() {
        return netBanking;
    }

    /**
     *
     * @param netBanking
     * The netBanking
     */
    public void setNetBanking(List<NetBankingPOJO> netBanking) {
        this.netBanking = netBanking;
    }

}