package com.vip.wallet.ui.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.vip.wallet.R;
import com.vip.wallet.entity.Path;
import com.vip.wallet.ui.adapter.PathListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/15 0015 13:55
 * 描述	      ${TODO}
 */

public class SelectPathPopWindow extends PopupWindow {

    private RecyclerView mRecyclerView;
    private List<Path> mData;
    private PathListAdapter mAdapter;
    private OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        mOnItemSelectListener = onItemSelectListener;
    }

    public interface OnItemSelectListener {
        void onSelect(Path path);
    }

    public SelectPathPopWindow(Context context) {
        super(context);
        initPaths(context);
        initView(context);
        initData();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Path item = mAdapter.getItem(position);
            for (Path path : mAdapter.getData()) {
                path.isSelect = false;
            }
            item.isSelect = true;
            mAdapter.notifyDataSetChanged();

            if (mOnItemSelectListener != null) {
                mOnItemSelectListener.onSelect(item);
            }
            dismiss();
        });
    }

    private void initPaths(Context context) {
        mData = new ArrayList<>();
        mData.add(new Path("m/44'/60'/0'/0/0", "m/44'/60'/0'/0/0 Jaxx,Metamask,imToken (ETH)", true));
        mData.add(new Path("m/44'/60'/0'/0", "m/44'/60'/0'/0 Ledger (ETH)", false));
        mData.add(new Path("m/44'/60'/1'/0/0", String.format("m/44'/60'/1'/0/0 %s", context.getString(R.string.custom_path)), false, true));
    }

    private void initView(Context context) {
        View rootView = View.inflate(context, R.layout.pop_select_path, null);
        setContentView(rootView);
        mRecyclerView = rootView.findViewById(R.id.psp_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new PathListAdapter(R.layout.item_path, mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        setFocusable(true);// 设置弹出窗口可
        //        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明

    }
}
