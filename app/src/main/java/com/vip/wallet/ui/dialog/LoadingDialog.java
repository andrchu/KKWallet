package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/26 0026 14:57
 * 描述	      ${TODO}
 */

public class LoadingDialog extends BaseDialog {

    private TextView mTitle;

    public LoadingDialog(Context context) {
        super(context);
    }

    private boolean canExit = true;

    public LoadingDialog setCanExit(boolean canExit) {
        this.canExit = canExit;
        return this;
    }

    @Override
    public void onBackPressed() {
        if (canExit) {
            super.onBackPressed();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView() {
        mTitle = findViewById(R.id.loading_title);
    }

    public LoadingDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
