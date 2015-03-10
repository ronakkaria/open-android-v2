package com.citruspay.sdkui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citrus.sdkui.CitrusCash;
import com.citrus.sdkui.NetbankingOption;
import com.citrus.sdkui.PaymentOption;

import java.util.ArrayList;
import java.util.List;

import static com.citruspay.sdkui.PaymentOptionsCardView.PaymentOptionsType;


/**
 * This fragment will handle displaying of all the available payment options.
 * <p/>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentOptionsFragment extends Fragment {

    private PaymentOptionsCardView mCardViewCitrusCash = null;
    private PaymentOptionsCardView mCardViewSavedCards = null;
    private PaymentOptionsCardView mCardViewDebitCreditCards = null;
    private PaymentOptionsCardView mCardViewNetbanking = null;
    private CitrusPaymentParams mPaymentParams = null;
    private OnPaymentOptionSelectedListener mListener;

    public PaymentOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PaymentOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentOptionsFragment newInstance(CitrusPaymentParams paymentParams) {
        PaymentOptionsFragment fragment = new PaymentOptionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.INTENT_EXTRA_PAYMENT_PARAMS, paymentParams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaymentParams = getArguments().getParcelable(Constants.INTENT_EXTRA_PAYMENT_PARAMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_options, container, false);
        mCardViewCitrusCash = (PaymentOptionsCardView) view.findViewById(R.id.cardview_citrus_cash);
        mCardViewSavedCards = (PaymentOptionsCardView) view.findViewById(R.id.cardview_saved_cards);
        mCardViewDebitCreditCards = (PaymentOptionsCardView) view.findViewById(R.id.cardview_debit_credit_cards);
        mCardViewNetbanking = (PaymentOptionsCardView) view.findViewById(R.id.cardview_netbanking);

        PaymentOptionsType debitCreditCardsType = PaymentOptionsType.DEBIT_CREDIT_CARDS;
        debitCreditCardsType.setHeaderText("Credit/Debit Cards");
        debitCreditCardsType.setFooterText("Pay with credit or debit card");
        debitCreditCardsType.setPaymentOptionList(null); //Debit credit card does not have list.
        mCardViewDebitCreditCards.init(debitCreditCardsType, mPaymentParams);

        PaymentOptionsType savedCardsType = PaymentOptionsType.SAVED_CARDS;
        List<PaymentOption> userSavedOptionList = mPaymentParams.userSavedOptionList;
        savedCardsType.setHeaderText("Saved Cards");
        savedCardsType.setFooterText("Add card");
        if (userSavedOptionList != null) {
            savedCardsType.setPaymentOptionList(userSavedOptionList);
        }
        mCardViewSavedCards.init(savedCardsType, mPaymentParams);

        List<NetbankingOption> netbankingOptionList = mPaymentParams.netbankingOptionList;
        if (netbankingOptionList != null && !netbankingOptionList.isEmpty()) {
            PaymentOptionsType netbankingCardType = PaymentOptionsType.NETBANKING;
            netbankingCardType.setHeaderText("Netbanking");
            netbankingCardType.setFooterText("Select Other Bank");
            netbankingCardType.setPaymentOptionList(netbankingOptionList);
            mCardViewNetbanking.init(netbankingCardType, mPaymentParams);
        } else {
            mCardViewNetbanking.setVisibility(View.GONE);
        }

        PaymentOptionsType citrusCashCardsType = PaymentOptionsType.CITRUS_CASH;
        List<CitrusCash> citrusCashList = new ArrayList<>();
        citrusCashList.add(new CitrusCash("1400.00"));
        citrusCashCardsType.setHeaderText("Citrus Cash");
        citrusCashCardsType.setFooterText("Pay Now");
        // TODO: Need to add button for add and pay now.
        citrusCashCardsType.setPaymentOptionList(citrusCashList);
        mCardViewCitrusCash.init(citrusCashCardsType, mPaymentParams);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOptionSelected(null);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPaymentOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
