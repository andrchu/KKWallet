package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.ImageView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/28 0028 16:36
 * 描述	      ${TODO}
 */

public class EosSignsHelpDialog extends BaseDialog {
    private ImageView mDetiExit;

    public EosSignsHelpDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_eos_signs_help;
    }

    @Override
    protected void initView() {
        mDetiExit = findViewById(R.id.deti_exit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mDetiExit.setOnClickListener(v -> dismiss());
    }
}
