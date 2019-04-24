package com.vip.wallet.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.vip.wallet.R;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 10:50   <br/><br/>
 * 描述:	      ${TODO}
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        this(context, R.style.base_dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
        setContentView(setLayoutId());
        initView();
        initData();
        initListener();
    }

    protected abstract int setLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();


    protected void init() {
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
        setCanceledOnTouchOutside(false);
    }
}
