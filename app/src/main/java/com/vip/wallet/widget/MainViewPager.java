package com.vip.wallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vip.wallet.R;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/31 16:38  <br/><br/>
 * 描述:	      自定义ViewPager  可禁止滑动
 */
public class MainViewPager extends ViewPager {
	public MainViewPager(Context context) {
		this(context, null);
	}

	public MainViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainViewPager);
		mIsScroll = typedArray.getBoolean(R.styleable.MainViewPager_isScroll,true);
	}

	private boolean mIsScroll = true;

	public void setEnableScroll(boolean isScroll) {
		mIsScroll = isScroll;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!mIsScroll)
			return false;
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!mIsScroll)
			return false;
		return super.onTouchEvent(ev);
	}

}
