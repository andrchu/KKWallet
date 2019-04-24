package com.vip.wallet.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.AdvertInfo;
import com.vip.wallet.ui.contract.SplashContract;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.ui.presenter.SplashPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.LoadImageUtil;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.vip.wallet.config.Constants.CONFIG_VERSION_16;
import static com.vip.wallet.config.Constants.CONFIG_VERSION_17;

/**
 * 创建者     金国栋
 * 创建时间   2018/1/30 14:17
 * 描述	     启动页面
 */
public class SplashActivity extends BaseActivity<SplashContract.ISplashPresenter> implements SplashContract.ISplashView {

    @Bind(R.id.as_bg_image)
    ImageView mAsBgImage;
    @Bind(R.id.as_skip)
    TextView mAsSkip;

    @Override
    protected void init() {
        //解决安装后直接点击打开，然后按home键回到桌面，再重新点击图标进来会重新创建一个新的SplashActivity
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        super.init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_spalsh;
    }

    @Override
    protected SplashContract.ISplashPresenter setPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void initData(SplashContract.ISplashPresenter presenter) {
        if (ScApplication.getInstance().getConfig().getVersion() >= CONFIG_VERSION_16) {
            presenter.getAdvertInfo();
        } else {
            gotoPage();
        }
    }

    @Override
    protected void initListener() {
        if (mAsSkip == null)
            return;
        mAsSkip.setOnClickListener(this);
        mAsBgImage.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.as_skip:
                //停止计时
                getPresenter().stopKeepTime();
                gotoPage();
                break;
            case R.id.as_bg_image:
                if (advertInfo == null || StringUtils.isEmpty(advertInfo.url))
                    return;
                if (isImageHasClick) {
                    isImageClick = true;
                    getPresenter().stopKeepTime();
                    gotoPage();
                }
                break;

        }
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    public void gotoInitWalletActivity() {
        startActivity(InitWalletActivity.class, true);
    }

    @Override
    public void gotoHomeActivity() {
        if (isImageClick) {
            HomeActivity.start(this, Constants.BACK_UP_TYPE_GOTO_URL, advertInfo.url);
            finish();
        } else {
            startActivity(HomeActivity.class, true);
        }
    }

    @Override
    public void getAdvertInfoSuccess(AdvertInfo advertInfo) {
        if (advertInfo == null) {
            gotoPage();
        } else {
            loadImage(advertInfo);
        }
    }

    @Override
    public void showKeepTime(long l) {
        mAsSkip.setText(String.format("%s %s", getString(R.string.skip), l));
        if (l <= 1)
            gotoPage();
    }

    private boolean isImageClick = false;
    private boolean isImageHasClick = false;
    private AdvertInfo advertInfo;

    void loadImage(AdvertInfo advertInfo) {
        this.advertInfo = advertInfo;
        LoadImageUtil.loadNetImage(this, advertInfo.img.replace("https", "http"), mAsBgImage, R.drawable.shape_splash_bg, R.drawable.shape_splash_bg, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                gotoPage();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mAsSkip.setVisibility(View.VISIBLE);
                showKeepTime(advertInfo.time);
                getPresenter().keepTime(advertInfo);
                isImageHasClick = true;
                return false;
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    void gotoPage() {
        Config config = ScApplication.getInstance().getConfig();
        if (config.getVersion() < CONFIG_VERSION_16) {
            //兼容操作  2.5.0数据更新
            showLoadingDialog("旧版本兼容更新...");
            getPresenter().compatibility();
            return;
        }

        if (config.getVersion() == CONFIG_VERSION_16) {
            //兼容操作  2.6.0数据更新
            getPresenter().compatibility17();
            return;
        }

        if (!config.isInit() || StringUtils.isEmpty(config.getPwd())) {
            gotoInitWalletActivity();
        } else {
            gotoHomeActivity();
        }
    }

    @Override
    public void compatibilityFinish() {
        hideLoadingDialog();
        ScApplication.getInstance().getConfig().setVersion(CONFIG_VERSION_16);
        gotoPage();
    }

    @Override
    public void compatibilityError(Throwable e) {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        if (loadingDialog != null) {
            loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            loadingDialog.setTitleText("更新失败，请卸载重新安装！");
            loadingDialog.setOnCancelListener(dialog -> finish());
        }
    }

    @Override
    public void compatibility17Finish() {
        ScApplication.getInstance().getConfig().setVersion(CONFIG_VERSION_17);
        gotoPage();
    }
}
