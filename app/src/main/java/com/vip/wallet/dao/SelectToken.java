package com.vip.wallet.dao;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.vip.wallet.config.ScApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/19 0019 16:38
 * 描述	      ${TODO}
 */
@Entity
public class SelectToken implements MultiItemEntity, Serializable {
    @Id(autoincrement = true)
    public Long id;
    public String contractAddress;
    public int decimals;
    public String tokenName;
    public String iconUrl;
    public String fileName;
    public boolean isSelect;
    @Transient
    public int item_type;
    public String balance = "0";
    public String amount_cny = "0";
    public String amount_usd = "0";
    public String wallet_address;

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

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTokenName() {
        return this.tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getDecimals() {
        return this.decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getContractAddress() {
        return this.contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 129775266)
    public SelectToken(Long id, String contractAddress, int decimals, String tokenName, String iconUrl, String fileName, boolean isSelect,
            String balance, String amount_cny, String amount_usd, String wallet_address) {
        this.id = id;
        this.contractAddress = contractAddress;
        this.decimals = decimals;
        this.tokenName = tokenName;
        this.iconUrl = iconUrl;
        this.fileName = fileName;
        this.isSelect = isSelect;
        this.balance = balance;
        this.amount_cny = amount_cny;
        this.amount_usd = amount_usd;
        this.wallet_address = wallet_address;
    }

    @Generated(hash = 1911358098)
    public SelectToken() {
    }

    @Override
    public int getItemType() {
        return item_type;
    }

    public String getAmount() {
        boolean b = ScApplication.getInstance().getConfig().getCurrency_unit() == 0;
        BigDecimal cny_bigDecimal = new BigDecimal(amount_cny);
        BigDecimal usd_bigDecimal = new BigDecimal(amount_usd);
        return b ? cny_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : usd_bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SelectToken that = (SelectToken) o;

        return contractAddress != null ? contractAddress.equals(that.contractAddress) : that.contractAddress == null;

    }

    @Override
    public int hashCode() {
        return contractAddress != null ? contractAddress.hashCode() : 0;
    }

    public String getWallet_address() {
        return this.wallet_address;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }
}
