package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.dao.SelectToken;
import com.vip.wallet.dao.Wallet;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.exception.ApiHttpException;

import java.util.ArrayList;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface TokenDetailsContract {

    interface ITokenDetailsView extends IBaseView {

        void loadDealRecordSuccess(ArrayList<DealRecord> dealRecords, boolean isCache);

        void loadDealRecordError(ApiHttpException exception);

        void loadMoreError(ApiHttpException exception);

        void loadMoreSuccess(ArrayList<DealRecord> arrayListResponse);
    }

    interface ITokenDetailsPresenter extends IPresenter {

        void loadDealRecord(SelectToken token, Wallet wallet);

        void fresh();

        void loadMore();
    }
}
