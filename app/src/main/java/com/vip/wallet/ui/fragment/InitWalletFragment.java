package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.other.SimpAnimaLitener;
import com.vip.wallet.ui.contract.InitWalletContract;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.ui.presenter.InitWalletPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.widget.UnderlineTextView;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 18:44
 * 描述	      ${TODO}
 */

public class InitWalletFragment extends BaseFragment<InitWalletContract.IInitWalletPresenter>
        implements InitWalletContract.IInitWalletView {

    @Bind(R.id.fiw_create_wallet)
    TextView mFiwCreateWallet;
    @Bind(R.id.fiw_import_wallet)
    UnderlineTextView mFiwImportWallet;

    public static InitWalletFragment newInstance() {
        return new InitWalletFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_init_wallet;
    }

    @Override
    protected InitWalletContract.IInitWalletPresenter setPresenter() {
        return new InitWalletPresenter(this);
    }

    @Override
    public void initData(InitWalletContract.IInitWalletPresenter presenter) {

    }

    @Override
    protected void initListener() {
        mFiwCreateWallet.setOnClickListener(this);
        mFiwImportWallet.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fiw_create_wallet:
                showLoadingDialog(StringUtil.getString(R.string.in_create));
                getPresenter().createWallet();
                break;
            case R.id.fiw_import_wallet:
                start(ImportWalletFragment.newInstance());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void createWalletSuccess() {
        ScApplication.getInstance().resetConfig();
        Config config = ScApplication.getInstance().getConfig();
        config.setInit(true);
        LogUtils.i("test >> createWalletSuccess");
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        if (loadingDialog != null) {
            loadingDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE, new SimpAnimaLitener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    hideLoadingDialog();
                    startWithPop(LockFragment.newInstance(LockFragment.SET_PWD));
                }
            });
            loadingDialog.setTitleText(getString(R.string.create_success));
        }
    }


    @Override
    public void createWalletError(Throwable e) {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        if (loadingDialog != null) {
            loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            loadingDialog.setTitleText(getString(R.string.create_error));
        }
    }
}
