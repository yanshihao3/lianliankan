package com.mh.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mh.match.PayActivity;
import com.mh.match.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 提示的对话框
 */
public class HintFragment extends DialogFragment {


    @BindView(R.id.fragment_hint_title)
    TextView mFragmentHintTitle;
    @BindView(R.id.fragment_hint_image)
    ImageView mFragmentHintImage;
    Unbinder unbinder;
    @BindView(R.id.fragment_hint_pay)
    ImageView mFragmentHintPay;

    private int mType;

    private AppCompatActivity mActivity;

    public static HintFragment newInstance(int type) {
        HintFragment hintFragment = new HintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        hintFragment.setArguments(bundle);
        return hintFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hint, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        if (mType == 0) { // 嗨币不足
            mFragmentHintTitle.setText("您的Hi币不足\n请前往充值");
            mFragmentHintImage.setImageResource(R.mipmap.iv_break_game);
            mFragmentHintPay.setVisibility(View.VISIBLE);
        }
        if (mType == 1) { //蓝钻不足
            mFragmentHintTitle.setText("您的蓝钻不足\n请前往充值");
            mFragmentHintImage.setImageResource(R.mipmap.iv_break_game);
            mFragmentHintPay.setVisibility(View.VISIBLE);
            mFragmentHintPay.setImageResource(R.mipmap.pay_pay);
        }
        if (mType == 2) { //充值嗨币成功
            mFragmentHintTitle.setText("充值成功,\n可以兑换蓝钻");
            mFragmentHintImage.setImageResource(R.mipmap.iv_sure);
            mFragmentHintPay.setVisibility(View.GONE);
        }
        if (mType == 3) { //兑换蓝钻成功
            mFragmentHintTitle.setText("兑换蓝钻成功\n可以购买道具");
            mFragmentHintImage.setImageResource(R.mipmap.iv_sure);
            mFragmentHintPay.setVisibility(View.GONE);
        }
        if (mType == 4) { //成功购买道具
            mFragmentHintTitle.setText("恭喜您购买成功");
            mFragmentHintImage.setImageResource(R.mipmap.iv_sure);
            mFragmentHintPay.setVisibility(View.GONE);
        }
        if (mType == 5) {
            mFragmentHintTitle.setText("你的道具不足\n请前往商店购买");
            mFragmentHintImage.setImageResource(R.mipmap.iv_break_game);
            mFragmentHintPay.setVisibility(View.VISIBLE);
            mFragmentHintPay.setImageResource(R.mipmap.pay_pay);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragment_hint_image,R.id.fragment_hint_pay})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.fragment_hint_image:
                dismiss();
                break;
            case R.id.fragment_hint_pay:
                dismiss();
                if (mType==0){
                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("type", 0);
                    startActivity(intent);
                }
                if (mType==1){
                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
                if (mType==5){
                    ShoreFragment shoreFragment = new ShoreFragment();
                    shoreFragment.show(mActivity.getSupportFragmentManager(), "hint_shore");
                }

                break;
        }
    }


}
