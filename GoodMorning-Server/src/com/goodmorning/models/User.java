package com.goodmorning.models;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.goodmorning.database.DatabaseEncryptionManager;
import com.goodmorning.util.Messages;

public class User {
	
	private String userId;
	private String deviceId;
	private String userToken;
	private String nickname;
	private Timestamp creationDate;
	private Timestamp lastActive;
	
	private Set<RSSFeed> rssFeeds = new HashSet<RSSFeed>(0);
	
	private Set<User> household = new HashSet<User>(0);
	private Set<Task> taskSet = new HashSet<Task>(0);
	
	public User() {
		Calendar now = Calendar.getInstance();
		setCreationDate(new Timestamp(now.getTimeInMillis()));
		setLastActive(new Timestamp(now.getTimeInMillis()));
		setNickname(Messages.UNKNOWN);
		setUserId(UUID.randomUUID().toString());	// Using Java UUID to randomly generate the user ids
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

	public Set<RSSFeed> getRssFeeds() {
		return rssFeeds;
	}

	public void setRssFeeds(Set<RSSFeed> rssFeeds) {
		this.rssFeeds = rssFeeds;
	}

	public Set<User> getHousehold() {
		return household;
	}

	public void setHousehold(Set<User> household) {
		this.household = household;
	}
	
	public void addHouseMember(User user) {
		getHousehold().add(user);
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
	
	public User encryptData() {
		try {
			this.setNickname(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getNickname()));
			this.setDeviceId(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getDeviceId()));
			this.setUserId(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getUserId()));
			this.setUserToken(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getUserToken()));
		} catch(InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO: Handle Exceptions
			e.printStackTrace();
		}
		
		return this;
	}
	
	public User decryptData() {
		try {
			this.setNickname(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getNickname()));
			this.setDeviceId(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getDeviceId()));
			this.setUserId(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getUserId()));
			this.setUserToken(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getUserToken()));
		} catch(InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO: Handle Exceptions
			e.printStackTrace();
		}
		
		return this;
	}
}
