package com.vip.wallet.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/21 0021 17:58
 * 描述	      ${TODO}
 */

public class DealRecordCache implements Serializable{
    public String allAddress;
    public ArrayList<DealRecord> dealRecords;

    public DealRecordCache(String allAddress, ArrayList<DealRecord> dealRecords) {
        this.allAddress = allAddress;
        this.dealRecords = dealRecords;
    }
}
