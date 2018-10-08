package com.mh.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.entry.MessageEvent;
import com.mh.match.PayActivity;
import com.mh.match.R;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.pay.Alipay;
import com.mh.pay.IAlPayResultListener;
import com.mh.pay.PayAsyncTask;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class PayFragment extends DialogFragment implements IAlPayResultListener {

    Unbinder unbinder;
    @BindView(R.id.fragment_pay_number)
    TextView mNumber;
    private PayActivity mPayActivity;
    private int mMoney;

    private String mId;

    public static PayFragment newInstance(int money, String id) {
        PayFragment payFragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("money", money);
        bundle.putString("id", id);
        payFragment.setArguments(bundle);
        return payFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPayActivity = (PayActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoney = getArguments().getInt("money");
        mId = getArguments().getString("id");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mNumber.setText("应付金额：" + mMoney + "元");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.iv_close, R.id.fragment_pay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close: //关闭
                dismiss();
                break;
            case R.id.fragment_pay_btn: //调用支付宝
                dismiss();
                alPay();
                break;
        }
    }

    private void alPay() {
        String user_id = (String) SPUtils.get(Config.ID, "");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("recharge", mMoney + "");
        ApiService.GET_SERVICE(Api.ALPAY, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                JSONObject jsonObject = json.getJSONObject("data");
                String payinfo = Alipay.buildOrderParam(jsonObject);
                //获取签名字符串
                final PayAsyncTask payAsyncTask = new PayAsyncTask(mPayActivity, PayFragment.this);
                payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, payinfo);
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });

    }

    @Override
    public void onPaySuccess() {
        initData();
    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {
        MatchToast.showToast(mPayActivity, "充值失败");
    }

    @Override
    public void onPayCancel() {
        MatchToast.showToast(mPayActivity, "支付取消");

    }

    @Override
    public void onPayConnectError() {
        MatchToast.showToast(mPayActivity, "充值失败");

    }

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", (String) SPUtils.get(Config.ID, ""));
        ApiService.GET_SERVICE(Api.QUERY, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                Log.e("pay", "requestSuccess: "+JSONObject.toJSONString(json));
                JSONObject jsonObject = json.getJSONObject("data");
                LoginInfo loginInfo = JSON.parseObject(JSON.toJSONString(jsonObject), LoginInfo.class);
                SPUtils.put(Config.HAI_MONEY, loginInfo.getAmount());
                SPUtils.put(Config.BLUE_DIAMOND, loginInfo.getDiamonds());
                SPUtils.put(Config.HELP, loginInfo.getPrompt());
                SPUtils.put(Config.REFRESH, loginInfo.getReset());
                SPUtils.put(Config.FREEZE, loginInfo.getFrozen());
                SPUtils.put(Config.BOMB, loginInfo.getBomb());
                EventBus.getDefault().post(new MessageEvent("充值完成 数据更新完成，", MessageEvent.UPDATA));
                HintFragment hintFragment = HintFragment.newInstance(2);
                hintFragment.show(mPayActivity.getSupportFragmentManager(), "2");
                mPayActivity.updata();
            }

            @Override
            public void requestFailure(int code, String msg) {
                MatchToast.showToast(mPayActivity, msg);
            }
        });
    }
}