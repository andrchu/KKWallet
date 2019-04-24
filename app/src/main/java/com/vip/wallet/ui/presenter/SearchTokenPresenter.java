package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Token;
import com.vip.wallet.dao.TokenDao;
import com.vip.wallet.entity.SearchToken;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.SearchTokenContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/3 0003 14:45
 * 描述	      ${TODO}
 */

public class SearchTokenPresenter extends RxPresenter<SearchTokenContract.ISearchTokenView> implements SearchTokenContract.ISearchTokenPresenter {

    private Subscription mAutoSearchSubscription;
    private Subscription mSearchSubscription;
    private Subscription mProcessDataSubscribe;

    public SearchTokenPresenter(SearchTokenContract.ISearchTokenView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void autoSearch(String searchKey) {
        autoSearch(searchKey, 800);
    }

    @Override
    public void autoSearch(String searchKey, long delay) {
        if (mAutoSearchSubscription != null)
            mAutoSearchSubscription.unsubscribe();
        mAutoSearchSubscription = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        search(searchKey);
                    }
                });
        addSubscribe(mAutoSearchSubscription);
    }

    @Override
    public void search(String searchKey) {
        if (mSearchSubscription != null)
            mSearchSubscription.unsubscribe();
        if (mProcessDataSubscribe != null)
            mProcessDataSubscribe.unsubscribe();

        if (StringUtils.isEmpty(searchKey)) {
            view.showView(Constants.EMPTY);
            return;
        } else
            view.showView(Constants.LOADING);

        mSearchSubscription = HttpRequest.getInstance().searchToken(searchKey)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<SearchToken>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.showView(Constants.ERROR, exception);
                    }

                    @Override
                    public void onNext(ArrayList<SearchToken> searchTokens) {
                        if (ListUtil.isEmpty(searchTokens)) {
                            view.showView(Constants.EMPTY);
                        } else {
                            processData(searchTokens);
                        }
                    }
                });

        addSubscribe(mSearchSubscription);
    }

    private void processData(ArrayList<SearchToken> searchTokens) {
        if (mProcessDataSubscribe != null)
            mProcessDataSubscribe.unsubscribe();
        mProcessDataSubscribe = Observable.unsafeCreate((Observable.OnSubscribe<ArrayList<SearchToken>>) subscriber -> {
            TokenDao tokenDao = ScApplication.getInstance().getDaoSession().getTokenDao();
            for (SearchToken searchToken : searchTokens) {
                List<Token> list = tokenDao.queryBuilder().where(TokenDao.Properties.ContractAddress.eq(searchToken.address)).build().list();
                searchToken.isAdd = !ListUtil.isEmpty(list);
            }
            subscriber.onNext(searchTokens);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<ArrayList<SearchToken>>() {
                    @Override
                    public void onNext(ArrayList<SearchToken> searchTokens) {
                        view.showView(Constants.SUCCESS);
                        view.searchSuccess(searchTokens);
                    }
                });

        addSubscribe(mProcessDataSubscribe);

    }
}
