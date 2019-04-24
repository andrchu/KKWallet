package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.view.jameson.library.BannerRecyclerView;
import com.view.jameson.library.BannerScaleHelper;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.EventMessage;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.adapter.PropertyCardAdapter;
import com.vip.wallet.ui.adapter.PropertyTokenListAdapter;
import com.vip.wallet.ui.contract.PropertyChainContract;
import com.vip.wallet.ui.presenter.PropertyChainPresenter;
import com.vip.wallet.utils.EventUtil;
import com.vip.wallet.utils.ListUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/15 0015 10:31
 * 描述	      ${TODO}
 */

public class PropertyChainFragment extends BaseFragment<PropertyChainContract.IPropertyChainPresenter>
        implements PropertyChainContract.IPropertyChainView {

    //    @Bind(R.id.fpc_card_name)
    //    TextView mFpcCardName;
    //    @Bind(R.id.fpc_show_property)
    //    CheckBox mFpcShowProperty;
    //    @Bind(R.id.fpc_amount)
    //    TextView mFpcAmount;
    //    @Bind(R.id.fpc_address)
    //    TextView mFpcAddress;
    //    @Bind(R.id.fpc_card_layout)
    LinearLayout mFpcCardLayout;
    @Bind(R.id.fpc_token_list)
    RecyclerView mFpcTokenList;
    @Bind(R.id.fpc_refresh)
    SwipeRefreshLayout mFpcRefresh;
    @Bind(R.id.fpc_top_card_list)
    BannerRecyclerView mFpcTopCardList;
    private PropertyTokenListAdapter mAdapter;
    private int chain_type;
    private WalletTokenInfos mWalletTokenInfos;
    private BannerScaleHelper mBannerScaleHelper;
    private PropertyCardAdapter mPropertyCardAdapter;

    @Override
    protected void init() {
        super.init();
        EventUtil.register(this);
    }

    @Override
    protected void destroy() {
        super.destroy();
        EventUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showProperty(EventMessage.UpdateShowProperty updateShowProperty) {
        if (mPropertyCardAdapter != null) {
            mPropertyCardAdapter.notifyDataSetChanged();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_property_chain;
    }

    public static PropertyChainFragment newInstance(int chain_type) {
        PropertyChainFragment propertyChainFragment = new PropertyChainFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CHAIN_TYPE, chain_type);
        propertyChainFragment.setArguments(args);
        return propertyChainFragment;
    }

    @Override
    protected PropertyChainContract.IPropertyChainPresenter setPresenter() {
        return new PropertyChainPresenter(this);
    }


    @Override
    public void initData(PropertyChainContract.IPropertyChainPresenter presenter) {
        int anInt = getArguments().getInt(Constants.CHAIN_TYPE, -1);
        if (anInt != -1)
            chain_type = anInt;
        List<Card> cards = getCards();
        if (ListUtil.isEmpty(cards)) {
            mFpcRefresh.setEnabled(false);
            return;
        }
        if (cards.size() == 1) {
            mFpcTopCardList.setScrollEnabled(false);
        } else {
            mFpcTopCardList.setScrollEnabled(true);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mFpcTopCardList.setLayoutManager(linearLayoutManager);
        mPropertyCardAdapter = new PropertyCardAdapter(cards);
        mFpcTopCardList.setAdapter(mPropertyCardAdapter);
        // mRecyclerView绑定scale效果
        if (cards.size() > 1) {
            mBannerScaleHelper = new BannerScaleHelper(6, 12);
            mBannerScaleHelper.setFirstItemPos(1000);
            mBannerScaleHelper.attachToRecyclerView(mFpcTopCardList);
        }

        mAdapter = new PropertyTokenListAdapter(R.layout.item_token, null);
        mFpcTokenList.setLayoutManager(new LinearLayoutManager(mContext));
        mFpcTokenList.setAdapter(mAdapter);

        mFpcRefresh.setRefreshing(true);
        getPresenter().loadDataByChainType(cards.get(0));

    }

    private List<Card> getCards() {
        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
        return cardDao.queryBuilder().where(CardDao.Properties.ChainType.eq(chain_type)).build().list();
    }

    @Override
    protected void initListener() {
        mFpcRefresh.setOnRefreshListener(() -> getPresenter().fresh());
        mFpcTopCardList.addOnPageChangeListener(position -> {
            List<Card> list = mPropertyCardAdapter.getList();
            LogUtils.i("card position >> " + position % list.size());
            Card card = list.get(mPropertyCardAdapter.getPosition(position));
            mFpcRefresh.setRefreshing(true);
            getPresenter().loadDataByChainType(card);
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(Constants.CHAIN_TYPE, chain_type);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        int anInt = savedInstanceState.getInt(Constants.CHAIN_TYPE, -1);
        if (anInt != -1)
            chain_type = anInt;
    }

    @Override
    public void loadSuccess(WalletTokenInfos walletTokenInfos, boolean b) {
        if (walletTokenInfos == null)
            return;
        mWalletTokenInfos = walletTokenInfos;
        mFpcRefresh.setRefreshing(b);
        mAdapter.setNewData(walletTokenInfos.token_infos);
        for (Card card : mPropertyCardAdapter.getList()) {
            if (card.getAddressOrAccount().equals(walletTokenInfos.address)) {
                card.amount = walletTokenInfos.getTotalAmount();
                card.isLoadEnd = true;
                mPropertyCardAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void loadError(ApiHttpException exception) {
        toast(exception.getMessage());
        mFpcRefresh.setRefreshing(false);
    }
}
