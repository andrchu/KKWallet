package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 15:47
 * 描述	      ${TODO}
 */

public class PushTypeData {
    public int type;
    public String data;
    public long time;

    public PushTypeData(int type, String data, long time) {
        this.type = type;
        this.data = data;
        this.time = time;
    }

    @Override
    public String toString() {
        return "PushTypeData{" +
                "type=" + type +
                ", data='" + data + '\'' +
                ", time=" + time +
                '}';
    }
}
