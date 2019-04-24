package com.vip.wallet.animator;

import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/22 0022 18:44
 * 描述	      ${TODO}
 */

public class BounceAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
//                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 0.6f, 1.2f, 0.9f, 1),
                ObjectAnimator.ofFloat(target, "scaleY", 0.6f, 1.2f, 0.9f, 1)
        );
    }
}