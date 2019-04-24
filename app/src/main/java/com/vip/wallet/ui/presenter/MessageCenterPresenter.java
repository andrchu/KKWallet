package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.PushMessage;
import com.vip.wallet.dao.PushMessageDao;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.MessageCenterContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 17:09
 * 描述	      ${TODO}
 */

public class MessageCenterPresenter extends RxPresenter<MessageCenterContract.IMessageCenterView> implements MessageCenterContract.IMessageCenterPresenter {

    public static final int LIMIT = 100;
    private Subscription mSubscribe;
    private int page = 0;

    public MessageCenterPresenter(MessageCenterContract.IMessageCenterView view) {
        super(view);
    }

    @Override
    public void loadData() {
        page = 0;
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<List<PushMessage>>) subscriber -> {
            List<PushMessage> pushMessages = ScApplication.getInstance()
                    .getDaoSession()
                    .getPushMessageDao()
                    .queryBuilder()
                    .orderDesc(PushMessageDao.Properties.Time)
                    .offset(page * LIMIT)
                    .limit(LIMIT)
                    .list();

            subscriber.onNext(pushMessages);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<List<PushMessage>>() {
                    @Override
                    public void onNext(List<PushMessage> pushMessages) {
                        if (ListUtil.isEmpty(pushMessages)) {
                            view.showView(Constants.EMPTY);
                        } else {
                            page++;
                            view.showView(Constants.SUCCESS);
                            view.loadSuccess(pushMessages);
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void loadMore() {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<List<PushMessage>>) subscriber -> {
            List<PushMessage> pushMessages = ScApplication.getInstance()
                    .getDaoSession()
                    .getPushMessageDao()
                    .queryBuilder()
                    .orderDesc(PushMessageDao.Properties.Time)
                    .offset(page * LIMIT)
                    .limit(LIMIT)
                    .list();

            subscriber.onNext(pushMessages);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper(1000))
                .subscribe(new SimpSubscriber<List<PushMessage>>() {
                    @Override
                    public void onNext(List<PushMessage> pushMessages) {
                        page++;
                        view.loadMoreSuccess(pushMessages);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void signAll() {
        if (mSubscribe != null)
            mSubscribe.unsubscribe();

        mSubscribe = Observable.unsafeCreate((Observable.OnSubscribe<List<PushMessage>>) subscriber -> {
            PushMessageDao pushMessageDao = ScApplication.getInstance().getDaoSession().getPushMessageDao();
            List<PushMessage> pushMessages = pushMessageDao.queryBuilder().where(PushMessageDao.Properties.IsRead.eq("FALSE")).list();
            for (PushMessage pushMessage : pushMessages) {
                pushMessage.setIsRead(true);
                pushMessageDao.update(pushMessage);
            }
            subscriber.onNext(null);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper(200))
                .subscribe(new SimpSubscriber<List<PushMessage>>() {
                    @Override
                    public void onNext(List<PushMessage> pushMessages) {
                        view.signAllSuccess();
                    }
                });
        addSubscribe(mSubscribe);

    }

}
