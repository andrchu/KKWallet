package com.vip.wallet.ui.activity;

import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.ui.fragment.BackUpSeedKeyFragment;
import com.vip.wallet.wallet.WalletHelper;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 16:38
 * 描述	      ${TODO}
 */

public class BackUpSeedKeyActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_back_up_seed_key;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.CardType.eq(1)).build().list();
        loadRootFragment(R.id.abusk_container, BackUpSeedKeyFragment.newInstance(WalletHelper.decrypt(list.get(0).getSeedKey())));
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void fragmentCallBack(FragmentCallBack fragmentCallBack) {
        super.fragmentCallBack(fragmentCallBack);
        finish();
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }
}
