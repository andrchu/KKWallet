package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface UserBackContract {

    interface IUserBackView extends IBaseView {

        void sendError(ApiHttpException exception);

        void sendSuccess(String message);
    }

    interface IUserBackPresenter extends IPresenter {

        void send(String contact, String content);
    }
}
