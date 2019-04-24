package com.vip.wallet.base;


import com.vip.wallet.exception.ApiHttpException;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2018/1/22 11:13   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface IBaseView {
	/**
	 * 切换视图
	 *
	 * @param viewState {@link BaseActivity#showView(int) 四种视图}
	 */
	void showView(int viewState);
	/**
	 * 切换视图
	 *
	 * @param viewState {@link BaseActivity#showView(int) 四种视图}
	 */
	void showView(int viewState, ApiHttpException e);
}
