package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.edexer.model.Phone;
import com.edexer.model.UserSubscription;

@Repository("phoneEntityDao")
public class PhoneEntityDaoImpl extends GenericEntityDaoImpl<Phone> {
	public boolean subscriptionHasPhone(String phone,
			UserSubscription subscription, int bcId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery(
						"CALL getPhoneByIdAndSubscription(:phoneNum,:user_id,:sub_type,:bcId)")
				.addEntity(Phone.class).setParameter("phoneNum", phone)
				.setParameter("bcId", bcId);
		if (subscription == null) {
			query.setParameter("user_id", 0).setParameter("sub_type", 0);
		} else {
			query.setParameter("user_id", subscription.getId().getUserId())
					.setParameter("sub_type", subscription.getId().getSubType());
		}
		List<Phone> result = query.list();
		if (result != null && result.size() != 0) {
			return true;
		}
		return false;
	}
}
