package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/30 0030 14:01
 * 描述	      ${TODO}
 */

public interface EosAuthContract {
    interface IEosAuthView extends IBaseView {

        void loadAuthListSuccess(EosSigns eosSigns);

        void authError(Throwable e);

        void authSuccess();

        void freshAuthError(ApiHttpException exception);

        void freshAuthSuccess();
    }

    interface IEosAuthPresenter extends IPresenter {

        void loadAuthList(EosAuthf eosAuthf);

        void auth(EosSigns eosSigns, EosSigns.Permissions permissions);

        void freshAuthList(EosSigns eosSigns);
    }
}
