package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.adapter.TokenListAdapter;
import com.vip.wallet.ui.contract.SelectTokenContract;
import com.vip.wallet.ui.presenter.SelectTokenPresenter;
import com.vip.wallet.utils.ListUtil;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 15:43
 * 描述	      选择代币
 */

public class SelectTokenFragment extends BaseFragment<SelectTokenContract.ISelectTokenPresenter>
        implements SelectTokenContract.ISelectTokenView {

    public static final int RESULT_CODE = 1001;
    @Bind(R.id.fst_token_list)
    RecyclerView mFstTokenList;
    @Bind(R.id.fst_pull_fresh)
    SwipeRefreshLayout mFstPullFresh;
    private TokenListAdapter mAdapter;
    private String address;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_select_token;
    }

    @Override
    protected SelectTokenContract.ISelectTokenPresenter setPresenter() {
        return new SelectTokenPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mFstPullFresh.setColorSchemeResources(R.color.title_bar_bg, R.color.title_bar_bg_pr);
    }

    @Override
    public void initData(SelectTokenContract.ISelectTokenPresenter presenter) {
        String string = getArguments().getString(Constants.ADDRESS);
        if (!StringUtils.isEmpty(string))
            this.address = string;
        mFstTokenList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new TokenListAdapter(R.layout.item_token, null);
        mFstTokenList.setAdapter(mAdapter);
        mFstPullFresh.setRefreshing(true);
        presenter.loadData(address);
    }

    public static SelectTokenFragment newInstance(String address) {
        SelectTokenFragment selectTokenFragment = new SelectTokenFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ADDRESS, address);
        selectTokenFragment.setArguments(args);
        return selectTokenFragment;
    }

    @Override
    protected void initListener() {
        mFstPullFresh.setOnRefreshListener(() -> getPresenter().loadData());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TOKEN, mAdapter.getItem(position));
            setFragmentResult(RESULT_CODE, bundle);
            pop();
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putString(Constants.ADDRESS, address);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        String string = savedInstanceState.getString(Constants.ADDRESS);
        if (!StringUtils.isEmpty(string))
            this.address = string;
    }

    @Override
    public void loadSuccess(WalletTokenInfos serializable, boolean isCache) {
        if (!isCache)
            mFstPullFresh.setRefreshing(false);
        if (ListUtil.isEmpty(serializable.token_infos))
            return;
        mAdapter.setNewData(serializable.token_infos);
    }

    @Override
    public void loadError(ApiHttpException exception) {
        mFstPullFresh.setRefreshing(false);
    }
}
