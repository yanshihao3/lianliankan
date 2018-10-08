package com.mh.match;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mh.entry.Config;
import com.mh.entry.MessageEvent;
import com.mh.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyActivity extends BaseActivity {

    @BindView(R.id.imageView)
    CircleImageView mImageView;
    @BindView(R.id.my_break)
    ImageView mMyBreak;
    @BindView(R.id.my_name)
    TextView mMyName;
    @BindView(R.id.my_id)
    TextView mMyId;
    @BindView(R.id.my_blue_money)
    TextView mMyBlueMoney;
    @BindView(R.id.my_pay)
    TextView mPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        mPay.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initView();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updata(MessageEvent event) {
        if (MessageEvent.UPDATA == event.getType()) {
            initView();
        }
    }

    private void initView() {
        String haiMoney = (String) SPUtils.get(Config.HAI_MONEY, "0");
        //mMyHaibiMoney.setText(haiMoney);

        String blueMoney = (String) SPUtils.get(Config.BLUE_DIAMOND, "0");
        mMyBlueMoney.setText(blueMoney);

        String name = (String) SPUtils.get(Config.NAME, "0");
        mMyName.setText("昵称：" + name);

        String id = (String) SPUtils.get(Config.ID, "0");
        mMyId.setText("ID：" + id);
    }

    @OnClick({R.id.my_break,  R.id.my_blue_add, R.id.my_pay})
    public void onViewClicked(View view) {
        Intent intent = new Intent(MyActivity.this, PayActivity.class);
        switch (view.getId()) {
            case R.id.my_break:
                finish();
                break;
            case R.id.my_blue_add: //兑换蓝钻
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.my_pay: //充值嗨币
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
