package com.vip.wallet.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.other.OnButtonClickListener;
import com.vip.wallet.utils.ColorUtil;
import com.vip.wallet.utils.DrawableUtil;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/27 0027 16:23
 * 描述	      ${TODO}
 */

public class EosSignAuthListAdapter extends BaseQuickAdapter<EosSigns.Permissions, BaseViewHolder> {

    private int mGray = ColorUtil.getColor(R.color.gray);
    private int mMainColor = ColorUtil.getColor(R.color.colorPrimary);
    private Drawable mBgDrawable = DrawableUtil.getDrawable(R.drawable.selector_confirm);

    public EosSignAuthListAdapter(@LayoutRes int layoutResId, @Nullable List<EosSigns.Permissions> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EosSigns.Permissions item) {
        helper.setText(R.id.iea_account, StringUtil.isEosAccount(item.account) ? item.account : StringUtil.getHideString(item.account, 5));
        helper.setText(R.id.iea_native_flag, item.isNative ? "(本地)" : "");
        helper.setText(R.id.iea_weight, String.format("权重：%s", item.weight));
        TextView authView = helper.getView(R.id.iea_bt_auth);
        boolean empty = StringUtils.isEmpty(item.signs);
        if (empty) {
            if (item.isNative) {
                authView.setText("授权");
                authView.setTextColor(Color.WHITE);
                authView.setBackground(mBgDrawable);
            } else {
                authView.setText("待授权");
                authView.setTextColor(mGray);
                authView.setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            authView.setText("已授权");
            authView.setTextColor(mMainColor);
            authView.setBackgroundColor(Color.TRANSPARENT);
        }
        authView.setOnClickListener(v -> {
            if (mButtonClickListener != null && empty && item.isNative) {
                mButtonClickListener.onButtonClick(item);
            }
        });
    }

    public void setButtonClickListener(OnButtonClickListener<EosSigns.Permissions> buttonClickListener) {
        mButtonClickListener = buttonClickListener;
    }

    private OnButtonClickListener<EosSigns.Permissions> mButtonClickListener;
}
