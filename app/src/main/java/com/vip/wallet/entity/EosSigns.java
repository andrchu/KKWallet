package com.vip.wallet.entity;


import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建者     金国栋
 * 创建时间   2018/8/24 0024 15:35
 * 描述	      ${TODO}
 */

public class EosSigns implements Serializable{
    public String from;
    public String to;
    public String amount;
    public String memo;
    public String head_block_time;
    public long last_irreversible_block_num;
    public long ref_block_prefix;
    public int threshold;
    @Expose(serialize = false, deserialize = false)
    public ArrayList<Permissions> permissions = new ArrayList<>();

    public String chain_id;

    @Expose(serialize = false, deserialize = false)
    public String signInfo;

    @Expose(serialize = false, deserialize = false)
    public String orderId;

    @Expose(serialize = false, deserialize = false)
    public String address;

    @Expose(serialize = false, deserialize = false)
    public OutToken outToken;


    public static class Permissions implements Serializable{
        public Permissions(String account, int weight) {
            this.account = account;
            this.weight = weight;
        }

        public Permissions(String account) {
            this.account = account;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Permissions that = (Permissions) o;

            return account != null ? account.equals(that.account) : that.account == null;

        }

        @Override
        public int hashCode() {
            return account != null ? account.hashCode() : 0;
        }

        @Expose(serialize = false, deserialize = false)
        public boolean isNative;
        public String account;
        public int weight;
        public String signs = "";
    }
}
