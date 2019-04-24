package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/19 0019 13:49
 * 描述	      ${TODO}
 */

public class Chain implements Serializable,Cloneable {
    public String str;
    public int index;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Chain(int index) {
        this.index = index;
        if (index == 0) {
            str = "ETH";
        } else if (index == 1) {
            str = "BTC";
        } else {
            str = "EOS";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Chain chain = (Chain) o;

        return index == chain.index;

    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return str;
    }
}
