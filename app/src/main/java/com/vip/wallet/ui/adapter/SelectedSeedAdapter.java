package com.vip.wallet.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.entity.SeedWord;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/21 0021 19:13
 * 描述	      ${TODO}
 */

public class SelectedSeedAdapter extends TagAdapter<SeedWord> {
    public SelectedSeedAdapter(List<SeedWord> datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, SeedWord seedWord) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_selected_seed, null);
        TextView seedView = rootView.findViewById(R.id.is_tv);
        seedView.setText(seedWord.seedWord);
        return rootView;
    }
}
