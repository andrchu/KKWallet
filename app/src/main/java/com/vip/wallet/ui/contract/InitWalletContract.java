package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface InitWalletContract {

    interface IInitWalletView extends IBaseView {

        void createWalletSuccess();

        void createWalletError(Throwable e);
    }

    interface IInitWalletPresenter extends IPresenter {

        void createWallet();

    }
}
