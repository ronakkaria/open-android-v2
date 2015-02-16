package com.citruspay.sdkui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CitrusWalletFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitrusWalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitrusWalletFragment newInstance() {
        CitrusWalletFragment fragment = new CitrusWalletFragment();
        return fragment;
    }

    public CitrusWalletFragment() {
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
        return inflater.inflate(R.layout.fragment_citrus_wallet, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
