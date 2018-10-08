package com.mh.net;

/**
 * Created by apple on 2018/5/18.
 */

public interface Api {

    String HOST = "http://test.api.bi.anwenqianbao.com/";

    String HIMONEY = HOST + "v1/game/getrecharge";

    String ALPAY = HOST + "v1/game/recharge"; //支付宝

    String BLUE = HOST + "v1/game/buydiamonds"; //购买钻石

    String BUY= HOST + "v1/game/buy"; //购买道具

    String UPDATA= HOST + "v1/game/update"; //更新道具

    String QUERY= HOST + "v1/game/query"; //查询数据




    interface LOGIN {

        /**
         * 验证码获取
         **/
        String CODE = HOST + "v1/sms/code";
        /**
         * 登录
         **/
        String CHECKCODE = HOST + "v1/register/login";

    }

}
