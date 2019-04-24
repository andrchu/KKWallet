package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/15 0015 14:10
 * 描述	      ${TODO}
 */

public class Path implements Serializable{
    public String path;
    public String desc;
    public boolean isSelect;
    public boolean isCustom;    //自定义

    public Path(String path, String desc, boolean isSelect, boolean isCustom) {
        this.path = path;
        this.desc = desc;
        this.isSelect = isSelect;
        this.isCustom = isCustom;
    }

    public Path(String path, String desc, boolean isSelect) {
        this.path = path;
        this.desc = desc;
        this.isSelect = isSelect;
    }
}
