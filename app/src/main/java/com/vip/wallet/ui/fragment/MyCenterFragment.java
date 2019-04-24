package com.vip.wallet.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.EosAuthf;
import com.vip.wallet.entity.QrCode;
import com.vip.wallet.http.GsonAdapter;
import com.vip.wallet.ui.activity.AboutWeActivity;
import com.vip.wallet.ui.activity.CardPkgActivity;
import com.vip.wallet.ui.activity.ContactsManageActivity;
import com.vip.wallet.ui.activity.DealRecordActivity;
import com.vip.wallet.ui.activity.DefCurrencyActivity;
import com.vip.wallet.ui.activity.EosAuthActivity;
import com.vip.wallet.ui.activity.LockActivity;
import com.vip.wallet.ui.activity.MessageCenterActivity;
import com.vip.wallet.ui.activity.PropertyActivity;
import com.vip.wallet.ui.activity.ReceiveSendTokenActivity;
import com.vip.wallet.ui.activity.ScannerCodeActivity;
import com.vip.wallet.ui.activity.SelectCardActivity;
import com.vip.wallet.ui.activity.WeChatServiceActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.widget.DrawableTextView;
import com.vip.wallet.widget.TitleBarView;

import java.util.List;

import butterknife.Bind;

import static com.vip.wallet.config.Constants.SCAN_CODE_RESULT;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/18 0018 11:10
 * 描述	     个人中心
 */

public class MyCenterFragment extends BaseFragment {
    @Bind(R.id.fmc_title_view)
    TitleBarView mFmcTitleView;
    @Bind(R.id.fmc_receive_send_token)
    DrawableTextView mFmcReceiveSendToken;
    @Bind(R.id.fmc_update_pwd)
    DrawableTextView mFmcUpdatePwd;
    @Bind(R.id.fmc_about_we)
    DrawableTextView mFmcAboutWe;
    @Bind(R.id.fmc_we_chat_service)
    DrawableTextView mFmcWeChatService;
    @Bind(R.id.fmc_contact)
    DrawableTextView mFmcContact;
    @Bind(R.id.fmc_def_currency)
    DrawableTextView mFmcDefCurrency;
    @Bind(R.id.fmc_message_center)
    DrawableTextView mFmcMessageCenter;
    @Bind(R.id.fmc_property)
    DrawableTextView mFmcProperty;
    @Bind(R.id.fmc_card_pkg)
    DrawableTextView mFmcWallet;
    @Bind(R.id.fmc_share_app)
    DrawableTextView mFmcTelegramGroup;
    @Bind(R.id.fmc_eos_resource)
    DrawableTextView mFmcEosResource;
    //    @Bind(R.id.fmc_log_out)
    //    DrawableTextView mLogOut;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_my_center;
    }

    public static MyCenterFragment newInstance() {
        return new MyCenterFragment();
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {

    }

    @Override
    protected void initListener() {
        mFmcReceiveSendToken.setOnClickListener(this);
        mFmcTitleView.setOnButtonClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            startActivity(DealRecordActivity.class);
        });
        mFmcUpdatePwd.setOnClickListener(this);
        mFmcAboutWe.setOnClickListener(this);
        mFmcWeChatService.setOnClickListener(this);
        mFmcContact.setOnClickListener(this);
        mFmcDefCurrency.setOnClickListener(this);
        mFmcMessageCenter.setOnClickListener(this);
        mFmcProperty.setOnClickListener(this);
        mFmcWallet.setOnClickListener(this);
        mFmcTelegramGroup.setOnClickListener(this);
        mFmcEosResource.setOnClickListener(this);
    }

    @Override
    protected void processClick(View view) {
        if (!ClickUtil.hasExecute())
            return;
        super.processClick(view);
        switch (view.getId()) {
            case R.id.fmc_receive_send_token:
                startActivity(ReceiveSendTokenActivity.class);
                break;
            case R.id.fmc_update_pwd:
                LockActivity.startPwdActivity(mContext, LockFragment.UPDATE_PWD);
                break;
            case R.id.fmc_about_we:
                startActivity(AboutWeActivity.class);
                break;
            case R.id.fmc_we_chat_service:
                startActivity(WeChatServiceActivity.class);
                break;
            case R.id.fmc_contact:
                startActivity(ContactsManageActivity.class);
                break;
            case R.id.fmc_def_currency:
                startActivity(DefCurrencyActivity.class);
                break;
            case R.id.fmc_message_center:
                startActivity(MessageCenterActivity.class);
                break;
            case R.id.fmc_property:
                startActivity(PropertyActivity.class);
                break;
            case R.id.fmc_card_pkg:
                startActivity(CardPkgActivity.class);
                break;
            case R.id.fmc_share_app:
                //                joinGroup();
                shareApp();
                //                startVo();
                break;
            case R.id.fmc_eos_resource:
                startEosResource();
                break;

            default:
                break;
        }

    }

    private void startVo() {
        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.ChainType.eq(2)).build().list();
        if (ListUtil.isEmpty(list)) {
            toast("没有EOS卡");
            return;
        }
        String s = list.get(0).defAddress;
        WebActivity.startWebActivity(mContext, new BrowserInfo("", Constants.BASE_URL + "app/eosvote/index" + StringUtil.signBuilder(s)));
    }

    private void shareApp() {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, "KKWallet,安全的去中心化钱包,更有丰富的Dapp等你来玩。下载地址: https://www.kkwallet.io/d");
        startActivity(Intent.createChooser(textIntent, getString(R.string.share)));
    }

    private void startEosResource() {

        List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.ChainType.eq(2)).build().list();
        if (ListUtil.isEmpty(list)) {
            toast("没有EOS卡");
            return;
        }

        String s = StringUtil.signBuilder(list.get(0).defAddress);
        if (list.size() == 1) {
            WebActivity.startWebActivity(mContext, new BrowserInfo("EOS资源", Constants.BASE_URL + "app/index/eosresource.html" + s));
        } else {
            SelectCardActivity.start(mContext, 2, Constants.BASE_URL + "app/index/eosresource.html");
        }
    }

    /**
     * 加入电报群
     */
    private void joinGroup() {
        Uri uri = Uri.parse("https://t.me/kkwallet");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }


    @Override
    protected void backClick() {
        startActivityForResult(new Intent(mActivity, ScannerCodeActivity.class), 10000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.SCAN_CODE_OK) {
            try {
                String jsonData = data.getStringExtra(SCAN_CODE_RESULT);
                LogUtils.i("扫描结果:" + jsonData);
                JsonParser parser = new JsonParser();  //创建JSON解析器
                JsonObject parse = (JsonObject) parser.parse(jsonData);
                String action = parse.get("action").getAsString();
                JsonElement actionData = parse.get("data");
                if (action.equals(QrCode.EOS_TRANSFER)) {
                    EosAuthf eosAuth = GsonAdapter.getGson().fromJson(actionData, EosAuthf.class);
                    EosAuthActivity.startEosAuthActivity(mActivity, eosAuth);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
