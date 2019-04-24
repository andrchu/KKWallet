package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.ui.dialog.HintUnableScreenshotDialog;
import com.vip.wallet.utils.ClickUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/21 11:04
 * 描述	     备份助记词
 */

public class BackUpSeedKeyFragment extends BaseFragment {

    @Bind(R.id.fbusk_flow_layout)
    TagFlowLayout mFbuskFlowLayout;
    @Bind(R.id.fbusk_next)
    TextView mFbuskNext;
    private ArrayList<String> mSeedKeys = new ArrayList<>();

    public static BackUpSeedKeyFragment newInstance(String SeedKey) {
        BackUpSeedKeyFragment backUpSeedKeyFragment = new BackUpSeedKeyFragment();
        Bundle args = new Bundle();
        args.putString(Constants.SEED_KEY, SeedKey);
        backUpSeedKeyFragment.setArguments(args);
        return backUpSeedKeyFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_back_up_seed_key;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        String string = getArguments().getString(Constants.SEED_KEY);
        if (!StringUtils.isEmpty(string)) {
            String[] split = string.split("[ ]+");
            List<String> list = Arrays.asList(split);
            mSeedKeys.clear();
            mSeedKeys.addAll(list);
        }
        updateSeedKeyUI();
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        showHintDialog();
    }

    private void showHintDialog() {
        HintUnableScreenshotDialog hintUnableScreenshotDialog = new HintUnableScreenshotDialog(mContext);
        hintUnableScreenshotDialog.show();
    }


    private void updateSeedKeyUI() {
        mFbuskFlowLayout.setAdapter(new TagAdapter<String>(mSeedKeys) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = View.inflate(parent.getContext(), R.layout.item_selected_seed, null);
                TextView textView = view.findViewById(R.id.is_tv);
                textView.setText(s);
                return view;
            }
        });

    }

    @Override
    protected void initListener() {
        mFbuskNext.setOnClickListener(v -> {
            if (!ClickUtil.hasExecute())
                return;
            //验证助记词
            CheckSeedKeyFragment checkSeedKeyFragment = CheckSeedKeyFragment.newInstance(mSeedKeys);
            start(checkSeedKeyFragment);
        });
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putSerializable(Constants.SEED_KEY, mSeedKeys);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        Serializable serializable = savedInstanceState.getSerializable(Constants.SEED_KEY);
        if (serializable != null)
            mSeedKeys = (ArrayList<String>) serializable;
        updateSeedKeyUI();
    }
}
