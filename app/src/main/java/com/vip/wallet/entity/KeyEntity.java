package com.vip.wallet.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/18 0018 10:41
 * 描述	      ${TODO}
 */

public class KeyEntity implements MultiItemEntity {
    public String key_str;
    public int type;

    public KeyEntity(String key_str) {
        this.key_str = key_str;
    }

    public KeyEntity setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
