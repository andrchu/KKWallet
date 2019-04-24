package com.vip.wallet.exception;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 14:30   <br/><br/>
 * 描述:	      HTTP请求约定异常
 */
public class ApiHttpException extends Exception {
	private int errorCode;

	public ApiHttpException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 获取错误码
	 */
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "ApiHttpException{" +
				"errorCode=" + errorCode +
				"msg=" + getMessage() +
				'}';
	}
}
