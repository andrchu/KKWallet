package com.vip.wallet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.vip.wallet.R;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/18 0018 11:44
 * 描述	      ${TODO}
 */

public class CardItem implements Parcelable {
    public int chain_type;
    public String card_name;
    public String address;
    public String accountName;

    public String getShowString() {
        if (chain_type == 2) {
            return accountName;
        } else {
            return address;
        }
    }

    public int getChainIconResId() {
        switch (chain_type) {
            case 0:
                return R.drawable.eth_min_icon;
            case 1:
                return R.drawable.card_btc;
            case 2:
                return R.drawable.card_eos;
            default:
                return R.drawable.eth_min_icon;
        }
    }

    public CardItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CardItem cardItem = (CardItem) o;

        return address != null ? address.equals(cardItem.address) : cardItem.address == null;

    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

    public CardItem(int chain_type, String card_name, String address, String accountName) {
        this.chain_type = chain_type;
        this.card_name = card_name;
        this.address = address;
        this.accountName = accountName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.chain_type);
        dest.writeString(this.card_name);
        dest.writeString(this.address);
        dest.writeString(this.accountName);
    }

    protected CardItem(Parcel in) {
        this.chain_type = in.readInt();
        this.card_name = in.readString();
        this.address = in.readString();
        this.accountName = in.readString();
    }

    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel source) {
            return new CardItem(source);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };
}
