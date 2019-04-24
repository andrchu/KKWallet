package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/19 11:23
 * 描述	      无责声明
 */

public class NoFaultDecDialog extends BaseDialog {

    private TextView mExit;
    private TextView mTitle;
    private TextView mDesc;

    public NoFaultDecDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_no_fault_dec;
    }

    @Override
    protected void initView() {
        mExit = findViewById(R.id.nfdd_exit);
        mTitle = findViewById(R.id.dnfd_title);
        mDesc = findViewById(R.id.dnfd_desc);
    }

    public NoFaultDecDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }


    public NoFaultDecDialog setDesc(String desc) {
        mDesc.setText(desc);
        return this;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mExit.setOnClickListener(v -> dismiss());
    }
}
