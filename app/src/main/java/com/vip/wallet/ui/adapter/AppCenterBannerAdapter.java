package com.vip.wallet.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.vip.wallet.R;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.utils.LoadImageUtil;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/7 0007 17:03
 * 描述	      ${TODO}
 */

public class AppCenterBannerAdapter implements BGABanner.Adapter<ImageView, AppCenterItem.DatasEntity> {

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable AppCenterItem.DatasEntity entity, int position) {
        itemView.setBackgroundResource(R.drawable.item_selector);
        LoadImageUtil.loadNetImage(itemView.getContext(), entity.img.replace("https:", "http"), itemView);
    }
}
