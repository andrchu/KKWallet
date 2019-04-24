package com.vip.wallet.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 创建者     金国栋
 * 创建时间   2018/4/2 0002 15:29
 * 描述	      ${TODO}
 */
@Entity
public class PushMessage implements Serializable{
    @Id(autoincrement = true)
    public Long id;
    public String title;    //标题
    public String content;  //内容
    public int type;    //0 - 普通消息  1 - 交易消息  2 - 打开内置浏览器  3 - 打开系统浏览器  4 - 跳转制定页面
    public String data;
    public long time;
    public boolean isRead;  //是否阅读
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 209314899)
    public PushMessage(Long id, String title, String content, int type,
            String data, long time, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.data = data;
        this.time = time;
        this.isRead = isRead;
    }
    @Generated(hash = 1468533071)
    public PushMessage() {
    }
}
