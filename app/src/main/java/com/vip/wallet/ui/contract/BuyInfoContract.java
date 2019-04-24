package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.BuyInfo;
import com.vip.wallet.entity.SymbolPrice;

import java.math.BigInteger;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/26 0026 14:04
 * 描述	      ${TODO}
 */

public interface BuyInfoContract {
    interface IBuyInfoView extends IBaseView {
        void payError(Throwable e);

        void paySuccess(String data);

        void getGasLimitSuccess(BigInteger bigInteger);

        void getGasLimitError(Throwable e);

        void getSymbolPriceSuccess(SymbolPrice symbolPrice);
    }

    interface IBuyInfoPresenter extends IPresenter {
        void pay(BuyInfo buyInfo);

        void getGasLimit(BuyInfo buyInfo);

        void getSymbolPrice();
    }
}
