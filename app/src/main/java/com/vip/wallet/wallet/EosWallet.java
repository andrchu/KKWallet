package com.vip.wallet.wallet;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 15:14
 * 描述	      ${TODO}
 */

public class EosWallet extends AbsWallet {

    public EosWallet(String address, String privateKey) {
        super(address, privateKey);
    }

    public String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
