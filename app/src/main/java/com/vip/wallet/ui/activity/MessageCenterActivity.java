package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.MessageCenterFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/13 14:37
 * 描述	     消息中心
 */
public class MessageCenterActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.amc_container, MessageCenterFragment.newInstance());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }
}
