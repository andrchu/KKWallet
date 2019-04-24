package com.vip.wallet.utils;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.vip.wallet.config.ScApplication;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/2/8 15:43   <br/><br/>
 * 描述:	      ${TODO}
 */
public class ColorUtil {
    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(ScApplication.getInstance(), resId);
    }
}
