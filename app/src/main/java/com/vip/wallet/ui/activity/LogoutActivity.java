package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ActivityManage;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.DaoSession;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.ui.fragment.LockFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 19:44
 * 描述	      ${TODO}
 */

public class LogoutActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_log_out;
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
        if (findFragment(LockFragment.class) == null) {
            loadRootFragment(R.id.alg_container, LockFragment.newInstance(LockFragment.INPUT_PWD));
        }
    }

    @Override
    public void fragmentCallBack(FragmentCallBack fragmentCallBack) {
        super.fragmentCallBack(fragmentCallBack);
        ScApplication.getInstance().getConfig().logOut();
        DaoSession daoSession = ScApplication.getInstance().getDaoSession();
        daoSession.getCardDao().deleteAll();
        daoSession.getAddressDao().deleteAll();
        startActivity(InitWalletActivity.class, true);
        ActivityManage.getInstance().exit();
        toast(getString(R.string.log_out_success));
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
