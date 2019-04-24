package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.ui.fragment.ContactsManageFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/25 0025 14:52
 * 描述	      ${TODO}
 */

public class ContactsManageActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_contacts_manage;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        loadRootFragment(R.id.acm_container, ContactsManageFragment.newInstance());
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
