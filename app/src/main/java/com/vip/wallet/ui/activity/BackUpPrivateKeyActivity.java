package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.ui.fragment.ExportPrivateKeyFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 16:50
 * 描述	      ${TODO}
 */

public class BackUpPrivateKeyActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_back_private_key;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        String privateKey = ScApplication.getInstance().getEthWallet().getPrivateKey();
        loadRootFragment(R.id.abpk_container, ExportPrivateKeyFragment.newInstance(privateKey));
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
