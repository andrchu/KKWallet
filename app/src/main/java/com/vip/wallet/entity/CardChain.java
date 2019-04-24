package com.vip.wallet.entity;

import android.graphics.Color;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.vip.wallet.R;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/18 0018 11:38
 * 描述	      ${TODO}
 */

public class CardChain extends ExpandableGroup<CardItem> {
    public int chain_type;

    public CardChain(String title, List<CardItem> items, int chain_type) {
        super(title, items);
        this.chain_type = chain_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CardChain cardChain = (CardChain) o;

        return chain_type == cardChain.chain_type;

    }

    @Override
    public int hashCode() {
        return chain_type;
    }

    public int getIconResId() {
        switch (chain_type) {
            case 0:
                return R.drawable.card_pkg_eth;
            case 1:
                return R.drawable.card_pkg_btc;
            case 2:
                return R.drawable.card_pkg_eos;
            default:
                return R.drawable.card_pkg_eth;
        }
    }

    public int getCardBgColor() {
        switch (chain_type) {
            case 0:
                return Color.parseColor("#426f92");
            case 1:
                return Color.parseColor("#febc3a");
            case 2:
                return Color.parseColor("#433e53");
            default:
                return Color.parseColor("#426f92");
        }
    }
}
