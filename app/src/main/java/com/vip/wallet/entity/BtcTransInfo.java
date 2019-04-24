package com.vip.wallet.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/21 0021 10:01
 * 描述	      ${TODO}
 */

public class BtcTransInfo implements Serializable {
    public ArrayList<UnSpent> unSpents;
    public double totalAmount;

    public double getTotalAmount() {

        return totalAmount;
    }
}
