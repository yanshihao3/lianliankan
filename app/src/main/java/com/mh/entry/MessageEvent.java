package com.mh.entry;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/17 上午8:19
 * - @Email whynightcode@gmail.com
 */
public class MessageEvent {

    public static final int UPDATA = 0;
    public static final int SHORE = 1;

    private String message;

    private int type;

    public MessageEvent(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
