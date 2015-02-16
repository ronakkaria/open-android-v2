package com.citruspay.sdkui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetbankingPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetbankingPaymentFragment extends Fragment implements View.OnClickListener {

    private OnPaymentOptionSelectedListener mListener = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetbankingPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetbankingPaymentFragment newInstance() {
        NetbankingPaymentFragment fragment = new NetbankingPaymentFragment();
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
        return inflater.inflate(R.layout.fragment_netbanking_payment, container, false);
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

        CardOption cardOption = null;

        mListener.OnOptionSelected(cardOption);

    }
}
