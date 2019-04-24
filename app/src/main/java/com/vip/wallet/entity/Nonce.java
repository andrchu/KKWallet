package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/1 0001 14:05
 * 描述	      ${TODO}
 */

public class Nonce {

    /**
     * result : 0x0
     * id : 1
     * jsonrpc : 2.0
     */
    public String result;
    public int id;
    public String jsonrpc;

    @Override
    public String toString() {
        return "Nonce{" +
                "result='" + result + '\'' +
                ", id=" + id +
                ", jsonrpc='" + jsonrpc + '\'' +
                '}';
    }
}
