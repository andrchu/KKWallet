package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/12 14:46
 * 描述	      ${TODO}
 */

public class DefHintDialog extends BaseDialog {

    private TextView mLeft;
    private TextView mRight;
    private TextView mTitleVice;
    private TextView mTitle;

    public DefHintDialog(Context context) {
        super(context);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_def_hint;
    }

    @Override
    protected void initView() {
        mTitle = findViewById(R.id.ddh_title);
        mLeft = findViewById(R.id.ddh_left);
        mRight = findViewById(R.id.ddh_right);
        mTitleVice = findViewById(R.id.ddh_title_vice);
    }


    public DefHintDialog setViceTitle(String viceTitle) {
        mTitleVice.setText(viceTitle);
        return this;
    }

    public DefHintDialog setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public DefHintDialog setLeftButtonTitle(String title) {
        mLeft.setText(title);
        return this;
    }

    public DefHintDialog setRightButtonTitle(String title) {
        mRight.setText(title);
        return this;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mLeft.setOnClickListener(v -> {
            if (mOnCancelClickListener != null) {
                mOnCancelClickListener.onCancelClick();
            }
            dismiss();
        });
        mRight.setOnClickListener(v -> {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onConfirmCLick();
            }
        });
    }

    private OnConfirmClickListener mConfirmClickListener;
    private OnCancelClickListener mOnCancelClickListener;

    public DefHintDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
        return this;
    }

    public DefHintDialog setConfirmClickListener(OnConfirmClickListener confirmClickListener) {
        mConfirmClickListener = confirmClickListener;
        return this;
    }

    public interface OnConfirmClickListener {
        void onConfirmCLick();
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}
