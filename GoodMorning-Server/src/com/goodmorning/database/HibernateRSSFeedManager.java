package com.goodmorning.database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.goodmorning.models.RSSFeed;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;

public class HibernateRSSFeedManager extends HibernateDatabaseManager {

	private static HibernateRSSFeedManager manager;
	
	private final String FEED_TABLE_NAME = "RSSFEEDS";
	private final String DROP_FEED_TABLE = "drop table " + getTableName() + ";";
	
	// TODO: Add RSS_TYPE
	private final String CREATE_FEED_TABLE = "create table " + getTableName() + "(FEED_ID_PK char(36) primary key, TITLE tinytext,"
			+ "CREATION_TIME timestamp, LAST_ACTIVE timestamp, LINK tinytext, DESCRIPTION tinytext,"
			+ "RSS_TYPE enum('NEWS', 'WEATHER', 'BUSINESS', 'SCIENCE', 'TECHNOLOGY', 'SPORTS', 'LIFESTYLE', 'ENTERTAINMENT', 'OTHER')," 
			+ "LANGUAGE tinytext, PUB_DATE timestamp, COPYRIGHT tinytext, USER_ID_FK char(36));";

	
	private final String FEED_CLASS_NAME = "RSSFeed";
	
	private final String SELECT_FEED_WITH_FEEDID = "from " + getClassName() + " as rssfeed where rssfeed.feedId = :feedId";
	
	HibernateRSSFeedManager() {
		super();
	}
	
	public static HibernateRSSFeedManager getDefault() {
		if(manager == null) {
			manager = new HibernateRSSFeedManager();
		}
		return manager;
	}
	
	@Override
	public boolean setupTable() {
		HibernateUtility.executeSQLQuery(DROP_FEED_TABLE);
		return HibernateUtility.executeSQLQuery(CREATE_FEED_TABLE);
	}

	@Override
	public String getClassName() {
		return FEED_CLASS_NAME;
	}

	@Override
	public String getTableName() {
		return FEED_TABLE_NAME;
	}
	
	/**
	 * Get an existing feed by feed id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized RSSFeed getFeedById(String id) {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_FEED_WITH_FEEDID);
			query.setParameter("feedId", id);
			List<RSSFeed> feeds = query.list();
			transaction.commit();

			if (feeds.isEmpty()) {
				return null;
			} else {
				RSSFeed feed = feeds.get(0);
				return feed;
			}
		} catch (HibernateException exception) {
			rollback(transaction);
			ServerLogger.getDefault().severe(this, Messages.METHOD_GET_RSSFEED_BY_FEEDID, "error.getRSSFeedByIdFromDatabase", exception);
			return null;
		} finally {
			closeSession();
		} 
	}
	
	public synchronized boolean add(Object object)  {
		System.out.println("====== Beginnning to Add Feed =======");
		Transaction transaction = null;
		Session session = null;
		RSSFeed feed = (RSSFeed) object;
		
		try {
			session = HibernateUtility.getCurrentSession();
			transaction = session.beginTransaction();
			session.save(feed);
			transaction.commit();
			return true;

		} catch (HibernateException exception) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_ADD_RSSFEED, "error.addRSSFeedToList", exception);
			rollback(transaction);
			return false;
			
		} finally {
			closeSession();
		}
	}

}
