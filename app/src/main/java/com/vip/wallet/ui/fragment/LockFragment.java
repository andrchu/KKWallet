package com.vip.wallet.ui.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.blankj.utilcode.util.LogUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.entity.PwdDot;
import com.vip.wallet.entity.PwdStep;
import com.vip.wallet.other.SimpAnimatorListener;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.adapter.PwdDotAdapter;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscription;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/22 0022 11:04
 * 描述	      ${TODO}
 */

public class LockFragment extends BaseFragment implements PatternLockViewListener {
    @Bind(R.id.pattern_lock_view)
    PatternLockView mPatternLockView;
    /**
     * 输入密码
     */
    public static final int INPUT_PWD = 1;
    /**
     * 修改密码
     */
    public static final int UPDATE_PWD = 2;
    /**
     * 设置密码
     */
    public static final int SET_PWD = 3;

    public static final int SUCCESS = 666;

    @Bind(R.id.fl_title_view)
    TitleBarView mFlTitleView;
    @Bind(R.id.fl_recyclerView)
    RecyclerView mFlRecyclerView;
    @Bind(R.id.fl_desc)
    TextView mFlDesc;
    @Bind(R.id.fl_error_desc)
    TextView mFlErrorDesc;
    PwdStep mPwdStep = new PwdStep();
    private Subscription mDelaySubscribe;
    private YoYo.YoYoString mAniamtion;
    private ArrayList<PwdDot> mPwdDots = new ArrayList<>();
    private PwdDotAdapter mDotAdapter;

    public static LockFragment newInstance(int option_type) {
        LockFragment lockFragment = new LockFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PWD_OPTION_TYPE, option_type);
        lockFragment.setArguments(args);
        return lockFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_lock;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        int anInt = getArguments().getInt(Constants.PWD_OPTION_TYPE, -1);
        if (anInt != -1)
            mPwdStep.setOption_type(anInt);
        updateTitleUI();
        updateDescUI();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mFlRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        for (int i = 0; i < 9; i++)
            mPwdDots.add(new PwdDot(i));
        mDotAdapter = new PwdDotAdapter(R.layout.item_dot, mPwdDots);
        mFlRecyclerView.setAdapter(mDotAdapter);
    }

    private void updateDescUI() {
        mFlDesc.setText(mPwdStep.getDesc());
    }

    private void updateTitleUI() {
        mFlTitleView.setTitleText(mPwdStep.getTitle());
    }

    @Override
    protected void initListener() {
        mPatternLockView.addPatternLockListener(this);
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.PWD, mPwdStep);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.PWD);
        if (serializable != null) {
            mPwdStep = (PwdStep) serializable;
        }
        updateTitleUI();
        updateDescUI();
    }

    @Override
    public void onStarted() {
        LogUtils.i("lockView >> onStarted");
    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {
        StringBuilder stringBuilder = new StringBuilder();
        cleanDot();
        for (PatternLockView.Dot dot : progressPattern) {
            PwdDot pwdDot = mPwdDots.get(dot.getId());
            pwdDot.isSelect = true;
            stringBuilder.append(dot.getId());
        }
        mDotAdapter.notifyDataSetChanged();
        LogUtils.i("lockView >> onProgress:::" + stringBuilder.toString());
    }

    private void cleanDot() {
        for (PwdDot pwdDot : mPwdDots)
            pwdDot.isSelect = false;
    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PatternLockView.Dot dot : pattern) {
            stringBuilder.append(dot.getId());
        }
        String pwd = stringBuilder.toString();
        if (mPwdStep.option_type == INPUT_PWD) {    //输入密码
            processInputPwd(pwd);
        } else if (mPwdStep.option_type == UPDATE_PWD || mPwdStep.option_type == SET_PWD) {    //修改密码或设置密码
            processUpdatePwd(pwd);
        }
        //
        LogUtils.i(pwd);
        //                EncryptUtils.encryptAES()
        //                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
    }

    /**
     * 修改密码
     *
     * @param pwd
     */
    private void processUpdatePwd(String pwd) {

        switch (mPwdStep.step) {
            case 0:
                String oldPwd = ScApplication.getInstance().getConfig().getPwd();
                if (oldPwd.equals(pwd)) {
                    //通过验证,进入下一步骤
                    successNextStep();
                } else {    //错误
                    setLockViewError();
                    updateErrorDescUI();
                }
                break;
            case 1:
                if (pwd.length() < Constants.PWD_MIN_LENGTH) { //密码不能小于4位
                    setLockViewError();
                    updateErrorDescUI();
                } else {
                    mPwdStep.prePwd = pwd;
                    successNextStep();
                }
                break;
            case 2:
                if (mPwdStep.prePwd.equals(pwd)) {  //设置密码成功
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    mPwdStep.confirmPwd = pwd;
                    Config config = ScApplication.getInstance().getConfig();
                    config.setPwd(pwd);
                    callBack(new FragmentCallBack(mPwdStep.option_type, getString(R.string.pwd_set_success)));
                } else {
                    updateErrorDescUI();
                    setLockViewError();
                }
                break;
            default:
                break;
        }
    }

    private void callBack(FragmentCallBack lockCallBack) {
        if (mActivity != null)
            mActivity.fragmentCallBack(lockCallBack);
    }

    /**
     * 成功, 进入下一步骤
     */
    private void successNextStep() {
        mPwdStep.nextStep();
        mFlErrorDesc.setVisibility(View.INVISIBLE);
        //播放动画
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setEnabled(false);
        stopAnimation();
        mAniamtion = YoYo.with(Techniques.SlideInRight)
                .duration(300)
                .withListener(new SimpAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        updateDescUI();
                        mPatternLockView.setEnabled(true);
                        mPatternLockView.clearPattern();
                        cleanDot();
                        mDotAdapter.notifyDataSetChanged();
                    }
                })
                .playOn(mFlDesc);
    }


    /**
     * 输入密码
     *
     * @param pwd
     */
    private void processInputPwd(String pwd) {
        if (pwd.equals(ScApplication.getInstance().getConfig().getPwd())) {
            //验证成功
            mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
            mPatternLockView.clearPattern();
            callBack(new FragmentCallBack(mPwdStep.option_type, "验证成功"));
        } else {
            setLockViewError();
            updateErrorDescUI();
        }
    }

    /**
     * 错误
     */
    private void setLockViewError() {
        mPatternLockView.setEnabled(false);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
        unSubscribeDelay();
        mDelaySubscribe = Observable.timer(Constants.PWD_DELAY, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mPatternLockView.setEnabled(true);
                        mPatternLockView.clearPattern();
                        cleanDot();
                        mDotAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void unSubscribeDelay() {
        if (mDelaySubscribe != null) {
            mDelaySubscribe.unsubscribe();
        }
    }

    private void stopAnimation() {
        if (mAniamtion != null) {
            mAniamtion.stop();
        }
    }

    private void updateErrorDescUI() {
        mFlErrorDesc.setVisibility(View.VISIBLE);
        mFlErrorDesc.setText(mPwdStep.getErrorString());
        YoYo.with(Techniques.Shake)
                .duration(300)
                .playOn(mFlErrorDesc);
    }

    @Override
    public void onCleared() {
        LogUtils.i("lockView >> onCleared");
    }

    @Override
    protected void destroy() {
        super.destroy();
        unSubscribeDelay();
        stopAnimation();
    }
}
