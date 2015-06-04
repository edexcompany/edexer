package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.component.subtable.SubTableRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edexer.model.Subscription;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

@Repository("userSubscriptionEntityDao")
public class UserSubscriptionEntityDaoImpl extends
		GenericEntityDaoImpl<UserSubscription> {

	@Autowired 
	SubscriptionEntityDaoImpl subscriptionDao;

	@Autowired
	UserEntityDaoImpl userDao;

	public List<UserSubscription> getUserSubscriptionsList(boolean loadRelations) {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<UserSubscription> list = null;
		Query q = session.createQuery("from UserSubscription");
		list = (ArrayList<UserSubscription>) q.list();
		// logger.info("Returning from getAdminNotes");
		if (loadRelations) {
			for (UserSubscription us : list) {
				Hibernate.initialize(us.getUserBySubscriptionOwner());
				Hibernate.initialize(us.getSubscriptionStatus());
				Hibernate.initialize(us.getActAs());
				Hibernate.initialize(us.getLastEditReason());
				// Hibernate.initialize(us.getUserByUserId());
				Hibernate.initialize(us.getUserByLastEditBy());
				Hibernate.initialize(us.getSubscription());
				us.setUserByUserId(userDao.get(User.class, us.getId()
						.getUserId()));
				// us.setSubscription(subscriptionDao.get(Subscription.class, us
				// .getId().getSubType()));
			}
		}
		return list;
	}

	public UserSubscription getUserSubscription(int userId, int subType) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("CALL getUserSubscription(:userId,:subType)")
				.addEntity(UserSubscription.class)
				.setParameter("userId", userId)
				.setParameter("subType", subType);
		List<UserSubscription> result = query.list();
		if (result != null && result.size() != 0) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result.get(0);
		}
		return null;
	}

	public List<UserSubscription> getUserSubscriptionsByUserId(int userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("CALL getUserSubscriptionsByUserId (:userId)")
				.addEntity(UserSubscription.class)
				.setParameter("userId", userId);
		List<UserSubscription> result = query.list();
		if (result != null) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result;
		}
		return null;
	}

	public List<UserSubscription> getUserSubscriptionsByUserId(int userId,
			int statusId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL getUserSubscriptionsByUserIdAndState (:userId,:statusId)")
				.addEntity(UserSubscription.class)
				.setParameter("userId", userId)
				.setParameter("stateId", statusId);
		List<UserSubscription> result = query.list();
		if (result != null) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result;
		}
		return null;
	}

	public List<UserSubscription> getUserSubscriptionsByStatus(int statusId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("CALL getUserSubscriptionsByStatus (:statusId)")
				.addEntity(UserSubscription.class)
				.setParameter("statusId", statusId);
		List<UserSubscription> result = query.list();
		if (result != null) {
			// logger.info("Found user with email = " + userEmail +
			// " in the db");
			return result;
		}
		return null;
	}

	public UserSubscription getUserSubscription(User user, boolean isPersonal) {
		int subscriptionTypeId = 1;
		if (!isPersonal)
			subscriptionTypeId = 3;
		int activeUserSubscriptionStatus = 1;
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL getUserSubscriptionByUserIdAndType (:userId,:subscriptionType)")
				.addEntity(UserSubscription.class)
				.setParameter("userId", user.getUserId())
				.setParameter("subscriptionType", subscriptionTypeId);
		List<UserSubscription> result = query.list();
		if (result != null && result.size() != 0) {
			for (UserSubscription userSubscription : result) {
				if (userSubscription.getSubscriptionStatus().getSubStatusId() == activeUserSubscriptionStatus)
					return userSubscription;
			}
		}
		return null;
	}

	public void updateUserSubscriptionWithSubType(int userId, int oldSubType,
			int newSubType) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createSQLQuery("update user_subscription set sub_type =:newSubType where user_id=:userId and sub_type =:oldSubType");
		q.setParameter("newSubType", newSubType);
		q.setParameter("oldSubType", oldSubType);
		q.setParameter("userId", userId);
		q.executeUpdate();
	}

	public List getUserSubscriptionsUnderUserId(Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from UserSubscription where admin_user=:userId and user_id != :userId");
		q.setParameter("userId", userId);

		List<UserSubscription> l = q.list();
		if (l == null || l.size() < 1) {
			return null;
		}
		for (UserSubscription u : l) {
			Hibernate.initialize(u.getUserByUserId());
			Hibernate.initialize(u.getActAs());
		}
		return l;
	}

	public List<UserSubscription> getUserSubscriptions(
			UserSubscription parentSubscription) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from UserSubscription where subscription_owner=:parentId");
		q.setParameter("parentId", parentSubscription.getId().getUserId());

		List<UserSubscription> l = q.list();
		if (l == null || l.size() < 1) {
			return null;
		}
		for (UserSubscription u : l) {
			Hibernate.initialize(u.getUserByUserId());
			Hibernate.initialize(u.getActAs());
		}
		return l;
	}

	public List<UserSubscription> getUserSubscriptionsUnderUserIdAndSubType(
			Integer userId, Integer subType) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from UserSubscription where user_id =:userId and sub_type=:subtype");
		q.setParameter("userId", userId);
		q.setParameter("subtype", subType);

		List<UserSubscription> l = q.list();
		if (l == null || l.size() < 1) {
			return null;
		}
		for (UserSubscription u : l) {
			Hibernate.initialize(u.getUserByUserId());
			Hibernate.initialize(u.getActAs());
		}
		return l;
	}

}