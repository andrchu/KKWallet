package com.vip.wallet.dao;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.vip.wallet.utils.StringUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/5 0005 10:53
 * 描述	      ${TODO}
 */
@Entity
public class Token implements Serializable, MultiItemEntity {
    @Id(autoincrement = true)
    public Long id;
    public String contractAddress = "ETH";
    public int decimals = 18;
    public String tokenName = "ETH";
    public String iconUrl = "http://p33cb8q1h.bkt.clouddn.com/coin/image/ethereum.png";
    public String fileName = "ETH";
    public boolean isSelect;
    @Transient
    public int item_type;
    @Transient
    public String balance;

    @Generated(hash = 397671396)
    public Token(Long id, String contractAddress, int decimals, String tokenName,
                 String iconUrl, String fileName, boolean isSelect) {
        this.id = id;
        this.contractAddress = contractAddress;
        this.decimals = decimals;
        this.tokenName = tokenName;
        this.iconUrl = iconUrl;
        this.fileName = fileName;
        this.isSelect = isSelect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Token token = (Token) o;

        return contractAddress != null ? contractAddress.equals(token.contractAddress) : token.contractAddress == null;

    }

    @Override
    public int hashCode() {
        return contractAddress != null ? contractAddress.hashCode() : 0;
    }

    @Generated(hash = 79808889)
    public Token() {
    }

    public boolean isEth() {
        return contractAddress.equals("ETH");
    }

    @Override
    public String toString() {
        return "Token{" +
                "contractAddress='" + contractAddress + '\'' +
                ", decimals=" + decimals +
                ", tokenName='" + tokenName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public SelectToken token2SelectToken() {
        return token2SelectToken("");
    }

    public SelectToken token2SelectToken(String address) {
        return new SelectToken(null, contractAddress, decimals, tokenName, iconUrl, fileName, isSelect, "0", "0", "0", StringUtil.getNo0xAddress(address));
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

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getItemType() {
        return item_type;
    }
}
