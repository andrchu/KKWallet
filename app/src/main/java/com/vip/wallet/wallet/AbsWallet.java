package com.vip.wallet.wallet;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/22 0022 16:21
 * 描述	      ${TODO}
 */

public abstract class AbsWallet {
    public String address;
    public String privateKey;

    public AbsWallet(String address, String privateKey) {
        this.address = address;
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "AbsWallet{" +
                "address='" + address + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
