package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateRSSFeedManager;
import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.enums.RSSType;
import com.goodmorning.enums.TaskType;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.opensymphony.xwork2.ActionSupport;

public class CreateNewRSSFeed extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = -4720540361530635240L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateRSSFeedManager feedManager;
	private HibernateUserManager userManager;
	
	private final String parameter_1 = "token";
	private final String parameter_2 = "url";
	private final String parameter_3 = "type";
	
	@Override
	public String execute() throws Exception {
		
		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();
		
		try {
			String token = getServletRequest().getParameter(parameter_1);
			String url = getServletRequest().getParameter(parameter_2);
			RSSType type = RSSType.fromString(getServletRequest().getParameter(parameter_3));
			
			if(token.isEmpty() || url.isEmpty() || type == null) {
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

					// TODO: Check that URL returns valid RSS data
					// (use RSSFeedParser class)
						// (remove feed message parsing from it) (that goes on the client)
					
					// If it doesn't send error to client
					// If it got RSS data check that the feed doesn't already exist from the same URL
						// (Select Feed by URL)
						// Send back error if it already exists (allow them to edit it maybe)
					// If not add it to the feed list for the user and return
					
					user.setLastActive(new Timestamp(now.getTimeInMillis()));
					//actionResponse = buildResponseCreateTaskForUser(user, time, days, notes, type, name);
					//setResponse(actionResponse);

				}
			}
			
		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_RSSFEED, "error.CreateFeedAction", e);
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
