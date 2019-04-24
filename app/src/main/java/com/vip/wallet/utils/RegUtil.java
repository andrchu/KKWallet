package com.vip.wallet.utils;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/8 0008 15:40
 * 描述	      ${TODO}
 */

public class RegUtil {
    public static int getPwdLevel(String pwd) {
        if (pwd.matches("(.{0,8})|(^((\\d+)|([A-Za-z]+)|(\\W+))$)")){
            //弱
            return 0;
        } else if (pwd.matches("([0-9]+(\\W+|\\_+|[a-z]+))+|([a-z]+(\\W+|\\_+|\\d+))+|((\\W+|\\_+)+(\\d+|\\w+))+")) {
            //中
            return 1;
        } else {
            //强
            return 2;
        }
    }
}
