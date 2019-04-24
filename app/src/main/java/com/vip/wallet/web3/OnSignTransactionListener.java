package com.vip.wallet.web3;

import trust.core.entity.Transaction;

public interface OnSignTransactionListener {
    void onSignTransaction(Transaction transaction);
}
