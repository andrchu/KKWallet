package com.vip.wallet.web3;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.util.Map;

import okhttp3.HttpUrl;

public class Web3ViewClient extends com.tencent.smtt.sdk.WebViewClient {

    private final Object lock = new Object();

    private final JsInjectorClient jsInjectorClient;
    private final UrlHandlerManager urlHandlerManager;

    private boolean isInjected;

    public Web3ViewClient(JsInjectorClient jsInjectorClient, UrlHandlerManager urlHandlerManager) {
        this.jsInjectorClient = jsInjectorClient;
        this.urlHandlerManager = urlHandlerManager;
    }

    void addUrlHandler(UrlHandler urlHandler) {
        urlHandlerManager.add(urlHandler);
    }

    void removeUrlHandler(UrlHandler urlHandler) {
        urlHandlerManager.remove(urlHandler);
    }

    @Override
    public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
        return shouldOverrideUrlLoading(webView, s, false, false);
    }

    @Override
    public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
        if (webResourceRequest == null || webView == null) {
            return false;
        }
        String url = webResourceRequest.getUrl().toString();
        boolean isMainFrame = webResourceRequest.isForMainFrame();
        return shouldOverrideUrlLoading(webView, url, isMainFrame, webResourceRequest.isRedirect());
    }

    private boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url, boolean isMainFrame, boolean isRedirect) {
        boolean result = false;
        synchronized (lock) {
            isInjected = false;
        }
        String urlToOpen = urlHandlerManager.handle(url);
        if (!url.startsWith("http")) {
            result = true;
        }
        if (isMainFrame && isRedirect) {
            urlToOpen = url;
            result = true;
        }

        if (result && !TextUtils.isEmpty(urlToOpen)) {
            webView.loadUrl(urlToOpen);
        }
        return result;
    }

    @Override
    public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView,
                                                                                                  com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
        if (webResourceRequest == null) {
            return null;
        }
        if (!webResourceRequest.getMethod().equalsIgnoreCase("GET") || !webResourceRequest.isForMainFrame()) {
            if (webResourceRequest.getMethod().equalsIgnoreCase("GET")
                    && (webResourceRequest.getUrl().toString().contains(".js")
                    || webResourceRequest.getUrl().toString().contains("json")
                    || webResourceRequest.getUrl().toString().contains("css"))) {
                synchronized (lock) {
                    if (!isInjected) {
                        injectScriptFile(webView);
                        isInjected = true;
                    }
                }
            }
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        HttpUrl httpUrl = HttpUrl.parse(webResourceRequest.getUrl().toString());
        if (httpUrl == null) {
            return null;
        }
        Map<String, String> headers = webResourceRequest.getRequestHeaders();
        JsInjectorResponse response;
        try {
            response = jsInjectorClient.loadUrl(httpUrl.toString(), headers);
        } catch (Exception ex) {
            return null;
        }
        if (response == null || response.isRedirect || response.data == null) {
            return null;
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(response.data.getBytes());
            com.tencent.smtt.export.external.interfaces.WebResourceResponse webResourceResponse = new com.tencent.smtt.export.external.interfaces.WebResourceResponse(
                    response.mime, response.charset, inputStream);
            synchronized (lock) {
                isInjected = true;
            }
            return webResourceResponse;
        }
    }

    private void injectScriptFile(com.tencent.smtt.sdk.WebView view) {
        String js = jsInjectorClient.assembleJs(view.getContext(), "%1$s%2$s");
        byte[] buffer = js.getBytes();
        String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);

        view.post(() -> view.loadUrl("javascript:(function() {" +
                "var parent = document.getElementsByTagName('head').item(0);" +
                "var script = document.createElement('script');" +
                "script.type = 'text/javascript';" +
                // Tell the browser to BASE64-decode the string into your script !!!
                "script.innerHTML = window.atob('" + encoded + "');" +
                "parent.appendChild(script)" +
                "})()"));
    }

    @Override
    public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
        sslErrorHandler.proceed();
    }

    public void onReload() {
        synchronized (lock) {
            isInjected = false;
        }
    }
}