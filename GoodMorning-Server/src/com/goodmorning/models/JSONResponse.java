package com.goodmorning.models;

import com.google.gson.Gson;

public class JSONResponse {

	private String json;

	public JSONResponse(User object) {
		Gson gsonObject = new Gson();
		setJson(gsonObject.toJson(object));	
	}
	
	public JSONResponse(Failure object) {
		Gson gsonObject = new Gson();
		setJson(gsonObject.toJson(object));	
	}

	public String getJson() {
		return json;
	}

	public void setJson(String response) {
		this.json = response;
	}
	
}
