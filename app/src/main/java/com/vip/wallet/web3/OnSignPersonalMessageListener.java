package com.vip.wallet.web3;

import trust.core.entity.Message;

public interface OnSignPersonalMessageListener {
    void onSignPersonalMessage(Message<String> message);
}
