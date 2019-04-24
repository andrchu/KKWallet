package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.tencent.bugly.beta.Beta;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ActivityManage;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.DaoSession;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.ui.activity.InitWalletActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.dialog.InputTextDialog;
import com.vip.wallet.utils.ClickUtil;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/24 0024 18:58
 * 描述	      ${TODO}
 */

public class AboutWeFragment extends BaseFragment {
    @Bind(R.id.awa_version)
    TextView mAwaVersion;
    @Bind(R.id.fm_about_we)
    TextView mFmAboutWe;
    @Bind(R.id.fm_check_update)
    TextView mFmCheckUpdate;

    public static AboutWeFragment newInstance() {
        return new AboutWeFragment();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_about_we;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        mAwaVersion.setText(getString(R.string.app_name) + "\n" + AppUtils.getAppVersionName());
    }

    @Override
    protected void initListener() {
        mFmAboutWe.setOnClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            WebActivity.startWebActivity(mContext, new BrowserInfo(getString(R.string.server_clause), Constants.CLAUSE_URL));
        });
        mFmCheckUpdate.setOnClickListener(v -> Beta.checkUpgrade());
        mAwaVersion.setOnClickListener(v -> {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - preTime < 1000) {
                count++;
                if (count == 10) {
                    count = 1;
                    logout();
                }
            } else {
                count = 1;
            }
            preTime = currentTimeMillis;
        });
    }

    /**
     * 注销
     */
    private void logout() {
        InputTextDialog inputTextDialog = new InputTextDialog(mContext);
        inputTextDialog.setConfirmClickListener(content -> {
            if ("123321".equals(content)) {
                ScApplication.getInstance().getConfig().logOut();
                DaoSession daoSession = ScApplication.getInstance().getDaoSession();
                daoSession.getCardDao().deleteAll();
                daoSession.getAddressDao().deleteAll();
                startActivity(InitWalletActivity.class);
                ActivityManage.getInstance().exit();
                toast(getString(R.string.log_out_success));
            } else {
                toast("密码错误");
            }
            inputTextDialog.dismiss();
        });
        inputTextDialog.show();
    }

    private long preTime;
    private int count = 1;

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }
}
