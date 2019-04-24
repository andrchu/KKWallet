package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.InitWalletContract;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.WalletUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.Subscription;

import static com.vip.wallet.utils.WalletUtil.SEED_ENTROPY_EXTRA;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 20:01
 * 描述	      ${TODO}
 */

public class InitWalletPresenter extends RxPresenter<InitWalletContract.IInitWalletView> implements InitWalletContract.IInitWalletPresenter {
    public InitWalletPresenter(InitWalletContract.IInitWalletView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void createWallet() {

        Subscription subscribe = Observable.unsafeCreate(subscriber -> {
            //创建钱包
            List<String> seeds = getSeeds();
            StringBuilder stringBuilder = new StringBuilder();
            for (String seed : seeds) {
                stringBuilder.append(seed).append(" ");
            }
            String s = stringBuilder.toString();
            try {
                WalletHelper.createOrImportWallet(s.substring(0, s.length() - 1));
                subscriber.onNext(null);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        }).compose(RxUtil.rxSchedulerHelper(500))
                .subscribe(new SimpSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        view.createWalletSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.createWalletError(e);
                    }
                });

        addSubscribe(subscribe);

    }

    private List<String> getSeeds() {
        List<String> strings = WalletUtil.generateMnemonic(SEED_ENTROPY_EXTRA);
        HashSet<String> s = new HashSet<>();
        for (String string : strings) {
            s.add(string);
        }
        if (s.size() < 12) {
            getSeeds();
        }
        return strings;
    }
}
