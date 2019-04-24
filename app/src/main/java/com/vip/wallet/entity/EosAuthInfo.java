package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/29 0029 14:11
 * 描述	      ${TODO}
 */

public class EosAuthInfo {

    /**
     * signs : xx
     * auth : 1
     * extra : {"a":1,"b":2}
     * weight : 2
     * account : a11
     */
    public String signs;
    public int auth;    //0-未授权 1-已授权
    public String extra;
    public int weight;
    public String account;

    public EosAuthInfo(String account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EosAuthInfo that = (EosAuthInfo) o;

        return account != null ? account.equals(that.account) : that.account == null;

    }

    @Override
    public int hashCode() {
        return account != null ? account.hashCode() : 0;
    }
}
