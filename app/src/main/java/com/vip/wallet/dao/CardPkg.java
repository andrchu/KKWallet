package com.vip.wallet.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/1 0001 14:31
 * 描述	      ${TODO}
 */
@Entity
public class CardPkg {
    @Id(autoincrement = true)
    public Long id;
    public String privateKey;
    public String address;
    public boolean isMain;
    public int chainType;       //0 - ETH   1 - BTC

    public int getChainType() {
        return this.chainType;
    }

    public void setChainType(int chainType) {
        this.chainType = chainType;
    }

    public boolean getIsMain() {
        return this.isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1602868255)
    public CardPkg(Long id, String privateKey, String address, boolean isMain,
                   int chainType) {
        this.id = id;
        this.privateKey = privateKey;
        this.address = address;
        this.isMain = isMain;
        this.chainType = chainType;
    }

    @Generated(hash = 1271841601)
    public CardPkg() {
    }
}
