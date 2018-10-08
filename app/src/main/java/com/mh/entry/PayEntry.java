package com.mh.entry;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/15 下午2:55
 * - @Email whynightcode@gmail.com
 */
public class PayEntry implements MultiItemEntity, Serializable {

    public static final int PAY = 1; //
    public static final int BLUE_PAY = 2; //

    private boolean isClicked;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private int itemType;

    private String title; //名字

    private int url; //图片地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PayEntry{" +
                "itemType=" + itemType +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", money=" + money +
                '}';
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    private int money; //钱数


    public PayEntry(int itemType, String title, int url, int money, boolean isClicked) {
        this.itemType = itemType;
        this.title = title;
        this.url = url;
        this.isClicked = isClicked;
        this.money = money;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
