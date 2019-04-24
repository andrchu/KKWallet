package com.vip.wallet.entity;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/10/20 14:04   <br/><br/>
 * 描述:	      ${TODO}
 */
public class Response<T> {

	public static final int SUCCESS = 0;

	public int code;
	public String message;
	public T data;

	@Override
	public String toString() {
		return "Response{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}

	public boolean isSuccess() {
		return code == SUCCESS;
	}
}
