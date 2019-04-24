package com.vip.wallet.ui.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseDialog;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.other.OnButtonClickListener;
import com.vip.wallet.ui.adapter.SelectCardAdapter;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 10:10
 * 描述	      ${TODO}
 */

public class SelectCardDialog extends BaseDialog {

    private View mExitView;
    private RecyclerView mDscList;
    private SelectCardAdapter mAdapter;
    private TextView mDscTitle;

    public SelectCardDialog(Context context) {
        super(context, R.style.pop_dialog);
    }

    @Override
    protected void init() {
        super.init();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_select_card;
    }

    @Override
    protected void initView() {
        mExitView = findViewById(R.id.dsc_exit);
        mDscList = findViewById(R.id.dsc_list);
        mDscTitle = findViewById(R.id.dsc_title);
    }

    public void setCurrentCard(Card currentCard) {
        if (mAdapter != null) {
            mAdapter.setCurrentCard(currentCard);
        }
        scrollToSelect();
    }

    public SelectCardDialog setTitle(String title) {
        mDscTitle.setText(title);
        return this;
    }

    @Override
    protected void initData() {
        mAdapter = new SelectCardAdapter(R.layout.item_card, getCardList());
        mDscList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDscList.setAdapter(mAdapter);
    }

    private void scrollToSelect() {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            Card card = mAdapter.getData().get(i);
            if (mAdapter.getCurrentCard().equals(card)) {
                mDscList.scrollToPosition(i);
                break;
            }
        }
    }

    private List<Card> getCardList() {
        return ScApplication.getInstance().getDaoSession().getCardDao().loadAll();
    }

    @Override
    protected void initListener() {
        mExitView.setOnClickListener(v -> dismiss());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCardOnButtonClickListener != null) {
                mCardOnButtonClickListener.onButtonClick(mAdapter.getItem(position));
                dismiss();
            }
        });
    }

    public void setCardOnButtonClickListener(OnButtonClickListener<Card> cardOnButtonClickListener) {
        mCardOnButtonClickListener = cardOnButtonClickListener;
    }

    private OnButtonClickListener<Card> mCardOnButtonClickListener;
}
