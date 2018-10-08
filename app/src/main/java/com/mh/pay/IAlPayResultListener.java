package com.mh.pay;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/15 下午6:00
 * - @Email whynightcode@gmail.com
 */
public interface IAlPayResultListener {
    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
