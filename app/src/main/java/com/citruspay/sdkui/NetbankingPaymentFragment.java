package com.citruspay.sdkui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.citrus.sdkui.NetbankingOption;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetbankingPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetbankingPaymentFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnPaymentOptionSelectedListener mListener = null;
    private NetbankingOption mNetbankingOption = null;
    private Spinner mSpinnerBankList = null;

    public void setBankList(List<NetbankingOption> mListBanks) {
        this.mListBanks = mListBanks;
    }

    private List<NetbankingOption> mListBanks = null;
    private Button mButtonPay = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetbankingPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetbankingPaymentFragment newInstance(List<NetbankingOption> bankList) {
        NetbankingPaymentFragment fragment = new NetbankingPaymentFragment();
        fragment.setBankList(bankList);
        return fragment;
    }

    public NetbankingPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_netbanking_payment, container, false);

        mButtonPay = (Button) rootView.findViewById(R.id.buttonPay);
        mButtonPay.setOnClickListener(this);

        mSpinnerBankList = (Spinner) rootView.findViewById(R.id.spinnerBankList);

        ArrayAdapter<NetbankingOption> adapter = new ArrayAdapter<NetbankingOption>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mListBanks);
        mSpinnerBankList.setAdapter(adapter);

        mSpinnerBankList.setOnItemSelectedListener(this);

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
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Pay Button Clicked...", Toast.LENGTH_SHORT).show();

        if (mNetbankingOption != null) {
            mListener.onOptionSelected(mNetbankingOption);
        } else {
            Toast.makeText(getActivity(), "Please choose the bank for payment!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mNetbankingOption = getNetbankingOption(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mNetbankingOption = null;
    }

    private NetbankingOption getNetbankingOption(int position) {
        NetbankingOption netbankingOption = null;

        if (mListBanks != null && position > -1 && position < mListBanks.size()) {
            netbankingOption = mListBanks.get(position);
        }
        return netbankingOption;
    }

}
