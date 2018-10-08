package match.mh.audio;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {


    // 定义SoundPool 对象
    private SoundPool soundPool;

    // 定义HASH表
    private HashMap<Integer, Integer> soundPoolMap;

    /**
     * 初始化声音系统
     *
     * @param context 上下文
     */
    @SuppressLint("UseSparseArrays")
    public void initSounds(Context context) {
        // 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);

        // 初始化HASH表
        soundPoolMap = new HashMap<Integer, Integer>();

    }

    /**
     * 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
     *
     * @param context 上下文
     * @param raw     资源ID
     * @param ID      指定的ID
     */
    public void loadSfx(Context context, int raw, int ID) {
        soundPoolMap.put(ID, soundPool.load(context, raw, 1));
    }

    /**
     * 播放音乐片段
     *
     * @param sound 要播放的音效的ID
     * @param uLoop 循环的次数
     */
    public void play(int sound, int uLoop) {
        soundPool.play(soundPoolMap.get(sound), 1, 1, 1,
                uLoop, 1);
    }
}