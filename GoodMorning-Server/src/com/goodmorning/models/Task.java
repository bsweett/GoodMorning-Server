package com.goodmorning.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.goodmorning.enums.TaskType;
import com.goodmorning.util.Messages;

public class Task {
	
	private String taskId;
	private String name;
	private Timestamp creationTimestamp;
	private Timestamp nextAlertTimestamp;
	private TaskType taskType;
	private Time alertTime;
	private String soundFileName;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	private String Notes;
	private transient User user;	// Transient makes this not serializable for a stream of bytes
									// causes GSON to ignore it
	
	// TODO: Set TaskHandler on client side
	public Task() {
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		
		now.add(Calendar.DATE, 1);
		setNextAlertTimestamp(new Timestamp(now.getTimeInMillis()));	
		
		setAlertTime(new Time(now.getTimeInMillis()));
		setTaskType(TaskType.UNKNOWN);
		setSoundFileName(Messages.UNKNOWN);
		initAllDays(false);
		setNotes(Messages.UNKNOWN);
	}
	
	public Task(TaskType taskType, String soundName, String name, LocalTime time, User user) {
		
		setName(name);
		setUser(user);
		
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		
		setAlertTimeFromLocal(time);

		DateTime today = time.toDateTimeToday();
		today.plusDays(1);
		setNextAlertTimestempFromLocalDate(today.toLocalDateTime());
		
		setTaskType(taskType);
		setSoundFileName(soundName);
		initAllDays(false);
		setNotes("");
		
		System.out.println("completed init");
	}
	
	private void initAllDays(boolean value) {
		setMonday(value);
		setTuesday(value);
		setWednesday(value);
		setThursday(value);
		setFriday(value);
		setSaturday(value);
		setSunday(value);
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

	public Time getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Time alertTime) {
		this.alertTime = alertTime;
	}
	
	public void setAlertTimeFromLocal(LocalTime time) {
		this.alertTime = new Time(time.toDateTimeToday().getMillis());
	}
	
	public LocalTime getLocalAlertTime() {
		return new LocalTime(this.alertTime);
	}
	
	public boolean equals(Task task) {
		return getTaskId().equals(task.getTaskId());
	}
	
	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public Timestamp getNextAlertTimestamp() {
		return nextAlertTimestamp;
	}

	public void setNextAlertTimestamp(Timestamp nextAlertTimestamp) {
		this.nextAlertTimestamp = nextAlertTimestamp;
	}
	
	public LocalDateTime getNextAlertLocalDate() {
		return new LocalDateTime(this.nextAlertTimestamp);
	}
	
	public void setNextAlertTimestempFromLocalDate(LocalDateTime date) {
		this.nextAlertTimestamp = new Timestamp(date.toDateTime().getMillis());
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUserId() {
		if(this.user != null) {
			return this.user.getUserId();
		}
		
		return "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSoundFileName() {
		return soundFileName;
	}

	public void setSoundFileName(String soundFileName) {
		this.soundFileName = soundFileName;
	}
}
