package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.PushMessage;

import java.io.Serializable;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 14:32
 * 描述	      ${TODO}
 */

public class MessageDetailFragment extends BaseFragment {
    @Bind(R.id.mda_title)
    TextView mMdaTitle;
    @Bind(R.id.mda_time)
    TextView mMdaTime;
    @Bind(R.id.mda_content)
    TextView mMdaContent;
    private PushMessage mPushMessage;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_message_detail;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    public static MessageDetailFragment newInstance(PushMessage message) {
        MessageDetailFragment messageDetailFragment = new MessageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PUSH_MESSAGE, message);
        messageDetailFragment.setArguments(args);
        return messageDetailFragment;
    }

    @Override
    public void initData(IPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.PUSH_MESSAGE);
        if (serializable != null) {
            mPushMessage = (PushMessage) serializable;
        }
        updateUI();
    }

    @Override
    protected void initListener() {

    }

    private void updateUI() {
        if (mPushMessage == null)
            return;
        mMdaTitle.setText(mPushMessage.title);
        mMdaContent.setText(mPushMessage.content);
        mMdaTime.setText(TimeUtils.millis2String(mPushMessage.time));
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.PUSH_MESSAGE, mPushMessage);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.PUSH_MESSAGE);
        if (serializable != null)
            mPushMessage = (PushMessage) serializable;
        updateUI();
    }
}
