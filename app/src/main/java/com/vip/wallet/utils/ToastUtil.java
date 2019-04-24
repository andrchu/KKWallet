package com.vip.wallet.utils;

import android.content.Context;
import android.widget.Toast;

import com.vip.wallet.config.ScApplication;


/**
 * 创建者     金国栋              <br/><br/>
 * 创建时间   2018/1/22 14:37       <br/><br/>
 * 描述	     吐司工具类
 */
public class ToastUtil {

    /**
     * 弹出吐司-时间稍短
     *
     * @param str
     */
    public static Toast toastS(String str) {
        return toastS(ScApplication.getInstance(), str);
    }

    /**
     * 弹出吐司-时间稍短
     *
     * @param str
     */
    public static Toast toastS(Context context, String str) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(str);
        toast.show();
        return toast;
    }

    /**
     * 弹出吐司-时间稍长
     *
     * @param str
     */
    public static Toast toastL(String str) {
        return toastL(ScApplication.getInstance(), str);
    }

    /**
     * 弹出吐司-时间稍长
     *
     * @param str
     */
    public static Toast toastL(Context context, String str) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        toast.setText(str);
        toast.show();
        return toast;
    }

    /**
     * 消失吐司
     */
    public static void cancelToast(Toast toast) {
        if (toast != null)
            toast.cancel();
    }
}
