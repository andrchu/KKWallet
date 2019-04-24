package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.dao.PushMessage;
import com.vip.wallet.utils.ColorUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/13 0013 14:43
 * 描述	      ${TODO}
 */

public class MessageCenterAdapter extends BaseQuickAdapter<PushMessage, BaseViewHolder> {

    public MessageCenterAdapter(@LayoutRes int layoutResId, @Nullable List<PushMessage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PushMessage item) {
        helper.setText(R.id.imc_title, item.title);
        helper.setText(R.id.imc_message, item.content);
        helper.setText(R.id.imc_time, TimeUtils.millis2String(item.time));
        if (item.isRead) {
            helper.setTextColor(R.id.imc_title, ColorUtil.getColor(R.color.gray));
            helper.setTextColor(R.id.imc_message, ColorUtil.getColor(R.color.gray));
        } else {
            helper.setTextColor(R.id.imc_title, ColorUtil.getColor(R.color.black));
            helper.setTextColor(R.id.imc_message, ColorUtil.getColor(R.color.black));
        }
    }
}
