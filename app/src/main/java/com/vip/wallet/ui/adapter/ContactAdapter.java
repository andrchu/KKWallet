package com.vip.wallet.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vip.wallet.R;
import com.vip.wallet.dao.Contact;
import com.vip.wallet.entity.Chain;
import com.vip.wallet.utils.StringUtil;

import java.util.List;

/**
 * 创建者     金国栋
 * 创建时间   2018/3/5 0005 10:25
 * 描述	      ${TODO}
 */

public class ContactAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {
    public ContactAdapter(@LayoutRes int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contact item) {
        String sName = "";
        if (!StringUtils.isEmpty(item.getName())) {
            sName = item.getName().substring(0, 1);
        }
        helper.setText(R.id.ci_sname, sName);
        helper.setText(R.id.ci_name, item.getName());
        helper.setText(R.id.ci_address, String.format("%s:%s", new Chain(item.chain_type).str, StringUtil.getHideString(item.getAddress(), 12)));
    }
}
