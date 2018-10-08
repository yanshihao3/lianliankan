package com.mh.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mh.entry.Config;
import com.mh.match.PlayActivity;
import com.mh.match.R;
import com.mh.utils.SPUtils;
import com.mh.view.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 失败弹窗
 */
@SuppressLint("ValidFragment")
public class LoseFragment extends DialogFragment {


    @BindView(R.id.lose_play_socal)
    TextView mLosePlaySocal;
    @BindView(R.id.lose_play_level)
    ImageView mLosePlayLevel;
    @BindView(R.id.lose_play_reset)
    ImageView mLosePlayReset;
    Unbinder unbinder;
    private PlayActivity mPlayActivity;

    private GameView mGameView;

    @SuppressLint("ValidFragment")
    public LoseFragment(GameView gameView) {
        this.mGameView = gameView;
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlayActivity = (PlayActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_less, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        int score = mGameView.getScore();
        mLosePlaySocal.setText("得分 ："+score);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.lose_play_level, R.id.lose_play_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lose_play_level:
                dismiss();
                mPlayActivity.exit();
                break;
            case R.id.lose_play_reset:
                dismiss();
                mGameView.replay();
                Log.e("lose", "totaltime:"+mGameView.getTotalTime());
                break;
        }
    }
}
