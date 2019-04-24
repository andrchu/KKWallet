package com.vip.wallet.receiver;

import android.content.Context;

import com.vip.wallet.utils.LogUtil;

import cn.jpush.android.api.JPushMessage;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 11:08
 * 描述	      ${TODO}
 */

public class JPushMessageReceiver extends cn.jpush.android.service.JPushMessageReceiver {
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        LogUtil.getInstance().i("ppppp  onTagOperatorResult>>>" + jPushMessage.toString());
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        LogUtil.getInstance().i("ppppp  onCheckTagOperatorResult>>>" + jPushMessage.toString());
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        LogUtil.getInstance().i("ppppp  onAliasOperatorResult>>>" + jPushMessage.toString());
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
        LogUtil.getInstance().i("ppppp  onMobileNumberOperatorResult>>>" + jPushMessage.toString());
    }
}
