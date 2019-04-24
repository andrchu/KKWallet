package com.vip.wallet.web3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.core.entity.TypedData;

public class Web3View extends com.tencent.smtt.sdk.WebView {
    private static final String JS_PROTOCOL_CANCELLED = "cancelled";
    private static final String JS_PROTOCOL_ON_SUCCESSFUL = "onSignSuccessful(%1$s, \"%2$s\")";
    private static final String JS_PROTOCOL_ON_FAILURE = "onSignError(%1$s, \"%2$s\")";
    @Nullable
    private OnSignTransactionListener onSignTransactionListener;
    @Nullable
    private OnSignMessageListener onSignMessageListener;
    @Nullable
    private OnSignPersonalMessageListener onSignPersonalMessageListener;
    @Nullable
    private OnSignTypedMessageListener onSignTypedMessageListener;
    private JsInjectorClient jsInjectorClient;
    private Web3ViewClient webViewClient;

    public Web3View(@NonNull Context context) {
        this(context, null);
    }

    public Web3View(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Web3View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void setWebChromeClient(com.tencent.smtt.sdk.WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(com.tencent.smtt.sdk.WebViewClient client) {
        super.setWebViewClient(new WrapWebViewClient(webViewClient, client, jsInjectorClient));
    }

/* @Override
    public void setWebViewClient(com.tencent.smtt.sdk.WebChromeClient  client) {
        super.setWebViewClient(new WrapWebViewClient(webViewClient, client, jsInjectorClient));
    }*/

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        jsInjectorClient = new JsInjectorClient(getContext());
        webViewClient = new Web3ViewClient(jsInjectorClient, new UrlHandlerManager());
        com.tencent.smtt.sdk.WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/

        addJavascriptInterface(new SignCallbackJSInterface(
                this,
                innerOnSignTransactionListener,
                innerOnSignMessageListener,
                innerOnSignPersonalMessageListener,
                innerOnSignTypedMessageListener), "trust");
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setWebViewClient(webViewClient);
    }

    public void setWalletAddress(@NonNull Address address) {
        jsInjectorClient.setWalletAddress(address);
    }

    @Nullable
    public Address getWalletAddress() {
        return jsInjectorClient.getWalletAddress();
    }

    public void setChainId(int chainId) {
        jsInjectorClient.setChainId(chainId);
    }

    public int getChainId() {
        return jsInjectorClient.getChainId();
    }

    public void setRpcUrl(@NonNull String rpcUrl) {
        jsInjectorClient.setRpcUrl(rpcUrl);
    }

    @Nullable
    public String getRpcUrl() {
        return jsInjectorClient.getRpcUrl();
    }

    public void addUrlHandler(@NonNull UrlHandler urlHandler) {
        webViewClient.addUrlHandler(urlHandler);
    }

    public void removeUrlHandler(@NonNull UrlHandler urlHandler) {
        webViewClient.removeUrlHandler(urlHandler);
    }

    public void setOnSignTransactionListener(@Nullable OnSignTransactionListener onSignTransactionListener) {
        this.onSignTransactionListener = onSignTransactionListener;
    }

    public void setOnSignMessageListener(@Nullable OnSignMessageListener onSignMessageListener) {
        this.onSignMessageListener = onSignMessageListener;
    }

    public void setOnSignPersonalMessageListener(@Nullable OnSignPersonalMessageListener onSignPersonalMessageListener) {
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
    }

    public void setOnSignTypedMessageListener(@Nullable OnSignTypedMessageListener onSignTypedMessageListener) {
        this.onSignTypedMessageListener = onSignTypedMessageListener;
    }

    public void onSignTransactionSuccessful(Transaction transaction, String signHex) {
        long callbackId = transaction.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignMessageSuccessful(Message message, String signHex) {
        long callbackId = message.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignPersonalMessageSuccessful(Message message, String signHex) {
        long callbackId = message.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_SUCCESSFUL, signHex);
    }

    public void onSignError(Transaction transaction, String error) {
        long callbackId = transaction.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_FAILURE, error);
    }

    public void onSignError(Message message, String error) {
        long callbackId = message.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_FAILURE, error);
    }

    public void onSignCancel(Transaction transaction) {
        long callbackId = transaction.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_FAILURE, JS_PROTOCOL_CANCELLED);
    }

    public void onSignCancel(Message message) {
        long callbackId = message.leafPosition;
        callbackToJS(callbackId, JS_PROTOCOL_ON_FAILURE, JS_PROTOCOL_CANCELLED);
    }

    private void callbackToJS(long callbackId, String function, String param) {
        String callback = String.format(function, callbackId, param);
        post(() -> loadUrl("javascript:" + callback));
    }

    private final OnSignTransactionListener innerOnSignTransactionListener = new OnSignTransactionListener() {
        @Override
        public void onSignTransaction(Transaction transaction) {
            if (onSignTransactionListener != null) {
                onSignTransactionListener.onSignTransaction(transaction);
            }
        }
    };

    private final OnSignMessageListener innerOnSignMessageListener = new OnSignMessageListener() {
        @Override
        public void onSignMessage(Message message) {
            if (onSignMessageListener != null) {
                onSignMessageListener.onSignMessage(message);
            }
        }
    };

    private final OnSignPersonalMessageListener innerOnSignPersonalMessageListener = new OnSignPersonalMessageListener() {
        @Override
        public void onSignPersonalMessage(Message message) {
            onSignPersonalMessageListener.onSignPersonalMessage(message);
        }
    };

    private final OnSignTypedMessageListener innerOnSignTypedMessageListener = new OnSignTypedMessageListener() {
        @Override
        public void onSignTypedMessage(Message<TypedData[]> message) {
            onSignTypedMessageListener.onSignTypedMessage(message);
        }
    };

    private class WrapWebViewClient extends com.tencent.smtt.sdk.WebViewClient {
        private final Web3ViewClient internalClient;
        private final com.tencent.smtt.sdk.WebViewClient externalClient;
        private final JsInjectorClient jsInjectorClient;

        public WrapWebViewClient(Web3ViewClient internalClient, com.tencent.smtt.sdk.WebViewClient externalClient, JsInjectorClient jsInjectorClient) {
            this.internalClient = internalClient;
            this.externalClient = externalClient;
            this.jsInjectorClient = jsInjectorClient;
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
            return externalClient.shouldOverrideUrlLoading(view, url)
                    || internalClient.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
            return externalClient.shouldOverrideUrlLoading(webView, webResourceRequest)
                    || internalClient.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        @Override
        public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
            com.tencent.smtt.export.external.interfaces.WebResourceResponse webResourceResponse = externalClient.shouldInterceptRequest(webView, webResourceRequest);
            if (webResourceResponse != null) {
                try {
                    InputStream in = webResourceResponse.getData();
                    int len = in.available();
                    byte[] data = new byte[len];
                    int readLen = in.read(data);
                    if (readLen == 0) {
                        throw new IOException("Nothing is read.");
                    }
                    String injectedHtml = jsInjectorClient.injectJS(new String(data));
                    webResourceResponse.setData(new ByteArrayInputStream(injectedHtml.getBytes()));
                } catch (IOException ex) {
                    Log.d("INJECT AFTER_EXTRNAL", "", ex);
                }
            } else {
                webResourceResponse = internalClient.shouldInterceptRequest(webView, webResourceRequest);
            }
            return webResourceResponse;
        }
    }
}
