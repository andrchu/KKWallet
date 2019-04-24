package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 15:36
 * 描述	      ${TODO}
 */

public interface SelectTokenContract {
    interface ISelectTokenView extends IBaseView {
        void loadSuccess(WalletTokenInfos serializable, boolean isCache);

        void loadError(ApiHttpException exception);
    }

    interface ISelectTokenPresenter extends IPresenter {
        void loadData(String address);
    }
}
