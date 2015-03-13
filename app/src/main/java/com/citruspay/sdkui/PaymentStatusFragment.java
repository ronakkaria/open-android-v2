package com.citruspay.sdkui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.citruspay.sdkui.PaymentStatusFragment.OnTransactionResponseListener} interface
 * to handle interaction events.
 * Use the {@link PaymentStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentStatusFragment extends Fragment implements View.OnClickListener {

    private OnTransactionResponseListener mListener;
    private CitrusTransactionResponse mTransactionResponse = null;
    private CitrusPaymentParams mPaymentParams = null;
    private ImageView mImgTransactionStatus = null;
    private TextView mTxtTransactionMessage = null;
    private TextView mTxtTitleTransactionId = null;
    private TextView mTxtTransactionId = null;
    private TextView mTxtTitleText2 = null;
    private TextView mTxtText2 = null;
    private Button mBtnRetryTransaction = null;
    private Button mBtnDismiss = null;

    public PaymentStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param transactionResponse Object of {@link com.citruspay.sdkui.CitrusTransactionResponse}
     * @return A new instance of fragment PaymentStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentStatusFragment newInstance(CitrusTransactionResponse transactionResponse, CitrusPaymentParams paymentParams) {
        PaymentStatusFragment fragment = new PaymentStatusFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARAM_TRANSACTION_RESPONSE, transactionResponse);
        args.putParcelable(Constants.INTENT_EXTRA_PAYMENT_PARAMS, paymentParams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTransactionResponse = getArguments().getParcelable(Constants.PARAM_TRANSACTION_RESPONSE);
            mPaymentParams = getArguments().getParcelable(Constants.INTENT_EXTRA_PAYMENT_PARAMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_status, container, false);

        mTxtTransactionMessage = (TextView) view.findViewById(R.id.txt_transaction_message);
        mTxtTitleTransactionId = (TextView) view.findViewById(R.id.txt_title_transaction_id);
        mTxtTransactionId = (TextView) view.findViewById(R.id.txt_transaction_id);
        mTxtTitleText2 = (TextView) view.findViewById(R.id.txt_title_text2);
        mTxtText2 = (TextView) view.findViewById(R.id.txt_text2);
        mBtnRetryTransaction = (Button) view.findViewById(R.id.btn_retry_transaction);
        mBtnDismiss = (Button) view.findViewById(R.id.btn_dismiss);

        mBtnRetryTransaction.setOnClickListener(this);
        mBtnDismiss.setOnClickListener(this);

        if (mTransactionResponse != null && mTransactionResponse.getTransactionDetails() != null) {

            if (mTransactionResponse.getTransactionStatus() == CitrusTransactionResponse.TransactionStatus.SUCCESS) {
                mTxtTransactionMessage.setText(getString(R.string.message_payment_successful));
                mTxtTitleTransactionId.setText(getString(R.string.title_transaction_id_success));
                mTxtTransactionId.setText(mTransactionResponse.getTransactionDetails().getTransactionId());
                mTxtTitleText2.setText(getString(R.string.title_text2_success));
                mTxtText2.setText(mTransactionResponse.getAmount());

                // Hide the retry and dismiss buttons.
                mBtnRetryTransaction.setVisibility(View.GONE);
                mBtnDismiss.setVisibility(View.GONE);
            } else {
                mTxtTransactionMessage.setText(getString(R.string.message_payment_error));
                mTxtTitleTransactionId.setText(getString(R.string.title_transaction_id_error));
                mTxtTransactionId.setText(mTransactionResponse.getTransactionDetails().getTransactionId());
                mTxtTitleText2.setText(getString(R.string.title_text2_error));
                mTxtText2.setText(mTransactionResponse.getMessage());

                if (mPaymentParams != null) {
                    mBtnRetryTransaction.setTextColor(Color.parseColor(mPaymentParams.colorPrimary));
                }
            }
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTransactionResponseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTransactionResponseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry_transaction:
                mListener.onRetryTransaction();

                break;
            case R.id.btn_dismiss:
                mListener.onDismiss();

                break;
        }
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
    public interface OnTransactionResponseListener {
        public void onRetryTransaction();

        public void onDismiss();
    }

}
