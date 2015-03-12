package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.SuccessMessage;
import com.goodmorning.models.User;
import com.opensymphony.xwork2.ActionSupport;

public class ConnectUser extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager manager;
	
	private final String parameter_1 = "device";
	
	@Override
	public String execute() throws Exception {
		
		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();
		
		try {
			
			String deviceId = getServletRequest().getParameter(parameter_1);
			
			if(deviceId.isEmpty()) {
				fail = new Failure("Invalid Request", "The request is missing parameters");
				actionResponse = new JSONResponse(fail);
				setResponse(actionResponse);
				
			} else {
				
				manager = getManager();
				user = manager.getUserByDeviceId(deviceId);
				
				// If a user is not found let them install as a new user
				if(user == null) {				
					actionResponse = new JSONResponse(new SuccessMessage(true));
					setResponse(actionResponse);
				
				// If we find a user with the deivce Id send them there content and ask them if they want to re-install as said user
				// If they say no send un-install request and then install from fresh user
				} else {
					user.setLastActive(new Timestamp(now.getTimeInMillis()));
					System.out.println(user.toString());
					actionResponse = new JSONResponse(user);
					setResponse(actionResponse);
					
				}	
			}
			
		} catch (Exception e) {
			//actionResponse = new JSONResponse(e);
			//setResponse(actionResponse);
			
		}
		
		return "response";
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public HttpServletRequest getServletRequest() {
		return request;
	}

	@Override
	public JSONResponse getResponse() {
		return response;
	}
	
	@Override
	public void setResponse(JSONResponse response) {
		this.response = response;
		
	}
	
	public HibernateUserManager getManager() {
		return HibernateUserManager.getDefault();
	}

}
