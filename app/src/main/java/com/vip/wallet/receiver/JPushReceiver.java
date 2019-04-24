package com.vip.wallet.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.PushMessage;
import com.vip.wallet.dao.PushMessageDao;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.entity.JPushMessageData;
import com.vip.wallet.entity.PushTypeData;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.ui.activity.MessageDetailActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.utils.EventUtil;
import com.vip.wallet.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushReceiver;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 11:07
 * 描述	      ${TODO}
 */

public class JPushReceiver extends PushReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //        String message = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
        String json = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
        String title = intent.getStringExtra(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(JPushInterface.EXTRA_ALERT);
        int messageId = intent.getIntExtra(JPushInterface.EXTRA_NOTIFICATION_ID, 0);
        JPushMessageData jPushMessageData = new JPushMessageData(title, content, json, messageId);

        LogUtil.getInstance().i(jPushMessageData.toString());

        String action = intent.getAction();
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            processReceived(context, jPushMessageData);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            processClick(context, jPushMessageData);
        }

    }

    /**
     * 处理点击通知
     *
     * @param context
     * @param data
     */
    private void processClick(Context context, JPushMessageData data) {
        PushMessageDao pushMessageDao = ScApplication.getInstance().getDaoSession().getPushMessageDao();
        PushMessage pushMessage = pushMessageDao.loadByRowId(data.msgId);
        if (pushMessage == null)
            return;
        switch (pushMessage.getType()) {
            case 0:
                gotoMessageDetail(context, pushMessage);
                break;
            case 1:
                gotoDealDetail(context, pushMessage);
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;

            default:
                break;
        }
        //标记为已读状态
        pushMessage.setIsRead(true);
        pushMessageDao.update(pushMessage);
        EventUtil.postMessage(pushMessage);
    }

    /**
     * 跳转至消息详情
     *
     * @param context
     * @param pushMessage
     */
    private void gotoMessageDetail(Context context, PushMessage pushMessage) {
        MessageDetailActivity.start(context, pushMessage);
    }

    /**
     * 跳转交易详情
     *
     * @param context
     * @param pushMessage
     */
    private void gotoDealDetail(Context context, PushMessage pushMessage) {
        try {
            DealRecord dealRecord = GsonAdapter.getGson().fromJson(pushMessage.data, DealRecord.class);
            WebActivity.startWebActivity(context, new BrowserInfo("交易详情", dealRecord.getDetailsUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理接收消息
     *
     * @param context
     * @param data
     */
    private void processReceived(Context context, JPushMessageData data) {
        //存入数据库
        PushMessage entity = getPushMessage(data);
        ScApplication.getInstance().getDaoSession().getPushMessageDao().insert(entity);
        //通知有新消息
        EventUtil.postMessage(entity);
    }

    @NonNull
    private PushMessage getPushMessage(JPushMessageData data) {
        PushMessage entity = new PushMessage(data.msgId, data.title, data.content, 0, "", System.currentTimeMillis(), false);
        try {
            PushTypeData pushTypeData = GsonAdapter.getGson().fromJson(data.data, PushTypeData.class);
            entity.setType(pushTypeData.type);
            entity.setData(pushTypeData.data);
            if (pushTypeData.time > 0) {
                pushTypeData.time *= 1000;
                entity.setTime(pushTypeData.time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}
