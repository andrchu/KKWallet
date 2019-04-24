package com.vip.wallet.entity;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 15:37
 * 描述	      ${TODO}
 */

public class JPushMessageData {
    public String title;
    public String content;
    public String data;
    public long msgId;

    @Override
    public String toString() {
        return "JPushMessageData{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", data='" + data + '\'' +
                ", msgId=" + msgId +
                '}';
    }

    public JPushMessageData(String title, String content, String data, long msgId) {
        this.title = title;
        this.content = content;
        this.data = data;
        this.msgId = msgId;
    }
}
