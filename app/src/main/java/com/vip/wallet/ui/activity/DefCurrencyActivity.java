package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.DefCurrencyFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 11:21
 * 描述	      ${TODO}
 */

public class DefCurrencyActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_def_currency;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.adc_container, DefCurrencyFragment.newInstance());
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
