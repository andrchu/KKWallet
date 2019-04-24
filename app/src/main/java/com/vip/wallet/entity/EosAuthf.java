package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/27 0027 15:43
 * 描述	      ${TODO}
 */

public class EosAuthf implements Serializable {
    public EosAuthf(String order_id) {
        this.order_id = order_id;
    }

    public EosAuthf() {
    }

    @Override
    public String toString() {
        return "EosAuthf{" +
                "order_id='" + order_id + '\'' +
                '}';
    }

    public String order_id;
}
