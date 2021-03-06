package com.mh.match;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.mh.entry.Config;
import com.mh.entry.LoginInfo;
import com.mh.entry.MessageEvent;
import com.mh.fragment.DeclareFragment;
import com.mh.fragment.HintFragment;
import com.mh.fragment.LoseFragment;
import com.mh.fragment.WinFragment;
import com.mh.net.Api;
import com.mh.net.ApiService;
import com.mh.net.OnRequestDataListener;
import com.mh.utils.MatchToast;
import com.mh.utils.SPUtils;
import com.mh.view.GameView;
import com.mh.view.OnStateChangeListener;
import com.mh.view.OnTimeChangeListener;
import com.mh.view.OnToolsChangeListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/14 下午4:05
 * - @Email whynightcode@gmail.com
 */
public class PlayActivity extends BaseActivity implements OnStateChangeListener
        , OnTimeChangeListener, OnToolsChangeListener {
    @BindView(R.id.play_time)
    ProgressBar mPlayTime;
    @BindView(R.id.play_customs)
    TextView mPlayCustoms;
    @BindView(R.id.play_refresh)
    ImageView mPlayRefresh;
    @BindView(R.id.play_refresh_number)
    TextView mPlayRefreshNumber;
    @BindView(R.id.play_query)
    ImageView mPlayQuery;
    @BindView(R.id.play_query_number)
    TextView mPlayQueryNumber;
    @BindView(R.id.play_bomb)
    ImageView mPlayBomb;
    @BindView(R.id.play_bomb_number)
    TextView mPlayBombNumber;
    @BindView(R.id.play_freeze)
    ImageView mPlayFreeze;
    @BindView(R.id.play_freeze_number)
    TextView mPlayFreezeNumber;
    private GameView mGameView;
    int time = 100;

    boolean isStarted = false;        //判断游戏是否开始
    boolean isStoped = false;        //判断游戏是否结束

    private MediaPlayer mpInitMusic;

    private int mLevel; //关卡

    private int mRefresh; //刷新次数

    private int mBomb; //炸弹次数

    private int mFreeze; //冻结次数

    private int mHelp; //帮助次数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(addGameView());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        setTotalNum();
    }

    /**
     * 设置界面上打乱工具的总次数，外部方法
     */
    public void setTotalNum() {
        mPlayRefreshNumber.setText(mRefresh + "");
        mPlayBombNumber.setText(mBomb + "");
        mPlayQueryNumber.setText(mHelp + "");
        mPlayFreezeNumber.setText(mFreeze + "");
        setPlayCustoms();
    }

    public void setPlayCustoms() {
        mPlayTime.setMax(mGameView.getTotalTime());
        mPlayTime.setProgress(mGameView.getTotalTime());
        int level = mGameView.getLevel();
        String s = "一";
        switch (level) {
            case 0:
                s = "一";
                break;
            case 1:
                s = "二";
                break;
            case 2:
                s = "三";
                break;
            case 3:
                s = "四";
                break;
            case 4:
                s = "五";
                break;
            case 5:
                s = "六";
                break;
            case 6:
                s = "七";
                break;
            case 7:
                s = "八";
                break;
            case 8:
                s = "九";
                break;
        }
        mPlayCustoms.setText("第" + s + "关");
    }

    private RelativeLayout addGameView() {
        Intent intent = getIntent();
        mLevel = intent.getIntExtra("level", 0);
        getTotalNum();
        final LayoutInflater inflater = LayoutInflater.from(PlayActivity.this);
        // 获取需要被添加控件的布局
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_play, null);
        int screeenWidth = getScreenWidth();
        int minus = 10;
        int width = screeenWidth - 2 * minus;
        mGameView = new GameView(this, width, mRefresh, mBomb, mHelp, mFreeze, mLevel);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.BELOW, R.id.play_customs_rl);
        lp.setMargins(0, 0, 0, 0);
        mGameView.setLayoutParams(lp);
        layout.addView(mGameView);
        return layout;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(MessageEvent event) {
        if (MessageEvent.SHORE == event.getType()) {
            getTotalNum();
            mPlayRefreshNumber.setText(mRefresh + "");
            mPlayBombNumber.setText(mBomb + "");
            mPlayQueryNumber.setText(mHelp + "");
            mPlayFreezeNumber.setText(mFreeze + "");
            mGameView.setDisruptLeftNum(mRefresh);
            mGameView.setFreezeLeftNum(mFreeze);
            mGameView.setHlepLeftNum(mHelp);
            mGameView.setTipLeftNum(mBomb);
        }
    }

    public void getTotalNum() {

        mHelp = Integer.parseInt((String) SPUtils.get(Config.HELP, ""));
        mRefresh = Integer.parseInt((String) SPUtils.get(Config.REFRESH, ""));
        mFreeze = Integer.parseInt((String) SPUtils.get(Config.FREEZE, ""));
        mBomb = Integer.parseInt((String) SPUtils.get(Config.BOMB, ""));
    }


    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void initView() {
        //播放刚开始的前导音乐
        mpInitMusic = MediaPlayer.create(PlayActivity.this, R.raw.init_bg);
        mpInitMusic.setLooping(true);
        mpInitMusic.start();

        mGameView.initGameImg();
        mGameView.setOnToolsChangeListener(this);
        mGameView.setOnStateChangeListener(this);
        mGameView.setOnTimeChangeListener(this);


        Log.e("play", "totalTime: " + mGameView.getTotalTime());
        DeclareFragment declareFragment = new DeclareFragment(mGameView);
        declareFragment.show(getSupportFragmentManager(), "declare");

    }

    //状态改变
    @Override
    public void OnStateChange(int StateMode) {
        switch (StateMode) {
            case GameView.WIN: //赢了 计算得分
                update();
                int pass = (int) SPUtils.get(Config.PASS, 0);
                int level = mGameView.getLevel();
                if (pass < level + 1) {
                    SPUtils.put(Config.PASS, mGameView.getLevel() + 1);
                }
                SPUtils.put(Config.SOCAL, mGameView.getScore());
                WinFragment winFragment = new WinFragment(mGameView);
                winFragment.show(getSupportFragmentManager(), "win");
                break;
            case GameView.LOSE: //失败 计算得分
                update();
                LoseFragment loseFragment = new LoseFragment(mGameView);
                loseFragment.show(getSupportFragmentManager(), "lose");
                break;
            case GameView.PAUSE:
                mGameView.pause();
                break;
            case GameView.QUIT:

                break;
            default:
                break;
        }
    }

    //计时改变
    @Override
    public void OnTimeChange(int leftTime) {
        mPlayTime.setProgress(leftTime);

    }

    @Override
    public void onDisruptChange(int leftNum, int state) {
        mPlayRefreshNumber.setText(String.valueOf(leftNum));
        if (state == TOOL_USED_UP) {
            HintFragment hintFragment = HintFragment.newInstance(5);
            hintFragment.show(getSupportFragmentManager(), "5");
        }
    }

    @Override
    public void onTipChange(int leftNum, int state) {
        mPlayBombNumber.setText(String.valueOf(leftNum));
        if (state == TOOL_USED_UP) {//弹出对话框 提示工具用完了，去购买
            HintFragment hintFragment = HintFragment.newInstance(5);
            hintFragment.show(getSupportFragmentManager(), "5");
        } else if (state == TOOL_FAIL) {
            MatchToast.showToast(PlayActivity.this, "智能查找失败!");
        }
    }

    @Override
    public void onChange(int leftNum, int state) {
        mPlayQueryNumber.setText(String.valueOf(leftNum));
        if (state == TOOL_USED_UP) {
            HintFragment hintFragment = HintFragment.newInstance(5);
            hintFragment.show(getSupportFragmentManager(), "5");
        } else if (state == TOOL_FAIL) {
            MatchToast.showToast(PlayActivity.this, "智能查找失败!");
        }
    }

    @Override
    public void onFreeze(int leftNum, int state) {
        mPlayFreezeNumber.setText(String.valueOf(leftNum));
        if (state == TOOL_USED_UP) {
            HintFragment hintFragment = HintFragment.newInstance(5);
            hintFragment.show(getSupportFragmentManager(), "5");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定残忍退出吗？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int arg) {
                    // TODO 自动生成的方法存根
                    dialog.cancel();
                }
            });

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg) {
                    update();
                    // TODO 自动生成的方法存根
                    PlayActivity.this.exit();
                }

            });
            builder.setCancelable(false).create().show();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 结束开始音乐，释放资源
     */
    public void stopInitMusic() {
        if (mpInitMusic != null && mpInitMusic.isPlaying()) {
            mpInitMusic.stop();
            mpInitMusic.release();
            mpInitMusic = null;
        }
        isStarted = true;
    }

    /**
     * 释放资源，停止线程
     */
    public void exit() {
        if (!isStarted) {
            stopInitMusic();
        } else {
            mGameView.exit();
        }
        isStoped = true;
        setResult(200);
        PlayActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @OnClick({R.id.play_refresh, R.id.play_query, R.id.play_bomb, R.id.play_freeze})
    public void onViewClicked(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        switch (view.getId()) {
            case R.id.play_refresh:
                mPlayRefresh.startAnimation(shake);
                mGameView.disrupt();
                break;
            case R.id.play_query:
                mPlayQuery.startAnimation(shake);
                mGameView.help();
                break;
            case R.id.play_bomb:
                mPlayBomb.startAnimation(shake);
                mGameView.helpAndDelete();
                break;
            case R.id.play_freeze:
                mPlayFreeze.startAnimation(shake);
                mGameView.freeze();
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO 自动生成的方法存根
        super.onPause();
        Log.e("play", "onPause: ");
        if (!isStoped) {
            if (!isStarted) {
                mpInitMusic.pause();
            } else {
                mGameView.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO 自动生成的方法存根
        super.onResume();
        Log.e("play", "onResume: ");
        if (!isStarted) {
            mpInitMusic.start();
        } else {
            mGameView.resume();
        }
    }

    private void update() {

        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("id", (String) SPUtils.get(Config.ID, ""));
        parmas.put("reset", mPlayRefreshNumber.getText().toString().trim());
        parmas.put("bomb", mPlayBombNumber.getText().toString().trim());
        parmas.put("frozen", mPlayFreezeNumber.getText().toString().trim());
        parmas.put("prompt", mPlayQueryNumber.getText().toString().trim());

        ApiService.GET_SERVICE(Api.UPDATA, JSON.toJSONString(parmas), new OnRequestDataListener() {
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
                EventBus.getDefault().post(new MessageEvent("数据更新完成，", MessageEvent.UPDATA));
            }

            @Override
            public void requestFailure(int code, String msg) {
                MatchToast.showToast(PlayActivity.this, msg);

            }
        });
    }
}
