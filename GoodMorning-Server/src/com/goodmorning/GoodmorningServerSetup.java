package com.goodmorning;

import com.goodmorning.database.HibernateTaskManager;
import com.goodmorning.database.HibernateUserManager;

public class GoodmorningServerSetup {

	public static void main(String[] args) {
		HibernateUserManager userManager = HibernateUserManager.getDefault();
		userManager.setupTable();
		
		HibernateTaskManager taskManager = HibernateTaskManager.getDefault();
		taskManager.setupTable();
	}
	
}
