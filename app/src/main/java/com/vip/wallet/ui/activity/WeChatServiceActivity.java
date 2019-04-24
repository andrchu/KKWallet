package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.WeChatServiceFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 19:55
 * 描述	      ${TODO}
 */

public class WeChatServiceActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_we_chat_service;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.awcs_container, WeChatServiceFragment.newInstance());
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
