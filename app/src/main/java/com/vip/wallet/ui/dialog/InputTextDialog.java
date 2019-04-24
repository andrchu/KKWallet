package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.utils.ToastUtil;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/27 0027 14:46
 * 描述	      ${TODO}
 */

public class InputTextDialog extends BaseDialog {

    private EditText mText;
    private TextView mLeft;
    private TextView mRight;
    private TextView mTitleVice;

    public InputTextDialog setText(String text) {
        mText.setText(text);
        if (!StringUtils.isEmpty(text))
            mText.setSelection(text.length());
        return this;
    }

    public InputTextDialog(Context context) {
        super(context, R.style.editText_dialog);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_input_text;
    }

    @Override
    protected void initView() {
        mText = findViewById(R.id.ddf_pwd);
        mLeft = findViewById(R.id.ddf_left);
        mRight = findViewById(R.id.ddf_right);
    }

    public void setTitleViceVisb(boolean isVis) {
        mTitleVice.setVisibility(isVis ? View.VISIBLE : View.GONE);
    }

    public void setViceTitle(String viceTitle) {
        mTitleVice.setText(viceTitle);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mLeft.setOnClickListener(v -> dismiss());
        mRight.setOnClickListener(v -> {
            if (StringUtils.isEmpty(getContent())) {
                ToastUtil.toastS(StringUtil.getString(R.string.input_content));
                return;
            }
            if (getContent().length() < 2) {
                ToastUtil.toastS(StringUtil.getString(R.string.must_2_str));
                return;
            }
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onConfirmCLick(getContent());
            }
        });
    }

    private OnConfirmClickListener mConfirmClickListener;

    public void setConfirmClickListener(OnConfirmClickListener confirmClickListener) {
        mConfirmClickListener = confirmClickListener;
    }

    public interface OnConfirmClickListener {
        void onConfirmCLick(String content);
    }

    private String getContent() {
        return mText.getText().toString().trim();
    }

    @Override
    public void dismiss() {
        KeyboardUtils.hideSoftInput(mText);
        super.dismiss();
    }
}
