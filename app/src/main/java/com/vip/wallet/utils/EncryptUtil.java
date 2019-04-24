package com.vip.wallet.utils;

import com.vip.wallet.config.Constants;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/25 0025 16:09
 * 描述	      ${TODO}
 */

public class EncryptUtil {
    public static String getSignString(String str) {
        String md5 = MD5.getMD5(StringUtil.toBytes(str + Constants.SIGN_KEY));
        return md5.substring(0, md5.length() / 2);
    }
}
