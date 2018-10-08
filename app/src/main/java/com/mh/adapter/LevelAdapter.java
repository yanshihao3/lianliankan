package com.mh.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mh.entry.MultipleItem;
import com.mh.match.R;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/13 下午3:59
 * - @Email whynightcode@gmail.com
 */
public class LevelAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LevelAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.PASS, R.layout.item_level_pass);
        addItemType(MultipleItem.NOT_PASS, R.layout.item_level_not_pass);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.PASS:
                helper.setText(R.id.item_number,item.getNumber());
                helper.setGone(R.id.item_tag,item.isTag());
                helper.addOnClickListener(R.id.item_view);
                break;
            case MultipleItem.NOT_PASS:

                break;
        }
    }
}
