package com.vip.wallet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.entity.BuyInfo;
import com.vip.wallet.entity.SymbolPrice;
import com.vip.wallet.other.SimpSeekBarChangeListener;
import com.vip.wallet.ui.contract.BuyInfoContract;
import com.vip.wallet.ui.dialog.DefHintDialog;
import com.vip.wallet.ui.dialog.SendBuyInfoDialog;
import com.vip.wallet.ui.presenter.BuyInfoPresenter;
import com.vip.wallet.utils.CalcUtil;
import com.vip.wallet.utils.ClickUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/26 0026 14:04
 * 描述	      购买信息
 */

public class BuyInfoActivity extends BaseActivity<BuyInfoContract.IBuyInfoPresenter> implements BuyInfoContract.IBuyInfoView {
    @Bind(R.id.bia_address)
    TextView mBiaAddress;
    @Bind(R.id.bia_amount)
    TextView mBiaAmount;
    @Bind(R.id.bia_seekbar)
    SeekBar mBiaSeekbar;
    @Bind(R.id.bia_miner_cost)
    TextView mBiaMinerCost;
    @Bind(R.id.bia_commit)
    TextView mBiaCommit;
    @Bind(R.id.bia_miner_cost_cny)
    TextView mBiaMinerCostCny;

    BuyInfo mBuyInfo = new BuyInfo();
    private DefHintDialog mDefHintDialog;
    private Intent mIntent = new Intent();

    public static void start(Activity context, BuyInfo buyInfo) {
        Intent intent = new Intent(context, BuyInfoActivity.class);
        intent.putExtra(Constants.BUY_INFO, buyInfo);
        context.startActivityForResult(intent, 100);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_buy_info;
    }

    @Override
    protected BuyInfoContract.IBuyInfoPresenter setPresenter() {
        return new BuyInfoPresenter(this);
    }

    @Override
    protected void initData(BuyInfoContract.IBuyInfoPresenter presenter) {
        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.BUY_INFO);
        if (serializableExtra != null)
            mBuyInfo = (BuyInfo) serializableExtra;
        calcFee();
        updateUI();
        showLoadingDialog(getString(R.string.get_data));
        presenter.getGasLimit(mBuyInfo);
        presenter.getSymbolPrice();
    }

    /**
     * 计算矿工费
     */
    private void calcFee() {
        BigDecimal bigDecimal = new BigDecimal(mBuyInfo.gas_limit.multiply(new BigInteger(String.valueOf(mBuyInfo.gas_price))));
        BigDecimal divide = bigDecimal.divide(new BigDecimal(Math.pow(10, 9)));
        mBuyInfo.fee = divide.toString();
        if (mBuyInfo.ethMarkeValueCNY != null) {
            mBuyInfo.feeCNY = CalcUtil.mul(mBuyInfo.ethMarkeValueCNY, mBuyInfo.fee).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    private void updateUI() {
        updateFeeUI();
        updateToAddressUI();
        updateAmountUI();
    }

    private void updateAmountUI() {
        String amountCny = "";
        String unit = ScApplication.getInstance().getUnit();
        if (!StringUtils.isEmpty(mBuyInfo.ethMarkeValueCNY)) {
            amountCny = String.format("(%s%s)", unit, CalcUtil.mul(mBuyInfo.ethMarkeValueCNY, mBuyInfo.amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        mBiaAmount.setText(String.format("%s ETH %s", mBuyInfo.amount, amountCny));

    }

    private void updateToAddressUI() {
        mBiaAddress.setText(mBuyInfo.toAddress);
    }

    private void updateFeeUI() {
        mBiaSeekbar.setProgress(mBuyInfo.gas_price - 1);
        mBiaMinerCost.setText(String.format("%s ETH", mBuyInfo.fee));
        String unit = ScApplication.getInstance().getUnit();
        if (!StringUtils.isEmpty(mBuyInfo.feeCNY)) {
            mBiaMinerCostCny.setText(String.format("%s%s", unit, mBuyInfo.feeCNY));
        }
    }

    @Override
    protected void initListener() {
        mBiaSeekbar.setOnSeekBarChangeListener(new SimpSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBuyInfo.gas_price = progress + 1;
                calcFee();
                updateFeeUI();
            }
        });
        mBiaCommit.setOnClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            SendBuyInfoDialog sendBuyInfoDialog = new SendBuyInfoDialog(this, mBuyInfo);
            sendBuyInfoDialog.updateUI();
            sendBuyInfoDialog.setOnConfirmClickListener(v1 -> {
                showLoadingDialog(getString(R.string.paying));
                getPresenter().pay(mBuyInfo);
                sendBuyInfoDialog.dismiss();
            });
            sendBuyInfoDialog.show();
        });
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.BUY_INFO);
        if (serializable != null)
            mBuyInfo = (BuyInfo) serializable;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.BUY_INFO, mBuyInfo);
    }

    @Override
    public void payError(Throwable e) {
        hideLoadingDialog();
        toast(e.getMessage());
    }

    @Override
    public void finish() {
        mIntent.putExtra(Constants.BUY_INFO, mBuyInfo);
        setResult(Constants.PAY_SUCCESS, mIntent);
        super.finish();
    }

    @Override
    public void paySuccess(String data) {
        hideLoadingDialog();
        toast(getString(R.string.pay_success));
        mIntent.putExtra(Constants.TX, data);
        finish();
    }

    @Override
    public void getGasLimitSuccess(BigInteger bigInteger) {
        hideLoadingDialog();
        mBuyInfo.gas_limit = bigInteger;
        LogUtils.i("gas Limit >>>>>>" + bigInteger);
        calcFee();
        updateFeeUI();
    }

    @Override
    public void getGasLimitError(Throwable e) {
        hideLoadingDialog();
        showErrorDialog(e);
    }

    @Override
    public void getSymbolPriceSuccess(SymbolPrice symbolPrice) {
        if (ScApplication.getInstance().getConfig().getCurrency_unit() == 0) {
            mBuyInfo.ethMarkeValueCNY = symbolPrice.price_cny;
        } else
            mBuyInfo.ethMarkeValueCNY = symbolPrice.price_usd;
        calcFee();
        updateFeeUI();
        updateAmountUI();
    }

    private void showErrorDialog(Throwable e) {
        mDefHintDialog = new DefHintDialog(this).setLeftButtonTitle("取消").setRightButtonTitle("重试")
                .setTitle("获取失败")
                .setViceTitle("获取GasLimit失败,请选择重试！")
                .setConfirmClickListener(() -> {
                    mDefHintDialog.dismiss();
                    showLoadingDialog(getString(R.string.paying));
                    getPresenter().getGasLimit(mBuyInfo);
                })
                .setOnCancelClickListener(this::finish);
        mDefHintDialog.show();
    }
}
