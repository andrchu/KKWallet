package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.ImportCard;
import com.vip.wallet.wallet.AbsWallet;
import com.vip.wallet.wallet.EosWallet;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 11:21
 * 描述	      ${TODO}
 */

public interface ImportCardContract {
    interface ImportCardView extends IBaseView {
        void importError(Throwable e);

        void importSuccess(AbsWallet wallet);

        void eosSelectAccount(List<String> account_names, ImportCard importCard, EosWallet finalEosWallet);
    }

    interface ImportCardPresenter extends IPresenter {
        void importCard(ImportCard importCard);

        void completeEosImport(String accountName, ImportCard importCard, EosWallet eosWallet);
    }
}
