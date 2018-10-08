package com.mh.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/15 下午4:22
 * - @Email whynightcode@gmail.com
 */
public class PayItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public PayItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
      /*  outRect.left = 3;
        outRect.top = 0;
        outRect.bottom = 20;
        outRect.right = 3;*/
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = 0;
            outRect.bottom = 0;
            outRect.right = 0;
            outRect.top = 40;
        } else {
            outRect.left = space;
            outRect.top = 40;
            outRect.bottom = 0;
            outRect.right = space;
        }
    }
}
