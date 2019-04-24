package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.contrarywind.view.WheelView;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.entity.Chain;
import com.vip.wallet.other.OnButtonClickListener;
import com.vip.wallet.ui.adapter.SelectChainAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 10:10
 * 描述	      选择主链
 */

public class SelectChainDialog extends BaseDialog {

    private View mExitView;
    private WheelView mWheelView;
    private TextView mConfirm;
    private List<Chain> mMOptionsItems;
    private TextView mTitle;

    public SelectChainDialog(Context context) {
        super(context, R.style.pop_dialog);
    }

    @Override
    protected void init() {
        super.init();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_select_chain;
    }

    @Override
    protected void initView() {
        mExitView = findViewById(R.id.dsc_exit);
        mWheelView = findViewById(R.id.dsc_wheel_view);
        mConfirm = findViewById(R.id.dsc_confirm);
        mTitle = findViewById(R.id.dsc_title);
    }

    @Override
    protected void initData() {
        mWheelView.setCyclic(false);

        mMOptionsItems = new ArrayList<>();
        mMOptionsItems.add(new Chain(0));
        mMOptionsItems.add(new Chain(1));
        mMOptionsItems.add(new Chain(2));

        mWheelView.setAdapter(new SelectChainAdapter(mMOptionsItems));
    }

    public void setCurrentItem(Chain chain) {
        mWheelView.setCurrentItem(mMOptionsItems.indexOf(chain));
    }

    @Override
    protected void initListener() {
        mExitView.setOnClickListener(v -> dismiss());
        mConfirm.setOnClickListener(v -> {
            if (mOnConfirmClickListener != null) {
                mOnConfirmClickListener.onButtonClick(mMOptionsItems.get(mWheelView.getCurrentItem()));
            }
            dismiss();
        });
    }

    public void setOnConfirmClickListener(OnButtonClickListener<Chain> onConfirmClickListener) {
        mOnConfirmClickListener = onConfirmClickListener;
    }

    public SelectChainDialog setTitleText(String text) {
        mTitle.setText(text);
        return this;
    }

    private OnButtonClickListener<Chain> mOnConfirmClickListener;
}
