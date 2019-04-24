package com.vip.wallet.http;


import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonParseException;
import com.vip.wallet.exception.ApiHttpException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/10/17 10:01   <br/><br/>
 * 描述:	      网络异常处理
 */
public class HttpExceptionHandler {
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1001;
    /**
     * 找不到服务器
     */
    public static final int UNKNOWN_HOST_ERROR = 1003;
    /**
     * Json解析错误
     */
    public static final int JSON_PARSE_ERROR = 1005;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1006;

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 1007;

    /**
     * 处理异常，返回约定异常
     *
     * @return 错误码有5种 {@link HttpExceptionHandler#NETWORK_ERROR 网络错误} {@link HttpExceptionHandler#UNKNOWN_HOST_ERROR 找不到服务器}
     * {@link HttpExceptionHandler#JSON_PARSE_ERROR Json解析错误} {@link HttpExceptionHandler#CONNECT_TIMEOUT 连接超时}
     * {@link HttpExceptionHandler#UNKNOWN_ERROR 未知错误}
     */
    public static ApiHttpException processException(Throwable e) {

        LogUtils.e("http request error:>>>>>>>>>>>>" + e.toString());

        if (e instanceof ApiHttpException) {
            return (ApiHttpException) e;
        }

        ApiHttpException exception = new ApiHttpException(e.getMessage(), UNKNOWN_ERROR);

        if (e instanceof ConnectException || e instanceof SocketTimeoutException) {

            exception = new ApiHttpException("连接超时", CONNECT_TIMEOUT);

        } else if (e instanceof TimeoutException) {

            exception = new ApiHttpException("请求超时", CONNECT_TIMEOUT);

        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (httpException.code() >= 500) {
                exception = new ApiHttpException("服务器异常！", NETWORK_ERROR);
            } else
                exception = new ApiHttpException("网络错误！", NETWORK_ERROR);

        } else if (e instanceof UnknownHostException) {

            exception = new ApiHttpException("找不到服务器", UNKNOWN_HOST_ERROR);

        } else if (e instanceof JsonParseException
                || e instanceof JSONException) {
            exception = new ApiHttpException("JSON解析错误", JSON_PARSE_ERROR);
        }

        return exception;
    }
}
