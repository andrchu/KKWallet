package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseActivity;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.Balance;
import com.vip.wallet.entity.BtcTransInfo;
import com.vip.wallet.entity.EosAccountInfo;
import com.vip.wallet.entity.OutToken;
import com.vip.wallet.entity.WalletTokenInfos;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.other.BaseJavaScriptObject;
import com.vip.wallet.other.SimpSeekBarChangeListener;
import com.vip.wallet.other.SimpTextWatcher;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.contract.SendTokenContract;
import com.vip.wallet.ui.dialog.SelectCardDialog;
import com.vip.wallet.ui.dialog.SendTokenDetailDialog;
import com.vip.wallet.ui.presenter.SendTokenPresenter;
import com.vip.wallet.utils.CalcUtil;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.wallet.WalletHelper;
import com.vip.wallet.widget.CustomWebView;
import com.vip.wallet.widget.TitleBarView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/26 0026 15:07
 * 描述	      ${TODO}
 */

public class SendTokenFragment extends BaseFragment<SendTokenContract.ISendTokenPresenter>
        implements SendTokenContract.ISendTokenView {

    @Bind(R.id.fst_title)
    TitleBarView mFstTitle;
    @Bind(R.id.fst_address)
    EditText mFstAddress;
    @Bind(R.id.fst_select_contact)
    ImageView mFstSelectContact;
    @Bind(R.id.fst_et_out_count)
    EditText mFstEtOutCount;
    @Bind(R.id.fst_bt_all_out)
    TextView mFstBtAllOut;
    @Bind(R.id.fst_token_name)
    TextView mFstTokenName;
    @Bind(R.id.fst_select_token)
    LinearLayout mFstSelectToken;
    @Bind(R.id.fst_tv_max_count)
    TextView mFstTvMaxCount;
    @Bind(R.id.fst_seekbar)
    SeekBar mFstSeekbar;
    @Bind(R.id.fst_miner_cost)
    TextView mFstMinerCost;
    @Bind(R.id.fst_send)
    TextView mFstSend;
    @Bind(R.id.saa_common_option)
    LinearLayout mSaaCommonOption;
    OutToken mOutToken = new OutToken();
    @Bind(R.id.fst_card_name)
    TextView mFstCardName;
    @Bind(R.id.fst_et_memo)
    TextView mFstEtMemo;
    @Bind(R.id.fst_select_card)
    LinearLayout mFstSelectCard;
    @Bind(R.id.fst_layout_fee)
    LinearLayout mFstLayoutFee;
    private SendTokenDetailDialog mSendTokenDetailDialog;
    private CustomWebView mWebView;

    public static SendTokenFragment newInstance() {
        return new SendTokenFragment();
    }

    @Override

    protected int setLayoutId() {
        return R.layout.fragment_send_token;
    }

    @Override
    protected SendTokenContract.ISendTokenPresenter setPresenter() {
        return new SendTokenPresenter(this);
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
    public void initData(SendTokenContract.ISendTokenPresenter presenter) {
        Card card = getCurrentCard();
        selectCurrentCard(card);
        initWebView();
    }

    private void initWebView() {
        mWebView = new CustomWebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new EosCallBack(mActivity), "android");
        mWebView.loadUrl(Constants.BASE_URL + "app/index/eos_interacts");
        mWebView.setListener(new CustomWebView.Listener() {
            @Override
            public void onError() {
                LogUtils.i("onError >>");
                loadJsIsError = true;
            }

            @Override
            public void onPageFinished(String url) {
                LogUtils.i("onPageFinished >>" + url);
            }
        });
    }

    private boolean loadJsIsError = false;

    private void sendEosAmount() {
        if (loadJsIsError) {
            hideLoadingDialog();
            toast("数据未获取成功！");
            return;
        }
        String privateKey = WalletHelper.decrypt(mOutToken.currentCard.privateKey);
        String format = String.format("javascript:dyeos('%s','%s','%s','%s','%s')", privateKey, mOutToken.fromAddress, mOutToken.toAddress,
                mOutToken.outCount.setScale(4, BigDecimal.ROUND_HALF_UP).toString() + " EOS", mOutToken.memo);
        LogUtils.i("format >> " + format);
        mWebView.loadUrl(format);
    }

    class Result {
        public int code;
        public String message;
    }

    class EosCallBack extends BaseJavaScriptObject {
        public EosCallBack(BaseActivity activity) {
            super(activity);
        }

        public void eosCallBack(Main main) {
            mActivity.runOnUiThread(() -> {
                Result result = GsonAdapter.getGson().fromJson(main.data, Result.class);
                hideLoadingDialog();
                if (result.code == 0) {
                    toast("发送成功");
                    back();
                    return;
                }
                switch (result.code) {
                    case 3050003:
                        result.message = "账户不存在";
                        break;
                    case 3080001:
                        result.message = "内存不足";
                        break;
                    case 3080002:
                        result.message = "网络资源不足";
                        break;
                    case 3080004:
                        result.message = "CPU不足";
                        break;
                    case 3090003:
                        result.message = "权限不足";
                        break;
                    default:
                        break;
                }
                toast(result.message);
            });
        }
    }

    private void updateFeeLayoutUI() {
        mFstLayoutFee.setVisibility(mOutToken.chain_type == 2 ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initListener() {
        mFstSelectToken.setOnClickListener(this);
        mFstTitle.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            startActivityForResult(new Intent(mContext, ScannerCodeActivity.class), 200);
        });
        mFstSelectContact.setOnClickListener(this);
        mFstBtAllOut.setOnClickListener(this);
        mFstSeekbar.setOnSeekBarChangeListener(new SimpSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mOutToken.progress = progress;
                updateMinerCostUI();
                if (mOutToken.chain_type == 0) {        //ETH
                    if (mOutToken.token.isEth()) {
                        updateMaxCountUI();
                        if (StringUtils.isEmpty(mFstEtOutCount.getText().toString().trim()))
                            return;
                        updateOutCountUI();
                    }
                } else if (mOutToken.chain_type == 1) {    //BTC
                    updateMaxCountUI();
                    if (StringUtils.isEmpty(mFstEtOutCount.getText().toString().trim()))
                        return;
                    updateOutCountUI();
                }
            }
        });
        mFstEtOutCount.addTextChangedListener(new SimpTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = mFstEtOutCount.getText().toString();
                if (StringUtils.isEmpty(string))
                    string = "0";
                mOutToken.outCount = new BigDecimal(string);
                if (mOutToken.chain_type == 0)
                    getPresenter().getLimit(mOutToken);
            }
        });
        mFstSend.setOnClickListener(this);
        mFstSelectCard.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.SCAN_CODE_OK) {
            mOutToken.toAddress = data.getStringExtra(Constants.SCAN_CODE_RESULT);
            updateAddressUI();
        }
    }

    private void updateAddressUI() {
        if (mOutToken.chain_type == 2) {
            mFstAddress.setHint("收款人的账户名");
        } else {
            mFstAddress.setHint("收款人的钱包地址");
        }
        mFstAddress.setText(mOutToken.toAddress);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == SelectTokenFragment.RESULT_CODE) {
            Serializable serializable = data.getSerializable(Constants.TOKEN);
            WalletTokenInfos.TokenInfosEntity tokenInfosEntity = (WalletTokenInfos.TokenInfosEntity) serializable;
            mOutToken.token.tokenName = tokenInfosEntity.symbol;
            mOutToken.token.contractAddress = tokenInfosEntity.contract_address;
            mOutToken.token.decimals = tokenInfosEntity.decimals;
            mOutToken.token.iconUrl = tokenInfosEntity.image_url;
            mOutToken.token.balance = tokenInfosEntity.getBalance();
            mOutToken.gasLimit_server = new BigInteger("21000");
            updateTokenUI();
            showLoadingDialog("获取数据...");
            getPresenter().getLimit(mOutToken);
        } else if (resultCode == ContactsManageFragment.RESULT_CODE) {
            //选择联系人
            mOutToken.toAddress = data.getString(Constants.CONTACT);
            updateAddressUI();
        }
    }

    private void updateMaxCountUI() {
        String balance = mOutToken.token.balance;
        if (mOutToken.chain_type == 0) {    //ETH
            if (mOutToken.token.isEth() && mOutToken.balance.compareTo(new BigDecimal(0)) > 0)
                balance = CalcUtil.sub(mOutToken.balance.toString(), mOutToken.fee).toString();
            mOutToken.token.balance = balance;
        } else if (mOutToken.chain_type == 1) {        //BTC
            balance = mOutToken.balance.toString();
            if (mOutToken.balance.compareTo(new BigDecimal(0)) > 0) {
                balance = CalcUtil.sub(mOutToken.balance.toString(), mOutToken.fee).toString();
            }
        } else if (mOutToken.chain_type == 2) {     //EOS
            balance = mOutToken.balance.toString();
        }
        mFstTvMaxCount.setText(EncodeUtils.htmlDecode(String.format("最大交易数量为 <font color='#fd914b'>%s</font>", balance)));
    }

    private void updateTokenUI() {
        if (mOutToken.currentCard.chainType == 0) {
            mFstTokenName.setText(mOutToken.token.tokenName);
        } else {
            mFstTokenName.setText(StringUtil.getChainName(mOutToken.currentCard.chainType));
        }
    }

    private void updateOutCountUI() {
        String text = mOutToken.outCount.toString();
        if (mOutToken.outCount.compareTo(new BigDecimal("0")) <= 0) {
            mFstEtOutCount.setText("");
            return;
        }
        mFstEtOutCount.setText(text);
        mFstEtOutCount.setSelection(mFstEtOutCount.getText().toString().length());
    }


    private void updateSeekBarUI() {
        if (mOutToken.chain_type == 0) {
            mFstSeekbar.setMax(100);
            mOutToken.progress = 7;
        } else if (mOutToken.chain_type == 1) {
            mFstSeekbar.setMax(290000);
            mOutToken.progress = 2000;
        }
        mFstSeekbar.setProgress(mOutToken.progress);
    }

    /**
     * 矿工费
     */
    private void updateMinerCostUI() {
        int unscaledVal = mOutToken.progress + (mOutToken.chain_type == 0 ? 1 : 10000);
        if (mOutToken.chain_type == 0) {    //ETH
            BigDecimal multiply = new BigDecimal(mOutToken.gasLimit_server).multiply(BigDecimal.valueOf(unscaledVal));
            BigDecimal divide = multiply.divide(new BigDecimal(Math.pow(10, 9)));
            mOutToken.fee = divide.toString();
            mFstMinerCost.setText(String.format("%s ETH", mOutToken.fee));
            mOutToken.gasPrice_seek_bar = new BigInteger(String.valueOf(unscaledVal));
        } else if (mOutToken.chain_type == 1) {    //BTC
            mOutToken.fee = CalcUtil.div(String.valueOf(unscaledVal), "100000000").toString();
            mFstMinerCost.setText(String.format("%s BTC", mOutToken.fee));
        }
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fst_select_token:
                if (mOutToken.currentCard.chainType == 0) {
                    startForResult(SelectTokenFragment.newInstance(mOutToken.currentCard.defAddress), 100);
                } else if (mOutToken.currentCard.chainType == 1) {
                    toast("BTC主链没有更多代币");
                } else if (mOutToken.currentCard.chainType == 2) {
                    toast("EOS主链暂没有更多代币");
                }
                break;
            case R.id.fst_select_contact:
                startForResult(ContactsManageFragment.newInstance(ContactsManageFragment.OPTION_SELECT), 300);
                break;
            case R.id.fst_bt_all_out:
                allOut();
                break;
            case R.id.fst_send:
                if (check())
                    transaction();
                break;
            case R.id.fst_select_card:
                showSelectCard();
                break;
            default:
                break;
        }
    }

    /**
     * 选择卡包
     */
    private void showSelectCard() {
        SelectCardDialog selectCardDialog = new SelectCardDialog(mContext);
        selectCardDialog.setCurrentCard(mOutToken.currentCard);
        selectCardDialog.setCardOnButtonClickListener(card -> {
            if (mOutToken.currentCard.equals(card))
                return;
            selectCurrentCard(card);

            ScApplication.getInstance().getConfig().setCurrentReceiveAddress(card.defAddress);
        });
        selectCardDialog.show();

    }

    private void selectCurrentCard(Card card) {
        mOutToken.currentCard = card;
        mOutToken.chain_type = card.chainType;
        if (mOutToken.chain_type == 2) {
            //如果是EOS 地址即账户名
            mOutToken.fromAddress = card.accountName;
        } else {
            mOutToken.fromAddress = card.defAddress;
        }

        String chainName = StringUtil.getChainName(mOutToken.chain_type);
        mOutToken.token.contractAddress = chainName;
        mOutToken.token.tokenName = chainName;

        updateSeekBarUI();
        updateMinerCostUI();
        updateMaxCountUI();
        updateTokenUI();
        updateCardName();
        updateFeeLayoutUI();
        updateMemoUI();
        updateOutCountUI();
        updateAddressUI();

        showLoadingDialog(getString(R.string.get_data));
        if (mOutToken.chain_type == 0) {
            getPresenter().getEthBalance(mOutToken.fromAddress);
        } else if (mOutToken.chain_type == 1) {
            getPresenter().getBtcBalance(mOutToken.fromAddress);
        } else if (mOutToken.chain_type == 2) {
            getPresenter().getEosBalance(mOutToken.fromAddress);
        }
    }

    private void updateMemoUI() {
        mFstEtMemo.setVisibility(mOutToken.chain_type == 2 ? View.VISIBLE : View.GONE);
    }

    private void updateCardName() {
        mFstCardName.setText(mOutToken.currentCard.getName());
    }

    private boolean check() {
        String address = mFstAddress.getText().toString();
        if (StringUtils.isEmpty(address)) {
            toast(getString(R.string.in_address));
            return false;
        }

        boolean chain = mOutToken.chain_type == 0;

        if (mOutToken.chain_type == 2) {
            if (!StringUtil.isEosAccount(address)) {
                toast(getString(R.string.account_format_error));
                return false;
            }
        } else {
            if (chain ? !StringUtil.isEthAddress(address) : !StringUtil.isBtcAddress(address)) {
                toast(getString(R.string.address_format_error));
                return false;
            }
        }

        mOutToken.toAddress = mFstAddress.getText().toString();

        if (mOutToken.outCount.compareTo(new BigDecimal("0")) <= 0) {
            toast(getString(R.string.empty_amount));
            return false;
        }

        //EOS最多只有4位小数
        if (mOutToken.chain_type == 2) {
            String s = mOutToken.outCount.toString();
            if (s.contains(".")) {
                String[] split = s.split("\\.");
                if (split.length == 2) {
                    if (split[1].length() > 4) {
                        toast("EOS最多只能有4位小数");
                        return false;
                    }
                }
            }
        }

        BigDecimal val;
        if (mOutToken.chain_type == 0) {
            val = new BigDecimal(mOutToken.token.balance);
        } else if (mOutToken.chain_type == 1)
            val = CalcUtil.sub(mOutToken.balance.toString(), mOutToken.fee);
        else {
            val = mOutToken.balance;
        }
        if (mOutToken.outCount.compareTo(val) > 0) {
            toast(getString(R.string.out_of_max_count_hint));
            return false;
        }
        if (mOutToken.chain_type == 0) {
            if (mOutToken.balance.compareTo(new BigDecimal(mOutToken.fee)) < 0) {
                toast(getString(R.string.eth_insufficient));
                return false;
            }
        }
        return true;
    }


    private void transaction() {

        giveData();

        if (mOutToken.chain_type == 2) {
            showLoadingDialog("请稍后");
            getPresenter().getEosAccountInfo(mOutToken);
            return;
        }
        showTokenDetailDialog();
    }

    private void showTokenDetailDialog() {
        mSendTokenDetailDialog = new SendTokenDetailDialog(mContext, mOutToken);
        mSendTokenDetailDialog.setOnConfirmClickListener(v -> {
            mSendTokenDetailDialog.dismiss();
            showLoadingDialog(getString(R.string.sending));
            if (mOutToken.chain_type == 0) {
                getPresenter().sendEthTypeAmount(mOutToken);
            } else if (mOutToken.chain_type == 1) {
                getPresenter().sendBtcAmount(mOutToken, mOutToken.mBtcTransInfo);
            } else if (mOutToken.chain_type == 2) {
                //                getPresenter().sendEosAmount(mOutToken);
                sendEosAmount();
            }
        });
        mSendTokenDetailDialog.updateUI();
        mSendTokenDetailDialog.show();
    }


    private void giveData() {
        mOutToken.memo = mFstEtMemo.getText().toString().trim();
    }

    private void allOut() {
        if (mOutToken.chain_type == 0) {
            if (StringUtils.isEmpty(mOutToken.token.balance))
                mOutToken.outCount = new BigDecimal(0);
            else
                mOutToken.outCount = new BigDecimal(mOutToken.token.balance);
        } else if (mOutToken.chain_type == 1) {
            mOutToken.outCount = CalcUtil.sub(mOutToken.balance.toString(), mOutToken.fee);
        } else if (mOutToken.chain_type == 2) {
            mOutToken.outCount = mOutToken.balance;
        }
        updateOutCountUI();
    }


    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.OUT_TOKEN, mOutToken);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.OUT_TOKEN);
        if (serializable != null) {
            mOutToken = (OutToken) serializable;
        }
    }

    @Override
    public void getAllCountSuccess(Balance balance) {

    }

    @Override
    public void getLimitError(Throwable e) {
        hideLoadingDialog();
        updateMinerCostUI();
        updateMaxCountUI();
    }

    @Override
    public void getLimitSuccess(BigInteger o) {
        hideLoadingDialog();
        //为了保证转出顺利, 在原来的基础上增加1000
        if (!mOutToken.token.isEth())
            mOutToken.gasLimit_server = o.add(new BigInteger("1000"));

        updateMinerCostUI();
        updateMaxCountUI();
        if (mSendTokenDetailDialog != null) {
            mSendTokenDetailDialog.updateUI();
        }
    }

    @Override
    public void sendAmountError(Throwable e) {
        hideLoadingDialog();
        toast(e.getMessage());
    }

    @Override
    public void sendAmountSuccess(String s) {
        hideLoadingDialog();
        toast(s);
        back();
    }

    @Override
    public void getEthBalanceError(Throwable e) {
        getPresenter().getLimit(mOutToken);
    }

    @Override
    public void getEthBalanceSuccess(BigDecimal bigDecimal) {
        mOutToken.token.balance = bigDecimal.toString();
        mOutToken.balance = bigDecimal;
        getPresenter().getLimit(mOutToken);
    }

    @Override
    public void getTokenBalanceError() {

    }

    @Override
    public void getTokenBalanceSuccess(BigDecimal div) {

    }

    @Override
    public void getBtcBalanceError(ApiHttpException exception) {
        hideLoadingDialog();
        mOutToken.balance = new BigDecimal("0");
        toast(exception.getMessage());
    }

    @Override
    public void getBtcBalanceSuccess(BtcTransInfo btcTransInfo) {
        hideLoadingDialog();
        mOutToken.mBtcTransInfo = btcTransInfo;
        mOutToken.balance = new BigDecimal(String.valueOf(btcTransInfo.getTotalAmount()));
        updateSeekBarUI();
        updateMinerCostUI();
        updateMaxCountUI();
    }

    @Override
    public void getEosBalanceError(ApiHttpException exception) {
        hideLoadingDialog();
        toast(exception.getMessage());
        mOutToken.balance = new BigDecimal("0");
    }

    @Override
    public void getEosBalanceSuccess(String s) {
        hideLoadingDialog();
        mOutToken.balance = new BigDecimal(s.split(" ")[0]);
        updateMaxCountUI();
        updateOutCountUI();
    }

    @Override
    public void getEosAccountInfoError(ApiHttpException exception) {
        hideLoadingDialog();
        toast(exception.getMessage());
    }

    @Override
    public void getEosAccountInfoSuccess(EosAccountInfo eosAccountInfo) {
        hideLoadingDialog();
        EosAccountInfo.PermissionsEntity active = ListUtil.getObj(eosAccountInfo.permissions, new EosAccountInfo.PermissionsEntity("active"));
        if (active == null || active.required_auth.threshold <= 1) {
            showTokenDetailDialog();
        } else {
            String defAddress = mOutToken.currentCard.getDefAddress();
            EosAccountInfo.PermissionsEntity.RequiredAuthEntity.KeysEntity obj = ListUtil.getObj(active.required_auth.keys, new EosAccountInfo.PermissionsEntity.RequiredAuthEntity.KeysEntity(defAddress));
            if (obj.weight >= active.required_auth.threshold) {
                showTokenDetailDialog();
            } else {
                mOutToken.mEosAccountInfo = eosAccountInfo;
                start(EosSignsFragment.newInstance(mOutToken));
            }
        }
    }
}
