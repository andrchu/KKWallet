package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.DealRecordContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 18:46
 * 描述	      ${TODO}
 */

public class DealRecordPresenter extends RxPresenter<DealRecordContract.IDealRecordView>
        implements DealRecordContract.IDealRecordPresenter {

    private int page = 1;

    private Card mCard;
    private Subscription mSubscribe;
    private Subscription mSubscribeLoadMore;

    public DealRecordPresenter(DealRecordContract.IDealRecordView view) {
        super(view);
    }

    @Override
    public void loadData() {
        loadData(mCard);
    }

    @Override
    public void loadMore() {
        if (mSubscribeLoadMore != null)
            mSubscribeLoadMore.unsubscribe();
        mSubscribeLoadMore = HttpRequest.getInstance().getDealRecords(mCard.getAddressOrAccount(), page, Constants.LOAD_ITEM_RAW, mCard.chainType)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<DealRecord>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.loadMoreError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<DealRecord> dealRecords) {
                        page++;
                        view.loadMoreSuccess(dealRecords);
                    }
                });
        addSubscribe(mSubscribeLoadMore);
    }

    @Override
    public void loadData(Card card) {
        page = 1;
        mCard = card;
        if (mSubscribe != null)
            mSubscribe.unsubscribe();
        if (mSubscribeLoadMore != null)
            mSubscribeLoadMore.unsubscribe();
        view.showView(Constants.LOADING);
        mSubscribe = HttpRequest.getInstance().getDealRecords(card.getAddressOrAccount(), page, Constants.LOAD_ITEM_RAW, mCard.chainType)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<DealRecord>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.loadError(exception);
                        view.showView(Constants.ERROR,exception);
                    }

                    @Override
                    public void onNext(ArrayList<DealRecord> dealRecords) {
                        if (ListUtil.isEmpty(dealRecords)) {
                            view.showView(Constants.EMPTY);
                        } else {
                            page++;
                            view.showView(Constants.SUCCESS);
                            view.loadSuccess(dealRecords);
                        }
                    }
                });
        addSubscribe(mSubscribe);
    }

    @Override
    public void fresh() {
        page = 1;
        if (mSubscribe != null)
            mSubscribe.unsubscribe();
        if (mSubscribeLoadMore != null)
            mSubscribeLoadMore.unsubscribe();
        mSubscribe = HttpRequest.getInstance().getDealRecords(mCard.getAddressOrAccount(), page, Constants.LOAD_ITEM_RAW, mCard.chainType)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<DealRecord>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.freshError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<DealRecord> dealRecords) {
                        view.freshSuccess(dealRecords);
                    }
                });
        addSubscribe(mSubscribe);
    }
}
