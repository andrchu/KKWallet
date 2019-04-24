package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/5 0005 11:18
 * 描述	      ${TODO}
 */

public class Balance{

    /**
     * result : 135499
     * message : OK
     * status : 1
     */
    public String result;
    public String message;
    public String status;

    @Override
    public String toString() {
        return "Balance{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
