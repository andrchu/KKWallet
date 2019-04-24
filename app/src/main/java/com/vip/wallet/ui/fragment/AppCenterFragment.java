package com.vip.wallet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.vip.wallet.R;
import com.vip.wallet.base.BaseFragment;
import com.vip.wallet.config.Constants;
import com.vip.wallet.config.ScApplication;
import com.vip.wallet.dao.Card;
import com.vip.wallet.dao.CardDao;
import com.vip.wallet.entity.AppCenterItem;
import com.vip.wallet.entity.BrowserInfo;
import com.vip.wallet.entity.DappBrowserInfo;
import com.vip.wallet.exception.ApiHttpException;
import com.vip.wallet.ui.activity.DappBrowserActivity;
import com.vip.wallet.ui.activity.SelectCardActivity;
import com.vip.wallet.ui.activity.WebActivity;
import com.vip.wallet.ui.adapter.AppCenterBannerAdapter;
import com.vip.wallet.ui.adapter.AppCenterGroupAdapter;
import com.vip.wallet.ui.adapter.AppCenterItemAdapter;
import com.vip.wallet.ui.contract.AppCenterContract;
import com.vip.wallet.ui.presenter.AppCenterPresenter;
import com.vip.wallet.utils.ClickUtil;
import com.vip.wallet.utils.ListUtil;
import com.vip.wallet.utils.LogUtil;
import com.vip.wallet.utils.StringUtil;
import com.vip.wallet.widget.CustomScrollView;
import com.vip.wallet.widget.TitleBarView;
import com.vip.wallet.widget.VpSwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 创建者     金国栋
 * 创建时间   2018/5/18 0018 11:10
 * 描述	      ${TODO}
 */

public class AppCenterFragment extends BaseFragment<AppCenterContract.IAppCenterPresenter> implements AppCenterContract.IAppCenterView, BGABanner.Delegate, CustomScrollView.OnScrollChangedListener {
    @Bind(R.id.fac_title_view)
    TitleBarView mFacTitleView;
    @Bind(R.id.fac_list)
    RecyclerView mFacList;
    @Bind(R.id.fac_list_group)
    RecyclerView mFacListGroup;
    @Bind(R.id.fac_pull_fresh)
    VpSwipeRefreshLayout mFacPullFresh;
    @Bind(R.id.fac_banner_view)
    BGABanner mFacBannerView;
    @Bind(R.id.fac_scroll_view)
    CustomScrollView mFacScrollView;
    @Bind(R.id.container)
    FrameLayout mContainer;
    @Bind(R.id.fac_list_group_hover)
    RecyclerView mFacListGroupHover;
    @Bind(R.id.fac_success_view)
    RelativeLayout mFacSuccessView;
    @Bind(R.id.fac_list_group_layout)
    RelativeLayout mFacListGroupLayout;
    private AppCenterItemAdapter mAppCenterAdapter;
    private AppCenterGroupAdapter mCenterGroupAdapter;

    @Override
    protected View setSuccessView() {
        return mFacSuccessView;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_app_center;
    }

    public static AppCenterFragment newInstance() {
        return new AppCenterFragment();
    }

    @Override
    protected AppCenterContract.IAppCenterPresenter setPresenter() {
        return new AppCenterPresenter(this);
    }

    @Override
    public void initData(AppCenterContract.IAppCenterPresenter presenter) {

        //组
        mCenterGroupAdapter = new AppCenterGroupAdapter(R.layout.item_group_app_center, null);

        mAppCenterAdapter = new AppCenterItemAdapter(R.layout.item_app_center, null);
        mFacList.setLayoutManager(new LinearLayoutManager(mContext));
        mFacList.setAdapter(mAppCenterAdapter);
        mFacList.setHasFixedSize(true);
        mFacList.setNestedScrollingEnabled(false);

        mFacBannerView.setAdapter(new AppCenterBannerAdapter());

        presenter.loadData();
    }

    void setGroupLayoutManage(List<AppCenterItem> data) {
        if (data.size() <= 4) {
            mFacListGroup.setLayoutManager(new GridLayoutManager(mContext, data.size()));
            mFacListGroupHover.setLayoutManager(new GridLayoutManager(mContext, data.size()));
        } else {
            LinearLayoutManager layout = new LinearLayoutManager(mContext);
            layout.setOrientation(LinearLayoutManager.HORIZONTAL);
            mFacListGroup.setLayoutManager(layout);
            mFacListGroupHover.setLayoutManager(layout);
        }
        mFacListGroup.setAdapter(mCenterGroupAdapter);
        mFacListGroupHover.setAdapter(mCenterGroupAdapter);
    }

    @Override
    protected void initListener() {
        mAppCenterAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppCenterItem.DatasEntity item = mAppCenterAdapter.getItem(position);
            disposeItemClick(item);
        });
        mFacPullFresh.setOnRefreshListener(() -> getPresenter().fresh());
        mFacBannerView.setDelegate(this);
        mCenterGroupAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppCenterItem item = mCenterGroupAdapter.getItem(position);
            for (AppCenterItem appCenterItem : mCenterGroupAdapter.getData()) {
                appCenterItem.isSeleced = false;
            }
            item.isSeleced = true;
            mCenterGroupAdapter.notifyDataSetChanged();
            mAppCenterAdapter.setNewData(item.datas);
        });


        mFacScrollView.setOnScrollChangedListener(this);
    }


    private void disposeItemClick(AppCenterItem.DatasEntity item) {

        if (item == null || item.isHeader || StringUtils.isEmpty(item.url))
            return;

        if (!ClickUtil.hasExecute())
            return;

        if (item.hasClick()) {
            if (item.isNeedCard()) {
                List<Card> list = ScApplication.getInstance().getDaoSession().getCardDao().queryBuilder().where(CardDao.Properties.ChainType.eq(item.chain_type)).build().list();
                if (ListUtil.isEmpty(list)) {
                    toast(String.format("没有找到%s类型卡", StringUtil.getChainName(item.chain_type)));
                    return;
                }
                String url = item.url + StringUtil.signBuilder(list.get(0).getAddressOrAccount());
                if (list.size() > 1)
                    SelectCardActivity.start(mContext, item.chain_type, item.url);
                else {
                    Card card = list.get(0);
                    if (url.contains(Constants.BASE_URL)) {
                        WebActivity.startWebActivity(mContext, new BrowserInfo("", item.url + StringUtil.signBuilder(card.getAddressOrAccount())));
                    } else
                        DappBrowserActivity.start(mContext, new DappBrowserInfo(item.url, "", card));
                }
            } else {
                WebActivity.startWebActivity(mContext, new BrowserInfo("", item.url));
            }
        } else {
            toast("此应用暂未开放,敬请期待!");
        }
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        disposeItemClick((AppCenterItem.DatasEntity) model);
    }

    @Override
    protected void onSaveState(Bundle outState) {

    }

    @Override
    protected void restoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void loadSuccess(ArrayList<AppCenterItem> appCenterItems) {
        AppCenterItem banner = ListUtil.getObj(appCenterItems, new AppCenterItem("Banner"));
        if (banner != null) {
            appCenterItems.remove(banner);
            mFacBannerView.setData(R.layout.image_banner, banner.datas, null);
        }
        AppCenterItem appCenterItem = appCenterItems.get(0);
        appCenterItem.isSeleced = true;

        setGroupLayoutManage(appCenterItems);
        mCenterGroupAdapter.setNewData(appCenterItems);
        mAppCenterAdapter.setNewData(appCenterItem.datas);
    }

    @Override
    public void freshError(ApiHttpException exception) {
        mFacPullFresh.setRefreshing(false);
        toast(exception.getMessage());
    }

    @Override
    public void freshSuccess(ArrayList<AppCenterItem> appCenterItems) {
        mFacPullFresh.setRefreshing(false);
        loadSuccess(appCenterItems);
    }

    @Override
    public void onChanged(int l, int t, int oldl, int oldt) {

        //        db =  t - bannerViewHeight >= 0;   //顶部
        int i = t - mFacBannerView.getMeasuredHeight();
        boolean isTop = i >= 0;     //true 大于或等于顶部
        //        boolean isDown = oldt > t;    //true 下滑   上滑
        if (isTop) {
            if (mFacListGroupLayout.getVisibility() != View.VISIBLE) {
                mFacListGroupLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (mFacListGroupLayout.getVisibility() != View.GONE) {
                mFacListGroupLayout.setVisibility(View.GONE);
            }
        }
        LogUtil.getInstance().i(String.format("i >>>> %s , l > %s , t > %s , oldl > %s , oldt > %s", i, l, t, oldl, oldt));
    }
}
