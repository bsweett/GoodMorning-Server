package com.goodmorning.models;

public class Failure {

	private String reason = "";
	private String message = "";
	
	public Failure(String reason, String message) {
		setReason(reason);
		setMessage(message);
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
