package com.mh.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mh.adapter.PayAdapter;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.entry.MessageEvent;
import com.mh.entry.PayEntry;
import com.mh.fragment.HintFragment;
import com.mh.fragment.PayFragment;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;
import com.mh.view.PayItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {


    @BindView(R.id.pay_blue_number)
    TextView mPayBlueNumber;
    @BindView(R.id.pay_recyclerview)
    RecyclerView mPayRecyclerview;
    @BindView(R.id.pay_paymoney)
    ImageView mPayPaymoney;
    private int mType;
    private PayAdapter mPayAdapter;
    private List<PayEntry> mPayEntryList;

    private int mMoney = 1;

    private int id = 1; //套餐id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updata();
    }

    public void updata() {
       // mPayHaiNumber.setText((String) SPUtils.get(Config.HAI_MONEY, ""));
        mPayBlueNumber.setText((String) SPUtils.get(Config.BLUE_DIAMOND, ""));
    }

    private void initView() {
       // mPayHaiNumber.setText((String) SPUtils.get(Config.HAI_MONEY, ""));
        mPayBlueNumber.setText((String) SPUtils.get(Config.BLUE_DIAMOND, ""));
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        mPayEntryList = new ArrayList<>();
        if (mType == 0) { //对应的 支付宝支付
            mPayPaymoney.setImageResource(R.mipmap.pay_pay);
            mPayEntryList.add(new PayEntry(PayEntry.PAY, "10Hi币", 0, 10, true));
            mPayEntryList.add(new PayEntry(PayEntry.PAY, "20Hi币", 1, 20, false));
            mPayEntryList.add(new PayEntry(PayEntry.PAY, "50Hi币", 2, 50, false));
            mPayEntryList.add(new PayEntry(PayEntry.PAY, "100Hi币", 3, 100, false));
        } else { //对应的 兑换蓝钻
            mPayPaymoney.setImageResource(R.mipmap.pay_pay);
            mPayEntryList.add(new PayEntry(PayEntry.BLUE_PAY, "10蓝钻", 0, 1, true));
            mPayEntryList.add(new PayEntry(PayEntry.BLUE_PAY, "20蓝钻", 1, 2, false));
            mPayEntryList.add(new PayEntry(PayEntry.BLUE_PAY, "50蓝钻", 2, 5, false));
            mPayEntryList.add(new PayEntry(PayEntry.BLUE_PAY, "100蓝钻", 3, 10, false));
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mPayRecyclerview.setLayoutManager(gridLayoutManager);
        mPayRecyclerview.addItemDecoration(new PayItemDecoration(10));
        mPayAdapter = new PayAdapter(mPayEntryList);
        mPayRecyclerview.setAdapter(mPayAdapter);

        mPayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                changeState(position);
                switch (position) {
                    case 0:
                        mMoney = 1;
                        id = 1;
                        break;
                    case 1:
                        mMoney = 2;
                        id = 2;
                        break;
                    case 2:
                        id = 3;

                        mMoney = 5;
                        break;
                    case 3:
                        id = 4;

                        mMoney = 10;
                        break;
                }
            }
        });
    }

    private void changeState(int position) {
        for (int i = 0; i < 4; i++) {
            PayEntry payEntry = mPayEntryList.get(i);
            if (i == position) {
                payEntry.setClicked(true);
            } else {
                payEntry.setClicked(false);
            }
        }
        mPayAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.pay_break, R.id.pay_paymoney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_break:
                finish();
                break;
            case R.id.pay_paymoney:
                if (mType == 0) { //支付宝

                } else { //兑换钻石
                    PayFragment payFragment = PayFragment.newInstance(mMoney, id + "");
                    payFragment.show(getSupportFragmentManager(), "pay");
                    //convert();
                }
                break;
        }
    }

    private void convert() {
        int haiMoney = Integer.parseInt((String) SPUtils.get(Config.HAI_MONEY, "0"));
        if (haiMoney < mMoney / 10) { //haibi 少于充值的金额
            HintFragment hintFragment = HintFragment.newInstance(0);
            hintFragment.show(getSupportFragmentManager(), "0");
        } else { //调用接口，兑换钻石
            final String user_id = (String) SPUtils.get(Config.ID, "");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("diamonds", mMoney + "");
            ApiService.GET_SERVICE(Api.BLUE, params, new OnRequestDataListener() {
                @Override
                public void requestSuccess(int code, JSONObject json) {
                    JSONObject jsonObject = json.getJSONObject("data");
                    LoginInfo loginInfo = JSON.parseObject(JSON.toJSONString(jsonObject), LoginInfo.class);
                    SPUtils.put(Config.HAI_MONEY, loginInfo.getAmount());
                    SPUtils.put(Config.BLUE_DIAMOND, loginInfo.getDiamonds());
                    SPUtils.put(Config.HELP, loginInfo.getPrompt());
                    SPUtils.put(Config.REFRESH, loginInfo.getReset());
                    SPUtils.put(Config.FREEZE, loginInfo.getFrozen());
                    SPUtils.put(Config.BOMB, loginInfo.getBomb());
                    EventBus.getDefault().post(new MessageEvent("兑换完钻石，更新数据", MessageEvent.UPDATA));
                    HintFragment hintFragment = HintFragment.newInstance(3);
                    hintFragment.show(getSupportFragmentManager(), "3");
                    updata();
                }

                @Override
                public void requestFailure(int code, String msg) {
                    MatchToast.showToast(PayActivity.this, "网络出现异常，请稍后重试");
                }
            });
        }
    }
}
