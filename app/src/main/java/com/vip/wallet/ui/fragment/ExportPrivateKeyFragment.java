package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.utils.SystemUtil;
import com.vip.wallet.utils.ToastUtil;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/21 0021 10:21
 * 描述	     导出私钥
 */

public class ExportPrivateKeyFragment extends BaseFragment {

    @Bind(R.id.fepk_tv_private_key)
    TextView mFepkTvPrivateKey;
    @Bind(R.id.fepk_copy)
    TextView mFepkCopy;
    private String mPrivateKey;

    public static ExportPrivateKeyFragment newInstance(String privateKey) {
        ExportPrivateKeyFragment exportPrivateKeyFragment = new ExportPrivateKeyFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PRIVATE_KEY, privateKey);
        exportPrivateKeyFragment.setArguments(args);
        return exportPrivateKeyFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_export_private_key;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        String string = getArguments().getString(Constants.PRIVATE_KEY);
        if (!StringUtils.isEmpty(string))
            mPrivateKey = string;
        updatePrivateKeyUI();
    }

    private void updatePrivateKeyUI() {
        mFepkTvPrivateKey.setText(mPrivateKey);
    }

    @Override
    protected void initListener() {
        mFepkCopy.setOnClickListener(v -> {
            SystemUtil.clipString(mContext, mPrivateKey);
            mFepkCopy.setText(R.string.have_copy);
            ToastUtil.toastS(getString(R.string.private_key_clip_hint));
            mFepkCopy.setEnabled(false);
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putString(Constants.PRIVATE_KEY, mPrivateKey);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        String string = savedInstanceState.getString(Constants.PRIVATE_KEY);
        if (!StringUtils.isEmpty(string))
            mPrivateKey = string;
        updatePrivateKeyUI();
    }
}
