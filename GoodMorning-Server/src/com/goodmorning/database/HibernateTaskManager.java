package com.goodmorning.database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.goodmorning.enums.TaskType;
import com.goodmorning.models.Task;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;

public class HibernateTaskManager extends HibernateDatabaseManager {

	private static HibernateTaskManager manager;
	
	private final String TASK_TABLE_NAME = "TASKS";
	private final String DROP_TASK_TABLE = "drop table " + getTableName() + ";";
	private final String CREATE_TASK_TABLE = "create table " + getTableName() + "(TASK_ID_PK char(36) primary key, NAME tinytext,"
			+ "CREATION_TIME timestamp, NEXT_ALERT_TIME timestamp, ALERT_TIME time, TASK_TYPE enum('CHORE', 'TRAVEL', 'ENTERTAINMENT', 'ALARM', 'UNKNOWN'), ALERT_TYPE enum('NOTIFICATION', 'SOUND', 'VIBERATE', 'ALL', 'NONE')," 
			+ "MON bit, TUE bit, WED bit, THU bit, FRI bit, SAT bit, SUN bit, NOTES tinytext, USER_ID_FK char(36));";
	
	private final String TASK_CLASS_NAME = "Task";
	
	private final String SELECT_TASK_WITH_TASKID = "from " + getClassName() + " as task where task.taskId = :taskId";
	private final String SELECT_TASK_WITH_TASKID_AND_TYPE = "from " + getClassName() + " as task where task.taskId = :taskId and where task.taskType = :type";
	private final String SELECT_LIST_WITH_USERID = "from " + getClassName() + " as task where task.userId = :userId";
	
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
	
	/**
	 * Get an existing task by task id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Task getTaskById(String id) {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TASK_WITH_TASKID);
			query.setParameter("taskId", id);
			List<Task> tasks = query.list();
			transaction.commit();

			if (tasks.isEmpty()) {
				return null;
			} else {
				Task task = tasks.get(0);
				return task;
			}
		} catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_TASK_BY_TASKID, "error.getTaskByIdFromDatabase", exception);
			return null;
		} finally {
			closeSession();
		} 
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Task getTaskByIdAndType(String id, TaskType type) {
		
		Session session = null;
		Transaction transaction = null;
		
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_TASK_WITH_TASKID_AND_TYPE);
			query.setParameter("taskId", id);
			query.setParameter("taskType", type.toString());
			List<Task> tasks = query.list();
			transaction.commit();

			if (tasks.isEmpty()) {
				return null;
			} else {
				Task task = tasks.get(0);
				return task;
			}
		} catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_TASK_BY_TASKID, "error.getTaskByIdFromDatabase", exception);
			return null;
		} finally {
			closeSession();
		} 
	}
	
	//TODO: Only add after a task of the same time within 60 seconds does not exist
	@SuppressWarnings("unchecked")
	public synchronized boolean add(Object object)  {
		System.out.println("====== Beginnning to Add Task =======");
		Transaction transaction = null;
		Session session = null;
		Task task = (Task) object;
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			session.save(task);
			transaction.commit();
			return true;
			
			/*
			Query query = session.createQuery(SELECT_LIST_WITH_USERID);
		 	query.setParameter("userId", task.getUserId());
			List<Task> taskList = query.list();

			if (!taskList.isEmpty()) {
				System.out.println("Adding Task to DB Failed\n");
				return false;
			}
			
			session.save(task);
			transaction.commit();
			System.out.println("Added Task to TaskList\n");
			return true;*/

		} catch (HibernateException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_TASK, "error.addTaskToTaskList", exception);
			rollback(transaction);
			return false;
			
		} finally {
			closeSession();
		}
	}

}
