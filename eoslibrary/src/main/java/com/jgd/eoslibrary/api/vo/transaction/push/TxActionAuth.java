package com.jgd.eoslibrary.api.vo.transaction.push;


import com.jgd.eoslibrary.api.vo.BaseVo;
import com.jgd.eoslibrary.ese.BeanField;

/**
 * 
 * @author espritblock http://eblock.io
 *
 */
public class TxActionAuth extends BaseVo {

	public TxActionAuth() {

	}

	public TxActionAuth(String actor, String permission) {
		this.actor = actor;
		this.permission = permission;
	}

	@BeanField(order = 0)
	private String actor;
	@BeanField(order = 1)
	private String permission;

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
