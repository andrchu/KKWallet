package com.vip.wallet.ui.presenter;

import com.blankj.utilcode.util.CacheUtils;
import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.dao.SelectToken;
import com.vip.wallet.dao.Wallet;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpRequest;
import com.vip.wallet.other.HttpSubscriber;
import com.vip.wallet.ui.contract.TokenDetailsContract;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;

import java.util.ArrayList;

import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/21 0021 14:49
 * 描述	      ${TODO}
 */

public class TokenDetailsPresenter extends RxPresenter<TokenDetailsContract.ITokenDetailsView> implements TokenDetailsContract.ITokenDetailsPresenter {

    private Subscription mLoadDealRecordSubscribe;
    private final CacheUtils cacheUtils;
    private Subscription mSubscribe;

    public TokenDetailsPresenter(TokenDetailsContract.ITokenDetailsView view) {
        super(view);
        cacheUtils = CacheUtils.getInstance(Constants.WALLET_CACHE_DIR);
    }

    @Override
    public void loadData() {

    }

    private SelectToken token;
    private int page = 1;
    private Wallet mWallet;

    @Override
    public void loadDealRecord(SelectToken token, Wallet wallet) {
        this.mWallet = wallet;
        this.token = token;
        //获取缓存
        Object serializable = cacheUtils.getSerializable(Constants.TOKEN_DEAL_RECORD + token.contractAddress + wallet.getWallet_address());
        if (serializable != null) {
            view.loadDealRecordSuccess((ArrayList<DealRecord>) serializable, true);
        }

        if (mLoadDealRecordSubscribe != null)
            mLoadDealRecordSubscribe.unsubscribe();

        page = 1;

        mLoadDealRecordSubscribe = HttpRequest.getInstance().getTokenDealRecord(token.contractAddress,
                StringUtil.formatAddress(wallet.getWallet_address()), page, Constants.LOAD_ITEM_RAW)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<DealRecord>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.loadDealRecordError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<DealRecord> arrayListResponse) {
                        if (ListUtil.isEmpty(arrayListResponse))
                            view.showView(Constants.EMPTY);
                        //添加到缓存中
                        cacheUtils.put(Constants.TOKEN_DEAL_RECORD + token.contractAddress + wallet.getWallet_address(), arrayListResponse);
                        page++;
                        view.loadDealRecordSuccess(arrayListResponse, false);
                    }
                });
        addSubscribe(mLoadDealRecordSubscribe);
    }

    @Override
    public void fresh() {
        loadDealRecord(token, mWallet);
    }

    @Override
    public void loadMore() {
        //添加到缓存中
        if (mSubscribe != null)
            mSubscribe.unsubscribe();
        mSubscribe = HttpRequest.getInstance().getTokenDealRecord(token.contractAddress,
                StringUtil.formatAddress(mWallet.getWallet_address()), page, Constants.LOAD_ITEM_RAW)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(new HttpSubscriber<ArrayList<DealRecord>>() {
                    @Override
                    protected void onError(ApiHttpException exception) {
                        view.loadMoreError(exception);
                    }

                    @Override
                    public void onNext(ArrayList<DealRecord> arrayListResponse) {
                        //添加到缓存中
                        page++;
                        view.loadMoreSuccess(arrayListResponse);
                    }
                });
        addSubscribe(mSubscribe);
    }
}
