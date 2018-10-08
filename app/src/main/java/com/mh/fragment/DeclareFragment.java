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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mh.match.BaseActivity;
import com.mh.match.InfiniteActivity;
import com.mh.match.PlayActivity;
import com.mh.match.R;
import com.mh.view.BoardView;
import com.mh.view.GameView;
import com.mh.view.InfiniteView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * 游戏说明对话框
 */
@SuppressLint("ValidFragment")
public class DeclareFragment extends DialogFragment {


    @BindView(R.id.declare_play)
    ImageButton mDeclarePlay;
    @BindView(R.id.declare_prop)
    ImageButton mDeclareProp;
    @BindView(R.id.declare_play_ll)
    LinearLayout mDeclarePlayLl;
    @BindView(R.id.declare_prop_ll)
    LinearLayout mDeclarePropLl;
    Unbinder unbinder;

    private BoardView mGameView;

    private BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @SuppressLint("ValidFragment")
    public DeclareFragment(BoardView gameView) {
        mGameView = gameView;
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
        View view = inflater.inflate(R.layout.fragment_declare, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        mDeclarePlay.setImageResource(R.mipmap.declare_play_selcet_on);
        mDeclareProp.setImageResource(R.mipmap.declare_prop_selcet);
        mDeclarePlayLl.setVisibility(View.VISIBLE);
        mDeclarePropLl.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.declare_play, R.id.declare_prop, R.id.declare_btn_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.declare_play:
                mDeclarePlay.setImageResource(R.mipmap.declare_play_selcet_on);
                mDeclareProp.setImageResource(R.mipmap.declare_prop_selcet);
                mDeclarePlayLl.setVisibility(View.VISIBLE);
                mDeclarePropLl.setVisibility(View.GONE);
                break;
            case R.id.declare_prop:
                mDeclarePlay.setImageResource(R.mipmap.declare_play_selcet);
                mDeclareProp.setImageResource(R.mipmap.declare_prop_selcet_on);
                mDeclarePlayLl.setVisibility(View.GONE);
                mDeclarePropLl.setVisibility(View.VISIBLE);
                break;
            case R.id.declare_btn_play:
                dismiss();
                if (mActivity instanceof PlayActivity) {
                    PlayActivity playActivity = (PlayActivity) mActivity;
                    playActivity.stopInitMusic();
                } else if (mActivity instanceof InfiniteActivity) {
                    InfiniteActivity infiniteActivity = (InfiniteActivity) mActivity;
                    infiniteActivity.stopInitMusic();
                }
                if (mGameView instanceof GameView) {
                    GameView gameView = (GameView) mGameView;
                    gameView.start();
                } else if (mGameView instanceof InfiniteView) {
                    InfiniteView infiniteView = (InfiniteView) mGameView;
                    infiniteView.start();
                }
                break;
        }
    }
}
