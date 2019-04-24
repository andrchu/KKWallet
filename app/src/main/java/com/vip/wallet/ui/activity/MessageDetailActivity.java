package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.PushMessage;
import com.vip.wallet.ui.fragment.MessageDetailFragment;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/13 15:04
 * 描述	     消息详情
 */
public class MessageDetailActivity extends BaseActivity {
    private PushMessage mPushMessage;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_message_detail;
    }


    public static void start(Context context, PushMessage pushMessage) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra(Constants.PUSH_MESSAGE, pushMessage);
        context.startActivity(intent);
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.PUSH_MESSAGE);
        if (serializableExtra != null)
            mPushMessage = (PushMessage) serializableExtra;
        loadRootFragment(R.id.amd_container, MessageDetailFragment.newInstance(mPushMessage));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.PUSH_MESSAGE);
        if (serializable != null)
            mPushMessage = (PushMessage) serializable;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.PUSH_MESSAGE, mPushMessage);
    }
}
