package com.vip.wallet.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.EosAccountInfo;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.entity.OutToken;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.adapter.EosSignAuthListAdapter;
import com.vip.wallet.ui.contract.EosSignsContract;
import com.vip.wallet.ui.dialog.EosSignsHelpDialog;
import com.vip.wallet.ui.dialog.EosTransferInfoDialog;
import com.vip.wallet.ui.dialog.SendTokenDetailDialog;
import com.vip.wallet.ui.presenter.EosSignsPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;
import java.math.BigDecimal;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/24 0024 10:43
 * 描述	      ${TODO}
 */

public class EosSignsFragment extends BaseFragment<EosSignsContract.IEosSignsPresenter> implements EosSignsContract.IEosSignsView {

    @Bind(R.id.fes_qr_image)
    ImageView mFesQrImage;
    @Bind(R.id.fes_show_transfer_info)
    TextView mFesShowTransferInfo;
    @Bind(R.id.fes_show_help_info)
    TextView mFesShowHelpInfo;
    @Bind(R.id.fes_fresh)
    ImageView mFesFresh;
    @Bind(R.id.fes_threshold)
    TextView mFesThreshold;
    @Bind(R.id.fes_commit)
    TextView mFesCommit;
    @Bind(R.id.fes_success_view)
    ScrollView mFesSuccessView;
    @Bind(R.id.fes_auth_list)
    RecyclerView mFesAuthList;
    @Bind(R.id.fes_title_view)
    TitleBarView mFesTitleView;
    @Bind(R.id.fes_show_help_info_2)
    TextView mFesShowHelpInfo2;

    EosSigns mEosSigns = new EosSigns();
    private EosSignAuthListAdapter mAdapter;
    private ObjectAnimator mRotation;

    @Override
    protected View setSuccessView() {
        return mFesSuccessView;
    }

    public static EosSignsFragment newInstance(OutToken outToken) {
        EosSignsFragment eosSignsFragment = new EosSignsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.OUT_TOKEN, outToken);
        eosSignsFragment.setArguments(args);
        return eosSignsFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_eos_sign;
    }

    @Override
    protected EosSignsContract.IEosSignsPresenter setPresenter() {
        return new EosSignsPresenter(this);
    }

    @Override
    public void initData(EosSignsContract.IEosSignsPresenter presenter) {

        mFesAuthList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new EosSignAuthListAdapter(R.layout.item_esf_auth, null);
        mFesAuthList.setAdapter(mAdapter);
        updateCommitButtonUI();
        if (StringUtils.isEmpty(mEosSigns.head_block_time)) {
            giveData();
            getPresenter().processEosInfo(mEosSigns);
        } else {
            updateUI();
        }
    }

    private void giveData() {
        Serializable serializable = getArguments().getSerializable(Constants.OUT_TOKEN);
        if (serializable != null) {
            OutToken outToken = (OutToken) serializable;
            mEosSigns.from = outToken.fromAddress;
            mEosSigns.to = outToken.toAddress;
            mEosSigns.amount = outToken.outCount.setScale(4, BigDecimal.ROUND_HALF_UP).toString() + " EOS";
            mEosSigns.memo = outToken.memo;
            mEosSigns.outToken = outToken;
            EosAccountInfo.PermissionsEntity active = ListUtil.getObj(outToken.mEosAccountInfo.permissions, new EosAccountInfo.PermissionsEntity("active"));

            for (EosAccountInfo.PermissionsEntity.RequiredAuthEntity.AccountsEntity account : active.required_auth.accounts) {
                EosSigns.Permissions e = new EosSigns.Permissions(account.permission.actor, account.weight);
                mEosSigns.permissions.add(e);
            }

            for (EosAccountInfo.PermissionsEntity.RequiredAuthEntity.KeysEntity key : active.required_auth.keys) {
                mEosSigns.permissions.add(new EosSigns.Permissions(key.key, key.weight));
            }
            mEosSigns.threshold = active.required_auth.threshold;
        }
    }

    @Override
    protected void initListener() {
        mFesShowTransferInfo.setOnClickListener(this);
        mFesQrImage.setOnClickListener(this);
        mFesShowHelpInfo.setOnClickListener(this);
        mFesFresh.setOnClickListener(this);
        mAdapter.setButtonClickListener(permissions -> {
            //授权
            showLoadingDialog("授权中...");
            getPresenter().auth(mEosSigns, permissions);
        });
        mFesTitleView.setOnButtonClickListener(v -> shareQrImage());
        mFesCommit.setOnClickListener(this);
        mFesShowHelpInfo2.setOnClickListener(this);
    }

    /**
     * 分享二维码
     */
    private void shareQrImage() {
        if (StringUtils.isEmpty(mEosSigns.orderId))
            return;
        showLoadingDialog("生成图片...");
        mFesQrImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mFesQrImage.getDrawingCache());
        mFesQrImage.setDrawingCacheEnabled(false);
        getPresenter().generateQrFile(bitmap);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fes_show_transfer_info:
            case R.id.fes_qr_image:
                showTransferInfoDialog();
                break;
            case R.id.fes_show_help_info:
            case R.id.fes_show_help_info_2:
                showHelpDialog();
                break;
            case R.id.fes_fresh:
                freshAuthList();
                break;
            case R.id.fes_commit:
                commitTransfer();
                break;
        }
    }

    private void commitTransfer() {
        SendTokenDetailDialog sendTokenDetailDialog = new SendTokenDetailDialog(mContext, mEosSigns.outToken);
        sendTokenDetailDialog.updateUI();
        sendTokenDetailDialog.setOnConfirmClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            showLoadingDialog("提交中...");
            sendTokenDetailDialog.dismiss();
            getPresenter().eosTransfer(mEosSigns);
        });
        sendTokenDetailDialog.show();
    }

    /**
     * 刷新授权列表
     */
    private void freshAuthList() {
        //启动动画
        if (mRotation != null) {
            freshAnimaEnd();
        } else {
            freshAnimaStart();
            getPresenter().freshAuthList(mEosSigns);
        }
    }

    private void freshAnimaEnd() {
        if (mRotation != null) {
            mRotation.end();
        }
        if (mFesFresh != null) {
            mFesFresh.setEnabled(true);
        }
        mRotation = null;
    }

    private void freshAnimaStart() {
        mFesFresh.setEnabled(false);
        mRotation = ObjectAnimator.ofFloat(mFesFresh, "rotation", 0f, 359f);
        mRotation.setRepeatCount(ObjectAnimator.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());
        mRotation.setDuration(500);
        mRotation.start();
    }

    private void showHelpDialog() {
        new EosSignsHelpDialog(mContext).show();
    }

    private void showTransferInfoDialog() {
        EosTransferInfoDialog eosTransferInfoDialog = new EosTransferInfoDialog(mContext);
        eosTransferInfoDialog.updateUI(mEosSigns);
        eosTransferInfoDialog.show();
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.EOS_SIGNS, mEosSigns);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.EOS_SIGNS);
        if (serializable != null)
            mEosSigns = (EosSigns) serializable;
    }

    @Override
    protected void destroy() {
        freshAnimaEnd();
        super.destroy();
    }

    @Override
    public void processSuccess() {
        //刷新UI
        updateUI();
    }

    private void updateUI() {
        //生成二维码
        getPresenter().generateQrCode(mEosSigns);
        //刷新授权列表UI
        updateAuthListUI();
        updateThresholdUI();
        updateCommitButtonUI();
    }

    private void updateCommitButtonUI() {
        int tempThreshold = 0;
        for (EosSigns.Permissions permission : mEosSigns.permissions) {
            if (!StringUtils.isEmpty(permission.signs)) {
                tempThreshold += permission.weight;
            }
        }
        boolean b = mEosSigns.threshold == 0 || mEosSigns.threshold > tempThreshold;
        mFesCommit.setEnabled(!b);
    }

    private void updateThresholdUI() {
        mFesThreshold.setText(String.format("阈值：%s", mEosSigns.threshold));
    }

    @Override
    public void generateQrCodeSuccess(Bitmap bitmap) {
        mFesQrImage.setImageBitmap(bitmap);
    }

    @Override
    public void freshAuthError(ApiHttpException exception) {
        freshAnimaEnd();
        toast(exception.getMessage());
    }

    @Override
    public void freshAuthSuccess() {
        freshAnimaEnd();
        toast("刷新成功");
        mAdapter.notifyDataSetChanged();
        updateCommitButtonUI();
    }

    @Override
    public void authError(Throwable e) {
        hideLoadingDialog();
        toast(e.getMessage());
    }

    @Override
    public void authSuccess() {
        hideLoadingDialog();
        toast("授权成功");
        mAdapter.notifyDataSetChanged();
        updateCommitButtonUI();
    }

    @Override
    public void generateQrFileSuccess(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享授权二维码"));
        hideLoadingDialog();
    }

    @Override
    public void generateQrFileError(Throwable e) {
        hideLoadingDialog();
        toast(e.getMessage());
    }

    @Override
    public void sendAmountError(Throwable e) {
        hideLoadingDialog();
        toast(e.getMessage());
    }

    @Override
    public void sendAmountSuccess(String message) {
        hideLoadingDialog();
        toast(message);
        popTo(SendTokenFragment.class, true);
    }

    private void updateAuthListUI() {
        mAdapter.setNewData(mEosSigns.permissions);
    }

}
