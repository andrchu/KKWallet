package com.vip.wallet.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/15 0015 18:01
 * 描述	      ${TODO}
 */

public class EventUtil {
    public static void register(Object o) {
        EventBus.getDefault().register(o);
    }

    public static void unRegister(Object o) {
        EventBus.getDefault().unregister(o);
    }

    public static void postMessage(Object message) {
        EventBus.getDefault().post(message);
    }
}
