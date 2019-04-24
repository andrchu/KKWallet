package com.vip.wallet.entity;

import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.Token;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/5 0005 10:45
 * 描述	      ${TODO}
 */

public class OutToken implements Serializable {
    //转出地址
    public String toAddress = "";
    //转出金额
    public BigDecimal outCount = new BigDecimal(0);
    //备注
    public String memo = " ";
    //gas price
    public BigInteger gasPrice;
    //gas price
    public BigInteger gasPrice_seek_bar;
    //gas limit
    public BigInteger gasLimit = new BigInteger("21000");
    //gas limit from server
    public BigInteger gasLimit_server = new BigInteger("21000");
    //from address
    public String fromAddress;
    //password
    public String passWord;
    //nonce
    public BigInteger nonce;
    //代币
    public Token token = new Token();
    //max count
    public BigDecimal maxCount;
    //高级选项
    public boolean highOption;
    //seekBar_process
    public int progress = 7;
    //eth | btc 余额
    public BigDecimal balance = new BigDecimal("0");
    //手续费
    public String fee;

    public int chain_type = 0;  //0-ETH  1-BTC

    public Card currentCard;

    public BtcTransInfo mBtcTransInfo;

    public EosAccountInfo mEosAccountInfo;

    @Override
    public String toString() {
        return "OutToken{" +
                "toAddress='" + toAddress + '\'' +
                ", outCount=" + outCount +
                ", memo='" + memo + '\'' +
                ", gasPrice=" + gasPrice +
                ", gasPrice_seek_bar=" + gasPrice_seek_bar +
                ", gasLimit=" + gasLimit +
                ", gasLimit_server=" + gasLimit_server +
                ", fromAddress='" + fromAddress + '\'' +
                ", passWord='" + passWord + '\'' +
                ", nonce=" + nonce +
                ", token=" + token +
                ", maxCount=" + maxCount +
                ", highOption=" + highOption +
                ", progress=" + progress +
                '}';
    }
}
