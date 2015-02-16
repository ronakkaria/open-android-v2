package com.citruspay.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public class CreditCardOption extends CardOption {


    public CreditCardOption(String cardHolderName, String cardNumber, String cardCVV, String cardExpiry) {
        super(cardHolderName, cardNumber, cardCVV, cardExpiry);
    }

    @Override
    public String getCardType() {
        return CardType.DEBIT.getCardType();
    }
}
