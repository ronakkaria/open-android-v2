package com.citrus.sdkui;

/**
 * Created by salil on 13/2/15.
 */
public class DebitCardOption extends CardOption {

    /**
     * @param cardHolderName - Name of the card holder.
     * @param cardNumber     - Card number
     * @param cardCVV        - CVV of the card. We do not store CVV at our end.
     * @param cardExpiry     - Expiry date in MM/YY format.
     */
    public DebitCardOption(String cardHolderName, String cardNumber, String cardCVV, String cardExpiry) {
        super(cardHolderName, cardNumber, cardCVV, cardExpiry);
    }

    /**
     * This constructor will be used internally, mostly for the cases of tokenized payments.
     *
     * @param name           - User friendly name of the card. e.g. Debit Card (4242) or Credit Card (1234)
     * @param token          - Stored token for Card payment.
     * @param cardHolderName - Name of the card holder.
     * @param cardNumber     - Card number
     * @param cardScheme     - Card scheme e.g. VISA, MASTER etc.
     * @param cardExpiry     - Card expiry date. In MMYYYY format.
     */
    public DebitCardOption(String name, String token, String cardHolderName, String cardNumber, String cardScheme, String cardExpiry) {
        super(name, token, cardHolderName, cardNumber, cardScheme, cardExpiry);
    }

    @Override
    public String getCardType() {
        return CardType.DEBIT.getCardType();
    }
}
