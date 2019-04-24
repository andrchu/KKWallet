package com.vip.wallet.ui.contract;


import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.AdvertInfo;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/30 14:02   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface SplashContract {

    interface ISplashView extends IBaseView {

        void gotoInitWalletActivity();

        void gotoHomeActivity();

        void getAdvertInfoSuccess(AdvertInfo advertInfo);

        void showKeepTime(long l);

        void compatibilityFinish();

        void compatibilityError(Throwable e);

        void compatibility17Finish();
    }

    interface ISplashPresenter extends IPresenter {
        void getAdvertInfo();

        void keepTime(AdvertInfo advertInfo);

        void compatibility();

        void stopKeepTime();

        void compatibility17();
    }
}
