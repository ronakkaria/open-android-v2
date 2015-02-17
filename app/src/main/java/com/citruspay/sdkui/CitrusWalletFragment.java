package com.citruspay.sdkui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citrus.sdkui.PaymentOption;

import java.util.List;

public class CitrusWalletFragment extends Fragment {

    private List<PaymentOption> mCitrusWallet = null;
    private RecyclerView mRecyclerViewWallet = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private CitrusWalletAdapter mAdapter = null;

    public CitrusWalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitrusWalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitrusWalletFragment newInstance(List<PaymentOption> citrusWallet) {
        CitrusWalletFragment fragment = new CitrusWalletFragment();
        fragment.setCitrusWallet(citrusWallet);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_citrus_wallet, container, false);


        mRecyclerViewWallet = (RecyclerView) rootView.findViewById(R.id.walletList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewWallet.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewWallet.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CitrusWalletAdapter(mCitrusWallet);
        mRecyclerViewWallet.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setCitrusWallet(List<PaymentOption> citrusWallet) {
        mCitrusWallet = citrusWallet;
    }
}
