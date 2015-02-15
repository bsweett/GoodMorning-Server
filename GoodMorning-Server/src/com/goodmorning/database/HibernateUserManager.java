package com.goodmorning.database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.goodmorning.models.User;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;

public class HibernateUserManager extends HibernateDatabaseManager{

	private static HibernateUserManager manager;
	
	private final String USER_TABLE_NAME = "USERS";
	private final String DROP_USER_TABLE = "drop table " + getTableName() + ";";
	private final String CREATE_USER_TABLE = "create table " + getTableName() + "(USER_ID_PK char(36) primary key,"
			+ "DEVICE_ID tinytext, TOKEN tinytext, NICKNAME tinytext, EMAIL tinytext, CREATION_TIME timestamp, LAST_ACCESSED_TIME timestamp);";
	
	private final String USER_CLASS_NAME = "User";
	private final String SELECT_USER_WITH_TOKEN = "from " + getClassName() + " as user where user.userToken = :userToken";
	private final String SELECT_USER_WITH_DEVICE = "from " + getClassName() + " as user where user.deviceId = :deviceId";
	private final String SELECT_NUMBER_USERS = "select count (*) from " + getClassName();

	private static DatabaseEncryptionManager encryptManager;
	
	HibernateUserManager() {
		super();
		HibernateUserManager.encryptManager = new DatabaseEncryptionManager();
	}
	
	public static HibernateUserManager getDefault() {
		if(manager == null) {
			manager = new HibernateUserManager();
		}
		return manager;
	}
	
	@Override
	public boolean setupTable() {
		HibernateUtility.executeSQLQuery(DROP_USER_TABLE);
		return HibernateUtility.executeSQLQuery(CREATE_USER_TABLE);
	}

	@Override
	public String getClassName() {
		return USER_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		return USER_TABLE_NAME; 
	}
	
	public synchronized boolean add(Object object) {
		
		System.out.println("====== Beginnning to Add User =======");
		Transaction transaction = null;
		Session session = null;
		User user = (User) object;
		//User enryptedUser = encryptUser(user);
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_USER_WITH_TOKEN);
		 	query.setParameter("userToken", user.getUserToken());
			@SuppressWarnings("unchecked")
			List<User> users = query.list();

			if (!users.isEmpty()) {
				System.out.println("RESULT: Failed\n");
				return false;
			}
				
			session.save(user);
			transaction.commit();
			System.out.println("RESULT: Successful\n");
			return true;

		} catch (HibernateException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_USER, "error.addUserToDatabase", exception);
			rollback(transaction);
			return false;
			
		} finally {
			closeSession();
		}
	}
	
	/**
	 * Updates given object (user).
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean updateUser(User user) {
		//User enryptedUser = encryptUser(user);
		boolean result = super.update(user);	
		return result;
	}
	
	/** TODO: This should be used to remove users if they want to reset their profile
	 * Deletes given user from the database.
	 * Returns true if successful, otherwise returns false.
	 * 
	 * @param object
	 * @return
	 */
	public synchronized boolean delete(User user){
		
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(user);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_DELETE_USER, "error.deleteUserFromDatabase", exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_DELETE_USER, "error.deleteUserFromDatabase", exception);
			return errorResult;
		}
		finally {
			closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized User getUserByToken(String token) {
		
		Session session = null;
		Transaction transaction = null;
		//String encryptedToken = encryptToken(token);
		
		System.out.println("Normal Token: " + token);
		//System.out.println("Enrypted token: " + encryptedToken);
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_USER_WITH_TOKEN);
			query.setParameter("userToken", token);
			List<User> users = query.list();
			transaction.commit();

			if (users.isEmpty()) {
				return null;
			} else {
				User user = users.get(0);
				return user;	//decryptUser(user);
			}
		} catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_USER_BY_TOKEN, "error.getUserByTokenFromDatabase", exception);
			return null;
		} finally {
			closeSession();
		} 
	}
	
	@SuppressWarnings("unchecked")
	public synchronized User getUserByDeviceId(String Id) {
		
		Session session = null;
		Transaction transaction = null;
		//String encryptedDeviceId = encryptManager.encrypt(Id);
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_USER_WITH_DEVICE);
			query.setParameter("deviceId", Id);
			List<User> users = query.list();
			transaction.commit();

			if (users.isEmpty()) {
				return null;
			} else {
				User user = users.get(0);
				return decryptUser(user);
			}
		} catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_USER_BY_DEVICE, "error.getUserByDeviceIdFromDatabase", exception);
			return null;
		} finally {
			closeSession();
		} 
	}
	
	public synchronized int getNumberOfUsers() {
		
		Session session = null;
		Long aLong;

		try {
			session = HibernateUtility.getCurrentSession();
			Query query = session.createQuery(SELECT_NUMBER_USERS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_USER_COUNT, "error.getTotalUsers", exception);
			return 0;
		} catch (HibernateException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_USER_COUNT, "error.getTotalUsers", exception);
			return 0;
		} catch (RuntimeException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_USER_COUNT, "error.getTotalUsers", exception);
			return 0;
		} finally {
			closeSession();
		}
	}
	
	private String encryptToken(String token) {
		return encryptManager.encrypt(token);
	}
	
	public User encryptUser(User user) {
		//user.setUserId(encryptManager.encrypt(user.getUserId()));
		user.setNickname(encryptManager.encrypt(user.getNickname()));
		user.setDeviceId(encryptManager.encrypt(user.getDeviceId()));
		user.setUserToken(encryptManager.encrypt(user.getUserToken()));
		user.setEmail(encryptManager.encrypt(user.getEmail()));
		
		return user;
	}
	
	public User decryptUser(User user) {
		//user.setUserId(encryptManager.decrypt(user.getUserId()));
		user.setNickname(encryptManager.decrypt(user.getNickname()));
		user.setDeviceId(encryptManager.decrypt(user.getDeviceId()));
		user.setUserToken(encryptManager.decrypt(user.getUserToken()));
		user.setEmail(encryptManager.decrypt(user.getEmail()));
		
		return user;
	}

}
