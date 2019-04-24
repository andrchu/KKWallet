package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.AboutWeFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 19:03
 * 描述	      ${TODO}
 */

public class AboutWeActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_abouot_we;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.aaw_continer, AboutWeFragment.newInstance());
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
