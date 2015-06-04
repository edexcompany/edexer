package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.Subscription;



@Repository("subscriptionEntityDao")
public class SubscriptionEntityDaoImpl extends GenericEntityDaoImpl<Subscription> {
	
	
	public List<Subscription> getSubscriptionsList() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<Subscription> list = null;
		Query q = session.createQuery("from Subscription");
		list = (ArrayList<Subscription>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}
	
	
}
