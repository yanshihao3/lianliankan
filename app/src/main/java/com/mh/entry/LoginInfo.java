package com.mh.entry;

import java.io.Serializable;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/14 下午2:54
 * - @Email whynightcode@gmail.com
 */
public class LoginInfo implements Serializable{

    /**
     * id : 5
     * user_name : 嗨心157949
     * mobile : 15201467353
     * token : vwLneStPC9Q7hlPDBkoZPqrVFhclm52j
     * amount : 0
     * diamonds : 0
     * reset : 0
     * bomb : 0
     * frozen : 0
     * prompt : 0
     * status : 1
     * created_at : 2018-06-13 18:40:07
     */

    private String id;
    private String user_name;
    private String mobile;
    private String token;
    private String amount;
    private String diamonds;
    private String reset;
    private String bomb;
    private String frozen;
    private String prompt;
    private String status;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(String diamonds) {
        this.diamonds = diamonds;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getBomb() {
        return bomb;
    }

    public void setBomb(String bomb) {
        this.bomb = bomb;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
