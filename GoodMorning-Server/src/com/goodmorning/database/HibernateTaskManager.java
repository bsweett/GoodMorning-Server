package com.goodmorning.database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.goodmorning.models.Task;

public class HibernateTaskManager extends HibernateDatabaseManager {

	private static HibernateTaskManager manager;
	
	private final String TASK_TABLE_NAME = "TASKS";
	private final String DROP_TASK_TABLE = "drop table " + getTableName() + ";";
	private final String CREATE_TASK_TABLE = "create table " + getTableName() + "(TASK_ID_PK char(36) primary key,"
			+ "CREATION_TIME timestamp, ALERT_TIME time, TASK_TYPE enum('CHORE', 'TRAVEL', 'ENTERTAINMENT', 'ALARM', 'UNKNOWN'), ALERT_TYPE enum('NOTIFICATION', 'SOUND', 'VIBERATE', 'ALL', 'NONE')," 
			+ "REPEAT_TYPE enum('DAILY', 'WEEKLY', 'MONTHLY', 'NONE'), NOTES tinytext, USER_ID_FK char(36));";
	
	private final String TASK_CLASS_NAME = "Task";
	//private final String SELECT_INBOX_WITH_RECEIVER_EMAIL = "from "
	//		+ getClassName() + " as inbox where inbox.receiverEmail = :receiverEmail";
	private final String SELECT_LIST_WITH_USERID = "from " + getClassName() + " as task where task.receiverEmail = :receiverEmail";
	// TODO: How do I select the task table for the user?
	/*
	 * Consider having userId in task object.. or allow tasks to be sent to multiple users i.e. assigned to or something
	 */
	
	HibernateTaskManager() {
		super();
	}
	
	public static HibernateTaskManager getDefault() {
		if(manager == null) {
			manager = new HibernateTaskManager();
		}
		return manager;
	}
	
	@Override
	public boolean setupTable() {
		HibernateUtility.executeSQLQuery(DROP_TASK_TABLE);
		return HibernateUtility.executeSQLQuery(CREATE_TASK_TABLE);
	}

	@Override
	public String getClassName() {
		return TASK_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		return TASK_TABLE_NAME;
	}
	
	//TODO: Exception and error handling
	//TODO: Check relationship with
	/*
	@SuppressWarnings("unchecked")
	public synchronized boolean add(Object object) 
	{
		System.out.println("====== Beginnning to Add Task =======");
		Transaction transaction = null;
		Session session = null;
		Task task = (Task) object;
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_INBOX_WITH_RECEIVER_EMAIL);
		 	query.setParameter("receiverEmail", task.getUser().get);
			List<Task> taskList = query.list();
			System.out.println("Testing");

			if (!taskList.isEmpty()) {
				System.out.println("Adding Reel to DB Failed\n");
				return false;
			}
			
			session.save(task);
			System.out.println("session save");
			
			transaction.commit();
			System.out.println("Added Task to TaskList\n");
			return true;

		} catch (HibernateException exception) {
			BookingLogger.getDefault().severe(this, Messages.METHOD_ADD_INBOX,
					"error.addUserToDatabase", exception);

			rollback(transaction);
			System.out.println("ROLLBACK\n");
			return false;
		} finally {
			System.out.println("FINALLY\n");
			closeSession();
		}
	}*/

}
