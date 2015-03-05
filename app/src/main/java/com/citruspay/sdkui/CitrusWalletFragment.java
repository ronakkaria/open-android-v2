package com.citruspay.sdkui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.citrus.sdkui.PaymentOption;

import java.util.List;

public class CitrusWalletFragment extends Fragment implements View.OnClickListener{

    private List<PaymentOption> mCitrusWallet = null;
    private RecyclerView mRecyclerViewWallet = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private CitrusWalletAdapter mAdapter = null;
    private RadioButton mCurrentSelectedItem = null;
    private int mCurrentSelectedItemIndex = -1;
    private Button mButtonPay = null;
    private OnPaymentOptionSelectedListener mListener = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_citrus_wallet, container, false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        // Initialize recyclerview
        mRecyclerViewWallet = (RecyclerView) rootView.findViewById(R.id.walletList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewWallet.setHasFixedSize(true);

        mRecyclerViewWallet.setLayoutManager(mLayoutManager);

        mRecyclerViewWallet.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = 20;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;

                // Add top margin only for the first item to avoid double space between items
                if(parent.getChildPosition(view) == 0)
                    outRect.top = space;
            }
        });

        // specify an adapter (see also next example)
        mAdapter = new CitrusWalletAdapter(mCitrusWallet);
        mRecyclerViewWallet.setAdapter(mAdapter);
        mRecyclerViewWallet.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mCurrentSelectedItem==null) {
                            mCurrentSelectedItem = (RadioButton) view.findViewById(R.id.radioWallet);
                            mCurrentSelectedItem.setChecked(true);
                        } else {
                            // Mark the older item as unchecked
                            mCurrentSelectedItem.setChecked(false);

                            mCurrentSelectedItem = (RadioButton) view.findViewById(R.id.radioWallet);
                            mCurrentSelectedItem.setChecked(true);
                        }

                        mCurrentSelectedItemIndex = position;

                        // TODO Accept CVV from user.
                    }
                })
        );

        mButtonPay = (Button) rootView.findViewById(R.id.buttonPay);
        mButtonPay.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setCitrusWallet(List<PaymentOption> citrusWallet) {
        mCitrusWallet = citrusWallet;
    }

    @Override
    public void onClick(View v) {

        if (mCurrentSelectedItem != null && mCurrentSelectedItemIndex > 0 && mCurrentSelectedItemIndex < mCitrusWallet.size()) {
            mListener.onOptionSelected(mCitrusWallet.get(mCurrentSelectedItemIndex));
        } else {
            Toast.makeText(getActivity(), "Please choose the wallet option for payment!", Toast.LENGTH_SHORT).show();
        }
    }
}

class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
}
