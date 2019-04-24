package com.vip.wallet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.daimajia.androidanimations.library.YoYo;
import com.vip.wallet.R;
import com.vip.wallet.animator.BounceAnimator;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.other.AndroidBug5497Workaround;
import com.vip.wallet.ui.contract.HomeContract;
import com.vip.wallet.ui.fragment.AppCenterFragment;
import com.vip.wallet.ui.fragment.DiscoverFragment;
import com.vip.wallet.ui.fragment.LockFragment;
import com.vip.wallet.ui.fragment.MyCenterFragment;
import com.vip.wallet.ui.presenter.HomePresenter;
import com.vip.wallet.utils.StringUtil;

import butterknife.Bind;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 创建者     金国栋
 * 创建时间   2018/1/30 14:17
 * 描述	     主页
 */
public class HomeActivity extends BaseActivity<HomeContract.IHomePresenter>
        implements HomeContract.IHomeView, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.home_rg)
    RadioGroup mHomeRg;
    @Bind(R.id.home_rb_app)
    RadioButton mHomeRbAppCenter;
    @Bind(R.id.home_rb_explore)
    RadioButton mHomeRbExplore;
    @Bind(R.id.home_rb_my)
    RadioButton mHomeRbMy;
    @Bind(R.id.home_view)
    LinearLayout mHomeView;
    private Toast mToast;
    public static final int EXPLORE = 0;

    public static final int APP_CENTER = 1;
    //个人中心
    public static final int MY_CENTER = 2;

    private SupportFragment[] mFragments = new SupportFragment[3];

    public static final int INIT_TYPE = 1;

    @Override
    protected void init() {
        super.init();
        AndroidBug5497Workaround.assistActivity(this);
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(Constants.START_TYPE, type);
        context.startActivity(intent);
    }

    public static void start(Context context, int type, String url) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(Constants.START_TYPE, type);
        intent.putExtra(Constants.WEB_URL, url);
        context.startActivity(intent);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected HomeContract.IHomePresenter setPresenter() {
        return new HomePresenter(this);
    }


    @Override
    protected void initData(HomeContract.IHomePresenter presenter) {

//        Card firstEthAddress = WalletHelper.getFirstEthAddress();

      /*  WebActivity.startWebActivity(this, new BrowserInfo("test", "https://www.wallet.vip/app/coindog/newsflash" +
                "?address=" + firstEthAddress.getDefAddress() + "&sign=" + EncryptUtil.getSignString(firstEthAddress.getDefAddress())));
*/
        //        WebActivity.startWebActivity(this, new BrowserInfo("test", "http://192.168.31.104:8020/Data/dome/App_h5.html?__hbt=1531281477034"));
        //        WebActivity.startWebActivity(this, new BrowserInfo("test", "https://www.baidu.com"));

        int startType = getIntent().getIntExtra(Constants.START_TYPE, 0);
        if (startType != 0) {
            ScApplication.getInstance().setUnLock(startType != INIT_TYPE);
        }

        if (startType == Constants.BACK_UP_TYPE_GOTO_URL) {
            String stringExtra = getIntent().getStringExtra(Constants.WEB_URL);
            WebActivity.startWebActivity(this, new BrowserInfo("详情", Constants.BASE_URL + stringExtra + "?" + StringUtil.getSignPartByTime()));
        }

        if (findFragment(DiscoverFragment.class) == null) {
            mFragments[EXPLORE] = DiscoverFragment.newInstance();
            mFragments[APP_CENTER] = AppCenterFragment.newInstance();
            mFragments[MY_CENTER] = MyCenterFragment.newInstance();
            loadMultipleRootFragment(R.id.home_container, startType == INIT_TYPE ? MY_CENTER : EXPLORE, mFragments[EXPLORE], mFragments[APP_CENTER], mFragments[MY_CENTER]);
            mHomeRbMy.setChecked(startType == INIT_TYPE);
        } else {
            mFragments[EXPLORE] = findFragment(DiscoverFragment.class);
            mFragments[APP_CENTER] = findFragment(AppCenterFragment.class);
            mFragments[MY_CENTER] = findFragment(MyCenterFragment.class);
        }

        boolean register = ScApplication.getInstance().getConfig().isRegister();
        if (!register)
            getPresenter().registerAddress();

    }

    @Override
    protected void initListener() {
        mHomeRg.setOnCheckedChangeListener(this);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.home_rb_explore:
                showHideFragment(mFragments[EXPLORE]);
                startAnima(mHomeRbExplore);
                prePosition = EXPLORE;
                break;
            case R.id.home_rb_app:
                showHideFragment(mFragments[APP_CENTER]);
                startAnima(mHomeRbAppCenter);
                prePosition = APP_CENTER;
                break;
            case R.id.home_rb_my:
                if (ScApplication.getInstance().isUnLock()) {
                    unLock();
                    if (prePosition == EXPLORE)
                        mHomeRbExplore.setChecked(true);
                    else
                        mHomeRbAppCenter.setChecked(true);
                } else {
                    showHideFragment(mFragments[MY_CENTER]);
                    startAnima(mHomeRbMy);
                }
                break;
            default:
                break;
        }
    }

    private int prePosition = EXPLORE;

    private void unLock() {
        Intent intent = new Intent(this, LockActivity.class);
        intent.putExtra(Constants.START_TYPE, LockFragment.INPUT_PWD);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("onActivityResult >> " + resultCode);
        if (resultCode == LockActivity.RESULT_SUCCESS) {
            ScApplication.getInstance().setUnLock(false);
            mHomeRbMy.setChecked(true);
        }
    }

    private void startAnima(RadioButton homeRbPro) {
        YoYo.with(new BounceAnimator())
                .duration(400)
                .playOn(homeRbPro);
    }

    @Override
    protected void destroy() {
        super.destroy();
        ScApplication.getInstance().setUnLock(true);
    }

    @Override
    protected void setOpenAnim() {
        overridePendingTransition(R.anim.home_in, R.anim.home_out);
    }

    private long preTime;

    @Override
    public void onBackPressedSupport() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            super.onBackPressedSupport();
        } else {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - preTime) < Constants.EXIT_TIME) {
                super.onBackPressedSupport();
            } else {
                if (mToast == null)
                    mToast = toast("再按一次退出");
                mToast.show();
            }
            preTime = currentTime;
        }
    }
}
