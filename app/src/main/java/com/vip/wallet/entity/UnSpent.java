package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/21 0021 16:23
 * 描述	      ${TODO}
 */

public class UnSpent implements Serializable{
    /**
     * scriptPubKey : 76a9143a01450297a12e49ad9dc6e3caf189a8d5857b0688ac
     * amount : 0.275
     * address : mkof1PfJEBph7gqwLqWHjsDsahiCWeHTzQ
     * txid : 9e012f3190703232488b34eb5e4d447891d9b1550b3cdedd56e246a2765a7ca0
     * confirmations : 4049
     * vout : 0
     * satoshis : 27500000
     * height : 1321945
     */
    public String scriptPubKey;
    public double amount;
    public String address;
    public String txid;
    public int confirmations;
    public int vout;
    public long satoshis;
    public long height;
}
