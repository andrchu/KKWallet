package com.vip.wallet.other;

import rx.Subscriber;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/5 0005 17:42
 * 描述	      ${TODO}
 */

public abstract class SimpSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
