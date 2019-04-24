package com.jgd.eoslibrary.api.vo.transaction.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jgd.eoslibrary.api.vo.BaseVo;
import com.jgd.eoslibrary.ese.BeanField;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author espritblock http://eblock.io
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tx extends BaseVo {

	@BeanField(order = 0)
	public Object expiration;
	@BeanField(order = 1)
	public Long ref_block_num;
	@BeanField(order = 2)
	public Long ref_block_prefix;
	@BeanField(order = 3)
	public Long net_usage_words;
	@BeanField(order = 4)
	public Long max_cpu_usage_ms;
	@BeanField(order = 5)
	public Long delay_sec;
	@BeanField(order = 6)
	public List<String> context_free_actions = new ArrayList<>();
	@BeanField(order = 7)
	public List<TxAction> actions;
	@BeanField(order = 8)
	public List<TxExtenstions> transaction_extensions = new ArrayList<>();

	public Object getExpiration() {
		return expiration;
	}

	public void setExpiration(Object expiration) {
		this.expiration = expiration;
	}

	public Long getRef_block_num() {
		return ref_block_num;
	}

	public void setRef_block_num(Long ref_block_num) {
		this.ref_block_num = ref_block_num;
	}

	public Long getRef_block_prefix() {
		return ref_block_prefix;
	}

	public void setRef_block_prefix(Long ref_block_prefix) {
		this.ref_block_prefix = ref_block_prefix;
	}

	public Long getNet_usage_words() {
		return net_usage_words;
	}

	public void setNet_usage_words(Long net_usage_words) {
		this.net_usage_words = net_usage_words;
	}

	public Long getMax_cpu_usage_ms() {
		return max_cpu_usage_ms;
	}

	public void setMax_cpu_usage_ms(Long max_cpu_usage_ms) {
		this.max_cpu_usage_ms = max_cpu_usage_ms;
	}

	public Long getDelay_sec() {
		return delay_sec;
	}

	public void setDelay_sec(Long delay_sec) {
		this.delay_sec = delay_sec;
	}

	public List<String> getContext_free_actions() {
		return context_free_actions;
	}

	public void setContext_free_actions(List<String> context_free_actions) {
		this.context_free_actions = context_free_actions;
	}

	public List<TxAction> getActions() {
		return actions;
	}

	public void setActions(List<TxAction> actions) {
		this.actions = actions;
	}

	public List<TxExtenstions> getTransaction_extensions() {
		return transaction_extensions;
	}

	public void setTransaction_extensions(List<TxExtenstions> transaction_extensions) {
		this.transaction_extensions = transaction_extensions;
	}
}
