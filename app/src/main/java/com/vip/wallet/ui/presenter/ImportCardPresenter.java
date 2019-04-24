package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.quincysx.crypto.bip32.ValidationException;
import com.vip.wallet.R;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Address;
import com.vip.wallet.dao.AddressDao;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.EosAccount;
import com.vip.wallet.entity.ImportCard;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.ImportCardContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.AbsWallet;
import com.vip.wallet.wallet.BtcWallet;
import com.vip.wallet.wallet.EosWallet;
import com.vip.wallet.wallet.EthWallet;
import com.vip.wallet.wallet.WalletHelper;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscription;

import static rx.plugins.RxJavaHooks.onError;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 11:25
 * 描述	      ${TODO}
 */

public class ImportCardPresenter extends RxPresenter<ImportCardContract.ImportCardView>
        implements ImportCardContract.ImportCardPresenter {

    public ImportCardPresenter(ImportCardContract.ImportCardView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void importCard(ImportCard importCard) {
        Subscription subscribe = Observable.unsafeCreate(subscriber -> {
            if (importCard.importType == 0) {
                List<String> seedList = Arrays.asList(importCard.seedKey.replaceAll("\\n", "").split("[ ]+"));
                LogUtils.i(GsonAdapter.getGson().toJson(seedList));
                try {
                    //检查助记词
                    MnemonicCode.INSTANCE.check(seedList);
                } catch (MnemonicException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(StringUtil.getString(R.string.seed_word_error)));
                    return;
                }
            }
            subscriber.onNext(null);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Object>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.importError(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        switch (importCard.chainType) {
                            case 0:
                                importEthCard(importCard);
                                break;
                            case 1:
                                importBtcCard(importCard);
                                break;
                            case 2:
                                importEosCard(importCard);
                                break;
                            default:
                                importEthCard(importCard);
                                break;
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void completeEosImport(String accountName, ImportCard importCard, EosWallet eosWallet) {
        eosWallet.setAccountName(accountName);
        //插入数据
        if (!insertData(importCard, eosWallet)) {
            onError(new ApiHttpException(StringUtil.getString(R.string.exist_card_hint), 1));
            return;
        }
        view.importSuccess(eosWallet);
    }


    /**
     * 导入ETH
     *
     * @param importCard
     */
    private void importEthCard(ImportCard importCard) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<AbsWallet>) subscriber -> {
            EthWallet ethWallet;
            boolean isSeedKey = importCard.importType == 0;
            if (isSeedKey) {
                ethWallet = WalletHelper.importEthBySeedKey(importCard.seedKey, importCard.path.path);
            } else
                ethWallet = WalletHelper.importEthByPrivateKey(importCard.privateKey);
            //插入数据
            if (!insertData(importCard, ethWallet)) {
                subscriber.onError(new Exception(StringUtil.getString(R.string.exist_card_hint)));
                subscriber.onCompleted();
                return;
            }
            subscriber.onNext(ethWallet);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<AbsWallet>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.importError(e);
                    }

                    @Override
                    public void onNext(AbsWallet wallet) {
                        view.importSuccess(wallet);
                    }
                });
        addSubscribe(subscribe);
    }

    /**
     * 插入数据库
     *
     * @param importCard
     * @param wallet
     * @return
     */
    private boolean insertData(ImportCard importCard, AbsWallet wallet) {
        //检查卡是否已经存在
        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
        AddressDao addressDao = ScApplication.getInstance().getDaoSession().getAddressDao();
        List<Card> list = cardDao.queryBuilder().where(CardDao.Properties.DefAddress.eq(wallet.address)).build().list();
        if (!ListUtil.isEmpty(list)) {
            return false;
        }

        //插入卡包
        String accountName = wallet instanceof EosWallet ? ((EosWallet) wallet).getAccountName() : "";
        Card entity = new Card(null, importCard.cardName, WalletHelper.encrypt(wallet.privateKey),
                importCard.importType == 0 ? WalletHelper.encrypt(importCard.seedKey) : "", wallet.address, 0, importCard.chainType, System.currentTimeMillis(), 1,
                accountName);
        long cardId = cardDao.insert(entity);
        //地址
        Address address = new Address(null, cardId, wallet.getAddress(), WalletHelper.encrypt(wallet.privateKey), System.currentTimeMillis(), 0);
        addressDao.insert(address);
        return true;
    }

    /**
     * 导入BTC
     *
     * @param importCard
     */
    private void importBtcCard(ImportCard importCard) {

        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<AbsWallet>) subscriber -> {
            BtcWallet btcWallet;
            boolean isSeedKey = importCard.importType == 0;
            try {
                if (isSeedKey) {
                    btcWallet = WalletHelper.importBtcBySeedKey(importCard.seedKey);
                } else
                    btcWallet = WalletHelper.importBtcByPrivateKey(importCard.privateKey);
            } catch (ValidationException e) {
                e.printStackTrace();
                subscriber.onError(e);
                subscriber.onCompleted();
                return;
            }
            //插入数据
            if (!insertData(importCard, btcWallet)) {
                subscriber.onError(new Exception(StringUtil.getString(R.string.exist_card_hint)));
                subscriber.onCompleted();
                return;
            }
            subscriber.onNext(btcWallet);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<AbsWallet>() {
                    @Override
                    public void onNext(AbsWallet wallet) {
                        view.importSuccess(wallet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.importError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    private void importEosCard(ImportCard importCard) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<AbsWallet>) subscriber -> {
            EosWallet eosWallet = new EosWallet("", "");
            boolean isSeedKey = importCard.importType == 0;
            try {
                if (isSeedKey) {
                    eosWallet = WalletHelper.importEosBySeedKey(importCard.seedKey);
                } else
                    eosWallet = WalletHelper.importEosByPrivateKey(importCard.privateKey);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(new Exception("私钥错误"));
                subscriber.onCompleted();
                return;
            }
            //获取账户
            EosWallet finalEosWallet = eosWallet;
            HttpRequest.getInstance().getEosAccountByPublicKey(eosWallet.address)
                    .compose(RxUtil.handleResult())
                    .subscribe(new HttpSubscriber<EosAccount>() {
                        @Override
                        protected void onError(ApiHttpException exception) {
                            subscriber.onError(exception);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onNext(EosAccount eosAccount) {
                            if (ListUtil.isEmpty(eosAccount.account_names)) {
                                onError(new ApiHttpException("没有找到对应的账户名", 1));
                                return;
                            }
                            if (eosAccount.account_names.size() >= 2) {
                                view.eosSelectAccount(eosAccount.account_names, importCard, finalEosWallet);
                                return;
                            }
                            finalEosWallet.setAccountName(eosAccount.account_names.get(0));
                            //插入数据
                            if (!insertData(importCard, finalEosWallet)) {
                                onError(new ApiHttpException(StringUtil.getString(R.string.exist_card_hint), 1));
                                return;
                            }
                            subscriber.onNext(finalEosWallet);
                            subscriber.onCompleted();
                        }
                    });
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<AbsWallet>() {
                    @Override
                    public void onNext(AbsWallet wallet) {
                        view.importSuccess(wallet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.importError(e);
                    }
                });
        addSubscribe(subscribe);
    }
}
