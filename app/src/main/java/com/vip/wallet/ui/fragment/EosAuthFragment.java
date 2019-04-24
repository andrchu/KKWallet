package com.vip.wallet.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.EosSigns;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.adapter.EosSignAuthListAdapter;
import com.vip.wallet.ui.contract.EosAuthContract;
import com.vip.wallet.ui.dialog.DefHintDialog;
import com.vip.wallet.ui.presenter.EosAuthPresenter;
import com.vip.wallet.utils.ClickUtil;

import java.io.Serializable;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/30 0030 13:57
 * 描述	      ${TODO}
 */

public class EosAuthFragment extends BaseFragment<EosAuthContract.IEosAuthPresenter> implements EosAuthContract.IEosAuthView {

    @Bind(R.id.fea_from)
    TextView mFeaFrom;
    @Bind(R.id.fea_to)
    TextView mFeaTo;
    @Bind(R.id.fea_amount)
    TextView mFeaAmount;
    @Bind(R.id.fea_memo)
    TextView mFeaMemo;
    @Bind(R.id.fea_fresh)
    ImageView mFeaFresh;
    @Bind(R.id.fea_threshold)
    TextView mFeaThreshold;
    @Bind(R.id.fea_auth_list)
    RecyclerView mFeaAuthList;
    @Bind(R.id.fea_success_view)
    LinearLayout mFeaSuccessView;

    private EosSigns mEosSigns;
    private EosAuthf mEosAuthf = new EosAuthf();
    private EosSignAuthListAdapter mAdapter;
    private ObjectAnimator mRotation;
    private DefHintDialog mDefHintDialog;

    public static EosAuthFragment newInstance(EosAuthf eosAuthf) {
        EosAuthFragment eosAuthFragment = new EosAuthFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EOS_AUTH_INFO, eosAuthf);
        eosAuthFragment.setArguments(args);
        return eosAuthFragment;
    }

    @Override
    protected View setSuccessView() {
        return mFeaSuccessView;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_eos_auth;
    }

    @Override
    protected EosAuthContract.IEosAuthPresenter setPresenter() {
        return new EosAuthPresenter(this);
    }

    @Override
    public void initData(EosAuthContract.IEosAuthPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.EOS_AUTH_INFO);
        if (serializable != null)
            mEosAuthf = (EosAuthf) serializable;

        mFeaAuthList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new EosSignAuthListAdapter(R.layout.item_esf_auth, null);
        mFeaAuthList.setAdapter(mAdapter);

        getPresenter().loadAuthList(mEosAuthf);
    }

    @Override
    protected void initListener() {
        mAdapter.setButtonClickListener(permissions -> {
            if (!ClickUtil.hasExecute())
                return;
            showAuthDialog(permissions);
        });
        mFeaFresh.setOnClickListener(v -> freshAuthList());
    }

    private void showAuthDialog(EosSigns.Permissions permissions) {
        mDefHintDialog = new DefHintDialog(mContext).setTitle("授权")
                .setViceTitle("是否对此交易进行授权?")
                .setLeftButtonTitle("取消")
                .setRightButtonTitle("授权")
                .setConfirmClickListener(() -> {
                    showLoadingDialog("授权中...");
                    getPresenter().auth(mEosSigns, permissions);
                    mDefHintDialog.dismiss();
                });
        mDefHintDialog.show();
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
        if (mFeaFresh != null)
            mFeaFresh.setEnabled(true);
        mRotation = null;
    }

    private void freshAnimaStart() {
        mFeaFresh.setEnabled(false);
        mRotation = ObjectAnimator.ofFloat(mFeaFresh, "rotation", 0f, 359f);
        mRotation.setRepeatCount(ObjectAnimator.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());
        mRotation.setDuration(500);
        mRotation.start();
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.EOS_AUTH_INFO, mEosAuthf);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.EOS_AUTH_INFO);
        if (serializable != null)
            mEosAuthf = (EosAuthf) serializable;
    }

    @Override
    public void loadAuthListSuccess(EosSigns eosSigns) {
        mEosSigns = eosSigns;
        updateSignInfoUI();
        updateAuthListUI();
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
    }

    @Override
    public void freshAuthError(ApiHttpException exception) {
        freshAnimaEnd();
        toast(exception.getMessage());
    }

    @Override
    protected void destroy() {
        freshAnimaEnd();
        super.destroy();
    }

    @Override
    public void freshAuthSuccess() {
        freshAnimaEnd();
        toast("刷新成功");
        mAdapter.notifyDataSetChanged();
    }

    private void updateAuthListUI() {
        mAdapter.setNewData(mEosSigns.permissions);
    }

    private void updateSignInfoUI() {
        mFeaFrom.setText(mEosSigns.from);
        mFeaTo.setText(mEosSigns.to);
        mFeaAmount.setText(mEosSigns.amount);
        mFeaMemo.setText(mEosSigns.memo);
        mFeaThreshold.setText(String.format("阈值：%s", mEosSigns.threshold));
    }
}
