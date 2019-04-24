package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.other.SimpSubscriber;
import com.vip.wallet.ui.dialog.DefHintDialog;
import com.vip.wallet.ui.dialog.SelectCardDialog;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.DrawableUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.RxUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.utils.SystemUtil;
import com.vip.wallet.utils.ToastUtil;
import com.vip.wallet.wallet.WalletHelper;

import java.util.List;

import butterknife.Bind;
import rx.Observable;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 16:08
 * 描述	      ${TODO}
 */

public class ReceiveSendFragment extends BaseFragment {
    @Bind(R.id.frs_qr_address)
    ImageView mFrsQrAddress;
    @Bind(R.id.frs_copy_address)
    TextView mFrsCopyAddress;
    @Bind(R.id.frs_share_address)
    ImageView mFrsShareAddress;
    @Bind(R.id.frs_send_token)
    TextView mFrsSendToken;
    @Bind(R.id.frs_select_chain)
    TextView mFrsSelectChain;
    private Card mCard;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_receive_send;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        mCard = getCurrentCard();
        updateQrCode();
        updateChainUI();
    }

    private Card getCurrentCard() {
        CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
        String currentReceiveAddress = ScApplication.getInstance().getConfig().getCurrentReceiveAddress();
        if (StringUtils.isEmpty(currentReceiveAddress)) {
            return cardDao.loadAll().get(0);
        } else {
            List<Card> list = cardDao.queryBuilder().where(CardDao.Properties.DefAddress.eq(currentReceiveAddress)).build().list();
            if (ListUtil.isEmpty(list))
                return cardDao.loadAll().get(0);
            else
                return list.get(0);
        }
    }

    @Override
    protected void initLazyData(IPresenter presenter) {
        super.initLazyData(presenter);
        showBackUpDialog();
    }

    private void showBackUpDialog() {
        if (mCard == null)
            mCard = getCurrentCard();

        if (mCard.isBackUp != 0)
            return;

        DefHintDialog defHintDialog = new DefHintDialog(mContext).setTitle(getString(R.string.back_up_seed_word)).setViceTitle(getString(R.string.back_up_seed_word_hint))
                .setLeftButtonTitle(getString(R.string.not_back_up)).setRightButtonTitle(getString(R.string.back_up));
        defHintDialog.setConfirmClickListener(() -> {
            defHintDialog.dismiss();
            start(BackUpSeedKeyFragment.newInstance(WalletHelper.decrypt(mCard.getSeedKey())));
        });
        defHintDialog.setOnCancelClickListener(defHintDialog::dismiss);
        defHintDialog.show();
    }

    private void updateQrCode() {
        Observable.unsafeCreate((Observable.OnSubscribe<Bitmap>) subscriber -> {
            int size = SizeUtils.dp2px(300);
            Bitmap image = CodeUtils.createImage(mCard.getAddressOrAccount(), size, size, null);
            subscriber.onNext(image);
            subscriber.onCompleted();
        }).compose(RxUtil.rxSchedulerHelper())
                .subscribe(new SimpSubscriber<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        mFrsQrAddress.setImageBitmap(bitmap);
                    }
                });
    }

    @Override
    protected void initListener() {
        mFrsShareAddress.setOnClickListener(this);
        mFrsCopyAddress.setOnClickListener(this);
        mFrsSendToken.setOnClickListener(this);
        mFrsSelectChain.setOnClickListener(this);
        mFrsQrAddress.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.frs_share_address:
                shareAddress();
                break;
            case R.id.frs_copy_address:
            case R.id.frs_qr_address:
                copyAddress();
                break;
            case R.id.frs_send_token:
                startForResult(SendTokenFragment.newInstance(), 100);
                break;
            case R.id.frs_select_chain:
                showSelectCardDialog();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        initData(null);
    }

    private void showSelectCardDialog() {
        SelectCardDialog selectCardDialog = new SelectCardDialog(mContext);
        selectCardDialog.setCurrentCard(mCard);
        selectCardDialog.setTitle(getString(R.string.priority_receive));
        selectCardDialog.setCardOnButtonClickListener(card -> {
            mCard = card;
            ScApplication.getInstance().getConfig().setCurrentReceiveAddress(card.getDefAddress());
            updateQrCode();
            updateChainUI();
            showBackUpDialog();
        });
        selectCardDialog.show();
    }

    private void updateChainUI() {
        mFrsSelectChain.setText(String.format("%s(%s)", mCard.getName(), StringUtil.lastSubString(mCard.getAddressOrAccount(), 4)));
        Drawable nav_left = DrawableUtil.getDrawable(mCard.getIconResId());
        nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());
        Drawable nav_right = DrawableUtil.getDrawable(R.drawable.you);
        nav_right.setBounds(0, 0, nav_right.getMinimumWidth(), nav_right.getMinimumHeight());

        mFrsSelectChain.setCompoundDrawables(nav_left, null, nav_right, null);
    }

    private void copyAddress() {
        SystemUtil.clipString(mContext, mCard.getAddressOrAccount());
        ToastUtil.toastS(getString(R.string.address_copy_board));
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    public static ReceiveSendFragment newInstance() {
        return new ReceiveSendFragment();
    }

    public void shareAddress() {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, mCard.getAddressOrAccount());
        startActivity(Intent.createChooser(textIntent, getString(R.string.share)));
    }
}
