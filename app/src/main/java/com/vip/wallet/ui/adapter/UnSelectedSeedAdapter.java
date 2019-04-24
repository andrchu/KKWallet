package com.vip.wallet.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.entity.SeedWord;
import com.vip.wallet.utils.ColorUtil;
import com.vip.wallet.utils.DrawableUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/21 0021 19:13
 * 描述	      ${TODO}
 */

public class UnSelectedSeedAdapter extends TagAdapter<SeedWord> {

    private final int mColor_black;
    private final Drawable mDrawable;

    public UnSelectedSeedAdapter(List<SeedWord> datas) {
        super(datas);
        mColor_black = ColorUtil.getColor(R.color.black);
        mDrawable = DrawableUtil.getDrawable(R.drawable.shape_item_selected_seed_bg);
    }

    @Override
    public View getView(FlowLayout parent, int position, SeedWord seedWord) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_unselected_seed, null);
        TextView textView = rootView.findViewById(R.id.ius_unselected_seed);
        if (seedWord.isSelect) {
            textView.setTextColor(Color.WHITE);
            textView.setBackground(mDrawable);
        } else {
            textView.setTextColor(mColor_black);
            textView.setBackgroundColor(Color.TRANSPARENT);
        }
        textView.setText(seedWord.seedWord);
        return rootView;
    }
}
