package com.vip.wallet.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/8 0008 11:22
 * 描述	      ${TODO}
 */

@SuppressLint("AppCompatCustomView")
public class UnderlineTextView extends TextView {

    public UnderlineTextView(Context context) {
        this(context, null);
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        getPaint().setAntiAlias(true);
        super.onDraw(canvas);
    }
}
