/**
 * @author Karim
 * This is the User specific DAO class
 */
package com.edexer.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.User;
import com.edexer.model.UserSubscription;

/**
 * This class responsible for handling all db operations for user table.
 * 
 * @author Karim
 *
 */
@Repository("userEntityDao")
public class UserEntityDaoImpl extends GenericEntityDaoImpl<User> {

	private static final Logger logger = Logger
			.getLogger(UserEntityDaoImpl.class);

	ResourceBundle settings = ResourceBundle.getBundle("settings");

	public User getUser(String userEmail, String userPassword)
			throws HibernateException {
		logger.info("Trying to get user with email = " + userEmail);
		Session session = sessionFactory.getCurrentSession();
		User u = null;
		String query = "from User where user_email = :email and password = :pass and status = :status";
		logger.debug(query);
		logger.debug("email: " + userEmail);
		logger.debug("pass: " + userPassword);
		logger.debug("status: "
				+ Integer.valueOf(settings.getString("USER_STATUS_ACTIVE")));
		Query q = session.createQuery(query);
		q.setParameter("email", userEmail);
		q.setParameter("pass", userPassword);
		q.setParameter("status",
				Integer.valueOf(settings.getString("USER_STATUS_ACTIVE")));
		@SuppressWarnings("unchecked")
		List<User> l = (ArrayList<User>) q.list();
		if (l != null && l.size() != 0) {
			logger.info("Found user with email = " + userEmail + " in the db");
			u = (User) l.get(0);
		}
		logger.info("returning from getUser ");
		if (u != null) {
			Hibernate.initialize(u.getRole());
			Hibernate.initialize(u.getUserSubscriptionsForUserId());
			Hibernate.initialize(u.getMailConfigs());
			// Hibernate.initialize(u.getUserSubscriptionsForAdminUser());
		}
		return u;
	}

	public User getUserByUserId(int userId) throws HibernateException {
		Session session = sessionFactory.getCurrentSession();
		List<User> list = null;
		Query q = session.createQuery("from User where user_id=:userId");
		q.setParameter("userId", userId);
		User u = null;
		list = (ArrayList<User>) q.list();
		if (list != null && list.size() != 0) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			u = (User) list.get(0);
		}
		logger.info("returning from getUser ");
		Hibernate.initialize(u.getRole());
		Hibernate.initialize(u.getUserSubscriptionsForUserId());
		Hibernate.initialize(u.getMailConfigs());
		Hibernate.initialize(u.getUserSubscriptionsForSubscriptionOwner());

		// Hibernate.initialize(u.getUserSubscriptionsForAdminUser());
		return u;
	}

	public User getUserByUserEmail(String userEmail) throws HibernateException {
		Session session = sessionFactory.getCurrentSession();
		List<User> list = null;
		Query q = session.createQuery("from User where user_email=:userEmail");
		q.setParameter("userEmail", userEmail);
		User u = null;
		list = (ArrayList<User>) q.list();
		if (list != null && list.size() != 0) {
			logger.info("Found user with email = " + userEmail + " in the db");
			u = (User) list.get(0);
			Hibernate.initialize(u.getRole());
			Hibernate.initialize(u.getUserSubscriptionsForUserId());
			Hibernate.initialize(u.getMailConfigs());
		}
		logger.info("returning from getUser ");
		return u;
	}

	//

	public boolean isUserExists(String userEmail) throws HibernateException {
		return getUserByUserEmail(userEmail) != null ? true : false;
	}

	public List<User> getUserList() {
		Session session = sessionFactory.getCurrentSession();
		List<User> list = null;
		Query q = session.createQuery("from User where status != :status");
		q.setParameter("status",
				Integer.valueOf(settings.getString("USER_STATUS_DELETED")));
		list = (ArrayList<User>) q.list();
		logger.info("Returning from getAdminNotes");
		for (User user : list) {
			Hibernate.initialize(user.getRole());
		}
		return list;
	}

	public List<User> getAllUserList() {
		Session session = sessionFactory.getCurrentSession();
		List<User> list = null;
		Query q = session.createQuery("from User");
		list = (ArrayList<User>) q.list();
		logger.info("Returning from getAdminNotes");
		for (User user : list) {
			Hibernate.initialize(user.getRole());
		}
		return list;
	}

	/*
	 * change staff password for target user using new password and user_id
	 */
	public boolean updateStaffPassword(String newPassword, int user_id) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createSQLQuery("update user set password =:newPassword where user_id=:userId");
		q.setParameter("newPassword", newPassword);
		q.setParameter("userId", user_id);
		q.executeUpdate();
		return true;
	}

	public List<User> getCorporateUsers(int ownerId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL getUsersForCorporateSubscription(:ownerId)")
				.addEntity(User.class).setParameter("ownerId", ownerId);
		List<User> result = query.list();
		if (result != null) {
			logger.debug("Corporate users found, count = " + result.size());
			for (User user : result) {
				initializeUser(user);
			}
			return result;
		}
		return null;
	}

	public User initializeUser(User user) {
		User u = new User();
		Hibernate.initialize(u.getMailConfigs());
		Hibernate.initialize(u.getRole());
		Hibernate.initialize(u.getUserStatus());
		Hibernate.initialize(u.getUserSubscriptionsForLastEditBy());
		Hibernate.initialize(u.getUserSubscriptionsForSubscriptionOwner());
		Hibernate.initialize(u.getUserSubscriptionsForUserId());
		return u;
	}
}
