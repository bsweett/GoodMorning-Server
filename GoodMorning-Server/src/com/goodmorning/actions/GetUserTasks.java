package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateTaskManager;
import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.enums.TaskType;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.Task;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Gets all the tasks for a given user by token
 * 
 * @author bensweett
 *
 */
public class GetUserTasks  extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateUserManager userManager;
	private HibernateTaskManager taskManager;

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
					actionResponse = buildResponseForTaskType(user, type);
					setResponse(actionResponse);
				}
			}

		} catch (Exception e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_ALL_TASKS, "error.GetUserTasksAction", e);
			fail = new Failure("Exception", e.getLocalizedMessage());
			actionResponse = new JSONResponse(fail);
			setResponse(actionResponse);
		}

		return "response";
	}
	
	private JSONResponse buildResponseForTaskType(User user, String type) {
		TaskType tasktype = TaskType.fromString(type);
		taskManager = getTaskManager();
		Set<Task> userTaskSet = null;
		
		if(tasktype != null && tasktype != TaskType.UNKNOWN) {
			userTaskSet = user.getTasksWithType(tasktype);
			bulkTaskUpdate(userTaskSet);
			return new JSONResponse(user.getTasksWithType(tasktype));
		} else {
			userTaskSet = user.getTaskSet();
			bulkTaskUpdate(userTaskSet);
			return new JSONResponse(user.getTaskSet());
		}
		
	}
	
	private void bulkTaskUpdate(Set<Task> userTaskSet) {
		for(Task task : userTaskSet) {
			task.updateNextAlertTime();
			boolean result = taskManager.updateTask(task);
			
			if(!result) {
				ServerLogger.getDefault().warn(this, Messages.METHOD_GET_ALL_TASKS, "Updating a task while fetching failed", null);
			}
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
