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
import com.goodmorning.models.RSSFeed;
import com.goodmorning.models.SuccessMessage;
import com.goodmorning.models.Task;
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
	private final String parameter_2 = "title";
	private final String parameter_3 = "type";
	private final String parameter_4= "updated";
	private final String parameter_5 = "link";
	private final String parameter_6 = "description";
	private final String parameter_7 = "source";
	private final String parameter_8 = "logo";
	private final String parameter_9 = "lang";
	
	@Override
	public String execute() throws Exception {
		
		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();
		
		try {
			String token = getServletRequest().getParameter(parameter_1);
			String title = getServletRequest().getParameter(parameter_2);
			RSSType type = RSSType.fromString(getServletRequest().getParameter(parameter_3));
			String updated = getServletRequest().getParameter(parameter_4);
			String link = getServletRequest().getParameter(parameter_5);
			String description = getServletRequest().getParameter(parameter_6);
			String source = getServletRequest().getParameter(parameter_7);
			String logoUrl = getServletRequest().getParameter(parameter_8);
			String lang = getServletRequest().getParameter(parameter_9);
			
			if(token.isEmpty() || title.isEmpty() || type == null || updated.isEmpty() || link.isEmpty() || source.isEmpty()) {
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
					RSSFeed feed;

					feedManager = getFeedManager();
					feed = feedManager.getFeedBySource(source);

					if(feed != null) {
						fail = new Failure("Feed already exists", "You are already subscribed to this feed");
						setResponse(new JSONResponse(fail));
						
					} else {
						//user.setLastActive(new Timestamp(now.getTimeInMillis()));
						feed = new RSSFeed(title, link, type, description, lang, source, updated, logoUrl, user);
						feed.setUser(user);
						user.addRssFeed(feed);
						
						if(userManager.update(user)) {
							setResponse(new JSONResponse(new SuccessMessage(true)));
							
						} else {
							fail = new Failure("Database update failed", "Hibernate failed to update the user");
							setResponse(new JSONResponse(fail));
						}
						
					}
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
