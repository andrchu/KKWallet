package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.AsKan;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/5/07 10:53   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface ExploreContract {

    interface IExploreView extends IBaseView {

        void checkAsKanError(ApiHttpException exception);

        void checkAsKanSuccess(AsKan data);
    }

    interface IExplorePresenter extends IPresenter {
        void checkAsKan();
    }
}
