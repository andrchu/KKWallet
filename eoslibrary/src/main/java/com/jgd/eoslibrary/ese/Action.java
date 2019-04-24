package com.jgd.eoslibrary.ese;

/**
 * Action
 * 
 * @author espritblock http://eblock.io
 *
 */
public enum Action {

	transfer("${precision},${quantity}@eosio.token"), account("account"), ram("ram"), delegate("${precision},${quantity}@eosio.token"), voteproducer("voteproducer");

	private String code;

	private Action(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}