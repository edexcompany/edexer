package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.User;
import com.edexer.model.UserStatus;

@Repository("userStatusEntityDaoImpl")
public class UserStatusEntityDaoImpl extends GenericEntityDaoImpl<User> {
	private static final Logger logger = Logger
			.getLogger(UserStatusEntityDaoImpl.class);
	
	public List<UserStatus> getUserStatusList() {
		Session session = sessionFactory.getCurrentSession();
		List<UserStatus> list = null;
		Query q = session.createQuery("from UserStatus");
		logger.info("Returning from getAdminNotes");
		List<UserStatus> result = q.list();
		return result;
	}
}
