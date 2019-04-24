package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/3 0003 14:50
 * 描述	      ${TODO}
 */

public class SearchToken {

    /**
     * symbol : SSS
     * address : 0x7d3e7d41da367b4fdce7cbe06502b13294deb758
     * decimals : 8
     * name : Sharechain
     * img_src : https://s2.coinmarketcap.com/static/img/coins/16x16/2177.png
     */
    public String symbol;
    public String address;
    public int decimals;
    public String name;
    public String img_src;
    public boolean isAdd;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SearchToken that = (SearchToken) o;

        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }
}
