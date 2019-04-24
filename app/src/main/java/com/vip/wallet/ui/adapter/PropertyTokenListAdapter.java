package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.utils.LoadImageUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 16:29
 * 描述	      ${TODO}
 */

public class PropertyTokenListAdapter extends BaseQuickAdapter<WalletTokenInfos.TokenInfosEntity, BaseViewHolder> {
    public PropertyTokenListAdapter(@LayoutRes int layoutResId, @Nullable List<WalletTokenInfos.TokenInfosEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletTokenInfos.TokenInfosEntity item) {
        boolean showProperty = ScApplication.getInstance().getConfig().isShowProperty();
        helper.setText(R.id.it_token_balance, showProperty ? item.getBalance() : "***");
        helper.setText(R.id.it_token_amount, showProperty ? ScApplication.getInstance().getUnitSymbol() + item.getTotalAmount() : "***");
        helper.setText(R.id.it_token_name, item.name);
        helper.setText(R.id.it_token_short_name, item.symbol);
        if (item.symbol.equals("BTC")) {
            helper.setImageResource(R.id.it_token_icon, R.drawable.card_btc);
        } else if (item.symbol.equals("EOS")) {
            helper.setImageResource(R.id.it_token_icon, R.drawable.eos50);
        } else
            LoadImageUtil.loadNetImage(mContext, item.image_url, helper.getView(R.id.it_token_icon), R.drawable.eth_min_icon, R.drawable.eth_min_icon);
    }
}
