package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.ui.contract.DiscoverContract;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/1 0001 14:27
 * 描述	      ${TODO}
 */

public class DiscoverPresenter extends RxPresenter<DiscoverContract.IDiscoverView> implements DiscoverContract.IDiscoverPresenter {
    public DiscoverPresenter(DiscoverContract.IDiscoverView view) {
        super(view);
    }

    @Override
    public void loadData() {
        view.reload();
    }
}
