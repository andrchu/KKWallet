package com.vip.wallet.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vip.wallet.base.BaseFragment;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/6/15 0015 10:30
 * 描述	      ${TODO}
 */

public class PropertyAdapter extends FragmentPagerAdapter {
    List<BaseFragment> fragments;
    private List<String> titles;

    @Override
    public String getPageTitle(int position) {
        return titles.get(position);
    }

    public PropertyAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
