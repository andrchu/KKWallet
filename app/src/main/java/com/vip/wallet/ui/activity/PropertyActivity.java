package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.PropertyFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 16:52
 * 描述	      ${TODO}
 */

public class PropertyActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_property;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        loadRootFragment(R.id.apy_container, PropertyFragment.newInstance());
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
