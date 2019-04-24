package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.AddressDao;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.DaoSession;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.dialog.DefHintDialog;
import com.vip.wallet.ui.dialog.InputTextDialog;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.io.Serializable;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/19 0019 15:52
 * 描述	      ${TODO}
 */

public class ManageCardFragment extends BaseFragment {
    @Bind(R.id.fmc_chain_type_icon)
    ImageView mFmcChainTypeIcon;
    @Bind(R.id.fmc_chain_type_text)
    TextView mFmcChainTypeText;
    @Bind(R.id.fmc_card_name)
    TextView mFmcCardName;
    @Bind(R.id.fmc_update_card_name)
    TextView mFmcUpdateCardName;
    @Bind(R.id.fmc_export_private_key)
    TextView mFmcExportPrivateKey;
    @Bind(R.id.fmc_export_seed_key)
    TextView mFmcExportSeedKey;
    @Bind(R.id.fmc_delete_card)
    TextView mFmcDeleteCard;
    @Bind(R.id.fmc_permission_manage)
    TextView mFmcPermissionManage;
    private DefHintDialog mDefHintDialog;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_manage_card;
    }

    public static ManageCardFragment newInstance(Card card) {
        ManageCardFragment manageCardFragment = new ManageCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.CARD, card);
        manageCardFragment.setArguments(args);
        return manageCardFragment;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    private Card mCard;

    @Override
    public void initData(IPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.CARD);
        if (serializable != null)
            mCard = (Card) serializable;
        updateUI();
    }

    private void updateUI() {
        mFmcChainTypeIcon.setImageResource(mCard.getIconResId());
        mFmcChainTypeText.setText(mCard.getChainTypeString());
        mFmcCardName.setText(String.format("%s (%s)", mCard.getName(), mCard.chainType == 2 ? mCard.accountName : StringUtil.lastSubString(mCard.defAddress, 4)));
        boolean empty = StringUtils.isEmpty(mCard.seedKey);
        mFmcExportSeedKey.setVisibility(empty ? View.GONE : View.VISIBLE);
        mFmcDeleteCard.setVisibility(mCard.isDefCard() ? View.GONE : View.VISIBLE);
        mFmcPermissionManage.setVisibility(mCard.chainType == 2 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initListener() {
        mFmcUpdateCardName.setOnClickListener(this);
        mFmcExportPrivateKey.setOnClickListener(this);
        mFmcExportSeedKey.setOnClickListener(this);
        mFmcDeleteCard.setOnClickListener(this);
        mFmcPermissionManage.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fmc_update_card_name:
                //修改卡名
                updateCardName();
                break;
            case R.id.fmc_export_private_key:
                //导出私钥
                start(ExportPrivateKeyFragment.newInstance(WalletHelper.decrypt(mCard.getPrivateKey())));
                break;
            case R.id.fmc_export_seed_key:
                //导出助记词
                start(BackUpSeedKeyFragment.newInstance(WalletHelper.decrypt(mCard.getSeedKey())));
                break;
            case R.id.fmc_delete_card:
                //删除卡包
                showDeleteCardDialog();
                break;
            case R.id.fmc_permission_manage:
                WebActivity.startWebActivity(mContext, new BrowserInfo("权限管理", Constants.BASE_URL + "app/auth/index?address=" + mCard.getDefAddress()));
                break;
            default:
                break;
        }
    }

    private void showDeleteCardDialog() {
        mDefHintDialog = new DefHintDialog(mContext)
                .setTitle("删除卡包")
                .setViceTitle("您确定删除此卡包?")
                .setRightButtonTitle(getString(R.string.delete))
                .setConfirmClickListener(() -> {
                    deleteCard();
                    mDefHintDialog.dismiss();
                });
        mDefHintDialog.show();
    }

    /**
     * 删除卡包
     */
    private void deleteCard() {
        DaoSession daoSession = ScApplication.getInstance().getDaoSession();
        daoSession.getCardDao().delete(mCard);
        daoSession.getAddressDao().queryBuilder().where(AddressDao.Properties.CardId.eq(mCard.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CARD, mCard);
        setFragmentResult(102, bundle);
        toast(getString(R.string.delete_success));
        pop();
    }

    private void updateCardName() {
        InputTextDialog inputTextDialog = new InputTextDialog(mActivity);
        inputTextDialog.setText(mCard.getName());
        inputTextDialog.setConfirmClickListener(content -> {
            mCard.name = content;
            ScApplication.getInstance().getDaoSession().getCardDao().update(mCard);
            updateUI();
            toast(getString(R.string.update_success));
            inputTextDialog.dismiss();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.CARD, mCard);
            setFragmentResult(100, bundle);
            pop();
        });
        inputTextDialog.show();
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.CARD, mCard);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.CARD);
        if (serializable != null)
            mCard = (Card) serializable;
    }
}
