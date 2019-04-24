package com.vip.wallet.web3;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import com.tencent.smtt.sdk.WebView ;

import com.google.gson.Gson;

import java.math.BigInteger;

import trust.core.entity.Address;
import trust.core.entity.Message;
import trust.core.entity.Transaction;
import trust.core.entity.TypedData;
import trust.core.util.Hex;

public class SignCallbackJSInterface {

    private final WebView webView;
    @NonNull
    private final OnSignTransactionListener onSignTransactionListener;
    @NonNull
    private final OnSignMessageListener onSignMessageListener;
    @NonNull
    private final OnSignPersonalMessageListener onSignPersonalMessageListener;
    @NonNull
    private final OnSignTypedMessageListener onSignTypedMessageListener;

    public SignCallbackJSInterface(
            WebView webView,
            @NonNull OnSignTransactionListener onSignTransactionListener,
            @NonNull OnSignMessageListener onSignMessageListener,
            @NonNull OnSignPersonalMessageListener onSignPersonalMessageListener,
            @NonNull OnSignTypedMessageListener onSignTypedMessageListener) {
        this.webView = webView;
        this.onSignTransactionListener = onSignTransactionListener;
        this.onSignMessageListener = onSignMessageListener;
        this.onSignPersonalMessageListener = onSignPersonalMessageListener;
        this.onSignTypedMessageListener = onSignTypedMessageListener;
    }

    @JavascriptInterface
    public void signTransaction(
            int callbackId,
            String recipient,
            String value,
            String nonce,
            String gasLimit,
            String gasPrice,
            String payload) {

        System.out.println("callbackId >> " + callbackId + "   recipient>>" + recipient + "   value>>" + value + "  nonce>>" + nonce + "   gasLimit>>" + gasLimit + "  gasPrice>>" + gasPrice + "  payload>>" + payload);
        gasLimit = "";
        gasPrice = "";
        Transaction transaction = new Transaction(
                TextUtils.isEmpty(recipient) ? Address.EMPTY : new Address(recipient),
                null,
                Hex.hexToBigInteger(value),
                Hex.hexToBigInteger(gasPrice, BigInteger.ZERO),
                Hex.hexToLong(gasLimit, 0),
                Hex.hexToLong(nonce, -1),
                payload,
                callbackId);
        onSignTransactionListener.onSignTransaction(transaction);

    }

    @JavascriptInterface
    public void signMessage(int callbackId, String data) {
        webView.post(() -> onSignMessageListener.onSignMessage(new Message<>(data, getUrl(), callbackId)));
    }

    @JavascriptInterface
    public void signPersonalMessage(int callbackId, String data) {
        webView.post(() -> onSignPersonalMessageListener.onSignPersonalMessage(new Message<>(data, getUrl(), callbackId)));
    }

    @JavascriptInterface
    public void signTypedMessage(int callbackId, String data) {
        webView.post(() -> {
            TrustProviderTypedData[] rawData = new Gson().fromJson(data, TrustProviderTypedData[].class);
            int len = rawData.length;
            TypedData[] typedData = new TypedData[len];
            for (int i = 0; i < len; i++) {
                typedData[i] = new TypedData(rawData[i].name, rawData[i].type, rawData[i].value);
            }
            onSignTypedMessageListener.onSignTypedMessage(new Message<>(typedData, getUrl(), callbackId));
        });
    }

    private String getUrl() {
        return webView == null ? "" : webView.getUrl();
    }

    private static class TrustProviderTypedData {
        public String name;
        public String type;
        public Object value;
    }
}
