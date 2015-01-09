package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.User;
import com.opensymphony.xwork2.ActionSupport;

public class ConnectUser extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager manager;
	
	private final String parameter_1 = "token";
	
	@Override
	public String execute() throws Exception {
		
		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();
		
		try {
			
			String token = getServletRequest().getParameter(parameter_1);
			
			if(token.isEmpty()) {
				fail = new Failure("Invalid Request", "The request is missing parameters");
				actionResponse = new JSONResponse(fail);
				setResponse(actionResponse);
				
			} else {
				
				manager = getManager();
				user = manager.getUserByToken(token);
				
				/**
				 * Because i never de-validate tokens if a token cannot find a user it's not a valid token.
				 * If this response is sent to client have client re-install 
				 */
				if(user == null) {				
					fail = new Failure("User not found", "The token provided did not return a user");
					actionResponse = new JSONResponse(fail);
					setResponse(actionResponse);
					
				} else {
					
					user.setLastActive(new Timestamp(now.getTimeInMillis()));
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
