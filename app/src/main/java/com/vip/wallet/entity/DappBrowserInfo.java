package com.vip.wallet.entity;

import com.vip.wallet.dao.Card;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/7/10 0010 14:12
 * 描述	      ${TODO}
 */

public class DappBrowserInfo implements Serializable {
    private String url;
    private String title;
    private Card card;

    public DappBrowserInfo(String url, String title, Card card) {
        this.url = url;
        this.title = title;
        this.card = card;
    }

    public DappBrowserInfo() {
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
