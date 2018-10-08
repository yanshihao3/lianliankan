package com.mh.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mh.adapter.LevelAdapter;
import com.mh.entry.Config;
import com.mh.entry.MultipleItem;
import com.mh.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//关卡界面
public class LevelActivity extends BaseActivity {

    @BindView(R.id.level_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.level_blue)
    TextView mLevelBlue;

    private LevelAdapter mLevelAdapter;

    private List<MultipleItem> mItemList;

    private static final String TAG = "LevelActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
        initView();
        ininDate();
    }


    private void ininDate() {

        mLevelBlue.setText((String) SPUtils.get(Config.BLUE_DIAMOND, ""));
        mItemList.clear();
        int pass = (int) SPUtils.get(Config.PASS, 0);
        Log.e(TAG, "level: " + pass);
        MultipleItem multipleItem = null;
        for (int i = 0; i < 9; i++) {
            if (i < pass) {
                multipleItem = new MultipleItem(MultipleItem.PASS);
                multipleItem.setNumber((i + 1) + "");
                multipleItem.setTag(false);
            }
            if (i == pass) {
                multipleItem = new MultipleItem(MultipleItem.PASS);
                multipleItem.setNumber((i + 1) + "");
                multipleItem.setTag(true);
            }
            if (i > pass) {
                multipleItem = new MultipleItem(MultipleItem.NOT_PASS);
            }
            mItemList.add(multipleItem);
        }
        mLevelAdapter.notifyDataSetChanged();
    }

    private void initView() {

        mItemList = new ArrayList<>();
        mLevelAdapter = new LevelAdapter(mItemList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mLevelAdapter);
        mLevelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(LevelActivity.this, PlayActivity.class);
                intent.putExtra("level", position);
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 200) {
            Log.e(TAG, "onActivityResult: ");
            ininDate();
        }
    }


    @OnClick({R.id.level_blue_rl,R.id.level_break})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.level_blue_rl: //充值蓝钻
                Intent intent = new Intent(LevelActivity.this, PayActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            case R.id.level_break:
                finish();
                break;
        }
    }
}
