package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Address;
import com.vip.wallet.entity.Response;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.HomeContract;
import com.vip.wallet.utils.RxUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/28 0028 10:43
 * 描述	      ${TODO}
 */

public class HomePresenter extends RxPresenter<HomeContract.IHomeView> implements HomeContract.IHomePresenter {
    public HomePresenter(HomeContract.IHomeView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void registerAddress() {

        List<RegisterAddress> addresses = new ArrayList<>();
        List<Address> addrs = ScApplication.getInstance().getDaoSession().getAddressDao().loadAll();
        for (Address addr : addrs) {
            addresses.add(new RegisterAddress(addr.getAddress(), addr.getCard().chainType));
        }
        Subscription subscribe = HttpRequest.getInstance().registerAddress(GsonAdapter.getGson().toJson(addresses))
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            ScApplication.getInstance().getConfig().setRegister(true);
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    private class RegisterAddress {
        public String address;
        public int chain_type;

        public RegisterAddress(String address, int chain_type) {
            this.address = address;
            this.chain_type = chain_type;
        }
    }
}
