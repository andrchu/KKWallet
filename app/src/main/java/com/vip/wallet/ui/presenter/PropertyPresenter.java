package com.vip.wallet.ui.presenter;

import com.vip.wallet.base.RxPresenter;
import com.vip.wallet.ui.contract.PropertyContract;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/28 0028 16:28
 * 描述	      ${TODO}
 */

public class PropertyPresenter extends RxPresenter<PropertyContract.IPropertyView> implements PropertyContract.IPropertyPresenter {
    public PropertyPresenter(PropertyContract.IPropertyView view) {
        super(view);
    }

    @Override
    public void loadData() {
    }
}
