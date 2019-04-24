package com.vip.wallet.ui.contract;

import android.graphics.Bitmap;
import android.net.Uri;

import com.vip.wallet.base.IBaseView;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/24 10:24
 * 描述	      ${TODO}
 */

public interface EosSignsContract {

    interface IEosSignsView extends IBaseView {
        void processSuccess();

        void generateQrCodeSuccess(Bitmap bitmap);

        void freshAuthError(ApiHttpException exception);

        void freshAuthSuccess();

        void authError(Throwable e);

        void authSuccess();

        void generateQrFileSuccess(Uri uri);

        void generateQrFileError(Throwable e);

        void sendAmountError(Throwable e);

        void sendAmountSuccess(String message);
    }

    interface IEosSignsPresenter extends IPresenter {
        void processEosInfo(EosSigns eosSigns);

        void generateQrCode(EosSigns eosSigns);

        void freshAuthList(EosSigns eosSigns);

        void auth(EosSigns eosSigns, EosSigns.Permissions permissions);

        void generateQrFile(Bitmap bitmap);

        void eosTransfer(EosSigns eosSigns);
    }
}
