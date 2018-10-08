package com.mh.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mh.entry.PayEntry;
import com.mh.match.R;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/6/15 下午2:52
 * - @Email whynightcode@gmail.com
 */
public class PayAdapter extends BaseMultiItemQuickAdapter<PayEntry, BaseViewHolder> {

    private int[] pay = {R.mipmap.haibi_moeny, R.mipmap.haibi_money_2, R.mipmap.haibi_money_3, R.mipmap.haibi_money_4};

    private int[] blue = {R.mipmap.lanzuan_money, R.mipmap.lanzuan_money_2, R.mipmap.lanzuan_money_3, R.mipmap.lanzuan_money_4};


    public PayAdapter(List<PayEntry> data) {
        super(data);
        addItemType(PayEntry.PAY, R.layout.item_pay);
        addItemType(PayEntry.BLUE_PAY, R.layout.item_pay);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayEntry item) {
        helper.addOnClickListener(R.id.pay_item_ll);
        helper.setBackgroundRes(R.id.pay_item_ll, item.isClicked() ? R.drawable.pay_item_seclet_on : R.drawable.pay_item_seclet);
        switch (helper.getItemViewType()) {
            case PayEntry.PAY:
                helper.setText(R.id.pay_item_title, item.getTitle())
                        .setText(R.id.pay_item_money, item.getMoney() + "元");
                helper.setImageResource(R.id.pay_item_iamge, pay[item.getUrl()]);
                break;
            case PayEntry.BLUE_PAY:
                helper.setText(R.id.pay_item_title, item.getTitle())
                        .setText(R.id.pay_item_money, item.getMoney() + " 元");
                helper.setImageResource(R.id.pay_item_iamge, blue[item.getUrl()]);
                break;
        }
    }
}
