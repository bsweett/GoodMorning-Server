package com.goodmorning.models;

public class SuccessMessage {

	private String success;

	public SuccessMessage(Boolean success) {
		setStatus(success.toString());
	}
	
	public String getStatus() {
		return success;
	}

	public void setStatus(String success) {
		this.success = success;
	}
	

	
}
