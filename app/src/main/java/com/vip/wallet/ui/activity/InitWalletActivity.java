package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.ui.fragment.InitWalletFragment;
import com.vip.wallet.ui.fragment.LockFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 18:59
 * 描述	      ${TODO}
 */

public class InitWalletActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_init_wallet;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        ScApplication.getInstance().resetConfig();
        Config config = ScApplication.getInstance().getConfig();
        LogUtils.i("test >> init::" + config.isInit());
        if (!config.isInit()) {
            loadRootFragment(R.id.aiw_container, InitWalletFragment.newInstance());
        } else if (StringUtils.isEmpty(config.getPwd())) {
            loadRootFragment(R.id.aiw_container, LockFragment.newInstance(LockFragment.SET_PWD));
        }
    }

    @Override
    public void fragmentCallBack(FragmentCallBack fragmentCallBack) {
        HomeActivity.start(this, HomeActivity.INIT_TYPE);
        finish();
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
