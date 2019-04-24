package com.vip.wallet.ui.fragment;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/18 0018 14:11
 * 描述	      ${TODO}
 */

public class TestFragment extends BaseFragment {
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_test;
    }

    public static TestFragment newInstance(){
        return new TestFragment();
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }
}
