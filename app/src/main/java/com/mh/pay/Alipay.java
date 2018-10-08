package com.mh.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by fengjh on 16/9/9.
 */
public class Alipay {
    private static final int SDK_PAY_FLAG = 1;
    private Activity mContext;
    private Handler mHandler;


    public Alipay(Activity mContext) {
        this.mContext = mContext;

    }

    public void setHander(Handler handler) {
        mHandler = handler;
    }


    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mContext);
        String version = payTask.getVersion();
        Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
//     *
//     * @param v
//     */
//    public void h5Pay(View v) {
//        Intent intent = new Intent(mContext, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//         * 商户可以根据自己的需求来实现
//         */
//        String url = "http://m.taobao.com";
//        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        mContext.startActivity(intent);
//    }

    /**
     * 构造授权参数列表
     *
     * @param pid
     * @param app_id
     * @param target_id
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", app_id);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", pid);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        return keyValues;
    }

    /**
     * 构造支付订单参数列表
     */
    public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", app_id);

        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() + "\"}");

        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        keyValues.put("timestamp", "2016-07-29 16:55:53");

        keyValues.put("version", "1.0");

        return keyValues;
    }

    /**
     * 构造支付订单参数信息
     * <p>
     * 支付订单参数
     *
     * @return
     */
    public static String buildOrderParam(JSONObject jo) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
            sb.append(buildKeyValue("app_id", jo.getString("app_id"), false));
            sb.append("&");
            sb.append(buildKeyValue("biz_content", jo.getString("biz_content"), false));
            sb.append("&");
            sb.append(buildKeyValue("charset", jo.getString("charset"), false));
            sb.append("&");
            sb.append(buildKeyValue("format", jo.getString("format"), false));
            sb.append("&");
            sb.append(buildKeyValue("method", jo.getString("method"), false));
            sb.append("&");
            sb.append(buildKeyValue("notify_url", jo.getString("notify_url"), false));
            sb.append("&");
            sb.append(buildKeyValue("sign_type", jo.getString("sign_type"), false));
            sb.append("&");
            sb.append(buildKeyValue("sign", jo.getString("sign"), false));
            sb.append("&");
            sb.append(buildKeyValue("timestamp", jo.getString("timestamp"), false));
            sb.append("&");
            sb.append(buildKeyValue("version", jo.getString("version"), false));

//            JSONObject biz_content=new JSONObject(URLDecoder.decode(jo.getString("biz_content"), "UTF-8"));
//            // 签约合作者身份ID
//            orderInfo.append("partner=" + "\"" + "2088031860770235" + "\"");
//
//            // 签约卖家支付宝账号
//            orderInfo.append("&seller_id=" + "\"" + "tantan19900701@163.com" + "\"");
//
//            // 商户网站唯一订单号
//            orderInfo .append("&out_trade_no=" + "\"" + biz_content.getString("out_trade_no") + "\"");
//
//            // 商品名称
//            orderInfo.append("&subject=" + "\"" + biz_content.getString("subject") + "\"");
//
//            // 商品详情
//            orderInfo.append("&body=" + "\"" + biz_content.getString("subject") + "\"");
//
//            // 商品金额
//            orderInfo.append("&total_fee=" + "\"" + biz_content.getString("total_amount") + "\"");
//
//            // 服务器异步通知页面路径
//            orderInfo.append("&notify_url=" + "\"" + URLDecoder.decode(jo.getString("notify_url") , "UTF-8")+ "\"");
//
//            // 服务接口名称， 固定值
//            orderInfo.append("&service=\"mobile.securitypay.pay\"");
//
//            // 支付类型， 固定值
//            orderInfo.append("&payment_type=\"1\"")  ;
//
//            // 参数编码， 固定值
//            orderInfo.append("&_input_charset=\"utf-8\"") ;
//
//            // 设置未付款交易的超时时间
//            // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//            // 取值范围：1m～15d。
//            // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//            // 该参数数值不接受小数点，如1.5h，可转换为90m。
//            orderInfo.append("&it_b_pay=\"30m\"");
//
//            // sign
//            orderInfo.append("&sign=" + "\"" + jo.getString("sign") + "\"");
//            // sign
//            orderInfo.append("&sign_type=" + "\"" + jo.getString("sign_type") + "\"");
//
//            // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//            // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//            // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
////            orderInfo += "&return_url=\"m.alipay.com\"";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
