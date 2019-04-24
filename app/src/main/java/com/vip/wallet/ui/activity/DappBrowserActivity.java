package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ActivityManage;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.BuyInfo;
import com.vip.wallet.entity.DappBrowserInfo;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.utils.CalcUtil;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.wallet.WalletHelper;
import com.vip.wallet.web3.Web3View;
import com.vip.wallet.widget.TitleBarView;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

import butterknife.Bind;
import trust.core.entity.Address;
import trust.core.entity.Transaction;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/27 0027 14:10
 * 描述	      ${TODO}
 */

public class DappBrowserActivity extends BaseActivity {
    @Bind(R.id.dba_title)
    TitleBarView mDbaTitle;
    @Bind(R.id.wa_back)
    ImageView mWaBack;
    @Bind(R.id.wa_forth)
    ImageView mWaForth;
    @Bind(R.id.wa_option_bar)
    RelativeLayout mWaOptionBar;
    @Bind(R.id.dba_view_stub)
    ViewStub mViewStub;
    @Bind(R.id.wa_line)
    View mWaLine;
    @Bind(R.id.wa_title_line)
    View mWaTitleLine;
    @Bind(R.id.atd_pb)
    ProgressBar mAtdPb;

    Web3View mDbaWebView;
    DappBrowserInfo mDappBrowserInfo = new DappBrowserInfo();

    public static void start(Context context, DappBrowserInfo dappBrowserInfo) {
        Intent intent = new Intent(context, DappBrowserActivity.class);
        intent.putExtra(Constants.DAPP_BROWSER_INFO, dappBrowserInfo);
        context.startActivity(intent);
    }

    @Override
    protected void destroy() {
        if (mDbaWebView != null) {
            mDbaWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mDbaWebView.removeJavascriptInterface("trust");
            mDbaWebView.setOnSignMessageListener(null);
            mDbaWebView.setOnSignPersonalMessageListener(null);
            mDbaWebView.setOnSignTransactionListener(null);
            mDbaWebView.setOnSignTypedMessageListener(null);
            mDbaWebView.tbsWebviewDestroy(true);
        }
        getWindow().getDecorView().removeCallbacks(delayRunnable);
        super.destroy();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_dapp_browser;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    protected void initData(IPresenter presenter) {
        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.DAPP_BROWSER_INFO);
        if (serializableExtra != null)
            mDappBrowserInfo = (DappBrowserInfo) serializableExtra;
        updateTitle();
        getWindow().getDecorView().postDelayed(delayRunnable, 300);
    }

    Runnable delayRunnable = this::initWebView;

    private void updateTitle() {
        mDbaTitle.setTitleText(mDappBrowserInfo.getTitle());
    }

    private void initWebView() {
        mDbaWebView = (Web3View) mViewStub.inflate();

        mDbaWebView.setChainId(1);
        mDbaWebView.setRpcUrl(Constants.WEB3J_URL);
        mDbaWebView.setWalletAddress(new Address(mDappBrowserInfo.getCard().defAddress));

        mDbaWebView.setOnSignMessageListener(message -> {
            LogUtils.i("setOnSignMessageListener >> " + message);
        });

        mDbaWebView.setOnSignPersonalMessageListener(message -> {
            LogUtils.i("setOnSignPersonalMessageListener >> " + message);
        });

        mDbaWebView.setOnSignTransactionListener(transaction -> {
            LogUtils.i("setOnSignTransactionListener >> " + GsonAdapter.getGson().toJson(transaction));
            BigDecimal div = CalcUtil.div(transaction.value == null ? "0" : transaction.value.toString(), String.valueOf(Math.pow(10, 18)));
            BuyInfo buyInfo = new BuyInfo(transaction.recipient.toString(), WalletHelper.getFirstEthAddress(), transaction.value == null ? "0" : div.toString(), transaction.payload);
            buyInfo.callBackId = transaction.leafPosition;
            if (!ActivityManage.getInstance().isActive(BuyInfoActivity.class)) {
                BuyInfoActivity.start(DappBrowserActivity.this, buyInfo);
            }
        });

        mDbaWebView.setOnSignTypedMessageListener(message -> {
            LogUtils.i("setOnSignTypedMessageListener >> " + message);
        });

        mDbaWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mDbaTitle != null && StringUtils.isEmpty(mDappBrowserInfo.getTitle())) {
                    mDbaTitle.setTitleText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
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
        });
        mDbaWebView.loadUrl(mDappBrowserInfo.getUrl());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String tx = data.getStringExtra(Constants.TX);
        Serializable serializableExtra = data.getSerializableExtra(Constants.BUY_INFO);
        BuyInfo buyInfo = (BuyInfo) serializableExtra;
        Transaction transaction = new Transaction(null, null, null, null, 0, 0, null, buyInfo.callBackId);
        LogUtils.i("tx >> " + tx + "  callId >> " + buyInfo.callBackId);
        if (!StringUtils.isEmpty(tx)) {
            mDbaWebView.onSignTransactionSuccessful(transaction, tx);
        } else {
            mDbaWebView.onSignCancel(transaction);
        }
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
                mDbaWebView.goBack();
                break;
            case R.id.wa_forth:
                mDbaWebView.goForward();
                break;
        }
    }

    public void onPageChange() {
        if (mWaBack == null || mWaForth == null || mDbaWebView == null) {
            return;
        }
        boolean enabled = mWaBack.isEnabled();
        boolean backEnable = mDbaWebView.canGoBack();
        if (enabled != backEnable) {
            mWaBack.setEnabled(backEnable);
        }
        boolean fwEnable = mWaForth.isEnabled();
        boolean fwFEnabled = mDbaWebView.canGoForward();
        if (fwEnable != fwFEnabled) {
            mWaForth.setEnabled(fwFEnabled);
        }
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.DAPP_BROWSER_INFO);
        if (serializable != null) {
            mDappBrowserInfo = (DappBrowserInfo) serializable;
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.DAPP_BROWSER_INFO, mDappBrowserInfo);
    }
}
