package com.vip.wallet.exception;

import com.vip.wallet.dao.Wallet;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/12 0012 16:20
 * 描述	      ${TODO}
 */

public class ExistWalletException extends Exception {
    private List<Wallet> mWallet;

    public ExistWalletException(List<Wallet> wallet) {
        super();
        mWallet = wallet;
    }

    public List<Wallet> getWallet() {
        return mWallet;
    }
}
