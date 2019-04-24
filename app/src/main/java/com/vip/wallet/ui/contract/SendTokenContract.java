package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.Balance;
import com.vip.wallet.entity.BtcTransInfo;
import com.vip.wallet.entity.EosAccountInfo;
import com.vip.wallet.entity.OutToken;
import com.vip.wallet.exception.ApiHttpException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 15:36
 * 描述	      ${TODO}
 */

public interface SendTokenContract {
    interface ISendTokenView extends IBaseView {
        void getAllCountSuccess(Balance balance);

        void getLimitError(Throwable e);

        void getLimitSuccess(BigInteger o);

        void sendAmountError(Throwable e);

        void sendAmountSuccess(String s);

        void getEthBalanceError(Throwable e);

        void getEthBalanceSuccess(BigDecimal bigDecimal);

        void getTokenBalanceError();

        void getTokenBalanceSuccess(BigDecimal div);

        void hideLoadingDialog();

        void getBtcBalanceError(ApiHttpException exception);

        void getBtcBalanceSuccess(BtcTransInfo btcTransInfo);

        void getEosBalanceError(ApiHttpException exception);

        void getEosBalanceSuccess(String s);

        void getEosAccountInfoError(ApiHttpException exception);

        void getEosAccountInfoSuccess(EosAccountInfo eosAccountInfo);
    }

    interface ISendTokenPresenter extends IPresenter {

        void sendEthTypeAmount(OutToken outToken);

        void sendEthAmount(OutToken outToken);

        void getAllCount(OutToken outToken);

        void getLimit(OutToken outToken);

        void getEthBalance(String address);

        void getOtherTokenBalance(OutToken outToken);

        void getBtcBalance(String defAddress);

        void sendBtcAmount(OutToken outToken, BtcTransInfo info);

        void getEosBalance(String accountName);

        void sendEosAmount(OutToken outToken);

        void getEosAccountInfo(OutToken outToken);
    }
}
