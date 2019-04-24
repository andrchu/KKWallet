package com.jgd.eoslibrary.api.vo.transaction.push;

import com.jgd.eoslibrary.api.vo.BaseVo;
import com.jgd.eoslibrary.ese.BeanField;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author espritblock http://eblock.io
 *
 */
public class TxAction extends BaseVo {

	public TxAction() {

	}

	public TxAction(String actor, String account, String name, Object data) {
		this.account = account;
		this.name = name;
		this.data = data;
		this.authorization = new ArrayList<>();
		this.authorization.add(new TxActionAuth(actor, "active"));
	}

	@BeanField(order = 0)
	public String account;
	@BeanField(order = 1)
	public String name;
	@BeanField(order = 2)
	public List<TxActionAuth> authorization;
	@BeanField(order = 3)
	public Object data;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TxActionAuth> getAuthorization() {
		return authorization;
	}

	public void setAuthorization(List<TxActionAuth> authorization) {
		this.authorization = authorization;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
