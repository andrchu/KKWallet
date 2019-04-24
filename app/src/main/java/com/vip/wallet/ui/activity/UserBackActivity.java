package com.vip.wallet.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.other.SimpTextWatcher;
import com.vip.wallet.ui.contract.UserBackContract;
import com.vip.wallet.ui.presenter.UserBackPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.widget.TitleBarView;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/26 09:45
 * 描述	     用户反馈
 */
public class UserBackActivity extends BaseActivity<UserBackContract.IUserBackPresenter> implements UserBackContract.IUserBackView {
    @Bind(R.id.uba_send)
    TitleBarView mUbaSend;
    @Bind(R.id.uba_content)
    EditText mUbaContent;
    @Bind(R.id.uba_limit_hint)
    TextView mUbaLimitHint;
    @Bind(R.id.uba_contact)
    EditText mUbaContact;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_back;
    }

    @Override
    protected UserBackContract.IUserBackPresenter setPresenter() {
        return new UserBackPresenter(this);
    }


    @Override
    protected void initData(UserBackContract.IUserBackPresenter presenter) {
        updateLimitHintUI();
    }

    @Override
    protected void initListener() {
        mUbaSend.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            if (check()) {
                showLoadingDialog(getString(R.string.sending));
                getPresenter().send(getContact(), getContent());
            }
        });
        mUbaContent.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateLimitHintUI();
            }
        });
    }

    private void updateLimitHintUI() {
        mUbaLimitHint.setText(String.format("%s/%s%s", mUbaContent.length(), 150, getString(R.string.zi)));
    }

    private boolean check() {
        if (StringUtils.isEmpty(getContact())) {
            toast(getString(R.string.empty_contact_hint));
            return false;
        }
        if (StringUtils.isEmpty(getContent())) {
            toast(getString(R.string.empty_content_hint));
            return false;
        }

        return true;
    }

    private String getContent() {
        return mUbaContent.getText().toString().trim();
    }

    private String getContact() {
        return mUbaContact.getText().toString().trim();
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    public void sendError(ApiHttpException exception) {
        hideLoadingDialog();
        toast(exception.getMessage());
    }

    @Override
    public void sendSuccess(String message) {
        hideLoadingDialog();
        toast(getString(R.string.thank_user_back));
        finish();
    }
}
