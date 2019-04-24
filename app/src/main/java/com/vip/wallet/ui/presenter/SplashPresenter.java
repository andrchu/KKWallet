package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.StringUtils;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Address;
import com.vip.wallet.dao.AddressDao;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.AdvertInfo;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.SplashContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.wallet.BtcWallet;
import com.vip.wallet.wallet.EthWallet;
import com.vip.wallet.wallet.WalletHelper;

import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/8 0008 10:13
 * 描述	      ${TODO}
 */

public class SplashPresenter extends RxPresenter<SplashContract.ISplashView> implements SplashContract.ISplashPresenter {

    private Subscription mSubscribe;

    public SplashPresenter(SplashContract.ISplashView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void getAdvertInfo() {
         /*
         获取广告信息
           1.超时取消
        * 2.获取缓存，有缓存则加载图片，无缓存直接跳转主页
        * */
        Subscription subscribe = HttpRequest.getInstance().getAdvertInfo()
                .timeout(Constants.SPLASH_TIME_OUT, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<AdvertInfo>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        Object serializable = CacheUtils.getInstance().getSerializable(Constants.ADVERT_INFO);
                        if (serializable == null) {
                            view.getAdvertInfoSuccess(null);
                        } else {
                            view.getAdvertInfoSuccess((AdvertInfo) serializable);
                        }
                    }

                    @Override
                    public void onNext(AdvertInfo advertInfo) {
                        CacheUtils.getInstance().put(Constants.ADVERT_INFO, advertInfo);
                        view.getAdvertInfoSuccess(advertInfo);
                    }
                });

        addSubscribe(subscribe);
    }

    /**
     * 跳过计时
     *
     * @param advertInfo
     */
    @Override
    public void keepTime(AdvertInfo advertInfo) {
        mSubscribe = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if (aLong >= advertInfo.time) {
                            unsubscribe();
                            return;
                        }
                        view.showKeepTime(advertInfo.time - aLong - 1);
                    }
                });
        addSubscribe(mSubscribe);
    }

    /**
     * 2.5.0
     * 旧版本升级兼容
     */
    @Override
    public void compatibility() {
        String seedKey = ScApplication.getInstance().getConfig().getSeedKey();
        List<Card> cards = ScApplication.getInstance().getDaoSession().getCardDao().loadAll();
        if (StringUtils.isEmpty(seedKey) || !ListUtil.isEmpty(cards)) {
            view.compatibilityFinish();
            return;
        }
        Subscription subscribe = Observable.unsafeCreate(subscriber -> {
            CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
            AddressDao addressDao = ScApplication.getInstance().getDaoSession().getAddressDao();
            //        1.copy ETH 卡包存入数据库
            EthWallet ethWallet = ScApplication.getInstance().getEthWallet();
            Card eth_card = new Card(null, "Eth_Card_1", WalletHelper.encrypt(ethWallet.getPrivateKey()), WalletHelper.encrypt(seedKey), ethWallet.getAddress(), 1, 0, System.currentTimeMillis(),
                    ScApplication.getInstance().getConfig().isBackUp() ? 1 : 0, "");
            //插入卡包
            long eth_card_id = cardDao.insert(eth_card);
            //插入地址
            Address address = new Address(null, eth_card_id, ethWallet.getAddress(), WalletHelper.encrypt(ethWallet.getPrivateKey()), System.currentTimeMillis(), 0);
            addressDao.insert(address);

            //        2.创建比特币地址 存入数据库
            try {
                byte[] seed = com.quincysx.crypto.bip39.MnemonicCode.toSeed(Arrays.asList(seedKey.split("[ ]+")), "");
                ExtendedKey extendedKey = ExtendedKey.create(seed);

                AddressIndex address1 = BIP44.m().purpose44()
                        .coinType(Constants.TEST ? CoinTypes.BitcoinTest : CoinTypes.Bitcoin)
                        .account(0)
                        .external()
                        .address(0);

                CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);

                ECKeyPair derive1 = coinKeyPair.derive(address1);

                //插入card
                String privateKey_1 = Hex.toHexString(derive1.getRawPrivateKey());
                Card card1 = new Card(null, "Btc_Card_1", WalletHelper.encrypt(privateKey_1), WalletHelper.encrypt(seedKey), derive1.getAddress(), 1, 1, System.currentTimeMillis()
                        , ScApplication.getInstance().getConfig().isBackUp() ? 1 : 0, "");
                long card1_id = cardDao.insert(card1);
                //插入地址
                Address address_1 = new Address(null, card1_id, derive1.getAddress(), WalletHelper.encrypt(privateKey_1), System.currentTimeMillis(), 0);
                addressDao.insert(address_1);
                subscriber.onNext(null);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        }).compose(RxUtil.rxSchedulerHelper(500))
                .subscribe(new SimpSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        view.compatibilityFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.compatibilityError(e);
                    }
                });
        addSubscribe(subscribe);
    }

    /**
     * 2.6.0数据兼容
     */
    @Override
    public void compatibility17() {
        Observable.unsafeCreate(subscriber -> {
            int i = ScApplication.getInstance().getConfig().isBackUp() ? 1 : 0;
            CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
            List<Card> cards = cardDao.loadAll();
            for (Card card : cards) {
                card.isBackUp = i;
                if (card.chainType == 1) {  //更改私钥格式
                    String decrypt = WalletHelper.decrypt(card.privateKey);
                    try {
                        BtcWallet btcWallet = WalletHelper.ImportBtcByEthPrivateKey(decrypt);
                        card.privateKey = WalletHelper.encrypt(btcWallet.privateKey);
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
                cardDao.update(card);
            }
            subscriber.onNext(null);
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        view.compatibility17Finish();
                    }
                });
    }

    @Override
    public void stopKeepTime() {
        if (mSubscribe != null) {
            mSubscribe.unsubscribe();
        }
    }

}
