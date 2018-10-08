package com.mh.view;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import match.mh.audio.SoundPlayer;

import com.mh.MyApp;
import com.mh.match.R;
import com.mh.utils.MatchToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class GameView extends BoardView {


    private static final int DRAW_LINKLINE = 1; //画线
    private static final int REFRESH_VIEW = 2;  //刷新view
    private static final int FREEZE = 3;  //冻结3秒
    private static final int HELP_NOT_DELETE = 4;  //显示不清除

    public static final int WIN = 1;
    public static final int LOSE = 2;
    public static final int PAUSE = 3;
    public static final int PLAY = 4;
    public static final int QUIT = 5;

    private int mScore = 0; //游戏分数

    /**
     * 打乱 刷新
     */
    private int disruptLeftNum;

    /**
     * 炸弹
     */
    private int tipLeftNum;

    /**
     * 帮助
     */
    private int hlepLeftNum;
    /**
     * 冻结次数
     */
    private int freezeLeftNum;
    /**
     * 第一关游戏总时间，为100s
     */
    private int totalTime = 100;
    private int leftTime;

    private int level = 0; //关卡

    public int getDisruptLeftNum() {
        return disruptLeftNum;
    }

    public void setDisruptLeftNum(int disruptLeftNum) {
        this.disruptLeftNum = disruptLeftNum;
    }

    public int getTipLeftNum() {
        return tipLeftNum;
    }

    public void setTipLeftNum(int tipLeftNum) {
        this.tipLeftNum = tipLeftNum;
    }

    public int getHlepLeftNum() {
        return hlepLeftNum;
    }

    public void setHlepLeftNum(int hlepLeftNum) {
        this.hlepLeftNum = hlepLeftNum;
    }

    public int getFreezeLeftNum() {
        return freezeLeftNum;
    }

    public void setFreezeLeftNum(int freezeLeftNum) {
        this.freezeLeftNum = freezeLeftNum;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getScore() {
        return mScore;
    }


    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }


    private static final int ID_SOUND_CHOOSE = 0;
    private static final int ID_SOUND_DISAPEAR = 1;
    private static final int ID_SOUND_WIN = 4;
    private static final int ID_SOUND_LOSE = 5;
    private static final int ID_SOUND_DISRUPT = 6;
    private static final int ID_SOUND_TIP = 7;
    private static final int ID_SOUND_ERROR = 8;


    public SoundPlayer soundPlayer;
    public MediaPlayer mpBGMusic;
    private RefreshHandler refreshHandler = new RefreshHandler();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (!isStop) {
                if (leftTime >= 0) {
                    timeChangeListener.OnTimeChange(leftTime);
                    leftTime--;
                } else {
                    isStop = true;
                    mpBGMusic.stop();
                    soundPlayer.play(ID_SOUND_LOSE, 0);
                    setMode(LOSE);
                }
            }
        }
    };

    /**
     * 用来停止计时器的线程
     */
    private boolean isStop;

    private OnTimeChangeListener timeChangeListener = null;
    private OnStateChangeListener stateChangeListener = null;
    private OnToolsChangeListener toolsChangeListener = null;

    public void setOnTimeChangeListener(OnTimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void setOnToolsChangeListener(OnToolsChangeListener toolsChangeListener) {
        this.toolsChangeListener = toolsChangeListener;
    }

    /**
     *
     * @param context
     * @param width
     * @param disruptLeftNum     刷新
     * @param tipLeftNum
     * @param hlepLeftNum
     * @param freezeLeftNum
     * @param level
     */
    public GameView(Context context, int width,
                    int disruptLeftNum,
                    int tipLeftNum,
                    int hlepLeftNum,
                    int freezeLeftNum,
                    int level) {
        super(context, width);
        initSound(context);
        this.disruptLeftNum = disruptLeftNum;
        this.tipLeftNum = tipLeftNum;
        this.hlepLeftNum = hlepLeftNum;
        this.freezeLeftNum = freezeLeftNum;
        this.level=level;
        this.totalTime=100-level*10;
    }

    @SuppressLint("HandlerLeak")
    class RefreshHandler extends Handler {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DRAW_LINKLINE) {
                drawLinkLine(selected.get(0), selected.get(1));
                GameView.this.invalidate();

                this.removeMessages(0);
                Message message = Message.obtain();
                message.what = REFRESH_VIEW;
                sendMessageDelayed(message, 100);
                return;

            }
            if (msg.what == REFRESH_VIEW) {
                mScore = mScore + 10;
                map[selected.get(0).y][selected.get(0).x] = BLANK;
                map[selected.get(1).y][selected.get(1).x] = BLANK;
                selected.clear();
                soundPlayer.play(ID_SOUND_DISAPEAR, 0);
                drawAllIcons();
                GameView.this.invalidate();
                if (win()) {
                    mpBGMusic.stop();
                    soundPlayer.play(ID_SOUND_WIN, 0);
                    setMode(WIN);
                    isStop = true;
                }
                return;
            }
            if (msg.what == FREEZE) {
                resume();
                return;
            }
            if (msg.what == HELP_NOT_DELETE) {
                selected.clear();
                drawAllIcons();
                GameView.this.invalidate();
                return;
            }
        }

        public void sleep(int delayTime, int type) {
            this.removeMessages(0);
            Message message = Message.obtain();
            message.what = type;
            sendMessageDelayed(message, delayTime);
        }
    }

    public void initSound(Context context) {
        soundPlayer = new SoundPlayer();
        soundPlayer.initSounds(context);
        soundPlayer.loadSfx(context, R.raw.choose, ID_SOUND_CHOOSE);
        soundPlayer.loadSfx(context, R.raw.disappear, ID_SOUND_DISAPEAR);
        soundPlayer.loadSfx(context, R.raw.win, ID_SOUND_WIN);
        soundPlayer.loadSfx(context, R.raw.lose, ID_SOUND_LOSE);
        soundPlayer.loadSfx(context, R.raw.disrupt, ID_SOUND_DISRUPT);
        soundPlayer.loadSfx(context, R.raw.tip, ID_SOUND_TIP);
        soundPlayer.loadSfx(context, R.raw.alarm, ID_SOUND_ERROR);

        mpBGMusic = MediaPlayer.create(context, R.raw.game_bg);
    }

    /**
     * 设置游戏模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.stateChangeListener.OnStateChange(mode);
    }

    /**
     * 开始游戏
     */
    public void start() {
//        initMap();
//        drawAllIcons();
        mpBGMusic.setLooping(true);
        mpBGMusic.start();
        leftTime = totalTime;
        timer.schedule(task, 200, 1000);    //延迟200ms执行，1s执行一次，循环执行得用三个参数的
    }

    /**
     * 重玩游戏
     */
    public void replay() {
        initGameImg();
        restartBGMusic();
        reset();
    }

    /**
     * 游戏暂停
     */
    public void pause() {
        mpBGMusic.pause();
        isStop = true;
    }

    /**
     * 游戏继续
     */
    public void resume() {
        mpBGMusic.start();
        isStop = false;
    }

    /**
     * 下一关游戏，时间减去10秒
     */
    public void next() {
        if (level>=8){ //所有游戏全部完成
            MatchToast.showToast(MyApp.getApplication(),"所有游戏全部通过，游戏胜利");
        }else {
            initGameImg();
            restartBGMusic();
            totalTime -= 10;
            level++;
            reset();
        }
    }

    /**
     * 重设游戏设置
     */
    private void reset() {
        leftTime = totalTime;
        isStop = false;
    }

    /**
     * 初始化游戏界面
     */
    public void initGameImg() {
        initMap();
        drawAllIcons();
        this.invalidate();
    }

    /**
     * 退出游戏
     */
    public void exit() {
        refreshHandler.removeCallbacksAndMessages(null);
        mpBGMusic.release();
        timer.cancel();
    }

    /**
     * 重新启动背景音乐
     */
    public void restartBGMusic() {
        try {
            mpBGMusic.prepare();
        } catch (IllegalStateException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        mpBGMusic.seekTo(0);
        mpBGMusic.start();
    }

    /**
     * 初始化游戏内部数组地图
     */
    public void initMap() {
        for (int i = 0; i < yCount; i++) {
            for (int j = 0; j < xCount; j++) {
                map[i][j] = j / 4 + i * xCount / 4;
            }
        }
        change();
    }


    /**
     * 判断游戏是否结束
     *
     * @return
     */
    private boolean win() {
        for (int i = 0; i < xCount; i++) {
            for (int j = 1; j < yCount; j++) {
                if (map[i][j] != BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 外部方法，打乱水果顺序
     */
    public void disrupt() {
        if (disruptLeftNum == 0) {
            toolsChangeListener.onDisruptChange(disruptLeftNum, OnToolsChangeListener.TOOL_USED_UP);
            soundPlayer.play(ID_SOUND_ERROR, 0);
            return;
        } else {
            soundPlayer.play(ID_SOUND_DISRUPT, 0);
            disruptLeftNum--;
            toolsChangeListener.onDisruptChange(disruptLeftNum, OnToolsChangeListener.TOOL_SUCCESS);
            change();
            selected.clear();
            drawAllIcons();
            this.invalidate();
        }
    }

    /**
     * 外部方法， 冻结
     */
    public void freeze() {
        if (freezeLeftNum == 0) {
            soundPlayer.play(ID_SOUND_ERROR, 0);
            toolsChangeListener.onFreeze(freezeLeftNum, OnToolsChangeListener.TOOL_USED_UP);
        } else {
            freezeLeftNum--;
            soundPlayer.play(ID_SOUND_TIP, 0);
            pause();
            refreshHandler.sleep(3000, FREEZE);
            toolsChangeListener.onFreeze(freezeLeftNum, OnToolsChangeListener.TOOL_SUCCESS);
        }
    }

    /**
     * 外部方法，智能提示 消除
     */
    public void helpAndDelete() {
        if (tipLeftNum == 0) {
            soundPlayer.play(ID_SOUND_ERROR, 0);
            toolsChangeListener.onTipChange(tipLeftNum, OnToolsChangeListener.TOOL_USED_UP);
            return;
        } else {
            selected.clear();    //清除已选中的图标
            tipLeftNum--;
            if (search()) {
                soundPlayer.play(ID_SOUND_TIP, 0);
                Point p1 = selected.get(0);
                Point p2 = selected.get(1);
                zommInIcon(p1.x, p1.y);
                zommInIcon(p2.x, p2.y);
                this.invalidate();
                refreshHandler.sleep(100, DRAW_LINKLINE);
                toolsChangeListener.onTipChange(tipLeftNum, OnToolsChangeListener.TOOL_SUCCESS);
            } else {
                toolsChangeListener.onTipChange(tipLeftNum, OnToolsChangeListener.TOOL_FAIL);
            }
        }
    }

    /**
     * 外部方法，智能提示不消除
     */
    public void help() {
        if (hlepLeftNum == 0) {
            soundPlayer.play(ID_SOUND_ERROR, 0);
            toolsChangeListener.onChange(hlepLeftNum, OnToolsChangeListener.TOOL_USED_UP);
            return;
        } else {
            selected.clear();    //清除已选中的图标
            hlepLeftNum--;
            if (search()) {
                soundPlayer.play(ID_SOUND_TIP, 0);
                Point p1 = selected.get(0);
                Point p2 = selected.get(1);
                zommInIcon(p1.x, p1.y);
                zommInIcon(p2.x, p2.y);
                this.invalidate();
                refreshHandler.sleep(2000, HELP_NOT_DELETE);
                toolsChangeListener.onChange(hlepLeftNum, OnToolsChangeListener.TOOL_SUCCESS);
            } else {
                toolsChangeListener.onChange(hlepLeftNum, OnToolsChangeListener.TOOL_FAIL);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (isEffectiveArea(x, y)) {
            Point p = screenToIndex(x, y);
            if (map[p.y][p.x] >= 0) {
                if (selected.size() == 0) {
                    selected.add(p);
                    zommInIcon(p.x, p.y);
                    soundPlayer.play(ID_SOUND_CHOOSE, 0);
                    this.invalidate();
                } else if (selected.size() == 1) {
                    Point p1 = selected.get(0);
                    Point p2 = p;
                    if (p1.x == p2.x && p1.y == p2.y) {
                        selected.clear();
                        zoomOutIcon();
                        soundPlayer.play(ID_SOUND_CHOOSE, 0);
                        this.invalidate();
                    } else {
                        if (isLink(p1, p2)) {
                            selected.add(p2);
                            zommInIcon(p2.x, p2.y);
                            soundPlayer.play(ID_SOUND_CHOOSE, 0);
                            refreshHandler.sleep(100, DRAW_LINKLINE);
                            this.invalidate();
                        } else {
                            selected.clear();
                            selected.add(p2);
                            zoomOutIcon();
                            zommInIcon(p2.x, p2.y);
                            soundPlayer.play(ID_SOUND_CHOOSE, 0);
                            this.invalidate();
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
