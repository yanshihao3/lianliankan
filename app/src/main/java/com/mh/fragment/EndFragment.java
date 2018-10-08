package com.mh.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mh.match.InfiniteActivity;
import com.mh.match.R;
import com.mh.view.InfiniteView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EndFragment extends DialogFragment {


    @BindView(R.id.end_play_socal)
    TextView mEndPlaySocal;
    @BindView(R.id.end_play_again)
    ImageView mEndPlayAgain;
    @BindView(R.id.end_play_end)
    ImageView mEndPlayEnd;
    Unbinder unbinder;

    private InfiniteView mGameView;

    private InfiniteActivity mActivity;

    @SuppressLint("ValidFragment")
    public EndFragment(InfiniteView gameView) {
        this.mGameView = gameView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (InfiniteActivity) context;
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
        View view = inflater.inflate(R.layout.fragment_end, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEndPlaySocal.setText("得分 ："+mGameView.getScore()+"");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.end_play_again, R.id.end_play_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.end_play_again: //再来一句
                dismiss();
                mGameView.replay();
                break;
            case R.id.end_play_end: //结束游戏
                dismiss();
                mActivity.exit();
                break;
        }
    }
}
