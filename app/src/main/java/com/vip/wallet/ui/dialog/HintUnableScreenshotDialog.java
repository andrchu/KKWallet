package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/9 0009 14:03
 * 描述	      提示不要截图对话框
 */

public class HintUnableScreenshotDialog extends BaseDialog {

    private TextView mExit;

    public HintUnableScreenshotDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_hint_unable_screen_shot;
    }

    @Override
    protected void initView() {
        mExit = findViewById(R.id.husd_exit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mExit.setOnClickListener(v -> dismiss());
    }
}
