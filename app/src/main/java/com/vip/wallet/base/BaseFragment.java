package com.vip.wallet.base;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.vip.wallet.R;
import com.vip.wallet.config.Constants;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.exception.NotFindContainerException;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.ui.fragment.AppCenterFragment;
import com.vip.wallet.ui.fragment.DiscoverFragment;
import com.vip.wallet.ui.fragment.InitWalletFragment;
import com.vip.wallet.ui.fragment.LockFragment;
import com.vip.wallet.ui.fragment.MyCenterFragment;
import com.vip.wallet.ui.fragment.PropertyChainFragment;
import com.vip.wallet.utils.LogUtil;
import com.vip.wallet.utils.ToastUtil;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:  2018/1/22 10:18   <br/><br/>
 * 描述:	      Fragment基类
 */
public abstract class BaseFragment<P extends IPresenter> extends SwipeBackFragment implements IBaseView, View.OnClickListener, View.OnTouchListener {

    protected BaseActivity mActivity;
    protected Context mContext;
    private P presenter;

    private int currentViewState = Constants.SUCCESS;
    private ViewGroup mContainer;
    private LogUtil log;
    private View mErrorView;
    private View mEmptyView;
    private View mLoadingView;
    private View mBackView;
    private UseSweetAlertDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        LogUtils.i(this.getClass().getSimpleName() + " >> onLazyInitView");
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState);
        initData(presenter);
        initClickListener();
        initListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.i(this.getClass().getSimpleName() + " >> onCreateView");
        View rootView = View.inflate(mContext, setLayoutId(), null);
        rootView.setOnTouchListener(this);
        initView(rootView);
        return attachToSwipeBack(rootView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        LogUtils.i(this.getClass().getSimpleName() + " >> onEnterAnimationEnd");
        super.onEnterAnimationEnd(savedInstanceState);
        initLazyData(presenter);
    }

    protected void initLazyData(P presenter) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        onSaveState(outState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.argument = getArguments();
        boolean b = this instanceof InitWalletFragment || this instanceof DiscoverFragment
                || this instanceof AppCenterFragment || this instanceof MyCenterFragment
                || this instanceof LockFragment || this instanceof PropertyChainFragment;
        setSwipeBackEnable(!b);
        init();
    }

    public Bundle argument;

    /**
     * 设置容器
     *
     * @param container
     */
    public void setContainerView(ViewGroup container) {
        mContainer = container;
    }

    protected void initView(View rootView) {
        mContainer = rootView.findViewById(R.id.container);
        mBackView = rootView.findViewById(R.id.back);
        ButterKnife.bind(this, rootView);
    }

    protected void initClickListener() {
        if (mBackView != null)
            mBackView.setOnClickListener(v -> backClick());
    }

    protected void backClick() {
        back();
    }

    public void back() {
        if (mActivity != null) {
            mActivity.onBackPressedSupport();
            //            int backStackEntryCount = mActivity.getSupportFragmentManager().getBackStackEntryCount();
            //            if (backStackEntryCount >= 1) {
            //                pop();
            //            } else {
            //                mActivity.finish();
            //            }
        }
    }


    /**
     * 获取当前视图状态
     *
     * @return viewState {@link BaseActivity#showView(int) 四种视图类型}
     */

    public int getCurrentViewState() {
        return currentViewState;
    }

    private View changeView(ApiHttpException error) {

        View view = null;

        switch (currentViewState) {

            case Constants.SUCCESS:
                view = setSuccessView();
                break;
            case Constants.ERROR:
                if (mErrorView == null)
                    mErrorView = setErrorView(error);
                view = mErrorView;
                break;
            case Constants.EMPTY:
                if (mEmptyView == null)
                    mEmptyView = setEmptyView();
                view = mEmptyView;
                break;
            case Constants.LOADING:
                if (mLoadingView == null)
                    mLoadingView = setLoadingView();
                view = mLoadingView;
                break;
        }

        return view;
    }

    /**
     * 成功视图
     */
    protected View setSuccessView() {
        return null;
    }

    /**
     * 默认等待视图
     */
    protected View setLoadingView() {
        return View.inflate(mContext, R.layout.loading_view, null);
    }

    /**
     * 默认空视图
     */
    protected View setEmptyView() {
        return View.inflate(mContext, R.layout.empty_view, null);
    }

    /**
     * 默认错误视图
     */
    protected View setErrorView(ApiHttpException error) {
        if (error != null) {
            ToastUtil.toastS(error.getMessage());
        }
        return View.inflate(mContext, R.layout.error_view, null);
    }

    /**
     * 改变视图
     *
     * @param viewState {@link Constants#SUCCESS 成功视图} {@link Constants#ERROR 错误视图}
     *                  {@link Constants#EMPTY 空视图} {@link Constants#LOADING 等待视图}
     */
    public void showView(int viewState) {
        showView(viewState, null);
    }

    /**
     * 改变视图
     *
     * @param viewState {@link Constants#SUCCESS 成功视图} {@link Constants#ERROR 错误视图}
     *                  {@link Constants#EMPTY 空视图} {@link Constants#LOADING 等待视图}
     */
    public void showView(int viewState, ApiHttpException error) {

        if (mContainer == null) {

            try {

                throw new NotFindContainerException("没有找到Container，需要在布局里声明Container");

            } catch (NotFindContainerException e) {

                e.printStackTrace();

            }
            return;
        }

        currentViewState = viewState;

        String str = viewState == Constants.SUCCESS ? "成功视图" : viewState == Constants.ERROR ?
                "错误视图" : viewState == Constants.EMPTY ? "空视图" : viewState == Constants.LOADING ? "等待视图" : "";

        log.i("msg from >> " + this.getClass().getSimpleName() + " : ViewChange -> " + str);

        View view = changeView(error);

        if (view == null) {

            log.e("msg from >> " + this.getClass().getSimpleName() + " : ViewChange -> view is null");

            return;
        }

        view.setAlpha(0);

        mContainer.removeAllViews();

        mContainer.addView(view);

        ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f)
                .setDuration(Constants.SHOW_VIEW_OFFSET)
                .start();

        //加载失败，点击重试
        if (currentViewState == Constants.ERROR) {
            TextView retry = view.findViewById(R.id.error_retry);
            if (retry != null) {
                retry.setOnClickListener(this);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    protected void destroy() {

        if (presenter != null)
            presenter.detachView();
        hideLoadingDialog();
        hideSoftInput();
        mActivity = null;
        mContext = null;
        presenter = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected abstract int setLayoutId();

    protected abstract P setPresenter();

    public abstract void initData(P presenter);

    protected abstract void initListener();

    /**
     * 保存数据状态
     *
     * @param outState
     */
    protected abstract void onSaveState(Bundle outState);

    /**
     * 回收后恢复调用
     *
     * @param savedInstanceState
     */
    protected abstract void restoreInstanceState(Bundle savedInstanceState);

    protected void init() {
        initPresenter();
        log = LogUtil.getInstance(this.getClass());
    }

    private void initPresenter() {
        if (presenter == null)
            presenter = setPresenter();
    }

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(mActivity, clazz);
        mActivity.startActivity(intent);
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void onClick(View view) {
        processClick(view);
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    public void showLoadingDialog(String title, boolean canExit) {
        hideLoadingDialog();
        mLoadingDialog = new UseSweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.setTitleText(title);
        mLoadingDialog.setCancelable(canExit);
        mLoadingDialog.show();
    }

    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.loading));
    }

    public void showLoadingDialog(String title) {
        showLoadingDialog(title, false);
    }

    public UseSweetAlertDialog getLoadingDialog() {
        return mLoadingDialog;
    }

    /**
     * 隐藏等待对话框
     */
    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public Toast toast(String str) {
        BaseActivity activity = (BaseActivity) mActivity;
        return activity.toast(str);
    }

    /**
     * 处理其他点击事件
     *
     * @param view
     */
    protected void processClick(View view) {
        switch (view.getId()) {
            case R.id.error_retry:
                //重新加载
                getPresenter().loadData();
                break;
        }
    }
}
