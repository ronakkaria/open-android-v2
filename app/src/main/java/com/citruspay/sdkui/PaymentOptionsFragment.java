package com.citruspay.sdkui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citrus.sdkui.CardOption;
import com.citrus.sdkui.CitrusCash;
import com.citrus.sdkui.CreditCardOption;
import com.citrus.sdkui.DebitCardOption;
import com.citrus.sdkui.NetbankingOption;
import com.citrus.sdkui.PaymentOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.citruspay.sdkui.PaymentOptionsCardView.PaymentOptionsCardType;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentOptionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PaymentOptionsCardView mCardViewCitrusCash = null;
    private PaymentOptionsCardView mCardViewSavedCards = null;
    private PaymentOptionsCardView mCardViewDebitCreditCards = null;
    private PaymentOptionsCardView mCardViewNetbanking = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPaymentOptionSelectedListener mListener;

    public PaymentOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentOptionsFragment newInstance(String param1, String param2) {
        PaymentOptionsFragment fragment = new PaymentOptionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        Random random = new Random();

        PaymentOptionsCardType savedCardsType = PaymentOptionsCardType.SAVED_CARDS;
        List<PaymentOption> cardOptionList = new ArrayList<>();
        cardOptionList.add(new DebitCardOption("VISA Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "visa", null));
        cardOptionList.add(new CreditCardOption("MCRD Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "mcrd", null));
        cardOptionList.add(new CreditCardOption("AMEX Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "amex", null));
        cardOptionList.add(new DebitCardOption("JCB Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "jcb", null));
        cardOptionList.add(new CreditCardOption("Discover Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "discover", null));
        cardOptionList.add(new DebitCardOption("Diner Club Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "dinerclub", null));
        cardOptionList.add(new CreditCardOption("Default Card", null, null, "XXXX XXXX XXXX " + random.nextInt(9999), "", null));
        cardOptionList.add(new NetbankingOption("Net Banking - AXIS BANK", "sfsfscsasassccss", "AXIS Bank"));

        savedCardsType.setHeaderText("Saved Cards");
        savedCardsType.setFooterText("Add card");
        savedCardsType.setPaymentOptionList(cardOptionList);
        mCardViewSavedCards.init(savedCardsType);

        PaymentOptionsCardType debitCreditCardsType = PaymentOptionsCardType.DEBIT_CREDIT_CARDS;
        debitCreditCardsType.setHeaderText("Credit/Debit Cards");
        debitCreditCardsType.setFooterText("Pay with credit or debit card");
        debitCreditCardsType.setPaymentOptionList(null); //Debit credit card does not have list.
        mCardViewDebitCreditCards.init(debitCreditCardsType);

        PaymentOptionsCardType netbankingCardType = PaymentOptionsCardType.NETBANKING;
        List<NetbankingOption> netbankingOptionList = new ArrayList<>();
        netbankingOptionList.add(new NetbankingOption("ICICI Bank", null));
        netbankingOptionList.add(new NetbankingOption("AXIS Bank", null));
        netbankingOptionList.add(new NetbankingOption("IDBI Bank", null));
        netbankingOptionList.add(new NetbankingOption("HDFC Bank", null));
        netbankingOptionList.add(new NetbankingOption("My Bank", null));

        netbankingCardType.setHeaderText("Netbanking");
        netbankingCardType.setFooterText("Select Other Bank");
        netbankingCardType.setPaymentOptionList(netbankingOptionList);
        mCardViewNetbanking.init(netbankingCardType);

        PaymentOptionsCardType citrusCashCardsType = PaymentOptionsCardType.CITRUS_CASH;
        List<CitrusCash> citrusCashList = new ArrayList<>();
        citrusCashList.add(new CitrusCash("1400.00"));
        citrusCashCardsType.setHeaderText("Citrus Cash");
        citrusCashCardsType.setFooterText("Pay Now");
        // TODO: Need to add button for add and pay now.
        citrusCashCardsType.setPaymentOptionList(citrusCashList);
        mCardViewCitrusCash.init(citrusCashCardsType);

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
