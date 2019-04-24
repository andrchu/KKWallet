package com.vip.wallet.dao;

import com.vip.wallet.entity.Chain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/2/10 0010 15:03
 * 描述	      联系人
 */
@Entity
public class Contact implements Serializable,Cloneable {
    @Id(autoincrement = true)
    public Long id;
    //姓
    public String surname;
    //名
    public String name;
    //地址
    public String address;
    //备注
    public String memo;
    //主链
    public int chain_type = 0;

    @Transient
    public Chain mChain = new Chain(chain_type);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getChain_type() {
        return this.chain_type;
    }

    public void setChain_type(int chain_type) {
        this.chain_type = chain_type;
    }

    public String getAddress() {
        return this.address;
    }

    @Generated(hash = 851701757)
    public Contact(Long id, String surname, String name, String address, String memo,
                   int chain_type) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.address = address;
        this.memo = memo;
        this.chain_type = chain_type;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }
}
