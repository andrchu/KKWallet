package com.vip.wallet.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * 创建者     金国栋
 * 创建时间   2018/3/23 0023 13:35
 * 描述	      ${TODO}
 */

public class CustomWebView extends com.tencent.smtt.sdk.WebView {
    public CustomWebView(Context context) {
        this(context, null);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        WebSettings webSettings = getSettings();
        //支持缩放，默认为true。
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //开启javascript
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的

        webSettings.setBlockNetworkImage(false);
        //开启缓存
        webSettings.setAppCacheEnabled(false);
//        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //获取触摸焦点
        requestFocusFromTouch();
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //多窗口
        webSettings.supportMultipleWindows();
        //允许访问文件
        webSettings.setAllowFileAccess(true);

        setWebViewClient(client);
    }

    public WebViewClient client = new WebViewClient() {

        @Override
        public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            sslErrorHandler.proceed();
        }


        @Override
        public void onReceivedError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.export.external.interfaces.WebResourceError webResourceError) {
            //                super.onReceivedError(view, request, error);
            LogUtils.e("error >> " + webResourceError.toString());
            if (mListener != null) {
                mListener.onError();
            }
        }

        /*@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.i("webView >> onPageStarted");
            if (mListener != null) {
                mListener.onPageChange();
            }
        }*/

        @Override
        public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (mListener != null) {
                mListener.onPageFinished(s);
            }
            //                webSettings.setBlockNetworkImage(false);
        }
    };

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private Listener mListener;

    public interface Listener {
        void onError();

        void onPageFinished(String url);
    }

    @Override
    public void destroy() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        mListener = null;
        tbsWebviewDestroy(true);
    }
}
