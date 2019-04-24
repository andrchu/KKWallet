package com.vip.wallet.ui.presenter;

import com.jgd.eoslibrary.OfflineSign;
import com.jgd.eoslibrary.api.vo.SignParam;
import com.jgd.eoslibrary.api.vo.transaction.push.TxRequest;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.EosAuthInfo;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.entity.Response;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.EosAuthContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/30 0030 15:24
 * 描述	      ${TODO}
 */

public class EosAuthPresenter extends RxPresenter<EosAuthContract.IEosAuthView> implements EosAuthContract.IEosAuthPresenter {

    public EosAuthPresenter(EosAuthContract.IEosAuthView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadAuthList(EosAuthf eosAuthf) {
        view.showView(Constants.LOADING);
        Subscription subscribe = HttpRequest.getInstance().getEosAuthInfo(eosAuthf.order_id)
                .compose(RxUtil.rxSchedulerHelper(1000))
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<EosAuthInfo>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.showView(Constants.ERROR, exception);
                    }

                    @Override
                    public void onNext(ArrayList<EosAuthInfo> eosAuthInfos) {
                        EosSigns eosSigns = GsonAdapter.getGson().fromJson(eosAuthInfos.get(0).extra, EosSigns.class);

                        for (EosAuthInfo eosAuthInfo : eosAuthInfos) {
                            EosSigns.Permissions e = new EosSigns.Permissions(eosAuthInfo.account, eosAuthInfo.weight);
                            e.signs = eosAuthInfo.signs;
                            eosSigns.permissions.add(e);
                        }
                        eosSigns.orderId = eosAuthf.order_id;

                        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
                        for (EosSigns.Permissions permission : eosSigns.permissions) {
                            List<Card> list = cardDao.queryBuilder()
                                    .whereOr(CardDao.Properties.AccountName.eq(permission.account), CardDao.Properties.DefAddress.eq(permission.account))
                                    .build()
                                    .list();
                            permission.isNative = !ListUtil.isEmpty(list);
                        }
                        view.showView(Constants.SUCCESS);
                        view.loadAuthListSuccess(eosSigns);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void auth(EosSigns eosSigns, EosSigns.Permissions permissions) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<String>) subscriber -> {
            CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
            List<Card> list = cardDao.queryBuilder()
                    .whereOr(CardDao.Properties.AccountName.eq(permissions.account), CardDao.Properties.DefAddress.eq(permissions.account))
                    .build()
                    .list();
            Card card = list.get(0);
            try {
                TxRequest txRequest = signTx(eosSigns, card);
                String sign = txRequest.getSignatures().get(0);
                subscriber.onNext(sign);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<String>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.authError(e);
                    }

                    @Override
                    public void onNext(String sign) {
                        authToServer(sign, permissions, eosSigns);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void freshAuthList(EosSigns eosSigns) {
        Subscription subscribe = HttpRequest.getInstance().getEosAuthInfo(eosSigns.orderId)
                .compose(RxUtil.rxSchedulerHelper(1000))
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<EosAuthInfo>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.freshAuthError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<EosAuthInfo> eosAuthInfos) {
                        for (EosSigns.Permissions permission : eosSigns.permissions) {
                            EosAuthInfo obj = ListUtil.getObj(eosAuthInfos, new EosAuthInfo(permission.account));
                            if (obj != null) {
                                permission.signs = obj.signs;
                            }
                        }
                        view.freshAuthSuccess();
                    }
                });
        addSubscribe(subscribe);
    }

    private void authToServer(String sign, EosSigns.Permissions permissions, EosSigns eosSigns) {
        Subscription subscribe = HttpRequest.getInstance().eosAuth(sign, permissions.account, eosSigns.orderId)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.authError(exception);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            permissions.signs = sign;
                            view.authSuccess();
                        } else {
                            onError(new ApiHttpException(response.message, response.code));
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    private TxRequest signTx(EosSigns eosSigns, Card card) throws Exception {
        SignParam signParam = new SignParam();
        signParam.setChainId(eosSigns.chain_id);
        //签名过期时间1个小时
        signParam.setExp(Constants.EXP);

        OfflineSign offlineSign = new OfflineSign();
        signParam.setHeadBlockTime(offlineSign.dateFormatter.parse(eosSigns.head_block_time));
        signParam.setLastIrreversibleBlockNum(eosSigns.last_irreversible_block_num);
        signParam.setRefBlockPrefix(eosSigns.ref_block_prefix);

        //交易签名
        return offlineSign.transfer(signParam, WalletHelper.decrypt(card.getPrivateKey()), "eosio.token", eosSigns.from, eosSigns.to, eosSigns.amount, eosSigns.memo);
    }
}
