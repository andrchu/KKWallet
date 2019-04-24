package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/9 15:23
 * 描述	      提示记住助记词对话框
 */

public class HintSeedWordDialog extends BaseDialog {

    private TextView mExit;

    public HintSeedWordDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_hint_seed_word;
    }

    @Override
    protected void initView() {
        mExit = findViewById(R.id.hswd_exit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mExit.setOnClickListener(v -> dismiss());
    }
}
