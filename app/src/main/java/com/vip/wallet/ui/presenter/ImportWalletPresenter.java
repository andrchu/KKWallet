package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.entity.ImportWallet;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.ImportWalletContract;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 14:04
 * 描述	      ${TODO}
 */

public class ImportWalletPresenter extends RxPresenter<ImportWalletContract.ImportWalletView>
        implements ImportWalletContract.ImportWalletPresenter {
    public ImportWalletPresenter(ImportWalletContract.ImportWalletView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void importWallet(ImportWallet importWallet) {

        Subscription subscribe = Observable.unsafeCreate(subscriber -> {
            //导入钱包
            try {
                WalletHelper.createOrImportWallet(importWallet.seedKey, importWallet.path.path, importWallet.walletName, true);
                subscriber.onNext(null);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }

        }).compose(RxUtil.rxSchedulerHelper(500))
                .subscribe(new SimpSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        view.importWalletSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.importWalletError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    private List<String> getSeedList(ImportWallet importWallet) {
        String[] split = importWallet.seedKey.split("[ ]+");
        return Arrays.asList(split);
    }
}
