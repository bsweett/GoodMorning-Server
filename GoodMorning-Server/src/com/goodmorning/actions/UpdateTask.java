package com.goodmorning.actions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.goodmorning.database.HibernateTaskManager;
import com.goodmorning.database.HibernateUserManager;
import com.goodmorning.enums.TaskType;
import com.goodmorning.models.Failure;
import com.goodmorning.models.JSONResponse;
import com.goodmorning.models.SuccessMessage;
import com.goodmorning.models.Task;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.goodmorning.util.Utility;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateTask extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateTaskManager taskManager;
	private HibernateUserManager userManager;

	private final String parameter_1 = "token";
	private final String parameter_2 = "id";
	private final String parameter_3 = "time";
	private final String parameter_4 = "days";
	private final String parameter_5 = "notes";	// Limit to 140 characters
	private final String parameter_6 = "type";
	private final String parameter_7 = "name";

	
	// TODO: Test User Task Update
	@Override
	public String execute() throws Exception {

		JSONResponse actionResponse;
		User user;
		Failure fail;
		Task task;
		Calendar now = Calendar.getInstance();

		try {

			String token = getServletRequest().getParameter(parameter_1);
			String taskId = getServletRequest().getParameter(parameter_2);
			String time = getServletRequest().getParameter(parameter_3);
			String days = getServletRequest().getParameter(parameter_4);
			String notes = getServletRequest().getParameter(parameter_5);
			TaskType type = TaskType.fromString(getServletRequest().getParameter(parameter_6));
			String name = getServletRequest().getParameter(parameter_7);

			if(token.isEmpty() || taskId.isEmpty() || time.isEmpty() || days.isEmpty() || type == null || name.isEmpty()) {
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

					taskManager = getTaskManager();
					
					task = taskManager.getTaskById(taskId);
					
					if(task == null) {
						
						fail = new Failure("Task not found", "The task id provided did not return a task for the given user token");
						actionResponse = new JSONResponse(fail);
						setResponse(actionResponse);
						
					} else {
						
						user.setLastActive(new Timestamp(now.getTimeInMillis()));
						System.out.println("User: " + user.toString());
						
						actionResponse = buildResponseCreateTaskForUser(user, task, time, days, notes, type, name);
						setResponse(actionResponse);
						
					}
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

	private JSONResponse buildResponseCreateTaskForUser(User user, Task task, String time, String days, String notes, TaskType type, String name) {
		Failure fail;

		task.setName(name);
		task.setNotes(notes);
		task.setTaskType(type);

		ArrayList<Boolean> DoTW = Utility.splitDaysString(days);
		task.setMonday(DoTW.get(0));
		task.setTuesday(DoTW.get(1));
		task.setWednesday(DoTW.get(2));
		task.setThursday(DoTW.get(3));
		task.setFriday(DoTW.get(4));
		task.setSaturday(DoTW.get(5));
		task.setSunday(DoTW.get(6));	

		try {
			task.setAlertTime(Utility.stringToTime(time));
		} catch (NullPointerException e) {
			fail = new Failure("Exception", "Invalid time caused a null pointer");
			return new JSONResponse(fail);
		}
		
		if(taskManager.updateTask(task)) {
			return new JSONResponse(new SuccessMessage(true));
		} 

		fail = new Failure("Database failure", "Failed to update user with task");
		return new JSONResponse(fail);
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
