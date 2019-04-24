package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.ui.fragment.EosAuthFragment;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/30 0030 16:09
 * 描述	      ${TODO}
 */

public class EosAuthActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_eos_auth;
    }

    public static void startEosAuthActivity(Context context, EosAuthf eosAuthf) {
        Intent intent = new Intent(context, EosAuthActivity.class);
        intent.putExtra(Constants.EOS_AUTH_INFO, eosAuthf);
        context.startActivity(intent);
    }


    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.EOS_AUTH_INFO);
        EosAuthf eosAuthf = null;
        if (serializableExtra != null)
            eosAuthf = (EosAuthf) serializableExtra;
        loadRootFragment(R.id.eos_auth_container, EosAuthFragment.newInstance(eosAuthf));
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
