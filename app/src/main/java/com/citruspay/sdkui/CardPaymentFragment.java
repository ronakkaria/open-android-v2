package com.citruspay.sdkui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.citrus.sdkui.CardOption;

import static com.citrus.sdkui.CardOption.CardType.CREDIT;
import static com.citrus.sdkui.CardOption.CardType.DEBIT;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.citruspay.sdkui.OnPaymentOptionSelectedListener} interface
 * to handle interaction events.
 * Use the {@link CardPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPaymentFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private OnPaymentOptionSelectedListener mListener = null;
    private RadioGroup mRadioGroup = null;
    private Button mButtonPay = null;
    private EditText mEditNameOnCard = null;
    private EditText mEditCardNo = null;
    private EditText mEditCVV = null;
    private EditText mEditExpiry = null;
    private CardOption.CardType mCardType = null;

    Spinner month;
    Spinner year;

    public CardPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CardPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardPaymentFragment newInstance() {
        return new CardPaymentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_card_paymentnew, container, false);
        month = (Spinner) rootView.findViewById(R.id.month);
        ArrayAdapter<CharSequence> monthAdapter = new ArrayAdapter<CharSequence>(
                getActivity(),R.layout.customtextview,android.R.id.text1,getResources().getStringArray(R.array.months_array));

        // Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        month.setAdapter(monthAdapter);
        year = (Spinner) rootView.findViewById(R.id.year);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(
                getActivity(),R.layout.customtextview,android.R.id.text1,getResources().getStringArray(R.array.years_array));
        // Specify the layout to use when the list of choices appears
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        year.setAdapter(yearAdapter);
        /*mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupCardType);
        mEditCardNo = (EditText) rootView.findViewById(R.id.editCardNo);
        mEditNameOnCard = (EditText) rootView.findViewById(R.id.editNameOnCard);
        mEditExpiry = (EditText) rootView.findViewById(R.id.editExpiryDate);
        mEditCVV = (EditText) rootView.findViewById(R.id.editCVV);
        mButtonPay = (Button) rootView.findViewById(R.id.buttonPay);

        mRadioGroup.setOnCheckedChangeListener(this);
        mButtonPay.setOnClickListener(this);*/

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPaymentOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPaymentOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
     /*   String cardName = mEditNameOnCard.getText().toString();
        String cardNumber = mEditCardNo.getText().toString();
        String cardCVV = mEditCVV.getText().toString();
        String cardExpiry = mEditExpiry.getText().toString();*/

        Toast.makeText(getActivity(), "Pay Button Clicked...", Toast.LENGTH_SHORT).show();

        CardOption cardOption = null;

        switch (mCardType) {
            case DEBIT:
             //   cardOption = new DebitCardOption(cardName, cardNumber, cardCVV, cardExpiry);
                break;

            case CREDIT:
             //   cardOption = new CreditCardOption(cardName, cardNumber, cardCVV, cardExpiry);
                break;
        }

        mListener.onOptionSelected(cardOption);

        Log.i("Citrus", "Card No. " + mEditCardNo.getText() + "  Card Name : " + mEditNameOnCard.getText() + " Expiry : " + mEditExpiry.getText() + " CVV : " + mEditCVV.getText() + " Card Type : " + mCardType);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioDebitCard:
                mCardType = DEBIT;
                break;
            case R.id.radioCreditCard:
                mCardType = CREDIT;
                break;
        }
    }
}
