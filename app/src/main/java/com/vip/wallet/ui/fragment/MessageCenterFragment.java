package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.PushMessage;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.adapter.MessageCenterAdapter;
import com.vip.wallet.ui.contract.MessageCenterContract;
import com.vip.wallet.ui.dialog.DefHintDialog;
import com.vip.wallet.ui.presenter.MessageCenterPresenter;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.widget.TitleBarView;

import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 11:39
 * 描述	      ${TODO}
 */

public class MessageCenterFragment extends BaseFragment<MessageCenterContract.IMessageCenterPresenter> implements MessageCenterContract.IMessageCenterView {
    @Bind(R.id.fmc_title)
    TitleBarView mFmcTitle;
    @Bind(R.id.fmc_list)
    RecyclerView mFmcList;
    private MessageCenterAdapter mAdapter;
    private View mEmptyView;

    public static MessageCenterFragment newInstance() {
        return new MessageCenterFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_message_center;
    }

    @Override
    protected View setSuccessView() {
        return mFmcList;
    }

    @Override
    protected View setEmptyView() {
        if (mEmptyView == null)
            mEmptyView = View.inflate(mContext, R.layout.layout_message_empty, null);
        return mEmptyView;
    }

    @Override
    protected MessageCenterContract.IMessageCenterPresenter setPresenter() {
        return new MessageCenterPresenter(this);
    }

    @Override
    public void initData(MessageCenterContract.IMessageCenterPresenter presenter) {
        mFmcList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MessageCenterAdapter(R.layout.item_message_center, null);
        mAdapter.setEnableLoadMore(true);
        mFmcList.setAdapter(mAdapter);
        getPresenter().loadData();
    }

    @Override
    protected void initListener() {
        mAdapter.setOnLoadMoreListener(() -> getPresenter().loadMore(), mFmcList);
        mFmcTitle.setOnButtonClickListener(v -> signAll());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PushMessage item = mAdapter.getItem(position);
            switch (item.type) {
                case 0:
                    gotoMessageDetail(item);
                    break;
                case 1:
                    gotoDealDetail(item);
                    break;
                default:
                    gotoMessageDetail(item);
                    break;
            }
            item.isRead = true;
            ScApplication.getInstance().getDaoSession().getPushMessageDao().update(item);
            mAdapter.notifyItemChanged(position);
        });
    }


    /**
     * 跳转至消息详情
     *
     * @param pushMessage
     */
    private void gotoMessageDetail(PushMessage pushMessage) {
        start(MessageDetailFragment.newInstance(pushMessage));
    }

    /**
     * 跳转交易详情
     *
     * @param pushMessage
     */
    private void gotoDealDetail(PushMessage pushMessage) {
        try {
            DealRecord dealRecord = GsonAdapter.getGson().fromJson(pushMessage.data, DealRecord.class);
            WebActivity.startWebActivity(mContext, new BrowserInfo("交易详情", dealRecord.getDetailsUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    /**
     * 全部标记
     */
    private void signAll() {
        if (ListUtil.isEmpty(mAdapter.getData())) {
            toast("暂无消息");
            return;
        }

        DefHintDialog dialog = new DefHintDialog(mContext).setTitle(getString(R.string.sign_read)).setViceTitle(getString(R.string.sign_hint));
        dialog.setConfirmClickListener(() -> {
            showLoadingDialog(getString(R.string.optioning));
            getPresenter().signAll();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void loadSuccess(List<PushMessage> pushMessages) {
        mAdapter.setNewData(pushMessages);
        if (pushMessages.size() < MessageCenterPresenter.LIMIT) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void signAllSuccess() {
        hideLoadingDialog();
        mAdapter.notifyDataSetChanged();
        toast(getString(R.string.sign_success));
    }

    @Override
    public void loadMoreSuccess(List<PushMessage> pushMessages) {
        if (!ListUtil.isEmpty(pushMessages)) {
            mAdapter.getData().addAll(pushMessages);
            mAdapter.notifyDataSetChanged();
        }
        if (ListUtil.isEmpty(pushMessages) || pushMessages.size() < MessageCenterPresenter.LIMIT) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }
}
