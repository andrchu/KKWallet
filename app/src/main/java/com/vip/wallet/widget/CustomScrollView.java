package com.vip.wallet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/10 0010 14:26
 * 描述	      ${TODO}
 */

public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface OnScrollChangedListener {
        void onChanged(int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        mOnScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onChanged(l, t, oldl, oldt);
        }
    }
}
