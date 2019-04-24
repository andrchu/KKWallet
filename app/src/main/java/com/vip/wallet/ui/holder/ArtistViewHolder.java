package com.vip.wallet.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.vip.wallet.R;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView cardName;
    private final ImageView mChainTypeIcon;
    private final TextView mChainTypeText;
    public final LinearLayout mLayout;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        cardName = itemView.findViewById(R.id.lic_card_name);
        mChainTypeIcon = itemView.findViewById(R.id.lic_chain_type_icon);
        mChainTypeText = itemView.findViewById(R.id.lic_chain_type_text);
        mLayout = itemView.findViewById(R.id.ic_layout);
    }

    public void setChainTypeIcon(int resId) {
        mChainTypeIcon.setImageResource(resId);
    }

    public void setChainTypeText(String text) {
        mChainTypeText.setText(text);
    }

    public void setCardName(String name) {
        cardName.setText(name);
    }
}
