package com.vip.wallet.utils;


import com.vip.wallet.entity.Response;
import com.vip.wallet.exception.ApiHttpException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 创建者     金国栋                  <br/><br/>
 * 创建时间    2016/09/21 11:18        <br/><br/>
 * 描述	     Rx工具类
 */
public class RxUtil {

	/**
	 * 统一线程处理
	 */
	public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {    //compose简化线程
		return rxSchedulerHelper(0);
	}

	/**
	 * 统一线程处理
	 *
	 * @param delay 延时 单位-毫秒
	 */
	public static <T> Observable.Transformer<T, T> rxSchedulerHelper(long delay) {
		return observable -> observable.subscribeOn(Schedulers.io())
				.delay(delay, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread());
	}

	/**
	 * 统一处理返回结果
	 *
	 * @param <T>
	 */
	public static <T> Observable.Transformer<Response<T>, T> handleResult() {
		return httpResponseObservable -> httpResponseObservable.flatMap(new Func1<Response<T>, Observable<T>>() {
			@Override
			public Observable<T> call(Response<T> response) {
				if (response.isSuccess()) {
					return createData(response.data);
				} else {
					return Observable.error(new ApiHttpException(response.message, response.code));
				}
			}
		});
	}

	public static <T> Observable<T> createData(final T t) {
		return Observable.unsafeCreate(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
	}
}
