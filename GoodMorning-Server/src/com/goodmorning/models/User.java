package com.goodmorning.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.goodmorning.util.Messages;

public class User {
	
	private String userId;
	private String userToken;
	private String nickname;
	private Timestamp creationDate;
	private Timestamp lastActive;
	
	private List<RSSFeed> rssFeeds = new ArrayList<RSSFeed>();
	
	private Set<Task> taskSet = new HashSet<Task>(0);
	
	public User() {
		Calendar now = Calendar.getInstance();
		setCreationDate(new Timestamp(now.getTimeInMillis()));
		setLastActive(new Timestamp(now.getTimeInMillis()));
		setNickname(Messages.UNKNOWN);
		
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

	public List<RSSFeed> getRssFeeds() {
		return rssFeeds;
	}

	public void setRssFeeds(List<RSSFeed> rssFeeds) {
		this.rssFeeds = rssFeeds;
	}
	
}
