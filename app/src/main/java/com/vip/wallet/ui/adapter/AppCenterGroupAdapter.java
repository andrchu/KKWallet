package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.utils.ColorUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/14 0014 14:29
 * 描述	      ${TODO}
 */

public class AppCenterGroupAdapter extends BaseQuickAdapter<AppCenterItem, BaseViewHolder> {

    public AppCenterGroupAdapter(@LayoutRes int layoutResId, @Nullable List<AppCenterItem> data) {
        super(layoutResId, data);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
        if (mData.size() > 4)
            view.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        return super.createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppCenterItem item) {
        helper.setText(R.id.igac_title, item.category);
        TextView title = helper.getView(R.id.igac_title);
        TextPaint paint = title.getPaint();
        if (item.isSeleced) {
            title.setTextColor(ColorUtil.getColor(R.color.colorPrimary));
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            paint.setFakeBoldText(true);
        } else {
            title.setTextColor(ColorUtil.getColor(R.color.gray));
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            paint.setFakeBoldText(false);
        }
    }
}
