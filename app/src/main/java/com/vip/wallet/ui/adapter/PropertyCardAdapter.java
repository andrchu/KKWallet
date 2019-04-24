
package com.vip.wallet.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.view.jameson.library.BannerAdapterHelper;
import com.vip.wallet.R;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.EventMessage;
import com.vip.wallet.utils.EventUtil;
import com.vip.wallet.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/14 0014 18:38
 * 描述	      ${TODO}
 */

public class PropertyCardAdapter extends RecyclerView.Adapter<PropertyCardAdapter.ViewHolder> {
    private List<Card> mList = new ArrayList<>();
    private BannerAdapterHelper mBannerAdapterHelper;

    public PropertyCardAdapter(List<Card> mList) {
        this.mList = mList;
        if (mList.size() <= 1) {
            mBannerAdapterHelper = new BannerAdapterHelper(0, 0);
        } else {
            mBannerAdapterHelper = new BannerAdapterHelper(6, 12);
        }
    }

    public List<Card> getList() {
        return mList;
    }

    public void setList(List<Card> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_card, parent, false);
        mBannerAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PropertyCardAdapter.ViewHolder holder, final int position) {
        mBannerAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Card card = mList.get(getPosition(position));
        holder.mCardName.setText(card.name);
        String unitSymbol = ScApplication.getInstance().getUnitSymbol();
        holder.mShowProperty.setText(String.format("%s(%s)", StringUtil.getString(R.string.total_amount), unitSymbol.substring(1, 2)));
        boolean showProperty = ScApplication.getInstance().getConfig().isShowProperty();
        holder.mShowProperty.setChecked(showProperty);
        boolean b = card.chainType == 2;
        holder.mAddress.setText(String.format("%s:%s", b ? StringUtil.getString(R.string.account) : StringUtil.getString(R.string.address), b ? card.accountName : StringUtil.getHideString(card.getDefAddress(), 10)));
        holder.mAmount.setText(String.format("≈%s", showProperty ? card.amount : "***"));
        holder.mCardLayout.setBackgroundResource(card.getPropertyCardBgResId());
        holder.mShowProperty.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ScApplication.getInstance().getConfig().setShowProperty(isChecked);
            holder.mAmount.setText(String.format("≈%s", ScApplication.getInstance().getConfig().isShowProperty() ? card.amount : "***"));
            EventUtil.postMessage(new EventMessage.UpdateShowProperty());
        });
    }

    public int getPosition(int position) {
        if (mList.size() <= 1) {
            return position;
        }
        return position % mList.size();
    }

    @Override
    public int getItemCount() {
        if (mList.size() <= 1) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mAddress;
        private final TextView mAmount;
        private final TextView mCardName;
        private final CheckBox mShowProperty;
        private final LinearLayout mCardLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            mAddress = itemView.findViewById(R.id.ipc_address);
            mAmount = itemView.findViewById(R.id.ipc_amount);
            mCardName = itemView.findViewById(R.id.ipc_card_name);
            mShowProperty = itemView.findViewById(R.id.ipc_show_property);
            mCardLayout = itemView.findViewById(R.id.ipc_card_layout);
        }
    }
}


/*package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.view.jameson.library.BannerAdapterHelper;
import com.vip.wallet.dao.Card;

import java.util.List;

*//**
 * 创建者     金国栋
 * 创建时间   2018/6/14 0014 18:38
 * 描述	      ${TODO}
 *//*

public class PropertyCardAdapter extends BaseQuickAdapter<Card, BaseViewHolder> {

    private BannerAdapterHelper mBannerAdapterHelper = new BannerAdapterHelper();

    public PropertyCardAdapter(@LayoutRes int layoutResId, @Nullable List<Card> data) {
        super(layoutResId, data);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
        mBannerAdapterHelper.onCreateViewHolder(parent, baseViewHolder.itemView);
        return baseViewHolder;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        mBannerAdapterHelper.onCreateViewHolder(parent, baseViewHolder.itemView);
        return baseViewHolder;
    }

    @Override
    protected void convert(BaseViewHolder helper, Card item) {
        mBannerAdapterHelper.onBindViewHolder(helper.itemView, getParentPosition(item), getItemCount());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}*/
