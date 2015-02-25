package com.goodmorning.models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.goodmorning.enums.RSSType;
import com.goodmorning.util.Messages;

public class RSSFeed {
	
	private static Logger LOGGER = Logger.getLogger(RSSFeed.class.getName());
	
	private String feedId;
	private String title;
	private String link;
	private String description;
	private String language;
	private String source;
	private Timestamp pubDate;
	private RSSType type;

	private Timestamp creationTimestamp;
	private Timestamp lastActiveTimestamp;
	private transient User user;

	public RSSFeed() {
		setTitle(Messages.UNKNOWN);
		setLink(Messages.UNKNOWN);
		setDescription(Messages.UNKNOWN);
		setLanguage(Messages.UNKNOWN);
		setSource(Messages.UNKNOWN);
		setType(RSSType.OTHER);
		
		// User is null when this is called
		Calendar now = Calendar.getInstance();
		setPubDate(new Timestamp(now.getTimeInMillis()));
		
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		setLastActiveTimestamp(new Timestamp(now.getTimeInMillis()));
	}
	
	public RSSFeed(String title, String link, RSSType type, String description, String language, String source, String pubDate, User user) {
		setTitle(title);
		setLink(link);
		setDescription(description);
		setLanguage(language);
		setSource(source);
		setPubDateFromString(pubDate);
		setType(type);
		
		setUser(user);
		
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		setLastActiveTimestamp(new Timestamp(now.getTimeInMillis()));
	}
	
	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	/*
	public List<RSSMessage> getMessages() {
		return entries;
	}
	
	public void setMessages(List<RSSMessage> messages) {
		this.entries = messages;
	}
	
	public boolean addMessage(RSSMessage message) {

		try{
			this.entries.add(message);
		} catch (Exception e) {
			LOGGER.warning("Exception adding task: " + e.getLocalizedMessage());
			return false;
		}

		return true;

	}*/

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String des) {
		this.description = des;
	}

	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String lang) {
		this.language = lang;
	}

	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public Timestamp getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(Timestamp pubDate) {
		this.pubDate = pubDate;
	}

	public RSSType getType() {
		return type;
	}
	
	public void setType(RSSType type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Timestamp getLastActiveTimestamp() {
		return lastActiveTimestamp;
	}

	public void setLastActiveTimestamp(Timestamp lastActiveTimestamp) {
		this.lastActiveTimestamp = lastActiveTimestamp;
	}
	
	// TODO: test for alternate format
	public String pubDateToString() {
		
		String result = "";
		
		try {
			result = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date((this.pubDate.getTime())));
		} catch(Exception e) {
			LOGGER.warning("Exception parsing pubDate to string: " + e.getLocalizedMessage());
		}
		
		return result;
	}
	
	// TODO: test for alternate format
	/*
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dts = p_date.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)","$1$2");
        return formatter.parse(dts);*/
	public void setPubDateFromString(String date) {
		
		//yyyy-MM-dd'T'HH:mm:ss.SSSZ
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		
		try {
			this.pubDate = new Timestamp(formatter.parse(date).getTime());
		} catch(Exception e) {
			LOGGER.warning("Exception parsing string to pubDate: " + e.getLocalizedMessage());
		}
		
	}
	
	@Override
	public String toString() {
		return "Feed [source=" + source + ", description=" + description
				+ ", language=" + language + ", link=" + link + ", pubDate="
				+ pubDate + ", title=" + title + "]";
	}

}
