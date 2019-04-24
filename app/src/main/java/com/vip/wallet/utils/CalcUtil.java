package com.vip.wallet.utils;

import java.math.BigDecimal;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/19 0019 17:53
 * 描述	      ${TODO}
 */

public class CalcUtil {
    public static BigDecimal add(String a1, String a2) {
        return new BigDecimal(a1).add(new BigDecimal(a2));
    }

    public static BigDecimal sub(String s1, String s2) {
        return new BigDecimal(s1).subtract(new BigDecimal(s2));
    }

    public static BigDecimal mul(String m1, String m2) {
        return new BigDecimal(m1).multiply(new BigDecimal(m2));
    }

    public static BigDecimal div(String d1, String d2) {
        return new BigDecimal(d1).divide(new BigDecimal(d2));
    }

}
