package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.CacheUtils;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.SelectTokenContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 16:07
 * 描述	      ${TODO}
 */

public class SelectTokenPresenter extends RxPresenter<SelectTokenContract.ISelectTokenView>
        implements SelectTokenContract.ISelectTokenPresenter {

    public SelectTokenPresenter(SelectTokenContract.ISelectTokenView view) {
        super(view);
    }

    @Override
    public void loadData() {
        loadData(address);
    }

    private String address;

    @Override
    public void loadData(String address) {
        this.address = address;
        //获取缓存
        Object serializable = CacheUtils.getInstance().getSerializable(Constants.ETH_TOKEN_LIST + address);
        if (serializable != null) {
            view.loadSuccess((WalletTokenInfos) serializable, true);
        }
        Subscription subscribe = HttpRequest.getInstance().getAllWalletTokenInfos(address, 0)
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
                            CacheUtils.getInstance().put(Constants.ETH_TOKEN_LIST + address, wf);
                        } else {
                            onError(new Exception("加载失败"));
                        }
                    }
                });
        addSubscribe(subscribe);
    }
}
