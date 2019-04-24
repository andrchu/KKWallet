package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:36   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface HomeContract {

    interface IHomeView extends IBaseView {

    }

    interface IHomePresenter extends IPresenter {

        void registerAddress();
    }
}
