package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.base.IPresenter;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Contact;
import com.vip.wallet.ui.adapter.ContactAdapter;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.widget.TitleBarView;

import java.util.List;

import butterknife.Bind;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/25 0025 09:50
 * 描述	      ${TODO}
 */

public class ContactsManageFragment extends BaseFragment {
    public static final int RESULT_CODE = 1111;
    @Bind(R.id.fcm_list)
    RecyclerView mFcmList;
    @Bind(R.id.fcm_title)
    TitleBarView mFcmTitle;
    private ContactAdapter mAdapter;
    private View mEmptyView;
    public static final int OPTION_SELECT = 1;
    private int option_type = -1;

    public static ContactsManageFragment newInstance() {
        return new ContactsManageFragment();
    }

    public static ContactsManageFragment newInstance(int option_type) {
        ContactsManageFragment contactsManageFragment = new ContactsManageFragment();
        if (option_type != -1) {
            Bundle args = new Bundle();
            args.putInt(Constants.OPTION_TYPE, option_type);
            contactsManageFragment.setArguments(args);
        }
        return contactsManageFragment;
    }

    @Override
    protected View setSuccessView() {
        return mFcmList;
    }

    @Override
    protected View setEmptyView() {
        if (mEmptyView == null)
            mEmptyView = View.inflate(mContext, R.layout.view_contact_empty, null);
        return mEmptyView;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_contacts_manage;
    }

    @Override
    protected IPresenter setPresenter() {
        return null;
    }

    @Override
    public void initData(IPresenter presenter) {
        int anInt = getArguments().getInt(Constants.OPTION_TYPE, 0);
        if (anInt != 0)
            option_type = anInt;
        mAdapter = new ContactAdapter(R.layout.item_contact, null);
        mFcmList.setLayoutManager(new LinearLayoutManager(mContext));
        mFcmList.setAdapter(mAdapter);
        updateTitleUI();
        updateList();
    }

    private void updateTitleUI() {
        mFcmTitle.setTitleText(getString(option_type == OPTION_SELECT ? R.string.select_contacts : R.string.contacts_manage));
    }

    private void updateList() {
        List<Contact> contact = getContactList();
        if (ListUtil.isEmpty(contact)) {
            if (getCurrentViewState() != Constants.EMPTY)
                showView(Constants.EMPTY);
        } else {
            if (getCurrentViewState() != Constants.SUCCESS)
                showView(Constants.SUCCESS);
        }
        mAdapter.setNewData(contact);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mAdapter != null) {
            updateList();
        }
    }

    private List<Contact> getContactList() {
        return ScApplication.getInstance().getDaoSession().getContactDao().loadAll();
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (option_type == OPTION_SELECT) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CONTACT, mAdapter.getItem(position).getAddress());
                setFragmentResult(RESULT_CODE, bundle);
                pop();
            } else
                start(AddOrEditContactFragment.newInstance(mAdapter.getItem(position)));
        });
        mFcmTitle.setOnButtonClickListener(v -> start(AddOrEditContactFragment.newInstance(null)));
    }

    @Override
    protected void onSaveState(Bundle outState) {
        outState.putInt(Constants.OPTION_TYPE, option_type);
    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {
        int anInt = savedInstanceState.getInt(Constants.OPTION_TYPE, 0);
        if (anInt != 0)
            option_type = anInt;
    }
}
