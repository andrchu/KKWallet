package com.vip.wallet.entity;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/10 0010 14:12
 * 描述	      ${TODO}
 */

public class BrowserInfo implements Serializable {
    private String url;
    private String title;

    public BrowserInfo() {
    }

    public BrowserInfo(String title, String url) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
