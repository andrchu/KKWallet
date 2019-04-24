package com.vip.wallet.ui.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.utils.ColorUtil;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 18:00
 * 描述	      ${TODO}
 */

public class DealRecordAdapter extends BaseQuickAdapter<DealRecord, BaseViewHolder> {
    public DealRecordAdapter(@LayoutRes int layoutResId, @Nullable List<DealRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DealRecord item) {
        helper.setText(R.id.idr_date, item.getDate());
        boolean type = item.type == 0;  //true 转出
        helper.setText(R.id.idr_address, StringUtil.getHideString(type ? item.to_address : item.from_address));
        helper.setText(R.id.idr_amount, String.format("%s%s %s", type ? "-" : "+", item.value, item.token_symbol));
        helper.setTextColor(R.id.idr_amount, type ? Color.parseColor("#ff9898") : ColorUtil.getColor(R.color.colorPrimary));
        helper.setImageResource(R.id.idr_icon, type ? R.drawable.deal_record_out : R.drawable.deal_record_in);
        helper.setText(R.id.idr_deal_status, item.getTypeDesc());
        helper.setTextColor(R.id.idr_deal_status, item.getStatus() == 2 ? Color.parseColor("#ff9898") : ColorUtil.getColor(R.color.gray_l));
    }
}
