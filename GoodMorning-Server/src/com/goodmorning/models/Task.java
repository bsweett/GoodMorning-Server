package com.goodmorning.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.goodmorning.enums.DeepLinkType;
import com.goodmorning.enums.TaskType;
import com.goodmorning.util.Messages;

public class Task {
	
	private String taskId;
	private String name;
	private Timestamp creationTimestamp;
	private Timestamp nextAlertTimestamp;
	private TaskType taskType;
	private DeepLinkType deepLink;
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
		setDeepLink(DeepLinkType.NONE);
	}
	
	public Task(TaskType taskType, String soundName, String name, LocalTime time, User user) {
		
		setName(name);
		setUser(user);
		
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		
		setAlertTimeFromLocal(time);

		//DateTime today = time.toDateTimeToday();
		//today.plusDays(1);
		setNextAlertTimestempFromLocalDate(time.toDateTimeToday().toLocalDateTime());
		
		setTaskType(taskType);
		setSoundFileName(soundName);
		initAllDays(false);
		setNotes("");
		setDeepLink(DeepLinkType.NONE);
		
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

	public DeepLinkType getDeepLink() {
		return deepLink;
	}

	public void setDeepLink(DeepLinkType deepLink) {
		this.deepLink = deepLink;
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
	
	public boolean isNeverRepeated() {
		if(!isSunday() && !isSaturday() && !isFriday() && !isThursday() && !isWednesday() && !isTuesday() && !isMonday()) {
			return true;
		}
		
		return false;
	}
	
	public boolean isRepeatedEveryday() {
		if(isSunday() && isSaturday() && isFriday() && isThursday() && isWednesday() && isTuesday() && isMonday()) {
			return true;
		}
		
		return false;
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

	public void updateNextAlertTime() {
		Timestamp oldAlertTime = getNextAlertTimestamp();
		Calendar now = Calendar.getInstance();
		Timestamp nowTime = new Timestamp(now.getTimeInMillis());
		LocalTime timeToFire = getLocalAlertTime();
		DateTime dtOrg = new DateTime(now);
		
		if(oldAlertTime.before(nowTime)) {
			
			// If its never repeated and already passed update the next alert till the next day
			// This will only be used for alarms. Other tasks will never be alerted again
			if(isRepeatedEveryday() || (isNeverRepeated() && getTaskType() == TaskType.ALARM)) {
				DateTime dtPlusOne = dtOrg.plusDays(1);
				dtPlusOne = dtPlusOne.withTime(timeToFire.getHourOfDay(), timeToFire.getMinuteOfHour(), 0, 0);
				setNextAlertTimestempFromLocalDate(dtPlusOne.toLocalDateTime());
			} else {
				checkDayOfWeekForUpdate(dtOrg.toLocalDateTime(), timeToFire);
			}
		}
	}
	
	private void checkDayOfWeekForUpdate(LocalDateTime now, LocalTime fireTime) {	
		LocalDateTime mon = now.withDayOfWeek(DateTimeConstants.MONDAY);
		mon = mon.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime tues = now.withDayOfWeek(DateTimeConstants.TUESDAY);
		tues = tues.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime wed = now.withDayOfWeek(DateTimeConstants.WEDNESDAY);
		wed = wed.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime thu = now.withDayOfWeek(DateTimeConstants.THURSDAY);
		thu = thu.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime fri = now.withDayOfWeek(DateTimeConstants.FRIDAY);
		fri = fri.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime sat = now.withDayOfWeek(DateTimeConstants.SATURDAY);
		sat = sat.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		LocalDateTime sun = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		sun = sun.withTime(fireTime.getHourOfDay(), fireTime.getMinuteOfHour(), 0, 0);
		
		if(isMonday() && now.isBefore(mon)) {
			setNextAlertTimestempFromLocalDate(mon);
		}  
		
		if(isTuesday() && now.isBefore(tues)) {
			setNextAlertTimestempFromLocalDate(tues);
		}
		
		if(isWednesday() && now.isBefore(wed))  {
			setNextAlertTimestempFromLocalDate(wed);
		}  
		
		if(isThursday() && now.isBefore(thu))  {
			setNextAlertTimestempFromLocalDate(thu);
		} 

		if(isFriday() && now.isBefore(fri))  {
			setNextAlertTimestempFromLocalDate(fri);
		} 

		if(isSaturday() && now.isBefore(sat))  {
			setNextAlertTimestempFromLocalDate(sat);
		} 

		if(isSunday() && now.isBefore(sun))  {
			setNextAlertTimestempFromLocalDate(sun);
		}
	}
	
	@SuppressWarnings("unused")
	private LocalDateTime calcNextPossibleDay(final LocalDateTime d, final int dayConstant) {
		  return d.plusWeeks(d.getDayOfWeek() < dayConstant ? 0 : 1).withDayOfWeek(dayConstant);
	}
}
