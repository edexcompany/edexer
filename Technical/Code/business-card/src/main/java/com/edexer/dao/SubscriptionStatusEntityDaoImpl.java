package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.ActAs;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;

@Repository("subscriptionStatusEntityDao")
public class SubscriptionStatusEntityDaoImpl extends
		GenericEntityDaoImpl<SubscriptionStatus> {

	public List<SubscriptionStatus> getSubscriptionStatusList() {

		Session session = sessionFactory.getCurrentSession();
		ArrayList<SubscriptionStatus> list = null;
		Query q = session.createQuery("from SubscriptionStatus");
		list = (ArrayList<SubscriptionStatus>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;

	}

	public SubscriptionStatus getSubscriptionStatusByName(String statusName) {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<SubscriptionStatus> list = null;
		Query q = session
				.createQuery("from subscription_status ss where ss.sub_status_name = "
						+ statusName);
		list = (ArrayList<SubscriptionStatus>) q.list();
		// logger.info("Returning from getAdminNotes");
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

}
