package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.ui.adapter.PropertyAdapter;
import com.vip.wallet.ui.contract.PropertyContract;
import com.vip.wallet.ui.presenter.PropertyPresenter;
import com.vip.wallet.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.vip.wallet.R.id.fp_smart_tab_layout;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 16:11
 * 描述	      ${TODO}
 */

public class PropertyFragment extends BaseFragment<PropertyContract.IPropertyPresenter> implements PropertyContract.IPropertyView {

    @Bind(R.id.fp_viewPager)
    ViewPager mFpViewPager;
    @Bind(fp_smart_tab_layout)
    SmartTabLayout mFpSmartTabLayout;
    @Bind(R.id.fp_title_view)
    TitleBarView mFpTitleView;
    @Bind(R.id.fp_layout)
    RelativeLayout mFpLayout;

    public static PropertyFragment newInstance() {
        return new PropertyFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_property;
    }

    @Override
    protected PropertyContract.IPropertyPresenter setPresenter() {
        return new PropertyPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mFpLayout.getLayoutParams().height = mFpTitleView.getTitleViewHeight();
        mFpSmartTabLayout.getLayoutParams().height = SizeUtils.dp2px(mFpTitleView.titleHeight - 4);
        mFpViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void initData(PropertyContract.IPropertyPresenter presenter) {
        List<BaseFragment> data = new ArrayList<>();
        data.add(PropertyChainFragment.newInstance(0));
        data.add(PropertyChainFragment.newInstance(1));
        data.add(PropertyChainFragment.newInstance(2));
        List<String> titles = new ArrayList<>();
        titles.add("ETH");
        titles.add("BTC");
        titles.add("EOS");
        PropertyAdapter adapter = new PropertyAdapter(getFragmentManager(), data, titles);
        mFpViewPager.setAdapter(adapter);
        mFpSmartTabLayout.setViewPager(mFpViewPager);
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
