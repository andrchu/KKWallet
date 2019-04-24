package com.vip.wallet.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/1 10:39
 * 描述	      ${TODO}
 */
@SuppressLint("AppCompatCustomView")
public class CountNumberView extends TextView {
    //动画时长
    private int duration = 800;
    // 显示数字
    private float number;
    // 显示表达式
    private String regex;
    //不保留小数，整数
    public static final String INTREGEX = "%1$01.0f";
    //保留2位小数
    public static final String FLOATREGEX = "%1$01.2f";
    private ObjectAnimator mObjectAnimator;

    public CountNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 显示带有动画效果的数字 * @param number * @param regex
     */
    public void showNumberWithAnimation(float number, String regex, int duration) {

        if (duration > 0)
            this.duration = duration;

        if (TextUtils.isEmpty(regex))
            this.regex = INTREGEX;
        else
            this.regex = regex;

        stopAnimator();

        if (number <= 0) {
            setText("0");
            return;
        }

        mObjectAnimator = ObjectAnimator.ofFloat(this, "number", 0, number);
        mObjectAnimator.setDuration(this.duration);
        mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mObjectAnimator.start();
    }

    public void showNumberWithAnimation(float number, String regex) {
        showNumberWithAnimation(number, regex, -1);
    }

    /**
     * 停止动画
     */
    public void stopAnimator() {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
    }

    /**
     * 获取当前数字 * @return
     */
    public float getNumber() {
        return number;
    }

    /**
     * 根据正则表达式，显示对应数字样式 * @param number
     */
    public void setNumber(float number) {
        this.number = number;
        setText(String.format(regex, number));
    }
}