package com.jgd.eoslibrary.api.vo.transaction.push;


import com.jgd.eoslibrary.api.vo.BaseVo;
import com.jgd.eoslibrary.ese.BeanField;

/**
 * 
 * @author espritblock http://eblock.io
 *
 */
public class TxSign extends BaseVo {

	public TxSign() {

	}

	public TxSign(String chain_id, Tx transaction) {
		this.chain_id = chain_id;
		this.transaction = transaction;
	}

	@BeanField(order = 0)
	public String chain_id;

	@BeanField(order = 1)
	public Tx transaction;

	public String getChain_id() {
		return chain_id;
	}

	public void setChain_id(String chain_id) {
		this.chain_id = chain_id;
	}

	public Tx getTransaction() {
		return transaction;
	}

	public void setTransaction(Tx transaction) {
		this.transaction = transaction;
	}

}
