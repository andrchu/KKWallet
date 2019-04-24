package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.entity.EosSigns;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/28 0028 15:33
 * 描述	      ${TODO}
 */

public class EosTransferInfoDialog extends BaseDialog {
    private TextView mDetiFrom;
    private TextView mDetiTo;
    private TextView mDetiAmount;
    private TextView mDetiMemo;
    private ImageView mDetiExit;

    public EosTransferInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_eos_transfer_info;
    }

    @Override
    protected void initView() {
        mDetiFrom = findViewById(R.id.deti_from);
        mDetiTo = findViewById(R.id.deti_to);
        mDetiAmount = findViewById(R.id.deti_amount);
        mDetiMemo = findViewById(R.id.deti_memo);
        mDetiExit = findViewById(R.id.deti_exit);
    }

    public void updateUI(EosSigns eosSigns) {
        mDetiFrom.setText(eosSigns.from);
        mDetiTo.setText(eosSigns.to);
        mDetiAmount.setText(eosSigns.amount);
        mDetiMemo.setText(eosSigns.memo);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mDetiExit.setOnClickListener(v -> dismiss());
    }
}
