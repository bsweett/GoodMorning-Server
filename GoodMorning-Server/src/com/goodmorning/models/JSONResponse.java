package com.goodmorning.models;

import java.util.Set;

import com.google.gson.Gson;

public class JSONResponse {

	private String json;

	public JSONResponse(User object) {
		
		try {
			Gson gsonObject = new Gson();
			setJson(gsonObject.toJson(object));	
		} catch (Exception e) {
			System.out.println("Exception parsing JSON");
			e.printStackTrace();
		}

	}
	
	public JSONResponse(Set<?> set) {
		Gson gsonObject = new Gson();
		setJson(gsonObject.toJson(set));	
	}
	
	public JSONResponse(Failure object) {
		Gson gsonObject = new Gson();
		setJson(gsonObject.toJson(object));	
	}
	
	public JSONResponse(SuccessMessage message) {
		Gson gsonObject = new Gson();
		setJson(gsonObject.toJson(message));
	}

	public String getJson() {
		return json;
	}

	public void setJson(String response) {
		this.json = response;
	}
	
}
