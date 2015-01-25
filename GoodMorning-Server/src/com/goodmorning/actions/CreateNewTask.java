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
import com.goodmorning.models.Task;
import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;
import com.goodmorning.util.Utility;
import com.opensymphony.xwork2.ActionSupport;

// TODO: Test adding a task

public class CreateNewTask extends ActionSupport implements StrutsAction {

	private static final long serialVersionUID = 1L;
	private JSONResponse response;
	private HttpServletRequest request;
	private HibernateTaskManager taskManager;
	private HibernateUserManager userManager;

	private final String parameter_1 = "token";
	private final String parameter_2 = "time";
	private final String parameter_3 = "days";
	private final String parameter_4 = "notes";	// Limit to 140 characters
	private final String parameter_5 = "type";
	private final String parameter_6 = "name";

	@Override
	public String execute() throws Exception {

		JSONResponse actionResponse;
		User user;
		Failure fail;
		Calendar now = Calendar.getInstance();

		try {

			String token = getServletRequest().getParameter(parameter_1);
			String time = getServletRequest().getParameter(parameter_2);
			String days = getServletRequest().getParameter(parameter_3);
			String notes = getServletRequest().getParameter(parameter_4);
			TaskType type = TaskType.fromString(getServletRequest().getParameter(parameter_5));
			String name = getServletRequest().getParameter(parameter_6);

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
					actionResponse = buildResponseCreateTaskForUser(user, time, days, notes, type, name);
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

	private JSONResponse buildResponseCreateTaskForUser(User user, String time, String days, String notes, TaskType type, String name) {
		Task task;
		Failure fail;

		taskManager = getTaskManager();
		task = taskManager.getTaskByIdAndType(name, type);

		if(task != null) {
			fail = new Failure("Task already exists", "The task name and type already exists");
			return new JSONResponse(fail);
		}
		
		task = new Task();
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
		
		task.setUser(user);
		user.addTask(task);
		
		if(userManager.update(user)) {
			return new JSONResponse("OK");
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
