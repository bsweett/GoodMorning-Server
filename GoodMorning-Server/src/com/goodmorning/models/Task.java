package com.goodmorning.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.goodmorning.enums.AlertType;
import com.goodmorning.enums.RepeatType;
import com.goodmorning.enums.TaskType;

public class Task {
	
	private String taskId;
	private Timestamp creationTimestamp;
	private Timestamp nextAlertTimestamp;
	private TaskType taskType;
	private Time alertTime;
	private AlertType alertType;
	private RepeatType repeatType;
	private User user;
	
	// TODO: Set TaskHandler on client side
	public Task() {
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		setNextAlertTimestamp(new Timestamp(now.getTimeInMillis()));
		setTaskType(TaskType.UNKNOWN);
		setAlertType(AlertType.NONE);
		setRepeatType(RepeatType.NONE);
	}
	
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}
	
	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public Time getAlertTimestamp() {
		return alertTime;
	}

	public void setAlertTimestamp(Time alertTime) {
		this.alertTime = alertTime;
	}
	
	public boolean equals(Task task) {
		return getTaskId().equals(task.getTaskId());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	public RepeatType getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}

	public Timestamp getNextAlertTimestamp() {
		return nextAlertTimestamp;
	}

	public void setNextAlertTimestamp(Timestamp nextAlertTimestamp) {
		this.nextAlertTimestamp = nextAlertTimestamp;
	}
}
