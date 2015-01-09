package com.goodmorning.actions;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.opensymphony.xwork2.ActionSupport;

public class CreateNewUser extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager manager;
	
	private final String parameter_1 = "device";
	private final String parameter_2 = "nickname";
	private final String parameter_3 = "email";
	
	@Override
	public String execute() throws Exception {
		
		JSONResponse actionResponse;
		User user;
		Failure fail;
		
		try {
			
			String deviceId = getServletRequest().getParameter(parameter_1);
			String name = getServletRequest().getParameter(parameter_2);
			String email = getServletRequest().getParameter(parameter_3);
			
			if(deviceId.isEmpty() || name.isEmpty() || email.isEmpty()) {
				fail = new Failure("Invalid Request", "The request is missing parameters");
				actionResponse = new JSONResponse(fail);
				setResponse(actionResponse);
				
			} else {
				
				manager = getManager();
				user = manager.getUserByDeviceId(deviceId);
				
				if(user != null) {				
					fail = new Failure("User already exists", "This User already exists in the system");
					actionResponse = new JSONResponse(fail);
					setResponse(actionResponse);
					
				} else {
					user = new User();
					user.setDeviceId(deviceId);
					user.setNickname(name);
					user.setEmail(email);
					
					manager.add(user);
					
					actionResponse = new JSONResponse(user);
					setResponse(actionResponse);
					
				}	
			}
			
		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_USER, "error.CreateNewUserAction", e);
			fail = new Failure("Exception", e.getLocalizedMessage());
			actionResponse = new JSONResponse(fail);
			setResponse(actionResponse);
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
