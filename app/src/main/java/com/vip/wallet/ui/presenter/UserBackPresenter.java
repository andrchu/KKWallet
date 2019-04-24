package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.entity.Response;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.UserBackContract;
import com.vip.wallet.utils.RxUtil;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/24 0024 16:36
 * 描述	      ${TODO}
 */

public class UserBackPresenter extends RxPresenter<UserBackContract.IUserBackView> implements UserBackContract.IUserBackPresenter {
    public UserBackPresenter(UserBackContract.IUserBackView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void send(String contact, String content) {
        Subscription subscribe = HttpRequest.getInstance().userAdvice(contact, content).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.sendError(exception);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            view.sendSuccess(response.message);
                        } else {
                            onError(new ApiHttpException(response.message, response.code));
                        }
                    }
                });
        addSubscribe(subscribe);
    }
}
