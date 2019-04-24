package com.vip.wallet.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.utils.DrawableUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.SystemUtil;
import com.vip.wallet.widget.UnderlineTextView;

import java.io.File;

import butterknife.Bind;
import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 19:47
 * 描述	      ${TODO}
 */

public class WeChatServiceFragment extends BaseFragment {
    @Bind(R.id.wcia_image)
    ImageView mWciaImage;
    @Bind(R.id.wcia_copy)
    UnderlineTextView mWciaCopy;
    private Subscription mSubscription;

    public static WeChatServiceFragment newInstance(){
        return new WeChatServiceFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_we_chat_service;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {

    }

    @Override
    protected void initListener() {
        mWciaCopy.setOnClickListener(v -> {
            SystemUtil.clipString(mContext, Constants.WE_CHAT_ID);
            toast(getString(R.string.we_char_id_copy_success));
            //            gotoWeChat();
        });
        mWciaImage.setOnLongClickListener(v -> {
            saveQrCodeImage();
            return false;
        });
    }

    /**
     * 保存二维码
     */
    private void saveQrCodeImage() {
        RxPermissions.getInstance(mContext).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (mSubscription != null)
                            mSubscription.unsubscribe();
                        mSubscription = Observable.unsafeCreate((Observable.OnSubscribe<Boolean>) subscriber -> {
                            Drawable drawable = DrawableUtil.getDrawable(R.drawable.we_chat_qr_code);
                            Bitmap bitmap = ImageUtils.drawable2Bitmap(drawable);
                            File file = new File(Environment.getExternalStorageDirectory(), "kkw_image");
                            if (!file.exists())
                                file.mkdir();
                            file = new File(file, "kingKongWallet.JPG");
                            boolean save = ImageUtils.save(bitmap, file, Bitmap.CompressFormat.JPEG, false);
                            if (save) {
                                // 插入到系统图库
//                                try {
//                                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
//                                            file.getAbsolutePath(), "kingKongWallet.JPG", null);
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                                通知图库更新
                                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                            }
                            subscriber.onNext(save);
                            subscriber.onCompleted();
                        }).compose(RxUtil.rxSchedulerHelper())
                                .subscribe(new SimpSubscriber<Boolean>() {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        toast(aBoolean ? getString(R.string.save_success) : getString(R.string.save_error));
                                        //                                        gotoWeChat();
                                    }
                                });
                    } else {
                        toast(getString(R.string.save_error_permission));
                    }
                });
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }
}
