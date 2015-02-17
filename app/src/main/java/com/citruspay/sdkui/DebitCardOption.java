package com.citruspay.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public class DebitCardOption extends CardOption {

    public DebitCardOption(String cardHolderName, String cardNumber, String cardCVV, String cardExpiry) {
        super(cardHolderName, cardNumber, cardCVV, cardExpiry);
    }

    DebitCardOption(String name, String token, String cardHolderName, String cardNumber, String cardExpiry) {
        super(name, token, cardHolderName, cardNumber, cardExpiry);
    }

    @Override
    public String getCardType() {
        return CardType.CREDIT.getCardType();
    }
}
