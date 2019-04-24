package com.vip.wallet.entity;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.vip.wallet.R;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Wallet;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

import static com.vip.wallet.utils.StringUtil.getString;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/9 0009 17:15
 * 描述	     交易记录
 */

public class DealRecord implements Serializable, MultiItemEntity {
    public int status;// 交易状态 0 - 交易完成 1 - 交易进行中 2- 交易失败
    public int type;// 交易类型 0 - 转出  1 - 转入
    public String date;// 时间
    public String value;// 交易额
    public String tx;// 哈希值
    public int item_type;
    public String token_symbol; //代币简称
    public String wallet_address;   //钱包地址  from
    public String wallet_name;  //钱包名
    public String from_address;
    public String to_address;
    public int chain_type = 0;  //0-ETH  1-BTC

    public String getDetailsUrl() {
        if (chain_type == 0) {
            return Constants.DETAIL_URL + tx;
        } else if (chain_type == 1) {
            return Constants.BTC_DETAIL_URL + tx;
        } else {
            return Constants.EOS_DETAIL_URL + tx;
        }
    }

    public String getToken_symbol() {
        return token_symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DealRecord that = (DealRecord) o;

        return tx != null ? tx.equals(that.tx) : that.tx == null;

    }

    @Override
    public int hashCode() {
        return tx != null ? tx.hashCode() : 0;
    }

    public String getTypeDesc() {
        switch (status) {
            case 0:
                return getString(R.string.deal_finish);
            case 1:
                return getString(R.string.dealing);
            case 2:
                return getString(R.string.deal_error);
            default:
                return getString(R.string.deal_finish);
        }
    }

   /* @DrawableRes
    public int getTypeIconResId() {
    }*/

    public int getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        if (type == 0)
            return "-" + value;
        else
            return "+" + value;
    }

    public String getTx() {
        return tx;
    }

    @Override
    public int getItemType() {
        return item_type;
    }

    public String getWalletName() {
        if (!StringUtils.isEmpty(wallet_name)) {
            return wallet_name;
        }
        if (StringUtils.isEmpty(wallet_address)) {
            return "";
        }
        List<Wallet> wallets = ScApplication.getInstance().getDaoSession().getWalletDao().queryRaw("WHERE WALLET_ADDRESS = ?", StringUtil.getNo0xAddress(wallet_address));
        if (ListUtil.isEmpty(wallets)) {
            return StringUtil.getHideString(wallet_address);
        }
        String wallet_name = wallets.get(0).wallet_name;
        this.wallet_name = wallet_name;
        return this.wallet_name = wallet_name;
    }
}
