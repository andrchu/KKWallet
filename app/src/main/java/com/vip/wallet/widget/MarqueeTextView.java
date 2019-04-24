package com.vip.wallet.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.vip.wallet.R;
import com.vip.wallet.entity.Inform;
import com.vip.wallet.utils.ColorUtil;
import com.vip.wallet.utils.ListUtil;

import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/3 11:39
 * 描述	     公告
 */
public class MarqueeTextView extends ViewFlipper {

    private Context mContext;
    private ArrayList<Inform> textArrays;
    private MarqueeTextViewClickListener marqueeTextViewClickListener;

    public MarqueeTextView(Context context) {
        super(context);
        mContext = context;
        initBasicView();
    }


    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    public void setTextArraysAndClickListener(ArrayList<Inform> textArrays, MarqueeTextViewClickListener marqueeTextViewClickListener) {
        this.textArrays = textArrays;
        this.marqueeTextViewClickListener = marqueeTextViewClickListener;
        initMarqueeTextView();
    }

    public void initBasicView() {
        setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        startFlipping();
    }

    public void initMarqueeTextView() {
        if (ListUtil.isEmpty(textArrays)) {
            return;
        }
        removeAllViews();
        for (Inform inform : textArrays) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setTextColor(ColorUtil.getColor(R.color.yellow));
            textView.setText(inform.title);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setMarqueeRepeatLimit(-1);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setSelected(true);
            textView.setOnClickListener(v -> {
                if (marqueeTextViewClickListener != null)
                    marqueeTextViewClickListener.onClick(v, inform);
            });
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(textView, lp);
        }
        if (textArrays.size() == 1)
            stopFlipping();

    }

    public void releaseResources() {
        stopFlipping();
        removeAllViews();
    }

    public interface MarqueeTextViewClickListener {
        void onClick(View view, Inform inform);
    }

}