package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.ReceiveSendFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 17:30
 * 描述	      ${TODO}
 */

public class ReceiveSendTokenActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_receive_send_token;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.arst_container, ReceiveSendFragment.newInstance());
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
