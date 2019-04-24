package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.DappBrowserInfo;
import com.vip.wallet.ui.activity.DappBrowserActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.adapter.CardAdapter;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/28 0028 12:52
 * 描述	      ${TODO}
 */

public class SelectCardFragment extends BaseFragment {
    @Bind(R.id.scf_recycler_view)
    RecyclerView mScfRecyclerView;
    private CardAdapter mCardAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_select_card;
    }

    public static SelectCardFragment newInstance(int chain_type, String url) {
        SelectCardFragment selectCardFragment = new SelectCardFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CHAIN_TYPE, chain_type);
        args.putString(Constants.DAPP_URL, url);
        selectCardFragment.setArguments(args);
        return selectCardFragment;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    private int chainType;
    private String url;

    @Override
    public void initData(IPresenter presenter) {
        int anInt = getArguments().getInt(Constants.CHAIN_TYPE, -1);
        String string = getArguments().getString(Constants.DAPP_URL);

        if (anInt != -1)
            chainType = anInt;
        if (!StringUtils.isEmpty(string)) {
            url = string;
        }
        mCardAdapter = new CardAdapter(R.layout.item_card_has_click, getCards());
        mScfRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mScfRecyclerView.setAdapter(mCardAdapter);
    }

    private List<Card> getCards() {
        return ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.ChainType.eq(chainType)).build().list();
    }

    @Override
    protected void initListener() {
        mCardAdapter.setOnItemClickListener((adapter, view, position) -> {
            Card item = mCardAdapter.getItem(position);
            back();
            if (url.contains(Constants.BASE_URL)) {
                WebActivity.startWebActivity(mContext, new BrowserInfo("", url + StringUtil.signBuilder(item.getAddressOrAccount())));
            } else
                DappBrowserActivity.start(mContext, new DappBrowserInfo(url, "", item));
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(Constants.CHAIN_TYPE, chainType);
        outState.putString(Constants.DAPP_URL, url);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        int anInt = savedInstanceState.getInt(Constants.CHAIN_TYPE, -1);
        String string = savedInstanceState.getString(Constants.DAPP_URL);
        if (anInt != -1)
            chainType = anInt;
        if (!StringUtils.isEmpty(string)) {
            url = string;
        }
    }
}
