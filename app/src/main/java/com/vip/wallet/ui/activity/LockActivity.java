package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.ui.fragment.LockFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 17:10
 * 描述	      ${TODO}
 */

public class LockActivity extends BaseActivity {

    public static final int RESULT_SUCCESS = 101;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pwd;
    }

    public static void startPwdActivity(Context context, int option_type) {
        Intent intent = new Intent(context, LockActivity.class);
        intent.putExtra(Constants.START_TYPE, option_type);
        context.startActivity(intent);
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void fragmentCallBack(FragmentCallBack fragmentCallBack) {
        if (fragmentCallBack.type == LockFragment.UPDATE_PWD)
            toast(getString(R.string.pwd_update_success));
        setResult(RESULT_SUCCESS);
        finish();
    }

    private int optionType = -1;

    @Override
    protected void initData(IPresenter presenter) {
        int intExtra = getIntent().getIntExtra(Constants.START_TYPE, -1);
        if (intExtra != -1) {
            optionType = intExtra;
            loadRootFragment(R.id.ap_container, LockFragment.newInstance(optionType));
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        int intExtra = savedInstanceState.getInt(Constants.START_TYPE, -1);
        if (intExtra != -1)
            optionType = intExtra;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(Constants.START_TYPE, optionType);
    }
}
