package com.mh.net;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mh.MyApp;


import java.util.Map;


/**
 * Created by apple on 2018/4/13.
 */

public class ApiService {
    /**
     * @param params
     * @param listener banner
     */
    public static void GET_SERVICE(String url, Map<String, String> params, final OnRequestDataListener listener) {
        newExcuteJsonPost(url, params, listener);
    }

    public static void GET_SERVICE(String url, String params, final OnRequestDataListener listener) {
        newExcuteJsonPost(url, params, listener);
    }

    private static void newExcuteJsonPost(String url, Map<String, String> params, final OnRequestDataListener listener) {
        OkGo.<String>post(url)
                .tag(MyApp.getApplication())
                .params(params, false)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null) {
                            try {
                                JSONObject jsonObject = JSONObject.parseObject(response.body());
                                int code = jsonObject.getIntValue("error_code");
                                if (code == 0) {
                                    listener.requestSuccess(code, jsonObject);
                                } else {
                                    listener.requestFailure(code, jsonObject.getString("error_message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            listener.requestFailure(-1, "网络请求失败,请稍后重试");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        listener.requestFailure(-1, "网络请求失败,请稍后重试");

                    }
                });
    }

    private static void newExcuteJsonPost(String url, String params, final OnRequestDataListener listener) {
        OkGo.<String>post(url)
                .tag(MyApp.getApplication())
                .upJson(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() != null) {
                            try {
                                JSONObject jsonObject = JSONObject.parseObject(response.body());
                                int code = jsonObject.getIntValue("error_code");
                                if (code == 0) {
                                    listener.requestSuccess(code, jsonObject);
                                } else {
                                    listener.requestFailure(code, jsonObject.getString("error_message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            listener.requestFailure(-1, "网络请求失败,请稍后重试");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        listener.requestFailure(-1, "网络请求失败,请稍后重试");

                    }
                });
    }

}
