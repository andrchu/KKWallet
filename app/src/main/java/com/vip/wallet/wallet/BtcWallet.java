package com.vip.wallet.wallet;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 15:13
 * 描述	      ${TODO}
 */

public class BtcWallet extends AbsWallet {
    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BtcWallet(String address, String privateKey) {
        super(address, privateKey);
    }
}
