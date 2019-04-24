package com.vip.wallet.utils;

import android.util.Log;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:    2018/1/22 10:35 <br/><br/>
 * 描述:	      日志工具类
 */
public final class LogUtil {

    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;
    private static final int ALL = 5;
    private static final int LOWEST = 0;
    private static final int LEVEL = ALL;
    public String tag = "LogUtil";

    public LogUtil(String tag) {
        this.tag = tag;
    }

    public LogUtil() {
    }

    public static LogUtil getInstance(Class cla) {
        return new LogUtil(cla.getSimpleName());
    }

    public static LogUtil getInstance() {
        return new LogUtil();
    }
    public void d(String msg, String tag) {
        if (LEVEL >= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public void d(String msg) {
        d(msg, tag);
    }

    public void i(String msg, String tag) {
        if (LEVEL >= INFO) {
            Log.i(tag, msg);
        }
    }

    public void i(String msg) {
        i(msg, tag);
    }

    public void w(String msg, String tag) {
        if (LEVEL >= WARN) {
            Log.w(tag, msg);
        }
    }

    public void w(String msg) {
        w(msg, tag);
    }

    public void e(String msg, String tag) {
        if (LEVEL >= ERROR) {
            Log.e(tag, msg);
        }
    }

    public void e(String msg) {
        e(msg, tag);
    }
}
