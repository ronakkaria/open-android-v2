package com.citruspay.sdkui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citrus.sdkui.NetbankingOption;

import java.util.List;

/**
 * Created by salil on 27/2/15.
 */
public class NetbankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<NetbankingOption> mNetbankingOptionList = null;
    private Context mContext = null;

    public NetbankingAdapter(Context context, List<NetbankingOption> netbankingOptionList) {
        this.mNetbankingOptionList = netbankingOptionList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            holder = new NetbankingHeaderViewHolder(itemView);

        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            holder = new NetbankingItemViewHolder(itemView);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int index) {
        if (viewHolder instanceof NetbankingHeaderViewHolder) {
            ((NetbankingHeaderViewHolder) viewHolder).txtHeader.setText("NET BANKING");

        } else if (viewHolder instanceof NetbankingItemViewHolder) {
            NetbankingItemViewHolder itemHolder = (NetbankingItemViewHolder) viewHolder;
            NetbankingOption netbankingOption = getItem(index);

            if (netbankingOption != null) {
                itemHolder.txtBankName.setText(netbankingOption.getBankName());
//            int id = mContext.getResources().getIdentifier(netbankingOption.getBankName().toLowerCase(), "drawable", mContext.getPackageName());

//            Drawable drawable = mContext.getResources().getDrawable(id);
//            viewHolder.txtHeader.setCompoundDrawablesWithIntrinsicBounds(new ScaleDrawable(drawable, Gravity.NO_GRAVITY, 51,33), null, null, null);
            }
        }


    }

    @Override
    public int getItemCount() {
        if (mNetbankingOptionList != null) {
            return mNetbankingOptionList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see #onAttachedToRecyclerView(android.support.v7.widget.RecyclerView)
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        mContext = null;
        if (mNetbankingOptionList != null) {
            mNetbankingOptionList.clear();
        }

        mNetbankingOptionList = null;
    }

    private NetbankingOption getItem(int position) {
        if (mNetbankingOptionList != null && position >= 0 && position < mNetbankingOptionList.size()) {
            return mNetbankingOptionList.get(position);
        }

        return null;
    }

    public static class NetbankingItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtBankName;

        public NetbankingItemViewHolder(View v) {
            super(v);
            txtBankName = (TextView) v.findViewById(android.R.id.text1);
        }
    }

    public static class NetbankingHeaderViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtHeader;

        public NetbankingHeaderViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(android.R.id.text1);
        }
    }
}