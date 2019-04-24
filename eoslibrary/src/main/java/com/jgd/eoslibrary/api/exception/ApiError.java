package com.jgd.eoslibrary.api.exception;

import java.lang.*;

/**
 * 
 * @author espritblock http://eblock.io
 *
 */
public class ApiError {

	private String message;

	private String code;

	private java.lang.Error error;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public java.lang.Error getError() {
		return error;
	}

	public void setError(java.lang.Error error) {
		this.error = error;
	}
}
