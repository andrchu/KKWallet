package com.vip.wallet.ui.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jgd.eoslibrary.OfflineSign;
import com.jgd.eoslibrary.api.vo.SignParam;
import com.jgd.eoslibrary.api.vo.transaction.push.TxRequest;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.EosAuthInfo;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.EosHeadInfo;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.entity.OutToken;
import com.vip.wallet.entity.QrCode;
import com.vip.wallet.entity.Response;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.contract.EosSignsContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/24 0024 15:05
 * 描述	      ${TODO}
 */

public class EosSignsPresenter extends RxPresenter<EosSignsContract.IEosSignsView> implements EosSignsContract.IEosSignsPresenter {
    public EosSignsPresenter(EosSignsContract.IEosSignsView view) {
        super(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void processEosInfo(EosSigns eosSigns) {
        view.showView(Constants.LOADING);
        Subscription subscribe = HttpRequest.getInstance().getEosHeadInfo()
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<EosHeadInfo>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.showView(Constants.ERROR, exception);
                    }

                    @Override
                    public void onNext(EosHeadInfo eosHeadInfo) {
                        processData(eosSigns, eosHeadInfo);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void generateQrCode(EosSigns eosSigns) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<Bitmap>) subscriber -> {

            int size = SizeUtils.dp2px(150);

            QrCode<EosAuthf> qrCode = new QrCode<>(QrCode.EOS_TRANSFER, new EosAuthf(eosSigns.orderId));

            Bitmap image = CodeUtils.createImage(GsonAdapter.getGson().toJson(qrCode), size, size, null);

            subscriber.onNext(image);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        view.generateQrCodeSuccess(bitmap);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void freshAuthList(EosSigns eosSigns) {

       /* Subscription subscribe = HttpRequest.getInstance().getEosAuthInfo(eosSigns.orderId)
                .flatMap(new Func1<Response<ArrayList<EosAuthInfo>>, Observable<ArrayList<EosAuthInfo>>>() {
                    @Override
                    public Observable<ArrayList<EosAuthInfo>> call(Response<ArrayList<EosAuthInfo>> arrayListResponse) {
                        LogUtils.i("处理数据。。。。" + Thread.currentThread().getName());
                        if (arrayListResponse.isSuccess()) {
                            for (EosSigns.Permissions permission : eosSigns.permissions) {
                                EosAuthInfo obj = ListUtil.getObj(arrayListResponse.data, new EosAuthInfo(permission.account));
                                if (obj != null) {
                                    permission.signs = obj.signs;
                                }
                            }
                            return RxUtil.createData(arrayListResponse.data);
                        } else {
                            return Observable.error(new ApiHttpException(arrayListResponse.message, arrayListResponse.code));
                        }
                    }
                })
                .compose(RxUtil.rxSchedulerHelper(1000))
                .subscribe(new HttpSubscriber<ArrayList<EosAuthInfo>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.freshAuthError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<EosAuthInfo> eosAuthInfos) {
                        if (eosAuthInfos == null) {
                            LogUtils.i("展示缓存数据" + Thread.currentThread().getName());
                        } else {
                            LogUtils.i("展示网络数据" + Thread.currentThread().getName());
                            view.freshAuthSuccess();
                        }
                    }
                });*/
        Observable<Response<ArrayList<EosAuthInfo>>> netData = HttpRequest.getInstance().getEosAuthInfo(eosSigns.orderId).subscribeOn(Schedulers.io()).delay(3000, TimeUnit.MILLISECONDS);
        Observable<Response<ArrayList<EosAuthInfo>>> localData = Observable.unsafeCreate((Observable.OnSubscribe<Response<ArrayList<EosAuthInfo>>>) subscriber -> {
            LogUtils.i("检查缓存数据。。。" + Thread.currentThread().getName());
            Response<ArrayList<EosAuthInfo>> t = new Response<>();
            t.code = Response.SUCCESS;
            subscriber.onNext(t);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io());
        Subscription subscribe = Observable.concat(localData, netData)
                .flatMap(new Func1<Response<ArrayList<EosAuthInfo>>, Observable<ArrayList<EosAuthInfo>>>() {
                    @Override
                    public Observable<ArrayList<EosAuthInfo>> call(Response<ArrayList<EosAuthInfo>> arrayListResponse) {
                        LogUtils.i("处理数据。。。。" + Thread.currentThread().getName());
                        if (arrayListResponse.isSuccess()) {
                            for (EosSigns.Permissions permission : eosSigns.permissions) {
                                EosAuthInfo obj = ListUtil.getObj(arrayListResponse.data, new EosAuthInfo(permission.account));
                                if (obj != null) {
                                    permission.signs = obj.signs;
                                }
                            }
                            return RxUtil.createData(arrayListResponse.data);
                        } else {
                            return Observable.error(new ApiHttpException(arrayListResponse.message, arrayListResponse.code));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<ArrayList<EosAuthInfo>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.freshAuthError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<EosAuthInfo> eosAuthInfos) {
                        if (eosAuthInfos == null) {
                            LogUtils.i("展示缓存数据" + Thread.currentThread().getName());
                        } else {
                            LogUtils.i("展示网络数据" + Thread.currentThread().getName());
                            view.freshAuthSuccess();
                        }
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
    public void generateQrFile(Bitmap bitmap) {

        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<Uri>) subscriber -> {
            File imageFile = new File(ScApplication.getInstance().getExternalCacheDir(), "eos_sign_qr.jpg");
            ImageUtils.save(bitmap, imageFile, Bitmap.CompressFormat.JPEG, true);
            String appPackageName = AppUtils.getAppPackageName();
            Uri photoUri = FileProvider.getUriForFile(
                    ScApplication.getInstance(),
                    appPackageName + ".fileProvider",
                    imageFile);
            subscriber.onNext(photoUri);

        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Uri>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.generateQrFileError(e);
                    }

                    @Override
                    public void onNext(Uri uri) {
                        view.generateQrFileSuccess(uri);
                    }
                });
        addSubscribe(subscribe);
    }

    @Override
    public void eosTransfer(EosSigns eosSigns) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<HashMap<String, String>>) subscriber -> {
            //获取私钥
            TxRequest txRequest = GsonAdapter.getGson().fromJson(eosSigns.signInfo, TxRequest.class);
            ArrayList<String> signatures = txRequest.getSignatures();
            signatures.clear();
            for (EosSigns.Permissions permission : eosSigns.permissions) {
                if (!StringUtils.isEmpty(permission.signs)) {
                    signatures.add(permission.signs);
                }
            }
            HashMap<String, String> map = getMap(eosSigns.outToken, GsonAdapter.getGson().toJson(txRequest));
            subscriber.onNext(map);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<HashMap<String, String>>() {
                    @Override
                    public void onNext(HashMap<String, String> map) {
                        pushEosTransfer(map);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.sendAmountError(e);
                    }
                });
        addSubscribe(subscribe);
    }


    private void pushEosTransfer(HashMap<String, String> map) {
        Subscription subscribe = HttpRequest.getInstance().sendAmount(map)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new HttpSubscriber<Response>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.sendAmountError(exception);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            view.sendAmountSuccess(response.message);
                        } else {
                            onError(new ApiHttpException(response.message, 1));
                        }
                    }
                });
        addSubscribe(subscribe);
    }

    private HashMap<String, String> getMap(OutToken outToken, String haxData) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from_address", outToken.fromAddress);
        map.put("to_address", outToken.toAddress);
        map.put("value", outToken.outCount.toString());
        String contractAddress = StringUtil.formatAddress(outToken.token.contractAddress);
        map.put("token_address", contractAddress.contains("ETH") || contractAddress.contains("BTC") || contractAddress.contains("EOS") ? StringUtil.getNo0xAddress(contractAddress) : contractAddress);
        map.put("token_symbol", outToken.token.tokenName);
        map.put("memo", outToken.memo);
        map.put("data", haxData);
        map.put("type", String.valueOf(outToken.chain_type));
        LogUtils.i(GsonAdapter.getGson().toJson(map));
        return map;
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

    private void processData(EosSigns eosSigns, EosHeadInfo eosHeadInfo) {
        Subscription subscribe = Observable.unsafeCreate((Observable.OnSubscribe<EosSigns>) subscriber -> {
            eosSigns.head_block_time = eosHeadInfo.head_block_time;
            eosSigns.last_irreversible_block_num = eosHeadInfo.last_irreversible_block_num;
            eosSigns.ref_block_prefix = eosHeadInfo.ref_block_prefix;
            eosSigns.chain_id = eosHeadInfo.chain_id;

            //标记本地
            CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
            for (EosSigns.Permissions permission : eosSigns.permissions) {
                List<Card> list = cardDao.queryBuilder()
                        .whereOr(CardDao.Properties.AccountName.eq(permission.account), CardDao.Properties.DefAddress.eq(permission.account))
                        .build()
                        .list();
                permission.isNative = !ListUtil.isEmpty(list);
            }

            Card card = cardDao
                    .queryBuilder()
                    .where(CardDao.Properties.AccountName.eq(eosSigns.from))
                    .build()
                    .list()
                    .get(0);

            try {
                TxRequest txRequest = signTx(eosSigns, card);
                String sign = txRequest.getSignatures().get(0);

                //给已授权账户或地址赋值签名
                EosSigns.Permissions obj = ListUtil.getObj(eosSigns.permissions, new EosSigns.Permissions(card.defAddress));
                obj.signs = sign;

                //存入签名json数据
                eosSigns.signInfo = GsonAdapter.getGson().toJson(txRequest);
                LogUtils.i(eosSigns.signInfo);
                eosSigns.address = card.defAddress;
                subscriber.onNext(eosSigns);

            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<EosSigns>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.showView(Constants.ERROR, new ApiHttpException(e.getMessage(), 1));
                    }

                    @Override
                    public void onNext(EosSigns eosSigns) {
                        //提交转账订单给后台
                        commitTransfer(eosSigns);

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

    private void commitTransfer(EosSigns eosSigns) {
        Subscription subscribe = HttpRequest.getInstance().commitTransfer(eosSigns.address, GsonAdapter.getGson().toJson(eosSigns), GsonAdapter.getGson().toJson(eosSigns.permissions))
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<EosAuthf>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        exception.printStackTrace();
                        view.showView(Constants.ERROR, exception);
                    }

                    @Override
                    public void onNext(EosAuthf eosAuthf) {
                        eosSigns.orderId = eosAuthf.order_id;
                        view.showView(Constants.SUCCESS);
                        view.processSuccess();
                    }
                });
        addSubscribe(subscribe);
    }
}
