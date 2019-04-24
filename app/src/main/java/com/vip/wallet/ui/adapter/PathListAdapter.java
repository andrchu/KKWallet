package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.entity.Path;
import com.vip.wallet.utils.ColorUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/15 0015 14:09
 * 描述	      ${TODO}
 */

public class PathListAdapter extends BaseQuickAdapter<Path, BaseViewHolder> {
    public PathListAdapter(@LayoutRes int layoutResId, @Nullable List<Path> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Path item) {
        helper.setText(R.id.ip_path_text, item.desc);
        TextView pathText = helper.getView(R.id.ip_path_text);
        ImageView imageView = helper.getView(R.id.ip_iv);
        if (item.isSelect) {
            pathText.setTextColor(ColorUtil.getColor(R.color.title_bar_bg));
            imageView.setVisibility(View.VISIBLE);
        } else {
            pathText.setTextColor(ColorUtil.getColor(R.color.gray_text));
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
