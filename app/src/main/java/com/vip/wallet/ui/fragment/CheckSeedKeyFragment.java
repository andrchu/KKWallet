package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.SeedWord;
import com.vip.wallet.ui.adapter.SelectedSeedAdapter;
import com.vip.wallet.ui.adapter.UnSelectedSeedAdapter;
import com.vip.wallet.utils.ListUtil;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/21 0021 17:17
 * 描述	      ${TODO}
 */

public class CheckSeedKeyFragment extends BaseFragment {

    @Bind(R.id.fcsk_selected_seed)
    TagFlowLayout mFcskSelectedSeed;
    @Bind(R.id.fcsk_unselected_seed)
    TagFlowLayout mFcskUnselectedSeed;
    @Bind(R.id.fcsk_confirm)
    TextView mFcskConfirm;
    /**
     * 助记词
     */
    private ArrayList<SeedWord> mSeedWords = new ArrayList<>();
    /**
     * 随机助记词
     */
    private ArrayList<SeedWord> mSeedWordsRandom = new ArrayList<>();
    /**
     * 选中助记词
     */
    private ArrayList<SeedWord> mSelectSeedWords = new ArrayList<>();
    private SelectedSeedAdapter mSelectedSeedAdapter;
    private UnSelectedSeedAdapter mUnSelectedSeedAdapter;

    public static CheckSeedKeyFragment newInstance(ArrayList<String> seeds) {
        CheckSeedKeyFragment checkSeedKeyFragment = new CheckSeedKeyFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.SEED_KEY, seeds);
        checkSeedKeyFragment.setArguments(args);
        return checkSeedKeyFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_check_seed_key;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        Serializable serializable = getArguments().getSerializable(Constants.SEED_KEY);
        if (serializable != null) {
            ArrayList<String> strings = (ArrayList<String>) serializable;
            mSeedWords.clear();
            mSeedWordsRandom.clear();
            mSelectSeedWords.clear();
            for (String string : strings)
                mSeedWords.add(new SeedWord(string));

            //助记词随机顺序
            ArrayList<SeedWord> words = new ArrayList<>();
            words.addAll(mSeedWords);
            for (int i = 0; i < mSeedWords.size(); i++) {
                Random random = new Random();
                int index = random.nextInt(words.size());
                SeedWord seedWord = words.remove(index);
                mSeedWordsRandom.add(seedWord);
            }
        }
        updateSelectedSeedUI();
        updateUnSelectedSeedUI();
        updateConfirmUI();
    }

    private void updateSelectedSeedUI() {
        mSelectedSeedAdapter = new SelectedSeedAdapter(mSelectSeedWords);
        mFcskSelectedSeed.setAdapter(mSelectedSeedAdapter);
    }

    private void updateUnSelectedSeedUI() {
        mUnSelectedSeedAdapter = new UnSelectedSeedAdapter(mSeedWordsRandom);
        mFcskUnselectedSeed.setAdapter(mUnSelectedSeedAdapter);
    }


    @Override
    protected void initListener() {
        mFcskSelectedSeed.setOnTagClickListener((view, position, parent) -> {
            SeedWord remove = mSelectSeedWords.remove(position);
            SeedWord obj = ListUtil.getObj(mSeedWordsRandom, remove);
            if (obj != null)
                obj.isSelect = false;
            mSelectedSeedAdapter.notifyDataChanged();
            mUnSelectedSeedAdapter.notifyDataChanged();
            updateConfirmUI();
            return true;
        });
        mFcskUnselectedSeed.setOnTagClickListener((view, position, parent) -> {
            SeedWord seedWord = mSeedWordsRandom.get(position);
            if (seedWord.isSelect)
                return true;
            seedWord.isSelect = true;
            mSelectSeedWords.add(seedWord);
            mSelectedSeedAdapter.notifyDataChanged();
            mUnSelectedSeedAdapter.notifyDataChanged();
            updateConfirmUI();
            return true;
        });
        mFcskConfirm.setOnClickListener(v -> {
            if (checkSeedWord()) {
                ScApplication.getInstance().getConfig().setBackUp(true);
                CardDao cardDao = ScApplication.getInstance().getDaoSession().getCardDao();
                List<Card> cards = cardDao.loadAll();
                for (Card card : cards) {
                    card.isBackUp = 1;
                    cardDao.update(card);
                }
                toast(getString(R.string.back_up_success));
                if (mActivity != null) {
                    popTo(BackUpSeedKeyFragment.class, true);
                    mActivity.fragmentCallBack(null);
                }
            } else {
                toast(getString(R.string.seed_order_error));
            }
        });
    }

    private void updateConfirmUI() {
        mFcskConfirm.setEnabled(!ListUtil.isEmpty(mSelectSeedWords) && mSelectSeedWords.size() >= mSeedWordsRandom.size());
    }

    /**
     * 检查助记词顺序是否正确
     */
    private boolean checkSeedWord() {
        for (int i = 0; i < mSelectSeedWords.size(); i++) {
            SeedWord select_sw = mSelectSeedWords.get(i);
            SeedWord sw = mSeedWords.get(i);
            if (!select_sw.equals(sw)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.SEED_WORDS_RANDOM, mSeedWordsRandom);
        outState.putSerializable(Constants.SELECT_SEED_WORDS, mSelectSeedWords);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable randomSerializable = savedInstanceState.getSerializable(Constants.SEED_WORDS_RANDOM);
        Serializable selectSerializable = savedInstanceState.getSerializable(Constants.SELECT_SEED_WORDS);
        if (randomSerializable != null && selectSerializable != null) {
            mSeedWordsRandom = (ArrayList<SeedWord>) randomSerializable;
            mSelectSeedWords = (ArrayList<SeedWord>) selectSerializable;
        }
        updateSelectedSeedUI();
        updateUnSelectedSeedUI();
    }
}
