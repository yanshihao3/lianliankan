package com.mh.entry;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/13 下午3:19
 * - @Email whynightcode@gmail.com
 */
public class MultipleItem implements MultiItemEntity {

    private String number;

    private boolean isTag;

    public void setTag(boolean tag) {
        isTag = tag;
    }

    public boolean isTag() {
        return isTag;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public static final int PASS = 1; //对应通过的关数
    public static final int NOT_PASS = 2;    //对应的没有通过的关数
    private int itemType;

    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}

