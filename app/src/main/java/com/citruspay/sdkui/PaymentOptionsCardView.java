package com.citruspay.sdkui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdkui.CardOption;
import com.citrus.sdkui.CitrusCash;
import com.citrus.sdkui.CreditCardOption;
import com.citrus.sdkui.DebitCardOption;
import com.citrus.sdkui.NetbankingOption;
import com.citrus.sdkui.PaymentOption;

import java.util.List;

/**
 * Created by salil on 2/3/15.
 */
public class PaymentOptionsCardView extends CardView implements View.OnClickListener {

    private Context mContext = null;
    private TextView mTxtHeader = null;
    private Button mBtnFooter = null;
    private LinearLayout mLayoutPaymentOptions = null;
    ;
    private PaymentOptionsCardType mPaymentOptionsCardType = null;

    public PaymentOptionsCardView(Context context) {
        super(context);

        mContext = context;
    }

    public PaymentOptionsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
    }

    public PaymentOptionsCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
    }

    public void init(PaymentOptionsCardType paymentOptionsCardType) {

        mPaymentOptionsCardType = paymentOptionsCardType;

        inflate(mContext, R.layout.payment_option_cards_layout, this);  //correct way to inflate..
        mTxtHeader = (TextView) findViewById(R.id.txt_card_header);
        mBtnFooter = (Button) findViewById(R.id.btn_footer);
        mBtnFooter.setOnClickListener(this);
        mLayoutPaymentOptions = (LinearLayout) findViewById(R.id.layout_payment_options);

        // Initialize the card view for the particular payment option.
        if (mPaymentOptionsCardType != null) {
            List<? extends PaymentOption> paymentOptionsList = null;

            mTxtHeader.setText(mPaymentOptionsCardType.getHeaderText());
            mBtnFooter.setText(mPaymentOptionsCardType.getFooterText());
            mBtnFooter.setTextColor(Color.parseColor("#F9A323"));
            mBtnFooter.setOnClickListener(this);

            // Initializing the list of payment options
            switch (mPaymentOptionsCardType) {

                case CITRUS_CASH:
                    paymentOptionsList = mPaymentOptionsCardType.getPaymentOptionsList();
                    if (paymentOptionsList != null && !paymentOptionsList.isEmpty()) {
                        CitrusCash citrusCash = (CitrusCash) paymentOptionsList.get(0);

                        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.payment_options_layout, null, false);
                        ((TextView) relativeLayout.findViewById(R.id.txt_payment_name)).setText(citrusCash.getAmount());
                        ((TextView) relativeLayout.findViewById(R.id.txt_payment_name)).setTextColor(Color.parseColor("#77C83E"));

                        ((TextView) relativeLayout.findViewById(R.id.txt_bank_name)).setText("Your Balance");
                        relativeLayout.findViewById(R.id.img_payment_logo).setBackgroundColor(Color.WHITE);
                        // Set the icon image
                        setBackgroundImage(relativeLayout.findViewById(R.id.img_payment_logo), citrusCash.getOptionIcon(mContext));
                        // Set click listener
                        relativeLayout.setOnClickListener(this);
                        mLayoutPaymentOptions.addView(relativeLayout);
                    } else {
                        TextView textView = new TextView(mContext);
                        // TODO: Need to change the message.
                        textView.setText("Seems you do not have Citrus Cash account!");
                        mLayoutPaymentOptions.addView(textView);
                    }
                    break;

                case SAVED_CARDS:
                    paymentOptionsList = mPaymentOptionsCardType.getPaymentOptionsList();
                    if (paymentOptionsList != null && !paymentOptionsList.isEmpty()) {
                        for (int i = 0; i < paymentOptionsList.size(); i++) {

                            PaymentOption paymentOption = paymentOptionsList.get(i);

                            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.payment_options_layout, null, false);

                            if (paymentOption instanceof CreditCardOption || paymentOption instanceof DebitCardOption) {
                                ((TextView) relativeLayout.findViewById(R.id.txt_payment_name)).setText(((CardOption) paymentOption).getCardNumber());
                                ((TextView) relativeLayout.findViewById(R.id.txt_bank_name)).setText(paymentOption.getName());
                            } else if (paymentOption instanceof NetbankingOption) {
                                ((TextView) relativeLayout.findViewById(R.id.txt_payment_name)).setText(paymentOption.getName());
                                ((TextView) relativeLayout.findViewById(R.id.txt_bank_name)).setText(((NetbankingOption) paymentOption).getBankName());
                            }

                            // Set the icon image
                            setBackgroundImage(relativeLayout.findViewById(R.id.img_payment_logo), paymentOption.getOptionIcon(mContext));
                            // Set click listener
                            relativeLayout.setOnClickListener(this);
                            mLayoutPaymentOptions.addView(relativeLayout);
                        }
                    } else {
                        TextView textView = new TextView(mContext);
                        // TODO: Need to change the message.
                        textView.setText("You have not saved any card for faster checkout!");
                        mLayoutPaymentOptions.addView(textView);
                    }
                    break;

                case DEBIT_CREDIT_CARDS:
                    // In case of credit and debit card, there will be no elements in the list.
                    // Hide the layout
                    mLayoutPaymentOptions.setVisibility(View.GONE);
                    break;

                case NETBANKING:
                    paymentOptionsList = mPaymentOptionsCardType.getPaymentOptionsList();
                    if (paymentOptionsList != null && !paymentOptionsList.isEmpty()) {
                        for (int i = 0; i < paymentOptionsList.size(); i++) {
                            NetbankingOption netbankingOption = (NetbankingOption) paymentOptionsList.get(i);

                            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.payment_options_layout, null, false);
                            ((TextView) relativeLayout.findViewById(R.id.txt_payment_name)).setText(netbankingOption.getBankName());
                            // Hide the secondary field in case of banks
                            relativeLayout.findViewById(R.id.txt_bank_name).setVisibility(View.GONE);
                            // Set the icon image
                            setBackgroundImage(relativeLayout.findViewById(R.id.img_payment_logo), netbankingOption.getOptionIcon(mContext));
                            // Set click listener
                            relativeLayout.setOnClickListener(this);
                            mLayoutPaymentOptions.addView(relativeLayout);
                        }
                    }
                    break;
            }
        }
    }

    private void setBackgroundImage(View view, Drawable drawable) {
        if (view != null && drawable != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "Clicked,,,," + v.getId(), Toast.LENGTH_SHORT).show();
    }

    public static enum PaymentOptionsCardType {

        CITRUS_CASH {
            public String getHeaderText() {
                return headerText;
            }

            public String getFooterText() {
                return footerText;
            }

            public List<? extends PaymentOption> getPaymentOptionsList() {
                return paymentOptionList;
            }

            public void setHeaderText(String headerText) {
                this.headerText = headerText;
            }

            public void setFooterText(String footerText) {
                this.footerText = footerText;
            }

            public void setPaymentOptionList(List<? extends PaymentOption> paymentOptionList) {
                this.paymentOptionList = paymentOptionList;
            }
        },
        SAVED_CARDS {
            public String getHeaderText() {
                return headerText;
            }

            public String getFooterText() {
                return footerText;
            }

            public List<? extends PaymentOption> getPaymentOptionsList() {
                return paymentOptionList;
            }

            public void setHeaderText(String headerText) {
                this.headerText = headerText;
            }

            public void setFooterText(String footerText) {
                this.footerText = footerText;
            }

            public void setPaymentOptionList(List<? extends PaymentOption> paymentOptionList) {
                this.paymentOptionList = paymentOptionList;
            }
        },
        DEBIT_CREDIT_CARDS {
            public String getHeaderText() {
                return headerText;
            }

            public String getFooterText() {
                return footerText;
            }

            public List<? extends PaymentOption> getPaymentOptionsList() {
                return paymentOptionList;
            }

            public void setHeaderText(String headerText) {
                this.headerText = headerText;
            }

            public void setFooterText(String footerText) {
                this.footerText = footerText;
            }

            public void setPaymentOptionList(List<? extends PaymentOption> paymentOptionList) {
                this.paymentOptionList = paymentOptionList;
            }
        },
        NETBANKING {
            public String getHeaderText() {
                return headerText;
            }

            public String getFooterText() {
                return footerText;
            }

            public List<? extends PaymentOption> getPaymentOptionsList() {
                return paymentOptionList;
            }

            public void setHeaderText(String headerText) {
                this.headerText = headerText;
            }

            public void setFooterText(String footerText) {
                this.footerText = footerText;
            }

            public void setPaymentOptionList(List<? extends PaymentOption> paymentOptionList) {
                this.paymentOptionList = paymentOptionList;
            }
        };

        protected String headerText = null;
        protected String footerText = null;
        protected List<? extends PaymentOption> paymentOptionList = null;

        public abstract String getHeaderText();

        public abstract void setHeaderText(String headerText);

        public abstract String getFooterText();

        public abstract void setFooterText(String footerText);

        public abstract List<? extends PaymentOption> getPaymentOptionsList();

        public abstract void setPaymentOptionList(List<? extends PaymentOption> paymentOptionList);
    }
}
