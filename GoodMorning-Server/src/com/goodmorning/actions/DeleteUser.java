package com.goodmorning.actions;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.SuccessMessage;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteUser extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager userManager;

	private final String parameter_1 = "token";
	private final String parameter_2 = "deviceId";

	@Override
	public String execute() throws Exception {

		JSONResponse actionResponse;
		User user;
		Failure fail;

		try {

			String token = getServletRequest().getParameter(parameter_1);
			String deviceId = getServletRequest().getParameter(parameter_2);

			if(token.isEmpty() || deviceId.isEmpty()) {
				fail = new Failure("Invalid Request", "The request is missing parameters");
				actionResponse = new JSONResponse(fail);
				setResponse(actionResponse);

			} else {
				userManager = getUserManager();
				user = userManager.getUserByToken(token);

				/**
				 * Because i never de-validate tokens if a token cannot find a user it's not a valid token.
				 * If this response is sent to client have client re-install 
				 */
				if(user == null) {				
					fail = new Failure("User not found", "The token provided did not return a user");
					actionResponse = new JSONResponse(fail);
					setResponse(actionResponse);

				} else {

					boolean result = userManager.delete(user);
					actionResponse = new JSONResponse(new SuccessMessage(result));
					setResponse(actionResponse);
				}
			}

		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_DELETE_USER, "error.DeleteUserAction", e);
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

	public HibernateUserManager getUserManager() {
		return HibernateUserManager.getDefault();
	}
}
