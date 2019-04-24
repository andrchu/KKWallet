package com.vip.wallet.entity;

import com.vip.wallet.dao.Card;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/26 0026 14:47
 * 描述	      ${TODO}
 */

public class BuyInfo implements Serializable{
    public String toAddress;
    public String amount;
    public int gas_price = 1;
    public BigInteger gas_limit = new BigInteger("21000");
    public String fee;  //矿工费
    public String ethMarkeValueCNY;    //ETH市值
    public String feeCNY;   //矿工费-人民币
    public String data;
    public Card card;
    public long callBackId;

    public BuyInfo() {
    }

    public BuyInfo(String toAddress, Card card, String amount, String data) {
        this.toAddress = toAddress;
        this.amount = amount;
        this.data = data;
        this.card = card;
    }
}
