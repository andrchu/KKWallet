package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.entity.OutToken;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 10:10
 * 描述	      ${TODO}
 */

public class SendTokenDetailDialog extends BaseDialog {

    private View mConfirm;
    private TextView mDstdCount;
    private TextView mDstdFee;
    private TextView mDstdToAddress;
    private ImageView mExit;
    private OutToken mOutToken;
    private TextView mDstd2Item;

    public SendTokenDetailDialog(Context context, OutToken outToken) {
        super(context, R.style.pop_dialog);
        mOutToken = outToken;
    }

    @Override
    protected void init() {
        super.init();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_send_token_detail;
    }

    @Override
    protected void initView() {
        mConfirm = findViewById(R.id.dstd_confirm);
        mDstdCount = findViewById(R.id.dstd_count);
        mDstdFee = findViewById(R.id.dstd_fee);
        mDstdToAddress = findViewById(R.id.dstd_to_address);
        mExit = findViewById(R.id.dstd_exit);
        mDstd2Item = findViewById(R.id.dstd_2_item);
    }

    @Override
    protected void initData() {
        //        updateUI();
    }

    public void updateUI() {
        boolean b = mOutToken.chain_type == 0;
        mDstdCount.setText(String.format("%s %s", mOutToken.outCount.toString(), mOutToken.token.getTokenName()));
        if (mOutToken.chain_type == 2) {
            mDstdFee.setText(mOutToken.memo);
            mDstd2Item.setText("备       注");
        } else {
            mDstd2Item.setText("矿  工  费");
            mDstdFee.setText(String.format("≈%s %s", mOutToken.fee, b ? "ETH" : "BTC"));
        }
        mDstdToAddress.setText(mOutToken.toAddress);
    }

    @Override
    protected void initListener() {
        mExit.setOnClickListener(v -> dismiss());
    }

    public SendTokenDetailDialog setOnConfirmClickListener(View.OnClickListener clickListener) {
        mConfirm.setOnClickListener(clickListener);
        return this;
    }
}
