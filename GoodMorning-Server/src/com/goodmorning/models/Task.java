package com.goodmorning.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.goodmorning.enums.TaskType;

public class Task {
	
	private String taskId;
	private Timestamp creationTimestamp;
	private TaskType taskType;
	private TaskHandler taskHandler;
	private Time alertTime;
	private boolean repeatDaily;
	private boolean repeatWeekly;
	
	public Task() {
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		setTaskType(TaskType.UNKNOWN);
		setTaskHandler(null);
		setRepeatDaily(false);
		setRepeatWeekly(false);
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

	public TaskHandler getTaskHandler() {
		return taskHandler;
	}

	public void setTaskHandler(TaskHandler taskHandler) {
		this.taskHandler = taskHandler;
	}

	public Time getAlertTimestamp() {
		return alertTime;
	}

	public void setAlertTimestamp(Time alertTime) {
		this.alertTime = alertTime;
	}

	public boolean isRepeatDaily() {
		return repeatDaily;
	}

	public void setRepeatDaily(boolean repeatDaily) {
		this.repeatDaily = repeatDaily;
	}

	public boolean isRepeatWeekly() {
		return repeatWeekly;
	}

	public void setRepeatWeekly(boolean repeatWeekly) {
		this.repeatWeekly = repeatWeekly;
	}
	
	public boolean equals(Task task) {
		return getTaskId().equals(task.getTaskId());
	}
}
