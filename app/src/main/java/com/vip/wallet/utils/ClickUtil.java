package com.vip.wallet.utils;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/2/8 09:57   <br/><br/>
 * 描述:	     防止手抖点击多次
 */
public class ClickUtil {

	public static final int OFFSET = 500;

	private static long preClickTime;

	/**
	 * 防抖判断
	 *
	 * @return true 能执行
	 */
	public static boolean hasExecute() {
		return hasExecute(OFFSET);
	}

	/**
	 * 防抖判断
	 *
	 * @return true 能执行
	 */
	public static boolean hasExecute(long offset_time) {

		long currentTime = System.currentTimeMillis();

		long offset = currentTime - preClickTime;

		preClickTime = currentTime;

		return offset > offset_time;
	}
}
