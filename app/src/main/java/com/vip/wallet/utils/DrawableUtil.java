package com.vip.wallet.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.vip.wallet.config.ScApplication;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/3 0003 16:00
 * 描述	      ${TODO}
 */

public class DrawableUtil {
    public static Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(ScApplication.getInstance(), resId);
    }
}
