package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.utils.LoadImageUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/10 0010 10:34
 * 描述	      ${TODO}
 */

public class AppCenterItemAdapter extends BaseQuickAdapter<AppCenterItem.DatasEntity, BaseViewHolder> {


    public AppCenterItemAdapter(@LayoutRes int layoutResId, @Nullable List<AppCenterItem.DatasEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppCenterItem.DatasEntity item) {
        helper.setText(R.id.iac_title, item.dappTitle);
        helper.setText(R.id.iac_vote_title, item.dappContent);
        helper.setText(R.id.iac_source, item.dappSource);
        LoadImageUtil.loadNetImage(mContext, item.smallUrl == null ? "" : item.smallUrl.replace("https:", "http:"), helper.getView(R.id.iac_icon));
    }
}
