package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.adapter.DealRecordAdapter;
import com.vip.wallet.ui.contract.DealRecordContract;
import com.vip.wallet.ui.dialog.SelectCardDialog;
import com.vip.wallet.ui.presenter.DealRecordPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.wallet.WalletHelper;
import com.vip.wallet.widget.TitleBarView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 17:51
 * 描述	      ${TODO}
 */

public class DealRecordFragment extends BaseFragment<DealRecordContract.IDealRecordPresenter>
        implements DealRecordContract.IDealRecordView {
    @Bind(R.id.fdr_list)
    RecyclerView mFdrList;
    @Bind(R.id.fdr_pull_fresh)
    SwipeRefreshLayout mFdrPullFresh;
    @Bind(R.id.fdr_title_view)
    TitleBarView mFdrTitleView;
    private DealRecordAdapter mAdapter;
    private Card mCard;

    public static DealRecordFragment newInstance() {
        return new DealRecordFragment();
    }

    @Override
    protected View setSuccessView() {
        return mFdrPullFresh;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_deal_record;
    }

    @Override
    protected DealRecordContract.IDealRecordPresenter setPresenter() {
        return new DealRecordPresenter(this);
    }

    @Override
    public void initData(DealRecordContract.IDealRecordPresenter presenter) {
        mFdrList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DealRecordAdapter(R.layout.item_deal_record, null);
        mAdapter.setEnableLoadMore(true);
        mFdrList.setAdapter(mAdapter);
        mCard = WalletHelper.getFirstEthAddress();
        updateTitleText();
        presenter.loadData(mCard);
    }

    private void updateTitleText() {
        if (mCard != null) {
            mFdrTitleView.setTitleText(mCard.getName());
        }
    }

    @Override
    protected void initListener() {
        mFdrPullFresh.setOnRefreshListener(() -> getPresenter().fresh());
        mAdapter.setOnLoadMoreListener(() -> getPresenter().loadMore(), mFdrList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!ClickUtil.hasExecute())
                return;
            DealRecord item = mAdapter.getItem(position);
            WebActivity.startWebActivity(mContext, new BrowserInfo("交易详情", item.getDetailsUrl()));
        });
        mFdrTitleView.setOnButtonClickListener(v -> showSelectCardDialog());
    }

    private void showSelectCardDialog() {
        SelectCardDialog selectCardDialog = new SelectCardDialog(mContext);
        selectCardDialog.setCardOnButtonClickListener(card -> {
            mCard = card;
            getPresenter().loadData(mCard);
            updateTitleText();
        });
        selectCardDialog.setCurrentCard(mCard);
        selectCardDialog.show();
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void loadError(ApiHttpException exception) {
        toast(exception.getMessage());
        mFdrPullFresh.setRefreshing(false);
    }

    @Override
    public void loadSuccess(ArrayList<DealRecord> dealRecords) {
        mAdapter.setNewData(dealRecords);
        boolean b = mCard.chainType == 2;
        if (ListUtil.isEmpty(dealRecords) || (b ? dealRecords.size() < 30 : dealRecords.size() < Constants.LOAD_ITEM_RAW)) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadMoreSuccess(ArrayList<DealRecord> dealRecords) {
        mAdapter.getData().addAll(dealRecords);
        mAdapter.notifyDataSetChanged();
        boolean b = mCard.chainType == 2;
        if (ListUtil.isEmpty(dealRecords) || (b ? dealRecords.size() < 30 : dealRecords.size() < Constants.LOAD_ITEM_RAW)) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadMoreError(ApiHttpException exception) {
        toast(exception.getMessage());
        mAdapter.loadMoreFail();
    }

    @Override
    public void freshError(ApiHttpException exception) {
        mFdrPullFresh.setRefreshing(false);
        toast(exception.getMessage());
    }

    @Override
    public void freshSuccess(ArrayList<DealRecord> dealRecords) {
        mFdrPullFresh.setRefreshing(false);
        loadSuccess(dealRecords);
    }
}
