package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.ImportWallet;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface ImportWalletContract {

    interface ImportWalletView extends IBaseView {
        void importWalletSuccess();

        void importWalletError(Throwable e);
    }

    interface ImportWalletPresenter extends IPresenter {
        void importWallet(ImportWallet importWallet);
    }
}
