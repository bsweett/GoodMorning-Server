package com.goodmorning.database;


public class GoodmorningServerSetup {

	public static void main(String[] args) {
		HibernateUserManager userManager = HibernateUserManager.getDefault();
		userManager.setupTable();
		
		HibernateTaskManager taskManager = HibernateTaskManager.getDefault();
		taskManager.setupTable();
		
		HibernateRSSFeedManager rssManager = HibernateRSSFeedManager.getDefault();
		rssManager.setupTable();
	}
	
}
