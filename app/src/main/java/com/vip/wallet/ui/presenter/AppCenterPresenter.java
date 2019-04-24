package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.AppCenterContract;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/1 0001 14:22
 * 描述	      ${TODO}
 */

public class AppCenterPresenter extends RxPresenter<AppCenterContract.IAppCenterView> implements AppCenterContract.IAppCenterPresenter {
    public AppCenterPresenter(AppCenterContract.IAppCenterView view) {
        super(view);
    }

    @Override
    public void loadData() {
        view.showView(Constants.LOADING);
        Subscription subscribe = HttpRequest.getInstance().getAppCenterItems()
                .compose(RxUtil.rxSchedulerHelper(500))
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<AppCenterItem>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.showView(Constants.ERROR, exception);
                    }

                    @Override
                    public void onNext(ArrayList<AppCenterItem> appCenterItems) {
                        view.showView(Constants.SUCCESS);
                        view.loadSuccess(appCenterItems);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void fresh() {
        Subscription subscribe = HttpRequest.getInstance().getAppCenterItems()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<AppCenterItem>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.freshError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<AppCenterItem> appCenterItems) {
                        view.freshSuccess(appCenterItems);
                    }
                });
        addSubscribe(subscribe);
    }
}
