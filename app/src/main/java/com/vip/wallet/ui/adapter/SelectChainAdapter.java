package com.vip.wallet.ui.adapter;

import com.contrarywind.adapter.WheelAdapter;
import com.vip.wallet.entity.Chain;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/19 0019 11:22
 * 描述	      ${TODO}
 */

public class SelectChainAdapter implements WheelAdapter<Chain> {

    private List<Chain> mList;

    public SelectChainAdapter(List<Chain> list) {
        mList = list;
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }

    @Override
    public Chain getItem(int index) {
        return mList.get(index);
    }

    @Override
    public int indexOf(Chain o) {
        return mList.indexOf(o);
    }
}
