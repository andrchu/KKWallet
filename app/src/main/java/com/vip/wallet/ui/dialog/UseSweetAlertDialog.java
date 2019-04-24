package com.vip.wallet.ui.dialog;

import android.content.Context;

import com.vip.wallet.R;
import com.vip.wallet.utils.ColorUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 15:38
 * 描述	      ${TODO}
 */

public class UseSweetAlertDialog extends SweetAlertDialog {
    public UseSweetAlertDialog(Context context, int alertType) {
        super(context, alertType);
        getProgressHelper().setBarColor(ColorUtil.getColor(R.color.colorPrimary));
        setCancelable(false);
    }
}
