package com.vip.wallet.utils;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/11/15 10:18   <br/><br/>
 * 描述:	      ${TODO}
 */
public class MD5 {

	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f'};
		try {
			final java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];

			int k = 0;
			for (int i = 0; i < 16; i++) {
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}
