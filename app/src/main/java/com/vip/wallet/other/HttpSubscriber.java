package com.vip.wallet.other;


import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.HttpExceptionHandler;

import rx.Subscriber;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:20   <br/><br/>
 * 描述:	     网络请求专用subscriber
 */
public abstract class HttpSubscriber<T> extends Subscriber<T> {

	@Override
	public void onError(Throwable e) {
		onError(HttpExceptionHandler.processException(e));
	}

	/**
	 * 网络请求错误
	 *
	 * @param exception {@link HttpExceptionHandler#processException(Throwable) 错误码}
	 */
	protected abstract void onError(ApiHttpException exception);

	@Override
	public void onCompleted() {

	}
}
