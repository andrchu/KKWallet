package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.dao.Card;
import com.vip.wallet.utils.ColorUtil;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/15 0015 17:38
 * 描述	      ${TODO}
 */

public class SelectCardAdapter extends BaseQuickAdapter<Card, BaseViewHolder> {
    private Card currentCard;

    public SelectCardAdapter(@LayoutRes int layoutResId, @Nullable List<Card> data) {
        super(layoutResId, data);
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, Card item) {
        String address = item.chainType == 2 ? item.accountName : StringUtil.lastSubString(item.defAddress, 4);
        helper.setText(R.id.fw_chain_type_text, item.getChainTypeString());
        helper.setImageResource(R.id.fw_chain_type_icon, item.getIconResId());
        helper.setText(R.id.ic_card_name, String.format("%s (%s)", item.getName(), address));
        if (currentCard == null || !currentCard.equals(item)) {
            helper.setBackgroundRes(R.id.ic_layout, R.drawable.shape_white_cir_rec);
            helper.setBackgroundRes(R.id.fw_chain_type_text, R.drawable.shape_card_chain_bg);
            //            helper.setTextColor(R.id.fw_address, ColorUtil.getColor(R.color.gray));
            helper.setTextColor(R.id.fw_chain_type_text, ColorUtil.getColor(R.color.black));
            helper.setTextColor(R.id.ic_card_name, ColorUtil.getColor(R.color.black));
        } else {
            //            helper.setTextColor(R.id.fw_address, ColorUtil.getColor(R.color.white));
            helper.setTextColor(R.id.fw_chain_type_text, ColorUtil.getColor(R.color.white));
            helper.setTextColor(R.id.ic_card_name, ColorUtil.getColor(R.color.white));
            helper.setBackgroundRes(R.id.fw_chain_type_text, R.drawable.shape_card_select_chain_bg);
            helper.setBackgroundRes(R.id.ic_layout, R.drawable.shape_def_cir_rec);
        }

        LinearLayout layout = helper.getView(R.id.ic_layout);
        int i = SizeUtils.dp2px(12);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        if (item.equals(mData.get(mData.size() - 1))) {
            //最后一个条目
            marginLayoutParams.setMargins(i, i, i, i);
        } else {
            marginLayoutParams.setMargins(i, i, i, 0);
        }
    }

}
