package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.dao.Card;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 16:11
 * 描述	      ${TODO}
 */

public interface PropertyChainContract {
    interface IPropertyChainView extends IBaseView {
        void loadSuccess(WalletTokenInfos serializable, boolean b);

        void loadError(ApiHttpException exception);
    }

    interface IPropertyChainPresenter extends IPresenter {
        void loadDataByChainType(Card card);

        void fresh();

        void loadCache(Card card);
    }
}
