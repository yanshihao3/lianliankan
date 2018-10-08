package com.mh;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/13 下午12:02
 * - @Email whynightcode@gmail.com
 */
public class MyApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setRetryCount(3);                          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    public static Context getApplication() {
        return sContext;
    }
}
