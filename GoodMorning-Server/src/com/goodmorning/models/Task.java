package com.goodmorning.models;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.goodmorning.database.DatabaseEncryptionManager;
import com.goodmorning.enums.AlertType;
import com.goodmorning.enums.RepeatType;
import com.goodmorning.enums.TaskType;
import com.goodmorning.util.Messages;

public class Task {
	
	private String taskId;
	private Timestamp creationTimestamp;
	private Timestamp nextAlertTimestamp;
	private TaskType taskType;
	private Time alertTime;
	private AlertType alertType;
	private RepeatType repeatType;
	private String Notes;
	private String userId;
	
	// TODO: Set TaskHandler on client side
	public Task() {
		Calendar now = Calendar.getInstance();
		setCreationTimestamp(new Timestamp(now.getTimeInMillis()));
		setNextAlertTimestamp(new Timestamp(now.getTimeInMillis()));
		setTaskType(TaskType.UNKNOWN);
		setAlertType(AlertType.NONE);
		setRepeatType(RepeatType.NONE);
		setNotes(Messages.UNKNOWN);
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}
	
	public Task encryptData() {
		try {
			this.setNotes(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getNotes()));
			this.setTaskId(DatabaseEncryptionManager.getInstance().encryptPlainText(this.getTaskId()));
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO: Handle exceptions
			e.printStackTrace();
		}
		
		return this;
	}
	
	public Task decryptData() {
		try {
			this.setNotes(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getNotes()));
			this.setTaskId(DatabaseEncryptionManager.getInstance().decryptCipherText(this.getTaskId()));
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}
