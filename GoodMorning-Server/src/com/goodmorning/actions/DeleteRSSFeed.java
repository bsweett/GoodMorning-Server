package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateRSSFeedManager;
import com.goodmorning.database.HibernateTaskManager;
import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.enums.TaskType;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.RSSFeed;
import com.goodmorning.models.SuccessMessage;
import com.goodmorning.models.Task;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.goodmorning.util.Utility;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteRSSFeed extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateRSSFeedManager feedManager;
	private HibernateUserManager userManager;

	private final String parameter_1 = "token";
	private final String parameter_2 = "id";

	@Override
	public String execute() throws Exception {

		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();

		try {

			String token = getServletRequest().getParameter(parameter_1);
			String feedId = getServletRequest().getParameter(parameter_2);

			if(token.isEmpty() || feedId.isEmpty()) {
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
					System.out.println("User: " + user.toString());
					
					feedManager = getFeedManager();
					
					RSSFeed feed = feedManager.getFeedById(feedId);
					boolean result = true;
					
					// If the feed isn't found they have a local copy not in sync with DB
					// The client will remove its copy and never see it again either way
					if(feed != null) {
						result = feedManager.delete(feed);
					}
					
					actionResponse = new JSONResponse(new SuccessMessage(result));
					setResponse(actionResponse);

				}
			}

		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_TASK, "error.CreateTaskAction", e);
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

	public HibernateRSSFeedManager getFeedManager() {
		return HibernateRSSFeedManager.getDefault();
	}

}
