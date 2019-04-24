package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/27 0027 15:55
 * 描述	      ${TODO}
 */

public class QrCode<T> {
    /**
     * EOS转账多重签名
     */
    public static final String EOS_TRANSFER = "eosTransfer";

    public String action;
    public T data;

    public QrCode() {
    }

    public QrCode(String action, T data) {
        this.action = action;
        this.data = data;
    }
}
