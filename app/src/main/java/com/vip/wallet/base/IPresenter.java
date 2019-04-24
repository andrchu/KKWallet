package com.vip.wallet.base;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 11:14  <br/><br/>
 * 描述:	     Presenter顶级接口
 */
public interface IPresenter {
	/**
	 * 加载网络数据
	 */
	void loadData();

	void detachView();
}
