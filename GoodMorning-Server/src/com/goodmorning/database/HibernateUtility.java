package com.goodmorning.database;

import java.io.File;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtility {
	
	private static SessionFactory sessionFactory;
	private static StandardServiceRegistryBuilder standardServiceRegistryBuilder;
	private static ServiceRegistry serviceRegistry; 

	/**
	 * Builds Hibernate session factory based on the Hibernate configuration file.
	 */
	private static void buildSessionFactory() {
		
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();				
				configuration.configure("hibernate.cfg.xml");
				standardServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
				serviceRegistry = standardServiceRegistryBuilder.build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (HibernateException ex) {
				throw new RuntimeException("Exception building SessionFactory: " + ex.getMessage(), ex);
			}
		}
	}

	public void clearSessionFactory() {

		if (sessionFactory != null) {
			try {
				sessionFactory.close();
			} catch (HibernateException ex) {
				sessionFactory = null;
				throw new RuntimeException("Exception closing SessionFactory: " + ex.getMessage(), ex);
			}
		}
		sessionFactory = null;
	}

	// Keeps track of all Hibernate threads
	public static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();

	/**
	 * Returns current Hibernate session based on the thread local.
	 * First looks into thread local for a session. If found returns the session.
	 * If not found creates new session and adds it to the thread local.
	 *
	 * @return
	 * @throws HibernateException
	 */
	public synchronized static Session getCurrentSession() throws HibernateException {

		Session session = (Session) sessionThreadLocal.get();

		// Open a new Session, if this Thread has none yet
		if (session == null) {
			buildSessionFactory();
			session = sessionFactory.openSession();
			session.setCacheMode(CacheMode.IGNORE);
			session.setFlushMode(FlushMode.COMMIT);
			sessionThreadLocal.set(session);
		}
		return session;
	}

	/**
	 * Closes current Hibernate session.
	 * Looks for a current session in thread local, and if found closes the session.
	 * Also sets thread local to null.
	 *
	 * @throws HibernateException
	 */
	public static void closeTheThreadLocalSession() throws HibernateException {

		Session session = (Session) sessionThreadLocal.get();
		sessionThreadLocal.set(null);
		if (session != null) {
			session.close();
		}
		session = null;
	}


	/**
	 * Used for initial table setup in the database.
	 *
	 * @param sql
	 */
	public static synchronized boolean executeSQLQuery(String sql) {

		Session session = null;
		Transaction transaction = null;

		try {
			session = getCurrentSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			closeTheThreadLocalSession();
		}
		return true;
	}
}
