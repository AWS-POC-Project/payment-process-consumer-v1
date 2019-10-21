package com.citizens.process.payment.error;

import java.util.Date;

public class ErrorMessage {

	private Date timestamp;
	
	private String errorCode;
	
	private String errorDesc;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	@Override
	public String toString() {
		return "ErrorMessage [timestamp=" + timestamp + ", errorCode=" + errorCode + ", errorDesc=" + errorDesc + "]";
	}
	
	
}
