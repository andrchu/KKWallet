package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.ui.adapter.SelectAccountAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/31 0031 11:40
 * 描述	      ${TODO}
 */

public class SelectAccountFragment extends BaseFragment {
    @Bind(R.id.sa_list)
    RecyclerView mSaList;
    private SelectAccountAdapter mAdapter;

    public static SelectAccountFragment newInstance(ArrayList<String> list) {
        SelectAccountFragment selectAccountFragment = new SelectAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.ACCOUNT_LIST, list);
        selectAccountFragment.setArguments(args);
        return selectAccountFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_select_account;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    ArrayList<String> mList = new ArrayList<>();

    @Override
    public void initData(IPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.ACCOUNT_LIST);
        if (serializable != null)
            mList = (ArrayList<String>) serializable;
        setFragmentResult(102, null);
        mSaList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SelectAccountAdapter(R.layout.item_select_account, mList);
        mSaList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String s = mList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCOUNT, s);
            setFragmentResult(101, bundle);
            pop();
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.ACCOUNT_LIST, mList);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.ACCOUNT_LIST);
        if (serializable != null)
            mList = (ArrayList<String>) serializable;
    }
}
