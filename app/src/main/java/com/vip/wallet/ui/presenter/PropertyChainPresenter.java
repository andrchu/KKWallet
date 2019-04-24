package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.CacheUtils;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.PropertyChainContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/15 0015 14:27
 * 描述	      ${TODO}
 */

public class PropertyChainPresenter extends RxPresenter<PropertyChainContract.IPropertyChainView>
        implements PropertyChainContract.IPropertyChainPresenter {

    private Subscription mSubscribe;

    public PropertyChainPresenter(PropertyChainContract.IPropertyChainView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    private Card mCard;

    @Override
    public void loadDataByChainType(Card card) {
        this.mCard = card;
        //获取缓存
        if (mSubscribe != null) {
            mSubscribe.unsubscribe();
        }
        Object serializable = CacheUtils.getInstance().getSerializable(Constants.PROPERTY + card.getDefAddress());
        if (serializable != null) {
            view.loadSuccess((WalletTokenInfos) serializable, true);
        }
        mSubscribe = HttpRequest.getInstance().getAllWalletTokenInfos(card.getAddressOrAccount(), card.chainType)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<WalletTokenInfos>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.loadError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<WalletTokenInfos> walletTokenInfoses) {
                        if (!ListUtil.isEmpty(walletTokenInfoses)) {
                            WalletTokenInfos wf = walletTokenInfoses.get(0);
                            view.loadSuccess(wf, false);
                            CacheUtils.getInstance().put(Constants.PROPERTY + card.getDefAddress(), wf);
                        } else {
                            onError(new Exception("加载失败"));
                        }
                    }
                });
        addSubscribe(mSubscribe);
    }

    @Override
    public void fresh() {
        loadDataByChainType(mCard);
    }

    @Override
    public void loadCache(Card card) {
        mCard = card;
        if (mSubscribe != null) {
            mSubscribe.unsubscribe();
        }
        Object serializable = CacheUtils.getInstance().getSerializable(Constants.PROPERTY + card.getDefAddress());
        if (serializable != null) {
            view.loadSuccess((WalletTokenInfos) serializable, false);
        }
    }
}
