package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.ui.fragment.SelectCardFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/28 0028 12:49
 * 描述	      ${TODO}
 */

public class SelectCardActivity extends BaseActivity {

    public static void start(Context context, int chainType, String url) {
        Intent intent = new Intent(context, SelectCardActivity.class);
        intent.putExtra(Constants.CHAIN_TYPE, chainType);
        intent.putExtra(Constants.DAPP_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_card;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    private int chainType;
    private String url;

    @Override
    protected void initData(IPresenter presenter) {

        int intExtra = getIntent().getIntExtra(Constants.CHAIN_TYPE, -1);
        String stringExtra = getIntent().getStringExtra(Constants.DAPP_URL);
        if (intExtra != -1)
            chainType = intExtra;
        if (!StringUtils.isEmpty(stringExtra))
            url = stringExtra;

        loadRootFragment(R.id.asc_container, SelectCardFragment.newInstance(chainType,url));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        int anInt = savedInstanceState.getInt(Constants.CHAIN_TYPE, -1);
        String string = savedInstanceState.getString(Constants.DAPP_URL);
        if (anInt != -1) {
            chainType = anInt;
        }
        if (!StringUtils.isEmpty(string)) {
            url = string;
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(Constants.CHAIN_TYPE, chainType);
        outState.putString(Constants.DAPP_URL, url);
    }
}
