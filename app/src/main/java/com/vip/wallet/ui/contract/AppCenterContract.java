package com.vip.wallet.ui.contract;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.exception.ApiHttpException;

import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/1 0001 14:20
 * 描述	      ${TODO}
 */

public interface AppCenterContract {

    interface IAppCenterView extends IBaseView {
        void loadSuccess(ArrayList<AppCenterItem> appCenterItems);

        void freshError(ApiHttpException exception);

        void freshSuccess(ArrayList<AppCenterItem> appCenterItems);
    }

    interface IAppCenterPresenter extends IPresenter {
        void fresh();
    }
}
