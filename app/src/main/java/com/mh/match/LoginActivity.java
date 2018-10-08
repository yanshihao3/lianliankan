package com.mh.match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mh.MyApp;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.fragment.VerListener;
import com.mh.fragment.VerificationFragment;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.utils.CaptchaTimeCount;
import com.mh.utils.CommonUtil;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;
import com.mh.utils.ToastUtils;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements VerListener {

    @BindView(R.id.login_btn_code)
    Button mBtnCode;
    @BindView(R.id.login_et_phone)
    EditText mEtPhone;
    @BindView(R.id.login_et_code)
    EditText mLoginEtCode;
    @BindView(R.id.login_code_rl)
    RelativeLayout mCodeRl;
    @BindView(R.id.login_btn)
    Button mLoginBtn;

    private String mPhone;

    private CaptchaTimeCount mCaptchaTimeCount;

    private KProgressHUD hud;

    private boolean isFrist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mCaptchaTimeCount = new CaptchaTimeCount(60000, 1000, mBtnCode, this);
        mCodeRl.setVisibility(View.GONE);
        mLoginBtn.setVisibility(View.GONE);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }

    @OnClick({R.id.login_btn_code, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_code: //获取验证码
                getCode();
                break;
            case R.id.login_btn: //登录
                login();
                break;
        }
    }

    private void login() {
        hud.show();
        mPhone = mEtPhone.getText().toString().trim();
        String code = mLoginEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            mEtPhone.requestFocus();
            MatchToast.showToast(MyApp.getApplication(), "手机号为空");
            return;
        }
        if (mPhone.length() != 11 || !mPhone
                .matches("^((13)|(14)|(15)|(17)|(18))\\d{9}$")) {
            mEtPhone.requestFocus();
            ToastUtils.showToast("手机号格式不对");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            mLoginEtCode.requestFocus();
            MatchToast.showToast(MyApp.getApplication(), "请输入验证码");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("phone", mPhone);
        params.put("code", code);
        ApiService.GET_SERVICE(Api.LOGIN.CHECKCODE, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                hud.dismiss();
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    LoginInfo loginInfo = JSON.parseObject(JSON.toJSONString(jsonObject), LoginInfo.class);
                    SPUtils.put(Config.IS_LOGIN, true);
                    SPUtils.put(Config.PHONE, loginInfo.getMobile());
                    SPUtils.put(Config.ID, loginInfo.getId());
                    SPUtils.put(Config.NAME, loginInfo.getUser_name());
                    SPUtils.put(Config.HAI_MONEY, loginInfo.getAmount());
                    SPUtils.put(Config.BLUE_DIAMOND, loginInfo.getDiamonds());
                    SPUtils.put(Config.HELP, loginInfo.getPrompt());
                    SPUtils.put(Config.REFRESH, loginInfo.getReset());
                    SPUtils.put(Config.FREEZE, loginInfo.getFrozen());
                    SPUtils.put(Config.BOMB, loginInfo.getBomb());
                    success();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                MatchToast.showToast(MyApp.getApplication(), msg);
                hud.dismiss();
            }
        });
    }

    public void getCode() {
        String phone = (String) SPUtils.get(Config.PHONE, "");
        if (TextUtils.isEmpty(phone)) { //第一次进来
            mPhone = mEtPhone.getText().toString();
            boolean b = CommonUtil.checkPhone(mPhone, true);
            if (b) { //发送验证码
                isFrist = true;
                notSendCode();

            }
        } else {// 有记录，不需要验证码
            mPhone = mEtPhone.getText().toString();
            boolean b = CommonUtil.checkPhone(mPhone, true);
            if (b) {
                if (phone.equals(mPhone)) { //同一个用户   不发送验证码
                    isFrist=false;
                    notSendCode();
                } else { //不同一个用户  发送验证码
                    isFrist = true;
                    notSendCode();
                }
            }
        }
    }


    private void notSendCode() { //不发送验证码
        VerificationFragment verification = new VerificationFragment();
        verification.show(getSupportFragmentManager(), "ver");
    }

    private void sendCode(String phone) { //发送验证码
        isFrist=false;
        mCaptchaTimeCount.start();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        ApiService.GET_SERVICE(Api.LOGIN.CODE, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                try {
                    JSONObject date = json.getJSONObject("data");
                    String isSucess = date.getString("isSucess");
                    if ("1".equals(isSucess)) { //验证码发送成功
                        mLoginBtn.setVisibility(View.VISIBLE);
                        mCodeRl.setVisibility(View.VISIBLE);
                    } else {
                        MatchToast.showToast(MyApp.getApplication(), "验证码发送失败，请稍后重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                MatchToast.showToast(MyApp.getApplication(), msg);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void success() { //登录成功
        mPhone = mEtPhone.getText().toString();
        if (isFrist) {
            sendCode(mPhone);
        }else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
