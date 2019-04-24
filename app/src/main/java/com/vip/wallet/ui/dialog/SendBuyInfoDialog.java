package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.BuyInfo;
import com.vip.wallet.utils.CalcUtil;

import java.math.BigDecimal;


/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 10:10
 * 描述	      ${TODO}
 */

public class SendBuyInfoDialog extends BaseDialog {

    ImageView mAbiExit;
    TextView mAbiAddress;
    TextView mDbiAmount;
    TextView mDbiConfirm;
    private BuyInfo mBuyInfo;

    public SendBuyInfoDialog(Context context, BuyInfo buyInfo) {
        super(context, R.style.pop_dialog);
        mBuyInfo = buyInfo;
    }

    @Override
    protected void init() {
        super.init();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_buy_info;
    }

    @Override
    protected void initView() {
        mAbiExit = findViewById(R.id.abi_exit);
        mAbiAddress = findViewById(R.id.abi_address);
        mDbiAmount = findViewById(R.id.dbi_amount);
        mDbiConfirm = findViewById(R.id.dbi_confirm);
    }

    @Override
    protected void initData() {

    }

    public void updateUI() {
        mAbiAddress.setText(mBuyInfo.toAddress);
        String amountCny = "";
        if (!StringUtils.isEmpty(mBuyInfo.ethMarkeValueCNY)) {
            String unit = ScApplication.getInstance().getUnit();
            amountCny = String.format("(%s%s)", unit, CalcUtil.mul(mBuyInfo.ethMarkeValueCNY, mBuyInfo.amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        mDbiAmount.setText(String.format("%sETH %s", mBuyInfo.amount, amountCny));
    }

    @Override
    protected void initListener() {
        mAbiExit.setOnClickListener(v -> dismiss());
    }

    public SendBuyInfoDialog setOnConfirmClickListener(View.OnClickListener clickListener) {
        mDbiConfirm.setOnClickListener(clickListener);
        return this;
    }
}
