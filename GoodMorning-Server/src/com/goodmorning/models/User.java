package com.goodmorning.models;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.joda.time.LocalTime;

import com.goodmorning.enums.AlertType;
import com.goodmorning.enums.TaskType;
import com.goodmorning.util.Messages;

public class User {

	private static Logger LOGGER = Logger.getLogger(User.class.getName());

	private String userId;
	private String deviceId;
	private String userToken;
	private String nickname;
	private String email;
	private Timestamp creationDate;
	private Timestamp lastActive;

	private Set<RSSFeed> rssFeeds = new HashSet<RSSFeed>(0);
	private Set<Task> taskSet = new HashSet<Task>(0);

	public User() {
		Calendar now = Calendar.getInstance();
		setCreationDate(new Timestamp(now.getTimeInMillis()));
		setLastActive(new Timestamp(now.getTimeInMillis()));
		setNickname(Messages.UNKNOWN);
		setUserId(UUID.randomUUID().toString());
		setEmail(Messages.UNKNOWN);
		setDeviceId(Messages.UNKNOWN);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getLastActive() {
		return lastActive;
	}

	public void setLastActive(Timestamp lastActive) {
		this.lastActive = lastActive;
	}

	public Set<Task> getTaskSet() {
		return taskSet;
	}

	public void setTaskSet(Set<Task> taskSet) {
		this.taskSet = taskSet;
	}

	public boolean addTask(Task task) {

		try{
			this.taskSet.add(task);
		} catch (Exception e) {
			LOGGER.warning("Exception adding task: " + e.getLocalizedMessage());
			return false;
		}

		return true;

	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RSSFeed> getRssFeeds() {
		return rssFeeds;
	}

	public void setRssFeeds(Set<RSSFeed> rssFeeds) {
		this.rssFeeds = rssFeeds;
	}

	public boolean addRssFeed(RSSFeed feed) {

		try{
			this.rssFeeds.add(feed);
		} catch (Exception e) {
			LOGGER.warning("Exception adding feed: " + e.getLocalizedMessage());
			return false;
		}

		return true;

	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void generateNewUserToken() {
		setUserToken(this.getUserId() + "$" + this.getDeviceId() + "$" + this.getCreationDate());
	}

	public final int getTaskCount() {
		return getTaskSet().size();
	}

	public String getUserIdFromToken() {
		String plainToken = this.getUserToken();
		String[] str_array = plainToken.split("\\$");
		if(str_array == null || str_array.length < 3) return "unknown";
		return str_array[0];
	}

	//Gets the password from a token
	public String getDeviceIdFromToken() {
		String plainToken = this.getUserToken();
		String[] str_array = plainToken.split("\\$");
		if(str_array == null || str_array.length < 3) return "unknown";
		return str_array[1];
	}

	//Gets the time from a token
	public String getTokenTime() {
		String plainToken = this.getUserToken();
		String[] str_array = plainToken.split("\\$");
		if(str_array == null || str_array.length < 3) return "unknown";
		return str_array[2];
	}

	
	/**
	 * Creates tasks for initial alarms and adds them to the users task list
	 * 
	 * @return true if all tasks were added without a problem
	 */
	public boolean buildInitialTasks() {

		try {
			
			LocalTime time = new LocalTime(7,0); 
			Task task1 = new Task(TaskType.ALARM, AlertType.SOUND, "Wake Up", time, this);

			time.plusMinutes(30);
			Task task2 = new Task(TaskType.ALARM, AlertType.SOUND, "Out of Bed", time, this);

			time.plusHours(1);
			Task task3 = new Task(TaskType.ALARM, AlertType.SOUND, "Time to leave", time, this);

			time = new LocalTime(0,0);
			Task task4 = new Task(TaskType.ALARM, AlertType.SOUND, "Time for Bed", time, this);

			return this.addTask(task1) && this.addTask(task2) && this.addTask(task3) && this.addTask(task4);
			
		} catch (Exception e) {
			System.out.println("Exception building tasks");
			return false;
		}

	}
}
