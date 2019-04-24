package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.ScApplication;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 10:46
 * 描述	      ${TODO}
 */

public class DefCurrencyFragment extends BaseFragment {


    @Bind(R.id.fdc_cny)
    RadioButton mFdcCny;
    @Bind(R.id.fdc_usd)
    RadioButton mFdcUsd;
    @Bind(R.id.fdc_rg)
    RadioGroup mFdcRg;

    public static DefCurrencyFragment newInstance() {
        return new DefCurrencyFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_def_currency;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        updateUI();
    }

    private void updateUI() {
        Config config = ScApplication.getInstance().getConfig();
        if (config.getCurrency_unit() == 0)
            mFdcCny.setChecked(true);
        else
            mFdcUsd.setChecked(true);
    }

    @Override
    protected void initListener() {
        mFdcRg.setOnCheckedChangeListener((group, checkedId) ->
                ScApplication.getInstance().getConfig().setCurrency_unit(checkedId == mFdcCny.getId() ? 0 : 1));
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }
}
