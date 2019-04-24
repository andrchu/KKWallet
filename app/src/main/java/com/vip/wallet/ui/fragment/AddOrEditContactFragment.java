package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Contact;
import com.vip.wallet.dao.ContactDao;
import com.vip.wallet.entity.Chain;
import com.vip.wallet.other.SimpTextWatcher;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.dialog.SelectChainDialog;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;

import butterknife.Bind;

import static com.vip.wallet.config.Constants.SCAN_CODE_RESULT;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/25 0025 11:05
 * 描述	      ${TODO}
 */

public class AddOrEditContactFragment extends BaseFragment {
    @Bind(R.id.fac_name)
    EditText mFacName;
    @Bind(R.id.fac_address)
    EditText mFacAddress;
    @Bind(R.id.fac_scanner_qr)
    ImageView mFacScannerQr;
    @Bind(R.id.fac_title_view)
    TitleBarView mFacTitleView;
    @Bind(R.id.fac_su_name)
    TextView mFacSuName;
    Contact mContact = new Contact();
    @Bind(R.id.fac_select_chain)
    LinearLayout mFacSelectChain;
    @Bind(R.id.fac_chain_text)
    TextView mFacChainText;

    public static AddOrEditContactFragment newInstance(Contact contact) {
        AddOrEditContactFragment addOrEditContactFragment = new AddOrEditContactFragment();
        Bundle args = new Bundle();
        if (contact != null)
            args.putSerializable(Constants.CONTACT, contact);
        addOrEditContactFragment.setArguments(args);
        return addOrEditContactFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_add_or_edit_contact;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.CONTACT);
        if (serializable != null) {
            try {
                mContact = (Contact) ((Contact) serializable).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            mContact.mChain = new Chain(mContact.chain_type);
        }
        updateTitleUI();
        updateNameUI();
        updateAddressUI();
        updateChainUI();
    }

    private void updateAddressUI() {
        mFacAddress.setText(mContact.address);
    }

    private void updateNameUI() {
        mFacName.setText(mContact.name);
        if (!StringUtils.isEmpty(mContact.name)) {
            mFacSuName.setText(mContact.name.substring(0, 1));
        }
    }

    private void updateTitleUI() {
        String titleText = mContact.id == null ? getString(R.string.add_contacts) : getString(R.string.edit_contacts);
        mFacTitleView.setTitleText(titleText);
    }

    @Override
    protected void initListener() {
        mFacName.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = mFacName.getText().toString();
                if (!StringUtils.isEmpty(str)) {
                    mFacSuName.setText(str.substring(0, 1));
                }
            }
        });
        mFacScannerQr.setOnClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            startActivityForResult(new Intent(mActivity, ScannerCodeActivity.class), 100);
        });

        mFacTitleView.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            giveData();
            if (!check())
                return;
            ContactDao contactDao = ScApplication.getInstance().getDaoSession().getContactDao();
            if (mContact.getId() == null) {  //保存
                contactDao.insert(mContact);
                toast(getString(R.string.save_success));
            } else {       //修改
                toast(getString(R.string.update_success));
                contactDao.update(mContact);
            }
            pop();
        });
        mFacSelectChain.setOnClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;

            SelectChainDialog selectChainDialog = new SelectChainDialog(mContext);
            selectChainDialog.setCurrentItem(mContact.mChain);
            selectChainDialog.setOnConfirmClickListener(chain -> {
                mContact.mChain = chain;
                mContact.chain_type = chain.index;
                updateChainUI();
            });
            selectChainDialog.show();
        });
    }

    private void updateChainUI() {
        mFacChainText.setText(mContact.mChain.str);
    }

    private boolean check() {
        if (StringUtils.isEmpty(mContact.getName())) {
            toast(getString(R.string.in_name));
            return false;
        }
        if (StringUtils.isEmpty(mContact.getAddress())) {
            toast(getString(R.string.in_address));
            return false;
        }
        if (mContact.chain_type == 0) { //ETH
            if (!StringUtil.isEthAddress(mContact.getAddress())) {
                toast(getString(R.string.address_format_error));
                return false;
            }
        } else if (mContact.chain_type == 1) {        //BTC
            if (!StringUtil.isBtcAddress(mContact.getAddress())) {
                toast(getString(R.string.address_format_error));
                return false;
            }
        } else {            //EOS
            if (!StringUtil.isEosAccount(mContact.getAddress())) {
                toast(getString(R.string.account_format_error));
                return false;
            }
        }
        return true;
    }

    private void giveData() {
        mContact.setAddress(mFacAddress.getText().toString().trim());
        mContact.setName(mFacName.getText().toString().trim());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.SCAN_CODE_OK) {
            mFacAddress.setText(data.getStringExtra(SCAN_CODE_RESULT));
            mFacAddress.setSelection(mFacAddress.getText().toString().length());
        }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.CONTACT, mContact);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.CONTACT);
        if (serializable != null)
            mContact = (Contact) serializable;
        updateTitleUI();
        updateAddressUI();
        updateNameUI();
    }
}
