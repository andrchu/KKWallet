package com.vip.wallet.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.other.BaseJavaScriptObject;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.contract.DiscoverContract;
import com.vip.wallet.ui.presenter.DiscoverPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.WalletHelper;
import com.vip.wallet.widget.CustomWebView;
import com.vip.wallet.widget.TitleBarView;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/18 0018 11:10
 * 描述	      ${TODO}
 */

public class DiscoverFragment extends BaseFragment<DiscoverContract.IDiscoverPresenter> implements DiscoverContract.IDiscoverView {


    @Bind(R.id.fdc_view_stub)
    ViewStub mViewStub;

    @Bind(R.id.df_title)
    TitleBarView mDfTitle;

    CustomWebView mFdWebView;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected View setSuccessView() {
        return mFdWebView;
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    protected DiscoverContract.IDiscoverPresenter setPresenter() {
        return new DiscoverPresenter(this);
    }

    @Override
    public void initData(DiscoverContract.IDiscoverPresenter presenter) {
        String address = WalletHelper.getFirstEthAddress().defAddress;
        String url = Constants.BASE_URL + "app/vote/index" + StringUtil.signBuilder(address);
        DiscoverJavaObject discoverJavaObject = new DiscoverJavaObject(mActivity);
        delayRunnable = () -> initWebView(url, discoverJavaObject);
        ScApplication.getInstance().getHandler().post(delayRunnable);
    }

    Runnable delayRunnable;

    void initWebView(String url, DiscoverJavaObject discoverJavaObject) {
        if (mFdWebView == null)
            mFdWebView = (CustomWebView) mViewStub.inflate();
        showView(Constants.LOADING);
        mFdWebView.addJavascriptInterface(discoverJavaObject, Constants.JS_CALL_NAME);
        mFdWebView.loadUrl(url);
        WebChromeClient client = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (getCurrentViewState() == Constants.LOADING)
                        try {
                            showView(Constants.SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtils.i("web title >>>> " + title);
            }
        };
        mFdWebView.setWebChromeClient(client);

    }

    @Override
    public boolean onBackPressedSupport() {
        if (mFdWebView != null && mFdWebView.canGoBack()) {
            mFdWebView.goBack();
            return true;
        } else {
            back();
            return true;
        }
    }

    @Override
    protected void initListener() {
        mDfTitle.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute()) {
                return;
            }
            gotoMarket();
        });
    }

    void gotoMarket() {
        String address = WalletHelper.getFirstEthAddress().defAddress;
        String url = Constants.BASE_URL + "app/coindog/market" + StringUtil.signBuilder(address);
        WebActivity.startWebActivity(mContext, new BrowserInfo("行情", url));
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void destroy() {
        if (mFdWebView != null) {
            mFdWebView.removeJavascriptInterface(Constants.JS_CALL_NAME);
            mFdWebView.destroy();
        }
        ScApplication.getInstance().getHandler().removeCallbacks(delayRunnable);
        super.destroy();
    }

    @Override
    public void reload() {
        showView(Constants.LOADING);
        mFdWebView.reload();
    }

    public class DiscoverJavaObject extends BaseJavaScriptObject {
        public DiscoverJavaObject(BaseActivity activity) {
            super(activity);
        }

        /**
         * 刷新页面
         *
         * @param main
         */
        public void refreshPage(Main main) {
            if (mActivity == null)
                return;
            mActivity.runOnUiThread(() -> {
                if (mFdWebView != null) {
                    mFdWebView.reload();
                }
            });
        }

        /**
         * 设置标题栏属性
         *
         * @param main
         */
        public void setTitleViewAttrs(Main main) {
            TitleViewAttrs titleViewAttrs = GsonAdapter.getGson().fromJson(main.data, TitleViewAttrs.class);
            if (mActivity == null)
                return;
            mActivity.runOnUiThread(() -> {
                try {
                    if (!StringUtils.isEmpty(titleViewAttrs.bgColor)) {
                        mDfTitle.setBackGround(Color.parseColor(titleViewAttrs.bgColor));
                    }

                    if (!StringUtils.isEmpty(titleViewAttrs.textColor)) {
                        mDfTitle.setTitleColor(Color.parseColor(titleViewAttrs.textColor));
                    }

                    if (!StringUtils.isEmpty(titleViewAttrs.title)) {
                        mDfTitle.setTitleText(titleViewAttrs.title);
                    }
                    mDfTitle.setEnableLine(titleViewAttrs.showShadow == 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
