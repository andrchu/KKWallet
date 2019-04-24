package com.vip.wallet.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/23 0023 11:52
 * 描述	      ${TODO}
 */

public class MyCenterOption implements MultiItemEntity {
    public int itemType;


    @Override
    public int getItemType() {
        return itemType;
    }
}
