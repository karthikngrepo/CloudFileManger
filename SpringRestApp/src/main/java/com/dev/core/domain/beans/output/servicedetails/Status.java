package com.dev.core.domain.beans.output.servicedetails;

public class Status {
	private int code = 200;
	
	private String message = "Success";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
