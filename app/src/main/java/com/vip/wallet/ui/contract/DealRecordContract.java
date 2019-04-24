package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.DealRecord;
import com.vip.wallet.exception.ApiHttpException;

import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 16:11
 * 描述	      ${TODO}
 */

public interface DealRecordContract {
    interface IDealRecordView extends IBaseView {
        void loadError(ApiHttpException exception);
        void loadSuccess(ArrayList<DealRecord> dealRecords);

        void loadMoreSuccess(ArrayList<DealRecord> dealRecords);

        void loadMoreError(ApiHttpException exception);

        void freshError(ApiHttpException exception);

        void freshSuccess(ArrayList<DealRecord> dealRecords);
    }

    interface IDealRecordPresenter extends IPresenter {
        void loadMore();
        void loadData(Card card);
        void fresh();
    }
}
