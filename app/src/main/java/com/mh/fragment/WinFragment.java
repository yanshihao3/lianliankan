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
import android.widget.TextView;

import com.mh.match.PlayActivity;
import com.mh.match.R;
import com.mh.view.GameView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 胜利弹窗
 */
@SuppressLint("ValidFragment")
public class WinFragment extends DialogFragment {


    @BindView(R.id.win_play_socal)
    TextView mWinPlaySocal;
    Unbinder unbinder;

    private GameView mGameView;

    private PlayActivity mPlayActivity;

    @SuppressLint("ValidFragment")
    public WinFragment(GameView gameView) {
        this.mGameView = gameView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPlayActivity = (PlayActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_win, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mWinPlaySocal.setText("得分 ：" + mGameView.getScore());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.win_play_next, R.id.win_play_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.win_play_next:
                dismiss();
                mGameView.setScore(0);
                mGameView.next();
                mPlayActivity.setPlayCustoms();

                Log.e("win", "totalTime"+mGameView.getTotalTime());
                break;
            case R.id.win_play_level:
                dismiss();
                mPlayActivity.exit();
                break;
        }
    }
}
