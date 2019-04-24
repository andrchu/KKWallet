package com.vip.wallet.dao;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.vip.wallet.R;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.utils.StringUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/26 0026 11:04
 * 描述	      钱包
 */
@Entity
public class Wallet implements Serializable, MultiItemEntity {
    @Id(autoincrement = true)
    public Long id;
    public String wallet_address;
    public String wallet_name;
    public String wallet_pwd_hint;
    public double wallet_amount;
    public String wallet_md5_pwd;
    public int token_count;
    @Transient
    public int item_type;
    public String seedKeyWord;
    public String amount_cny = "0";
    public String amount_usd = "0";
    public boolean isBackup;

    public boolean isBackup() {
        return StringUtils.isEmpty(seedKeyWord) || isBackup;
    }


    public String getAmount() {
        boolean b = ScApplication.getInstance().getConfig().getCurrency_unit() == 0;
        if (amount_cny == null)
            amount_cny = wallet_amount + "";
        if (amount_usd == null) {
            amount_usd = wallet_amount + "";
        }
        BigDecimal cny_bigDecimal = new BigDecimal(amount_cny);
        BigDecimal usd_bigDecimal = new BigDecimal(amount_usd);
        return b ? cny_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : usd_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getWallet_md5_pwd() {
        return this.wallet_md5_pwd;
    }

    public void setWallet_md5_pwd(String wallet_md5_pwd) {
        this.wallet_md5_pwd = wallet_md5_pwd;
    }

    public double getWallet_amount() {
        return this.wallet_amount;
    }

    public void setWallet_amount(double wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getWallet_pwd_hint() {
        return this.wallet_pwd_hint;
    }

    public void setWallet_pwd_hint(String wallet_pwd_hint) {
        this.wallet_pwd_hint = wallet_pwd_hint;
    }

    public String getWallet_name() {
        return this.wallet_name;
    }

    public String getSuName() {
        if (StringUtils.isEmpty(this.wallet_name))
            return this.wallet_name;
        return this.wallet_name.substring(0, 1);
    }

    public String getAllWalletName() {
        if (!isBackup()) {
            return String.format("%s(%s)", wallet_name, StringUtil.getString(R.string.un_back_up));
        }
        return this.wallet_name;
    }


    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getWallet_address() {
        return this.wallet_address;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 685687243)
    public Wallet(Long id, String wallet_address, String wallet_name, String wallet_pwd_hint, double wallet_amount, String wallet_md5_pwd,
                  int token_count, String seedKeyWord, String amount_cny, String amount_usd, boolean isBackup) {
        this.id = id;
        this.wallet_address = wallet_address;
        this.wallet_name = wallet_name;
        this.wallet_pwd_hint = wallet_pwd_hint;
        this.wallet_amount = wallet_amount;
        this.wallet_md5_pwd = wallet_md5_pwd;
        this.token_count = token_count;
        this.seedKeyWord = seedKeyWord;
        this.amount_cny = amount_cny;
        this.amount_usd = amount_usd;
        this.isBackup = isBackup;
    }

    @Generated(hash = 1197745249)
    public Wallet() {
    }

    @Override
    public int getItemType() {
        return item_type;
    }

    public int getToken_count() {
        return this.token_count;
    }

    public void setToken_count(int token_count) {
        this.token_count = token_count;
    }

    public String getSeedKeyWord() {
        return this.seedKeyWord;
    }

    public void setSeedKeyWord(String seedKeyWord) {
        this.seedKeyWord = seedKeyWord;
    }

    public String getAmount_usd() {
        return this.amount_usd;
    }

    public void setAmount_usd(String amount_usd) {
        this.amount_usd = amount_usd;
    }

    public String getAmount_cny() {
        return this.amount_cny;
    }

    public void setAmount_cny(String amount_cny) {
        this.amount_cny = amount_cny;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Wallet wallet = (Wallet) o;

        return wallet_address != null ? wallet_address.equals(wallet.wallet_address) : wallet.wallet_address == null;

    }

    @Override
    public int hashCode() {
        return wallet_address != null ? wallet_address.hashCode() : 0;
    }

    public boolean getIsBackup() {
        return this.isBackup;
    }

    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }
}
