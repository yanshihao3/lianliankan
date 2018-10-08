package com.mh.match;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.fragment.ShoreFragment;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_type_classics)
    ImageView mClassics;
    @BindView(R.id.home_type_infinite)
    ImageView mInfinite;
    @BindView(R.id.home_store)
    ImageView mStore;
    @BindView(R.id.home_sound)
    ImageView mSound;
    @BindView(R.id.home_my)
    ImageView mMy;

    private MediaPlayer mpInitMusic;
    private boolean isSound = true;//音效是否打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        isSound = (boolean) SPUtils.get(Config.IS_SOUND, false);
        mpInitMusic = MediaPlayer.create(this, R.raw.init_bg);
        mpInitMusic.setLooping(true);
        if (isSound) {
            mpInitMusic.start();
            mSound.setImageResource(R.mipmap.home_sound);
        } else {
            mSound.setImageResource(R.mipmap.home_sound_on);
        }
        initData();
    }

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", (String) SPUtils.get(Config.ID, ""));
        ApiService.GET_SERVICE(Api.QUERY, params, new OnRequestDataListener() {
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
            }

            @Override
            public void requestFailure(int code, String msg) {
                MatchToast.showToast(HomeActivity.this, "网络异常");
            }
        });
    }

    @OnClick({R.id.home_type_classics, R.id.home_type_infinite, R.id.home_store, R.id.home_sound, R.id.home_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_type_classics: //经典
                startActivity(new Intent(HomeActivity.this, LevelActivity.class));
                break;
            case R.id.home_type_infinite: //无限
                startActivity(new Intent(HomeActivity.this, InfiniteActivity.class));
                break;
            case R.id.home_store://商店
                showStore();
                break;
            case R.id.home_sound: //声音
                if (!isSound) {
                    mSound.setImageResource(R.mipmap.home_sound);
                    mpInitMusic.start();
                    isSound = true;
                    SPUtils.put(Config.IS_SOUND, true);
                } else {
                    mSound.setImageResource(R.mipmap.home_sound_on);
                    mpInitMusic.pause();
                    isSound = false;
                    SPUtils.put(Config.IS_SOUND, false);
                }
                break;
            case R.id.home_my:
                Intent intent = new Intent(HomeActivity.this, MyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showStore() {
        ShoreFragment shoreFragment = new ShoreFragment();
        shoreFragment.show(getSupportFragmentManager(), "shore");
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isSound) {
            mpInitMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSound) {
            mpInitMusic.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpInitMusic.release();
    }
    long exitTime=0L;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
