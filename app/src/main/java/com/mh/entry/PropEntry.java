package com.mh.entry;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/17 上午9:58
 * - @Email whynightcode@gmail.com
 *
 * 道具bean
 */
public class PropEntry {

    /**
     * money : 300
     * user_id : 5
     * totle : [{"type":"1","number":"10"},{"type":"1","number":"10"},{"type":"1","number":"10"}]
     */

    private String money;
    private String user_id;
    private List<TotleBean> totle;

    public PropEntry(String money, String user_id, List<TotleBean> totle) {
        this.money = money;
        this.user_id = user_id;
        this.totle = totle;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<TotleBean> getTotle() {
        return totle;
    }

    public void setTotle(List<TotleBean> totle) {
        this.totle = totle;
    }

    @Override
    public String toString() {
        return "{" +
                "money='" + money + '\'' +
                ", user_id='" + user_id + '\'' +
                ", totle=" + totle +
                '}';
    }

    public static class TotleBean {
        /**
         * type : 1
         * number : 10
         */

        private String type;
        private String number;

        public TotleBean(String type,String number) {
            this.type = type;
            this.number=number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "{" +
                    "type='" + type + '\'' +
                    ", number='" + number + '\'' +
                    '}';
        }
    }
}
