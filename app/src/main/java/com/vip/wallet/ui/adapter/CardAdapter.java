package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.dao.Card;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/13 0013 17:17
 * 描述	      ${TODO}
 */

public class CardAdapter extends BaseQuickAdapter<Card, BaseViewHolder> {
    public CardAdapter(@LayoutRes int layoutResId, @Nullable List<Card> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Card item) {
        helper.setText(R.id.fw_chain_type_text, item.getChainTypeString());
        helper.setImageResource(R.id.fw_chain_type_icon, item.getIconResId());
        helper.setText(R.id.ic_card_name, String.format("%s (%s)", item.getName(), StringUtil.lastSubString(item.getDefAddress(), 4)));
    }
}
