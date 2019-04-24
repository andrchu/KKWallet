package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.SearchToken;

import java.util.ArrayList;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface SearchTokenContract {

    interface ISearchTokenView extends IBaseView {

        void searchSuccess(ArrayList<SearchToken> searchTokens);
    }

    interface ISearchTokenPresenter extends IPresenter {

        void autoSearch(String searchKey);

        void autoSearch(String searchKey, long delay);

        void search(String searchKey);
    }
}
