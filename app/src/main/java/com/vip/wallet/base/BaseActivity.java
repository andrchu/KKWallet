package com.vip.wallet.base;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.vip.wallet.R;
import com.vip.wallet.config.ActivityManage;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.FragmentCallBack;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.exception.NotFindContainerException;
import com.vip.wallet.exception.NotFindSuccessViewException;
import com.vip.wallet.ui.activity.HomeActivity;
import com.vip.wallet.ui.activity.InitWalletActivity;
import com.vip.wallet.ui.activity.LockActivity;
import com.vip.wallet.ui.activity.SplashActivity;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.utils.LogUtil;
import com.vip.wallet.utils.ToastUtil;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 09:10   <br/><br/>
 * 描述:	      Activity基类
 */
public abstract class BaseActivity<P extends IPresenter> extends SwipeBackActivity implements IBaseView, View.OnClickListener {

    private P presenter;
    private View mBackView;
    private Window mWindow;

    private int currentViewState = Constants.SUCCESS;
    private ViewGroup mContainer;
    protected Bundle savedInstanceState;
    public LogUtil log;
    private View mErrorView;
    private View mEmptyView;
    private View mLoadingView;
    private ImmersionBar mImmersionBar;
    private View mRootView;
    private UseSweetAlertDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.savedInstanceState = savedInstanceState;

        initStateBar();

        initWindow();

        mRootView = View.inflate(this, setLayoutId(), null);

        setContentView(mRootView);

        init();

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        presenter = setPresenter();

        initView();

        initData(presenter);

        initListener();

        initClickListener();

    }

    protected void initWindow() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        onSaveState(outState);
    }

    private void fullScreen() {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void initView() {
        mBackView = findViewById(R.id.back);
        mContainer = findViewById(R.id.container);
    }


    /**
     * 设置容器
     *
     * @param container
     */
    public void setContainerView(ViewGroup container) {
        mContainer = container;
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
     * 等待视图
     */
    protected View setLoadingView() {
        return View.inflate(this, R.layout.loading_view, null);
    }

    /**
     * 空视图
     */
    protected View setEmptyView() {
        return View.inflate(this, R.layout.empty_view, null);
    }

    /**
     * 错误视图
     */
    protected View setErrorView(ApiHttpException error) {
        if (error != null) {
            toast(error.getMessage());
        }
        return View.inflate(this, R.layout.error_view, null);
    }

    /**
     * 切换视图
     */
    public void showView(int viewState) {
        showView(viewState, null);
    }

    /**
     * 切换视图
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

        mContainer.removeAllViews();

        View view = changeView(error);

        if (view == null) {

            try {

                throw new NotFindSuccessViewException("没有找到成功视图，需要覆写setSuccessView方法！");

            } catch (NotFindSuccessViewException e) {

                e.printStackTrace();

            }

            log.e("msg from >> " + this.getClass().getSimpleName() + " : ViewChange -> view is null");

            return;
        }

        view.setAlpha(0);

        mContainer.addView(view);

        ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f)        //过度动画
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

    protected abstract int setLayoutId();

    protected abstract P setPresenter();

    protected abstract void initData(P presenter);

    protected abstract void initListener();

    /**
     * 回收后恢复调用
     *
     * @param savedInstanceState
     */
    protected abstract void restoreInstanceState(Bundle savedInstanceState);

    /**
     * 保存数据状态
     *
     * @param outState
     */
    protected abstract void onSaveState(Bundle outState);

    private void initClickListener() {
        if (mBackView != null)
            mBackView.setOnClickListener(this);
    }

    protected void init() {

        log = LogUtil.getInstance(this.getClass());

        setOpenAnim();

        ActivityManage.getInstance().addActivity(this);
        ButterKnife.bind(this);
        boolean b = this instanceof SplashActivity || this instanceof InitWalletActivity
                || this instanceof HomeActivity || this instanceof LockActivity;
        setSwipeBackEnable(!b);
    }

    /**
     * 设置开始动画
     */
    protected void setOpenAnim() {
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 设置退出动画
     */
    protected void setExitAnim() {
        overridePendingTransition(R.anim.back_in_from_left, R.anim.back_out_to_right);
    }

    /**
     * 沉浸状态栏
     */
    private void initStateBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    @Override
    public void finish() {
        super.finish();
        setExitAnim();
    }

    public Toast toast(String value) {
        Toast toast = ToastUtil.toastS(value);
        toast.show();
        return toast;
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
    }

    protected void destroy() {
        if (presenter != null)
            presenter.detachView();

        ButterKnife.unbind(this);

        hideLoadingDialog();

        hideSoftInput();

        ActivityManage.getInstance().removeActivity(this);

        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    private void hideSoftInput() {
        View view = getWindow().getDecorView();
        SupportHelper.hideSoftInput(view);
    }

    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(clazz, false);
    }

    /**
     * 启动Activity
     *
     * @param isFinish true-销毁自身
     */
    public void startActivity(Class<? extends Activity> clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mBackView) {
            back();
        } else {
            processClick(view);
        }
    }

    /**
     * 返回
     */
    protected void back() {
        finish();
    }

    /**
     * 处理其他点击事件<br/>
     *
     * @since 统一处理事件 -> 加载失败点击重试
     */
    protected void processClick(View view) {
        switch (view.getId()) {
            case R.id.error_retry:
                //重新加载
                if (presenter != null) {
                    presenter.loadData();
                }
                break;
        }
    }

    public void showLoadingDialog(String title, boolean canExit) {
        hideLoadingDialog();
        mLoadingDialog = new UseSweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
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

    public void fragmentCallBack(FragmentCallBack fragmentCallBack) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO:友盟统计上报-活跃度,启动次数,统计页面上报
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO:友盟统计上报-活跃度,启动次数,统计页面上报
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        if (findFragment(toFragment.getClass()) == null) {
            super.loadRootFragment(containerId, toFragment);
        }
    }
}
