package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/9 0009 16:02
 * 描述	      ${TODO}
 */

public class SeedWord implements Serializable,Cloneable{
    public String seedWord;
    public boolean isSelect;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SeedWord seedWord1 = (SeedWord) o;

        return seedWord != null ? seedWord.equals(seedWord1.seedWord) : seedWord1.seedWord == null;

    }

    @Override
    public int hashCode() {
        return seedWord != null ? seedWord.hashCode() : 0;
    }

    public SeedWord(String seedWord) {
        this.seedWord = seedWord;
    }
}
