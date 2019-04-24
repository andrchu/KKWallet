package com.vip.wallet.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.vip.wallet.R;
import com.vip.wallet.entity.CardChain;
import com.vip.wallet.entity.CardItem;
import com.vip.wallet.other.OnButtonClickListener;
import com.vip.wallet.ui.holder.ArtistViewHolder;
import com.vip.wallet.ui.holder.GenreViewHolder;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/18 0018 11:10
 * 描述	      ${TODO}
 */

public class CardGroupAdapter extends ExpandableRecyclerViewAdapter<GenreViewHolder, ArtistViewHolder> {
    public CardGroupAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chain, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public ArtistViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card, parent, false);
        return new ArtistViewHolder(view);
    }

    public void setOnChildClickListener(OnButtonClickListener<CardItem> onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    private OnButtonClickListener<CardItem> mOnChildClickListener;

    @Override
    public void onBindChildViewHolder(ArtistViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        CardItem cardItem = (CardItem) group.getItems().get(childIndex);
        holder.setCardName(String.format("%s(%s)", cardItem.card_name,StringUtil.lastSubString(cardItem.getShowString(),4)));
        holder.setChainTypeText(StringUtil.getChainName(cardItem.chain_type));
        holder.setChainTypeIcon(cardItem.getChainIconResId());
        holder.mLayout.setOnClickListener(v -> mOnChildClickListener.onButtonClick(cardItem));
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        CardChain cardChain = (CardChain) group;
        holder.setGenreTitle(cardChain.getTitle());
        int iconResId = cardChain.getIconResId();
        holder.setChainIcon(iconResId);
        holder.itemView.setBackgroundColor(cardChain.getCardBgColor());
    }
}
