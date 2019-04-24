package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.dao.PushMessage;

import java.util.List;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface MessageCenterContract {

    interface IMessageCenterView extends IBaseView {

        void loadSuccess(List<PushMessage> pushMessages);

        void signAllSuccess();

        void loadMoreSuccess(List<PushMessage> pushMessages);
    }

    interface IMessageCenterPresenter extends IPresenter {

        void signAll();

        void loadMore();
    }
}
