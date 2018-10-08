package com.mh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.entry.MessageEvent;
import com.mh.entry.PropEntry;
import com.mh.match.HomeActivity;
import com.mh.match.R;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 商店 对话框
 */
public class ShoreFragment extends DialogFragment {


    @BindView(R.id.shore_query_number)
    TextView mShoreQueryNumber;
    @BindView(R.id.shore_refresh_number)
    TextView mShoreRefreshNumber;
    @BindView(R.id.shore_bomb_number)
    TextView mShoreBombNumber;
    @BindView(R.id.shore_freeze_number)
    TextView mShoreFreezeNumber;
    Unbinder unbinder;

    private AppCompatActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.alerlog_item_shore, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void cut(TextView textView) {
        int number = Integer.parseInt(textView.getText().toString().trim());
        if (number == 0) {

        } else {
            number--;
        }
        textView.setText(number + "");
    }

    private void add(TextView textView) {
        int number = Integer.parseInt(textView.getText().toString().trim());
        number++;
        textView.setText(number + "");
    }

    @OnClick({R.id.shore_query_cut, R.id.shore_query_add, R.id.shore_refresh_cut, R.id.shore_refresh_add, R.id.shore_bomb_cut, R.id.shore_bomb_add, R.id.shore_freeze_cut, R.id.shore_freeze_add, R.id.shore_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shore_query_cut:
                cut(mShoreQueryNumber);
                break;
            case R.id.shore_query_add:
                add(mShoreQueryNumber);
                break;
            case R.id.shore_refresh_cut:
                cut(mShoreRefreshNumber);
                break;
            case R.id.shore_refresh_add:
                add(mShoreRefreshNumber);
                break;
            case R.id.shore_bomb_cut:
                cut(mShoreBombNumber);
                break;
            case R.id.shore_bomb_add:
                add(mShoreBombNumber);
                break;
            case R.id.shore_freeze_cut:
                cut(mShoreFreezeNumber);
                break;
            case R.id.shore_freeze_add:
                add(mShoreFreezeNumber);
                break;
            case R.id.shore_pay:
                pay();
                break;
        }
    }

    private int getNumber(TextView textView) {
        int number = Integer.parseInt(textView.getText().toString().trim());
        return number;
    }

    private void pay() {
        int number_query = getNumber(mShoreQueryNumber);
        int number_refresh = getNumber(mShoreRefreshNumber);
        int number_bomb = getNumber(mShoreBombNumber);
        int number_freeze = getNumber(mShoreFreezeNumber);
        int sum = (number_query + number_refresh + number_bomb + number_freeze) * 10;

        int blueMoeny = Integer.parseInt((String) SPUtils.get(Config.BLUE_DIAMOND, "0"));
        if (blueMoeny < sum) { //钻石不足，提示去充值
            dismiss();
            HintFragment hintFragment = HintFragment.newInstance(1);
            hintFragment.show(mActivity.getSupportFragmentManager(), "1");
        } else {
            if (sum == 0) {
                MatchToast.showToast(mActivity, "没有选择商品");
            } else {
                String id = (String) SPUtils.get(Config.ID, "");
                ArrayList<PropEntry.TotleBean> list = new ArrayList<>();
                PropEntry.TotleBean query = new PropEntry.TotleBean("4", number_query + "");
                PropEntry.TotleBean refresh = new PropEntry.TotleBean("1", number_refresh + "");
                PropEntry.TotleBean bomb = new PropEntry.TotleBean("2", number_bomb + "");
                PropEntry.TotleBean freeze = new PropEntry.TotleBean("3", number_freeze + "");
                list.add(query);
                list.add(refresh);
                list.add(bomb);
                list.add(freeze);
                PropEntry propEntry = new PropEntry(sum + "", id, list);
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(propEntry);
                ApiService.GET_SERVICE(Api.BUY, jsonObject.toJSONString(), new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject json) {
                        dismiss();
                        JSONObject jsonObject = json.getJSONObject("data");
                        LoginInfo loginInfo = JSON.parseObject(JSON.toJSONString(jsonObject), LoginInfo.class);
                        SPUtils.put(Config.HAI_MONEY, loginInfo.getAmount());
                        SPUtils.put(Config.BLUE_DIAMOND, loginInfo.getDiamonds());
                        SPUtils.put(Config.HELP, loginInfo.getPrompt());
                        SPUtils.put(Config.REFRESH, loginInfo.getReset());
                        SPUtils.put(Config.FREEZE, loginInfo.getFrozen());
                        SPUtils.put(Config.BOMB, loginInfo.getBomb());
                        EventBus.getDefault().post(new MessageEvent("数据更新完成，", MessageEvent.SHORE));
                        HintFragment hintFragment = HintFragment.newInstance(4);
                        hintFragment.show(mActivity.getSupportFragmentManager(), "4");
                    }

                    @Override
                    public void requestFailure(int code, String msg) {
                        MatchToast.showToast(mActivity, msg);

                    }
                });
            }
        }
    }
}
