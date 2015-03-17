package com.citruspay.sdkui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.citrus.sdkui.CardOption;
import com.citrus.sdkui.CreditCardOption;
import com.citrus.sdkui.DebitCardOption;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.citruspay.sdkui.OnPaymentOptionSelectedListener} interface
 * to handle interaction events.
 * Use the {@link CardPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPaymentFragment extends Fragment implements View.OnClickListener {
    private OnCardPaymentListener mListener = null;
    private RadioGroup mRadioGroup = null;
    private Button mButtonPay = null;
    private EditText mEditNameOnCard = null;
    private EditText mEditCardNo = null;
    private EditText mEditCVV = null;
    private Spinner mSpinnerMonth = null;
    private Spinner mSpinnerYear = null;

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

        View rootView = inflater.inflate(R.layout.fragment_card_payment, container, false);
        mSpinnerMonth = (Spinner) rootView.findViewById(R.id.month);
        ArrayAdapter<CharSequence> monthAdapter = new ArrayAdapter<CharSequence>(
                getActivity(), R.layout.customtextview, android.R.id.text1, getResources().getStringArray(R.array.months_array));

        // Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerMonth.setAdapter(monthAdapter);
        mSpinnerYear = (Spinner) rootView.findViewById(R.id.year);
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(
                getActivity(), R.layout.customtextview, android.R.id.text1, getResources().getStringArray(R.array.years_array));
        // Specify the layout to use when the list of choices appears
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerYear.setAdapter(yearAdapter);
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_card_type);
        mEditCardNo = (EditText) rootView.findViewById(R.id.edit_card_no);
        mEditNameOnCard = (EditText) rootView.findViewById(R.id.edit_name_on_card);
        mEditCVV = (EditText) rootView.findViewById(R.id.edit_cvv);
        mButtonPay = (Button) rootView.findViewById(R.id.button_pay);

        mButtonPay.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCardPaymentListener) activity;
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
        String cardName = mEditNameOnCard.getText().toString();
        String cardNumber = mEditCardNo.getText().toString();
        String cardCVV = mEditCVV.getText().toString();
        String cardExpiryMonth = String.valueOf(mSpinnerMonth.getSelectedItemPosition() + 1);
        String cardExpiryYear = mSpinnerYear.getSelectedItem().toString();
        boolean savePaymentOption = true;

        int id = mRadioGroup.getCheckedRadioButtonId();

        CardOption cardOption = null;

        switch (id) {
            case R.id.radio_debit_card:
                cardOption = new DebitCardOption(cardName, cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear);
                break;

            case R.id.radio_credit_card:
                cardOption = new CreditCardOption(cardName, cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear);
                break;
        }

        cardOption.setSavePaymentOption(savePaymentOption);
        mListener.onCardPaymentSelected(cardOption);
    }

    public static interface OnCardPaymentListener {
        public void onCardPaymentSelected(CardOption cardOption);
    }
}
