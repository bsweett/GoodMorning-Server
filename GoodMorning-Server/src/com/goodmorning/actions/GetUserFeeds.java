package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateTaskManager;
import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.enums.RSSType;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.opensymphony.xwork2.ActionSupport;

public class GetUserFeeds extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager userManager;

	private final String parameter_1 = "token";
	private final String parameter_2 = "type";

	@Override
	public String execute() throws Exception {

		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();

		try {

			String token = getServletRequest().getParameter(parameter_1);
			String type = getServletRequest().getParameter(parameter_2);

			if(token.isEmpty()) {
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

					user.setLastActive(new Timestamp(now.getTimeInMillis()));
					actionResponse = buildResponseForRSSType(user, type);
					setResponse(actionResponse);
				}
			}

		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_RSSFEEDS, "error.GetUserFeedsAction", e);
			fail = new Failure("Exception", e.getLocalizedMessage());
			actionResponse = new JSONResponse(fail);
			setResponse(actionResponse);
		}

		return "response";
	}
	
	private JSONResponse buildResponseForRSSType(User user, String type) {
		RSSType feedType = RSSType.fromString(type);

		if(feedType != null && feedType != RSSType.OTHER) {
			return new JSONResponse(user.getFeedsWithType(feedType));
		} else {
			return new JSONResponse(user.getRssFeeds());
		}
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

	public HibernateTaskManager getTaskManager() {
		return HibernateTaskManager.getDefault();
	}

}
