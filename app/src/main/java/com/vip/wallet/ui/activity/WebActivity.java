package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.other.AndroidBug5497Workaround;
import com.vip.wallet.other.BaseJavaScriptObject;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.widget.CustomWebView;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;

import butterknife.Bind;

import static com.vip.wallet.config.Constants.SCAN_CODE_OK;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/24 0024 18:17
 * 描述	      ${TODO}
 */

public class WebActivity extends BaseActivity {
    @Bind(R.id.wa_title)
    TitleBarView mWaTitle;

    @Bind(R.id.atd_pb)
    ProgressBar mAtdPb;
    @Bind(R.id.wa_option_bar)
    RelativeLayout mWaOptionBar;
    @Bind(R.id.wa_line)
    View mWaLine;
    @Bind(R.id.wa_back)
    ImageView mWaBack;
    @Bind(R.id.wa_forth)
    ImageView mWaForth;
    @Bind(R.id.wa_title_line)
    View mWaTitleLine;
    @Bind(R.id.wa_view_stub)
    ViewStub mViewStub;

    CustomWebView mWaWebView;
    private BrowserInfo browserInfo;

    public static void startWebActivity(Context context, BrowserInfo browserInfo) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constants.BROWSER_INFO, browserInfo);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        super.init();
        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.BROWSER_INFO);
        if (serializableExtra != null)
            this.browserInfo = (BrowserInfo) serializableExtra;

        updateTitle();

        getWindow().getDecorView().postDelayed(delayRunnable, 300);
    }

    Runnable delayRunnable = this::initWebView;

    private void initWebView() {
        mWaWebView = (CustomWebView) mViewStub.inflate();
        JavaJsObject javaJsObject = new JavaJsObject(this);
        mWaWebView.addJavascriptInterface(javaJsObject, Constants.JS_CALL_NAME);
        LogUtils.i("webView >> " + browserInfo.getUrl());
        mWaWebView.loadUrl(browserInfo.getUrl());
        WebChromeClient client = new WebChromeClient() {
            @Override
            public void onReceivedTitle(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (StringUtils.isEmpty(browserInfo.getTitle())) {
                    setTitle(s);
                }
            }

            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mAtdPb != null) {
                    mAtdPb.setProgress(newProgress);
                    if (newProgress >= 100) {
                        mAtdPb.setVisibility(View.GONE);
                    } else {
                        if (mAtdPb.getVisibility() != View.VISIBLE) {
                            mAtdPb.setVisibility(View.VISIBLE);
                        }
                    }
                    onPageChange();
                }
            }

        };
        mWaWebView.setWebChromeClient(client);
    }

    private void updateTitle() {
        setTitle(browserInfo.getTitle());
    }

    private void setTitle(String title) {
        if (mWaTitle != null) {
            mWaTitle.setTitleText(title);
        }
    }

    private void setTitleBg(String color) {
        int c = Color.parseColor(color);
        mWaTitle.setBackGround(c);
    }

    private void setTitleTextColor(String color) {
        int c = Color.parseColor(color);
        mWaTitle.setTitleColor(c);
        mWaTitle.setBackImageColor(c);
    }


    @Override
    protected void initListener() {
        mWaBack.setEnabled(false);
        mWaForth.setEnabled(false);
        mWaBack.setOnClickListener(this);
        mWaForth.setOnClickListener(this);
    }


    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.wa_back:
                mWaWebView.goBack();
                break;
            case R.id.wa_forth:
                mWaWebView.goForward();
                break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (mWaWebView != null && mWaWebView.canGoBack())
            mWaWebView.goBack();
        else
            super.onBackPressedSupport();
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.BROWSER_INFO);
        if (serializable != null)
            browserInfo = (BrowserInfo) serializable;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.BROWSER_INFO, browserInfo);
    }


    @Override
    protected void destroy() {
        if (mWaWebView != null) {
            mWaWebView.removeJavascriptInterface(Constants.JS_CALL_NAME);
            mWaWebView.destroy();
        }
        getWindow().getDecorView().removeCallbacks(delayRunnable);
        super.destroy();
    }

    @Override
    @JavascriptInterface
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SCAN_CODE_OK) {
            //js调用扫描二维码结果
            String result = data.getStringExtra(Constants.SCAN_CODE_RESULT);
            String callback = data.getStringExtra(Constants.JS_CALL_BACK);
            if (mWaWebView != null) {
                String format = String.format("javascript:%s('%s')", callback, result);
                LogUtils.i("call js >> " + format);
                mWaWebView.loadUrl(format);
            }
        }
    }

    public void onPageChange() {
        if (mWaBack == null || mWaForth == null || mWaWebView == null) {
            return;
        }
        boolean enabled = mWaBack.isEnabled();
        boolean backEnable = mWaWebView.canGoBack();
        if (enabled != backEnable) {
            mWaBack.setEnabled(backEnable);
        }
        boolean fwEnable = mWaForth.isEnabled();
        boolean fwFEnabled = mWaWebView.canGoForward();
        if (fwEnable != fwFEnabled) {
            mWaForth.setEnabled(fwFEnabled);
        }
    }


    public class JavaJsObject extends BaseJavaScriptObject {

        public JavaJsObject(BaseActivity activity) {
            super(activity);
        }

        /**
         * 刷新页面
         *
         * @param main
         */
        public void refreshPage(Main main) {
            runOnUiThread(() -> {
                if (mWaWebView != null) {
                    mWaWebView.reload();
                }
            });
        }

        /**
         * 设置标题栏属性
         *
         * @param main
         */
        public void setTitleViewAttrs(Main main) {
            LogUtils.i("webView call Thread >> " + Thread.currentThread().getName());
            TitleViewAttrs titleViewAttrs = GsonAdapter.getGson().fromJson(main.data, TitleViewAttrs.class);
            runOnUiThread(() -> {
                if (!StringUtils.isEmpty(titleViewAttrs.bgColor)) {
                    setTitleBg(titleViewAttrs.bgColor);
                }

                if (!StringUtils.isEmpty(titleViewAttrs.textColor)) {
                    setTitleTextColor(titleViewAttrs.textColor);
                }

                if (!StringUtils.isEmpty(titleViewAttrs.title)) {
                    setTitle(titleViewAttrs.title);
                }
                if (mWaTitleLine != null)
                    mWaTitleLine.setVisibility(titleViewAttrs.showShadow == 0 ? View.VISIBLE : View.GONE);
            });
        }

        /**
         * 控制进度条显示隐藏(默认显示)
         *
         * @param main
         */
        public void showProgressBar(Main main) {
            runOnUiThread(() -> {
                if (main.data.equals("0")) {
                    mAtdPb.setVisibility(View.VISIBLE);
                } else {
                    mAtdPb.setVisibility(View.GONE);
                }
            });
        }

        /**
         * @param main
         */
        public void showToolItem(Main main) {
            runOnUiThread(() -> {
                if (main.data.equals("0")) {
                    mWaOptionBar.setVisibility(View.VISIBLE);
                    mWaLine.setVisibility(View.VISIBLE);
                } else {
                    mWaOptionBar.setVisibility(View.GONE);
                    mWaLine.setVisibility(View.GONE);
                }
            });
        }
    }
}
