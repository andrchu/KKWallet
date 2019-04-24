package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.ImportCard;
import com.vip.wallet.other.SimpAnimaLitener;
import com.vip.wallet.other.SimpTextWatcher;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.contract.ImportCardContract;
import com.vip.wallet.ui.dialog.UseSweetAlertDialog;
import com.vip.wallet.ui.popwindow.SelectPathPopWindow;
import com.vip.wallet.ui.presenter.ImportCardPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.utils.ToastUtil;
import com.vip.wallet.wallet.AbsWallet;
import com.vip.wallet.wallet.EosWallet;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/17 0017 11:20
 * 描述	      ${TODO}
 */

public class ImportCardFragment extends BaseFragment<ImportCardContract.ImportCardPresenter>
        implements ImportCardContract.ImportCardView, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.fic_title)
    TitleBarView mFicTitle;
    @Bind(R.id.fic_button_seed_word)
    RadioButton mFicButtonSeedWord;
    @Bind(R.id.fic_button_private_key)
    RadioButton mFicButtonPrivateKey;
    @Bind(R.id.fic_button_group)
    RadioGroup mFicButtonGroup;
    @Bind(R.id.fic_edit_seed_key)
    EditText mFicEditSeedKey;
    @Bind(R.id.fic_wallet_name)
    EditText mFicWalletName;
    @Bind(R.id.fic_path_line)
    View mFicPathLine;
    @Bind(R.id.fic_path)
    EditText mFicPath;
    @Bind(R.id.fic_path_more_button)
    ImageView mFicPathMoreButton;
    @Bind(R.id.fic_path_layout)
    LinearLayout mFicPathLayout;
    @Bind(R.id.fic_cb_consent)
    CheckBox mFicCbConsent;
    @Bind(R.id.fic_clause)
    TextView mFicClause;
    @Bind(R.id.fic_button_import)
    TextView mFicButtonImport;

    private ImportCard mImportCard = new ImportCard();
    private SelectPathPopWindow mSelectPathPopWindow;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_import_card;
    }

    public static ImportCardFragment newInstance(int chainType) {
        ImportCardFragment importCardFragment = new ImportCardFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CHAIN_TYPE, chainType);
        importCardFragment.setArguments(args);
        return importCardFragment;
    }

    @Override
    protected ImportCardContract.ImportCardPresenter setPresenter() {
        return new ImportCardPresenter(this);
    }

    @Override
    public void initData(ImportCardContract.ImportCardPresenter presenter) {
        int anInt = getArguments().getInt(Constants.CHAIN_TYPE, -1);
        if (anInt != -1) {
            mImportCard.chainType = anInt;
            if (mImportCard.chainType == 2) {
                mImportCard.importType = 1;
            }
        }
        updateUI();
    }

    @Override
    protected void initListener() {
        mFicEditSeedKey.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean b = mImportCard.importType == 0;
                String key = mFicEditSeedKey.getText().toString();
                if (b)
                    mImportCard.seedKey = key;
                else
                    mImportCard.privateKey = key;
            }
        });

        mFicWalletName.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImportCard.cardName = mFicWalletName.getText().toString();
            }
        });
        mFicCbConsent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mImportCard.isConsent = isChecked;
            updateImportButtonUI();
        });
        mFicTitle.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            startActivityForResult(new Intent(mContext, ScannerCodeActivity.class), 100);
        });

        mFicButtonGroup.setOnCheckedChangeListener(this);
        mFicPathMoreButton.setOnClickListener(this);
        mFicClause.setOnClickListener(this);
        mFicButtonImport.setOnClickListener(this);
    }


    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fic_path_more_button:
                showSelectPathPopWindow();
                break;
            case R.id.fic_clause:
                WebActivity.startWebActivity(mActivity, new BrowserInfo(getString(R.string.server_clause), Constants.CLAUSE_URL));
                break;
            case R.id.fic_button_import:
                //开始导入
                if (check()) {
                    showLoadingDialog(getString(R.string.importing));
                    getPresenter().importCard(mImportCard);
                }
                break;
            default:
                break;
        }

    }

    private boolean check() {

        if (mImportCard.importType == 0) {
            if (StringUtils.isEmpty(mImportCard.seedKey)) {
                toast(getString(R.string.hint_seed_key));
                return false;
            }

            //检查助记词长度
            List<String> seedList = getSeedList();
            if (ListUtil.isEmpty(seedList) || seedList.size() < 12) {
                ToastUtil.toastL(getString(R.string.seed_work_error));
                return false;
            }

        } else {
            if (StringUtils.isEmpty(mImportCard.privateKey)) {
                ToastUtil.toastL(getString(R.string.input_hint_private_key));
                return false;
            }
        }
        //检查路径
        if (mImportCard.chainType == 0 && mImportCard.importType == 0 && StringUtils.isEmpty(mImportCard.path.path)) {
            ToastUtil.toastL(getString(R.string.input_path));
            return false;
        }
        //卡名
        if (StringUtils.isEmpty(mImportCard.cardName)) {
            toast(getString(R.string.input_card_name));
            return false;
        }

        //卡名长度
        if (mImportCard.cardName.length() < 2) {
            toast(getString(R.string.card_name_error_hint));
            return false;
        }

        return true;

    }

    public List<String> getSeedList() {
        String[] split = mImportCard.seedKey.split("[ ]+");
        if (split.length < 1) {
            return null;
        }
        return Arrays.asList(split);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.SCAN_CODE_OK) {
            boolean b = mImportCard.importType == 0;
            String key = data.getStringExtra(Constants.SCAN_CODE_RESULT);
            if (b)
                mImportCard.seedKey = key;
            else
                mImportCard.privateKey = key;
            updateKeyUI();
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.IMPORT_CARD, mImportCard);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.IMPORT_CARD);
        if (serializable != null)
            mImportCard = (ImportCard) serializable;
    }

    private void showSelectPathPopWindow() {
        if (mSelectPathPopWindow == null) {
            mSelectPathPopWindow = new SelectPathPopWindow(mContext);
        }
        mSelectPathPopWindow.setOnItemSelectListener(path -> {
            mImportCard.path = path;
            updatePathUI();
        });
        mSelectPathPopWindow.showAsDropDown(mFicPath);
    }

    private void updateUI() {
        updateTitleUI();
        updateImportTypeButtonUI();
        updateTypeUI();
        updatePathUI();
        updateKeyUI();
        updateCardNameUI();
        updateImportButtonUI();
    }

    private void updateTitleUI() {
        mFicTitle.setTitleText(String.format("%s(%s)", getString(R.string.import_card), StringUtil.getChainName(mImportCard.chainType)));
    }

    private void updateImportButtonUI() {
        mFicButtonImport.setEnabled(mImportCard.isConsent);
    }

    private void updateCardNameUI() {
        mFicWalletName.setText(mImportCard.cardName);
    }

    private void updateKeyUI() {
        mFicEditSeedKey.setText(mImportCard.importType == 0 ? mImportCard.seedKey : mImportCard.privateKey);
    }

    private void updatePathUI() {
        mFicPath.setEnabled(mImportCard.path.isCustom);
        mFicPath.setText(mImportCard.path.path);
    }

    /**
     * 如果选择私钥则不显示path选项
     */
    private void updateTypeUI() {
        //助记词导入
        if (mImportCard.importType == 0) {
            mFicEditSeedKey.setHint(getString(R.string.input_seed_word_hint));
            mFicPathLayout.setVisibility(View.VISIBLE);
            mFicPathLine.setVisibility(View.VISIBLE);
        } else {
            mFicPathLayout.setVisibility(View.GONE);
            mFicPathLine.setVisibility(View.GONE);
            mFicEditSeedKey.setHint(getString(R.string.input_hint_private_key));
        }
        if (mImportCard.chainType != 0) {
            mFicPathLayout.setVisibility(View.GONE);
            mFicPathLine.setVisibility(View.GONE);
        }
    }

    private void updateImportTypeButtonUI() {
        if (mImportCard.chainType == 2) {
            mFicButtonGroup.setVisibility(View.GONE);
        } else {
            boolean b = mImportCard.importType == 0;
            mFicButtonSeedWord.setChecked(b);
            mFicButtonPrivateKey.setChecked(!b);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.fic_button_seed_word:
                mImportCard.importType = 0;
                break;
            case R.id.fic_button_private_key:
                mImportCard.importType = 1;
                break;

            default:
                break;
        }
        updateKeyUI();
        updateTypeUI();
    }

    @Override
    public void importError(Throwable e) {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        loadingDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE, new SimpAnimaLitener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                hideLoadingDialog();
                pop();
            }
        });
        loadingDialog.setTitleText(e.getMessage());
    }

    @Override
    public void importSuccess(AbsWallet wallet) {
        UseSweetAlertDialog loadingDialog = getLoadingDialog();
        loadingDialog.show();
        loadingDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE, new SimpAnimaLitener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                hideLoadingDialog();
                List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.DefAddress.eq(wallet.address)).build().list();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CARD, list.get(0));
                setFragmentResult(101, bundle);
                pop();
            }
        });
        loadingDialog.setTitleText(getString(R.string.import_success));
    }

    @Override
    public void eosSelectAccount(List<String> account_names, ImportCard importCard, EosWallet finalEosWallet) {
        getLoadingDialog().dismiss();
        ArrayList<String> list = new ArrayList<>();
        list.addAll(account_names);
        mImportCard = importCard;
        mEosWallet = finalEosWallet;
        startForResult(SelectAccountFragment.newInstance(list), 100);
    }

    private EosWallet mEosWallet;

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == 101) {
            String accountName = data.getString(Constants.ACCOUNT);
            getPresenter().completeEosImport(accountName, mImportCard, mEosWallet);
        } else {
            getLoadingDialog().dismiss();
        }
    }
}
