package com.vip.wallet.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.vip.wallet.R;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/2 0002 10:26
 * 描述	      ${TODO}
 */

public class TitleBarView extends FrameLayout {

    public int titleHeight = 51;
    /**
     * 状态栏高度
     */
    private int mStatusBarHeight;
    private String mTitleText;
    private String mButtonText;
    private boolean mEnableButton;
    private View mTitleView;
    private TextView mTitle;
    private TextView mButton;
    private ColorStateList mButtonColorStateList;
    private int mHeight;
    private int mButtonTextColor = Color.WHITE;
    private int mButtonResourceId;
    private int mTitleColor;
    private boolean mEnableLine;
    private View mLine;
    private LinearLayout mTitleL;
    private TextView mViceTitle;
    private String mViceTitleString;
    private boolean mEnableBack;
    private ImageView mBackView;
    private int mBackGroundColor;
    private View titleBg;
    private int mBackColor;
    private float mButton_margin_right;
    private int mBackIconImage;

    public TitleBarView(@NonNull Context context) {
        this(context, null);
    }

    public int getTitleViewHeight() {
        return mHeight;
    }

    public TitleBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(attrs);
        initView();
        initData();
    }

    private void init() {
        mStatusBarHeight = BarUtils.getStatusBarHeight();
        calcTitleHeight();
    }

    private void calcTitleHeight() {
        mHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                mStatusBarHeight + SizeUtils.dp2px(titleHeight) : SizeUtils.dp2px(titleHeight);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mTitleText = ta.getString(R.styleable.TitleBarView_title);
        mButtonText = ta.getString(R.styleable.TitleBarView_button_text);
        mEnableButton = ta.getBoolean(R.styleable.TitleBarView_enable_button, false);
        mButtonColorStateList = ta.getColorStateList(R.styleable.TitleBarView_button_text_colors);
        mButtonTextColor = ta.getColor(R.styleable.TitleBarView_button_text_color, Color.BLACK);
        mButtonResourceId = ta.getResourceId(R.styleable.TitleBarView_button_image, -1);
        mBackIconImage = ta.getResourceId(R.styleable.TitleBarView_back_icon_image, -1);
        mTitleColor = ta.getColor(R.styleable.TitleBarView_title_text_color, Color.BLACK);
        mEnableLine = ta.getBoolean(R.styleable.TitleBarView_enable_line, true);
        mViceTitleString = ta.getString(R.styleable.TitleBarView_vice_title);
        mEnableBack = ta.getBoolean(R.styleable.TitleBarView_enable_back, true);
        mBackGroundColor = ta.getColor(R.styleable.TitleBarView_back_ground_color, -1);
        mBackGroundColor = ta.getColor(R.styleable.TitleBarView_back_ground_color, -1);
        mBackColor = ta.getColor(R.styleable.TitleBarView_back_ground_color, -1);
        mButton_margin_right = ta.getDimension(R.styleable.TitleBarView_button_margin_right, -1);
    }

    private void initView() {
        mTitleView = View.inflate(getContext(), R.layout.view_title_bar, null);
        mTitle = mTitleView.findViewById(R.id.vtb_title);
        mButton = mTitleView.findViewById(R.id.vtb_right_button);
        mLine = mTitleView.findViewById(R.id.tbv_line);
        mTitleL = mTitleView.findViewById(R.id.tbv_title_l);
        mViceTitle = mTitleView.findViewById(R.id.tbv_title_vice);
        mBackView = mTitleView.findViewById(R.id.back);
        titleBg = mTitleView.findViewById(R.id.tbv_title_bg);
        mTitleView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight));
        addView(mTitleView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, mHeight);
    }

    private void initData() {
        setEnableButton(mEnableButton);
        setTitleText(mTitleText);
        setButtonText(mButtonText);
        setButtonTextColor(mButtonTextColor);
        setButtonTextColors(mButtonColorStateList);
        setButtonImage(mButtonResourceId);
        setTitleColor(mTitleColor);
        setEnableLine(mEnableLine);
        setViceTitle(mViceTitleString);
        setEnableBack(mEnableBack);
        setBackGround(mBackGroundColor);
        setBackColor(mBackColor);
        setMarginRight(mButton_margin_right);
        setBackImage(mBackIconImage);
    }

    public void setBackImage(int backIconImage) {
        if (backIconImage != -1) {
            mBackView.setImageResource(backIconImage);
        }
    }

    public void setBackImageColor(int color) {
        mBackView.setColorFilter(color);
    }

    private void setMarginRight(float button_margin_right) {
        if (button_margin_right == -1)
            return;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, (int) button_margin_right, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mButton.setLayoutParams(layoutParams);
    }

    private void setBackColor(int backColor) {
        if (backColor != -1) {
            mBackView.setBackgroundColor(backColor);
        }
    }

    public void setBackGround(int backGroundColor) {
        if (backGroundColor == -1)
            return;
        mTitleL.setBackgroundColor(backGroundColor);
        titleBg.setBackgroundColor(backGroundColor);
    }

    private void setEnableBack(boolean enableBack) {
        mBackView.setVisibility(enableBack ? View.VISIBLE : View.INVISIBLE);
        mBackView.setEnabled(enableBack);
    }

    public void setViceTitle(String viceTitleString) {
        mViceTitle.setText(viceTitleString);
    }

    public void setOnButtonClickListener(OnClickListener clickListener) {
        mButton.setOnClickListener(clickListener);
    }

    public void setTitleColor(int titleColor) {
        mTitle.setTextColor(titleColor);
    }

    public void setButtonImage(int buttonResourceId) {
        if (buttonResourceId == -1)
            return;
        Drawable drawable = ContextCompat.getDrawable(getContext(), buttonResourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mButton.setCompoundDrawables(null, null, drawable, null);
        mButton.setPadding(SizeUtils.dp2px(12), 0, SizeUtils.dp2px(10), 0);
        //        mButton.setBackgroundResource(buttonResourceId);
    }

    public void setButtonTextColor(int color) {
        mButton.setTextColor(color);
    }

    public void setButtonTextColors(ColorStateList buttonColorStateList) {
        if (buttonColorStateList != null) {
            mButton.setTextColor(buttonColorStateList);
        }
    }

    public void setEnableButton(boolean enable) {
        mButton.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public void setButtonText(String buttonText) {
        mButton.setText(buttonText);
    }


    public void setTitleText(String titleText) {
        mTitle.setText(titleText);
    }

    public void setEnableLine(boolean enableLine) {
        if (!enableLine) {
            titleHeight = 48;
            mLine.setVisibility(View.GONE);
            calcTitleHeight();
        } else {
            titleHeight = 51;
            mLine.setVisibility(View.VISIBLE);
            calcTitleHeight();
        }
        invalidate();
    }
}
