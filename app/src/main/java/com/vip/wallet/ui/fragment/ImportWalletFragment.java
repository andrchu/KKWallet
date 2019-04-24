package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Config;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.ImportWallet;
import com.vip.wallet.other.SimpAnimaLitener;
import com.vip.wallet.other.SimpTextWatcher;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.contract.ImportWalletContract;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.ui.popwindow.SelectPathPopWindow;
import com.vip.wallet.ui.presenter.ImportWalletPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.ToastUtil;
import com.vip.wallet.widget.TitleBarView;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 18:25
 * 描述	      ${TODO}
 */

public class ImportWalletFragment extends BaseFragment<ImportWalletContract.ImportWalletPresenter>
        implements ImportWalletContract.ImportWalletView {
    @Bind(R.id.iwa_title)
    TitleBarView mIwaTitle;
    @Bind(R.id.iwf_edit_seed_key)
    EditText mIwfEditSeedKey;
    @Bind(R.id.iwf_wallet_name)
    EditText mIwfWalletName;
    @Bind(R.id.iwa_path_line)
    View mIwaPathLine;
    @Bind(R.id.iwf_path)
    EditText mIwfPath;
    @Bind(R.id.iwf_path_more_button)
    ImageView mIwfPathMoreButton;
    @Bind(R.id.iwa_path_layout)
    LinearLayout mIwaPathLayout;
    @Bind(R.id.iwf_cb_consent)
    CheckBox mIwfCbConsent;
    @Bind(R.id.iwf_clause)
    TextView mIwfClause;
    @Bind(R.id.iwf_next)
    TextView mIwfNext;
    ImportWallet mImportWallet = new ImportWallet();
    private SelectPathPopWindow mSelectPathPopWindow;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_import_wallet;
    }

    public static ImportWalletFragment newInstance() {
        return new ImportWalletFragment();
    }

    @Override
    protected ImportWalletContract.ImportWalletPresenter setPresenter() {
        return new ImportWalletPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        updateCheckBox();
        updateNextButtonUI();
        updatePathUI();
    }

    @Override
    public void initData(ImportWalletContract.ImportWalletPresenter presenter) {

    }

    @Override
    protected void initListener() {
        mIwaTitle.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            Intent intent = new Intent(mContext, ScannerCodeActivity.class);
            startActivityForResult(intent, 100);
        });

        mIwfEditSeedKey.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImportWallet.seedKey = mIwfEditSeedKey.getText().toString().trim();
            }
        });

        mIwfWalletName.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImportWallet.walletName = mIwfWalletName.getText().toString().trim();
            }
        });

        mIwfCbConsent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mImportWallet.isConsent = isChecked;
            updateNextButtonUI();
        });

        mIwfPathMoreButton.setOnClickListener(this);
        mIwfClause.setOnClickListener(this);
        mIwfNext.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.iwf_path_more_button:
                showSelectPathPopWindow();
                break;
            case R.id.iwf_clause:
                WebActivity.startWebActivity(mActivity, new BrowserInfo(getString(R.string.server_clause), Constants.CLAUSE_URL));
                break;
            case R.id.iwf_next:
                giveData();
                if (check())
                    importWallet();
                break;
            default:
                break;
        }
    }

    private void giveData() {
        mImportWallet.seedKey = mIwfEditSeedKey.getText().toString().trim();
        mImportWallet.walletName = mIwfWalletName.getText().toString().trim();
    }

    private boolean check() {
        if (StringUtils.isEmpty(mImportWallet.seedKey)) {
            toast(getString(R.string.hint_seed_key));
            return false;
        }
        //检查助记词长度
        List<String> seedList = getSeedList();
        if (ListUtil.isEmpty(seedList) || seedList.size() < 12) {
            ToastUtil.toastL(getString(R.string.seed_work_error));
            return false;
        }
        //检查助记词单词
        try {
            MnemonicCode.INSTANCE.check(seedList);
        } catch (MnemonicException e) {
            e.printStackTrace();
            ToastUtil.toastL(getString(R.string.seed_word_error));
            return false;
        }
        //检查路径
        if (StringUtils.isEmpty(mImportWallet.path.path)) {
            ToastUtil.toastL(getString(R.string.input_path));
            return false;
        }

        if (StringUtils.isEmpty(mImportWallet.walletName)) {
            toast("请输入账户名");
            return false;
        }
        if (mImportWallet.walletName.length() < 2) {
            toast("账户名至少需要2个字符");
            return false;
        }

        return true;
    }

    /**
     * 导入钱包
     */
    private void importWallet() {
        showLoadingDialog(getString(R.string.in_import));
        getPresenter().importWallet(mImportWallet);
    }


    private void showSelectPathPopWindow() {
        if (mSelectPathPopWindow == null) {
            mSelectPathPopWindow = new SelectPathPopWindow(mContext);
        }
        mSelectPathPopWindow.setOnItemSelectListener(path -> {
            mImportWallet.path = path;
            updatePathUI();
        });
        mSelectPathPopWindow.showAsDropDown(mIwfPath);
    }

    private void updatePathUI() {
        mIwfPath.setEnabled(mImportWallet.path.isCustom);
        mIwfPath.setText(mImportWallet.path.path);
    }

    private void updateNextButtonUI() {
        mIwfNext.setEnabled(mImportWallet.isConsent);
    }

    private void updateSeedKeyUI() {
        if (mImportWallet.seedKey == null)
            return;
        mIwfEditSeedKey.setText(mImportWallet.seedKey);
        mIwfEditSeedKey.setSelection(mImportWallet.seedKey.length());
    }

    private void updateCheckBox() {
        mIwfCbConsent.setChecked(mImportWallet.isConsent);
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.IMPORT_WALLET, mImportWallet);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.IMPORT_WALLET);
        if (serializable != null) {
            mImportWallet = (ImportWallet) serializable;
        }
        updateCheckBox();
        updatePathUI();
        updateSeedKeyUI();
        updateNextButtonUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.SCAN_CODE_OK) {
            mImportWallet.seedKey = data.getStringExtra(Constants.SCAN_CODE_RESULT);
            updateSeedKeyUI();
        }
    }

    @Override
    public void importWalletSuccess() {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        if (loadingDialog != null) {
            loadingDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE, new SimpAnimaLitener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    hideLoadingDialog();
                    ScApplication.getInstance().getConfig().setBackUp(true);
                    //                    startWithPop();
                    startWithPopTo(LockFragment.newInstance(LockFragment.SET_PWD), InitWalletFragment.class, true);
                }
            });
            loadingDialog.setTitleText(getString(R.string.import_success));
        }
        Config config = ScApplication.getInstance().getConfig();
        config.setInit(true);
    }

    @Override
    public void importWalletError(Throwable e) {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        if (loadingDialog != null) {
            loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            loadingDialog.setTitleText(getString(R.string.import_error));
        }
    }

    public List<String> getSeedList() {
        String[] split = mImportWallet.seedKey.split("[ ]+");
        if (split.length < 1) {
            return null;
        }
        return Arrays.asList(split);
    }
}
