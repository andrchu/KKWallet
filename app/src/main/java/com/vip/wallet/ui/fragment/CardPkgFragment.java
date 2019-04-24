package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.CardChain;
import com.vip.wallet.entity.CardItem;
import com.vip.wallet.entity.Chain;
import com.vip.wallet.ui.adapter.CardGroupAdapter;
import com.vip.wallet.ui.dialog.SelectChainDialog;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 17:32
 * 描述	      ${TODO}
 */

public class CardPkgFragment extends BaseFragment {
    @Bind(R.id.fcp_recycler_view)
    RecyclerView mFcpList;
    @Bind(R.id.fcp_title)
    TitleBarView mFcpTitle;

    private Chain mChain = new Chain(0);
    private CardGroupAdapter mCardGroupAdapter;
    private List<CardChain> mCardList;

    public static CardPkgFragment newInstance() {
        return new CardPkgFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_card_pkg;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        RecyclerView.ItemAnimator animator = mFcpList.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mCardList = getCardList();
        mCardGroupAdapter = new CardGroupAdapter(mCardList);
        mFcpList.setLayoutManager(layoutManager);
        mFcpList.setAdapter(mCardGroupAdapter);
        mCardGroupAdapter.toggleGroup(0);
        mCardGroupAdapter.toggleGroup(mCardGroupAdapter.getGroups().get(0).getItems().size() + 1);
        mCardGroupAdapter.toggleGroup(mCardGroupAdapter.getGroups().get(0).getItems().size() + mCardGroupAdapter.getGroups().get(1).getItems().size() + 2);
    }

    private List<CardChain> getCardList() {
        List<CardChain> list = new ArrayList<>();
        list.add(new CardChain("ETH", new ArrayList<>(), 0));
        list.add(new CardChain("BTC", new ArrayList<>(), 1));
        list.add(new CardChain("EOS", new ArrayList<>(), 2));
        for (CardChain cardChain : list) {
            List<Card> cards = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().
                    where(CardDao.Properties.ChainType.eq(cardChain.chain_type)).build().list();
            for (Card card : cards) {
                cardChain.getItems().add(new CardItem(card.chainType, card.name, card.defAddress, card.accountName));
            }
        }
        return list;
    }

    @Override
    protected void initListener() {
        mCardGroupAdapter.setOnChildClickListener(cardItem -> {
            LogUtils.i("card_name >> " + cardItem.card_name);
            if (!ClickUtil.hasExecute())
                return;
            List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.DefAddress.eq(cardItem.address)).build().list();
            startForResult(ManageCardFragment.newInstance(list.get(0)), 1);
        });
        mFcpTitle.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            showSelectChainDialog();
        });
        mCardGroupAdapter.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {
                LogUtils.i("flatPos >> " + flatPos);
                return false;
            }
        });
        mCardGroupAdapter.setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                //展开
                if (group.getTitle().equals("EOS")) {
                    mFcpList.scrollToPosition(mCardGroupAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {
                //收缩

            }
        });
    }

    private void showSelectChainDialog() {
        SelectChainDialog selectChainDialog = new SelectChainDialog(mContext);
        selectChainDialog.setCurrentItem(mChain);
        selectChainDialog.setTitleText(getString(R.string.select_card_chain));
        selectChainDialog.setOnConfirmClickListener(chain -> {
            mChain = chain;
            startForResult(ImportCardFragment.newInstance(mChain.index), 101);
        });
        selectChainDialog.show();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (data == null || data.getSerializable(Constants.CARD) == null)
            return;
        Serializable serializable = data.getSerializable(Constants.CARD);
        Card card = (Card) serializable;
        switch (resultCode) {
            case 100:
                List items = mCardGroupAdapter.getGroups().get(card.chainType).getItems();
                CardItem obj = ListUtil.getObj(items, new CardItem(card.chainType, card.name, card.defAddress, card.accountName));
                obj.card_name = card.name;
                mCardGroupAdapter.notifyDataSetChanged();
                break;
            case 101:
                mCardGroupAdapter.getGroups().get(card.chainType).getItems().add(new CardItem(card.chainType, card.name, card.defAddress, card.accountName));
                mCardGroupAdapter.notifyDataSetChanged();
                //判断是否展开，没有展开自动展开
                int position = 0;
                for (int i = 0; i < card.chainType; i++) {
                    if (!mCardGroupAdapter.isGroupExpanded(i + position)) {
                        position += i + 1;
                    } else {
                        position += mCardGroupAdapter.getGroups().get(i).getItems().size() + 1;
                    }
                }
                if (!mCardGroupAdapter.isGroupExpanded(position)) {
                    //未展开自动展开
                    mCardGroupAdapter.toggleGroup(position);
                }
                break;
            case 102:
                mCardGroupAdapter.getGroups().get(card.chainType).getItems().remove(new CardItem(card.chainType, card.name, card.defAddress, card.accountName));
                mCardGroupAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.CHAIN, mChain);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.CHAIN);
        if (serializable != null)
            mChain = (Chain) serializable;
    }
}
