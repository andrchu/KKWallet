package com.vip.wallet.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.blankj.utilcode.util.StringUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.widget.TitleBarView;

import butterknife.Bind;

import static com.vip.wallet.config.Constants.SCAN_CODE_ERROR;
import static com.vip.wallet.config.Constants.SCAN_CODE_OK;
import static com.vip.wallet.config.Constants.SCAN_CODE_RESULT;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/10 13:38
 * 描述	     扫描二维码
 */
public class ScannerCodeActivity extends BaseActivity implements CodeUtils.AnalyzeCallback {

    @Bind(R.id.sca_title_view)
    TitleBarView mScaTitleView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scanner_code;
    }

    private String callback;

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    public static void startScanner(Activity activity) {
        activity.startActivityForResult(new Intent(activity, ScannerCodeActivity.class), 100);
    }

    public static void startScanner(Activity activity, String callback) {
        Intent intent = new Intent(activity, ScannerCodeActivity.class);
        intent.putExtra(Constants.JS_CALL_BACK, callback);
        activity.startActivityForResult(intent, 100);
    }

    @Override
    protected void initData(IPresenter presenter) {
        String stringExtr = getIntent().getStringExtra(Constants.JS_CALL_BACK);
        if (!StringUtils.isEmpty(stringExtr))
            callback = stringExtr;
        RxPermissions.getInstance(this).request(Manifest.permission.CAMERA).compose(RxUtil.rxSchedulerHelper()).subscribe(aBoolean -> {
            if (aBoolean) {
                /**
                 * 执行扫面Fragment的初始化操作
                 */
                CaptureFragment captureFragment = new CaptureFragment();
                // 为二维码扫描界面设置定制化界面
                CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_scanner_camera);

                captureFragment.setAnalyzeCallback(ScannerCodeActivity.this);

                getSupportFragmentManager().beginTransaction().replace(R.id.sca_scanner_fragment, captureFragment).commit();
                //                captureFragment.setCameraInitCallBack(null);
            } else {
                toast("权限被禁止,无法进行扫描!");
                finish();
            }
        });
    }

    @Override
    protected void initListener() {
        mScaTitleView.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            selectPick();
        });
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        String stringExtr = savedInstanceState.getString(Constants.JS_CALL_BACK);
        if (!StringUtils.isEmpty(stringExtr))
            callback = stringExtr;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putString(Constants.JS_CALL_BACK, callback);
    }

    @Override
    public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
        hideLoadingDialog();
        Intent intent = new Intent();
        intent.putExtra(SCAN_CODE_RESULT, result);
        intent.putExtra(Constants.JS_CALL_BACK, callback);
        setResult(SCAN_CODE_OK, intent);
        finish();
    }

    @Override
    public void onAnalyzeFailed() {
        hideLoadingDialog();
        Intent intent = new Intent();
        intent.putExtra(SCAN_CODE_RESULT, "error");
        setResult(SCAN_CODE_ERROR, intent);
        finish();
    }

    /**
     * 选择图库
     */
    public void selectPick() {
        // 激活系统图库，选择一张图片
        RxPermissions.getInstance(this).request(Manifest.permission.READ_EXTERNAL_STORAGE).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, 200);
                        } else {
                            toast("权限被禁止");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (data == null || data.getData() == null)
                return;
            Uri uri = data.getData();
            parseQrImage(uri);
        }
    }

    private void parseQrImage(Uri uri) {
        showLoadingDialog("解析中...");
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = ScannerCodeActivity.this.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            CodeUtils.analyzeBitmap(picturePath, this);
            cursor.close();
        }
    }
}
