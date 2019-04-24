package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.PwdDot;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/22 0022 17:51
 * 描述	      ${TODO}
 */

public class PwdDotAdapter extends BaseQuickAdapter<PwdDot, BaseViewHolder> {
    public PwdDotAdapter(@LayoutRes int layoutResId, @Nullable List<PwdDot> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PwdDot item) {
        helper.setBackgroundRes(R.id.id_dot, item.isSelect ? R.drawable.shape_selected_dot : R.drawable.shape_dot);
    }
}
