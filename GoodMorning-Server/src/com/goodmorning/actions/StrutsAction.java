package com.goodmorning.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.goodmorning.models.JSONResponse;

public interface StrutsAction extends ServletRequestAware {

	@Override
	public void setServletRequest(HttpServletRequest request);
	public String execute() throws Exception;
	public HttpServletRequest getServletRequest();
	public JSONResponse getResponse();
	public void setResponse(JSONResponse response);
	
}
