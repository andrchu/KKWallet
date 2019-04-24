package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/31 0031 13:44
 * 描述	      ${TODO}
 */

public class SelectAccountAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SelectAccountAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.isa_account, item);
    }
}
