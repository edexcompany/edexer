package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.edexer.model.Fax;
import com.edexer.model.UserSubscription;

@Repository("faxEntityDao")
public class FaxEntityDaoImpl extends GenericEntityDaoImpl<Fax> {
	public boolean subscriptionHasFax(String fax,
			UserSubscription subscription, int bcId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL getFaxByIdAndSubscription(:faxNum,:user_id,:sub_type,:bcId)")
				.addEntity(Fax.class).setParameter("faxNum", fax)
				.setParameter("bcId", bcId);
		if (subscription == null) {
			query.setParameter("user_id", 0).setParameter("sub_type", 0);
		} else {
			query.setParameter("user_id", subscription.getId().getUserId())
					.setParameter("sub_type", subscription.getId().getSubType());
		}
		List<Fax> result = query.list();
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

}
