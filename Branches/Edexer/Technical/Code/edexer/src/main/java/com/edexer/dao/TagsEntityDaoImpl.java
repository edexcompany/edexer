package com.edexer.dao;

import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edexer.model.Tags;
import com.edexer.model.User;

@Repository("tagsEntityDao")
public class TagsEntityDaoImpl extends GenericEntityDaoImpl<Tags> {

	@Autowired
	UserSubscriptionEntityDaoImpl userSubscriptionDao;
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");

	// public List<Tags> getTagsList(User user, boolean isPersonal) {
	// UserSubscription userSubscription = userSubscriptionDao
	// .getUserSubscription(user, isPersonal);
	//
	// Session session = sessionFactory.getCurrentSession();
	// Query query = session
	// .createSQLQuery("CALL getTagsByUserSubscription(:user_id,:sub_type)")
	// .addEntity(Tags.class)
	// .setParameter("user_id", user.getUserId())
	// .setParameter("sub_type",
	// userSubscription.getSubscription().getSubscriptionId());
	// List<Tags> result = query.list();
	// if (result != null) {
	// // logger.info("Found user with email = " + userEmail +
	// // " in the db");
	// return result;
	// }
	// return null;
	// }

	public List<Tags> getTagsList(User user, boolean isCorp) {
		int subscriptionTypeId = 0;
		List<Tags> result=null;
		Session session = sessionFactory.getCurrentSession();
		if (isCorp) {
			subscriptionTypeId = Integer.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_CORP"));
			Query query = session
					.createSQLQuery(
							"CALL getTagsByUserSubscription(:user_id,:sub_type)")
					.addEntity(Tags.class)
					.setParameter("user_id", user.getUserId())
					.setParameter("sub_type", subscriptionTypeId);
			result = query.list();
		}else{
			subscriptionTypeId = Integer.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_FREE"));
			Query query = session
					.createSQLQuery(
							"CALL getTagsByUserSubscription(:user_id,:sub_type)")
					.addEntity(Tags.class)
					.setParameter("user_id", user.getUserId())
					.setParameter("sub_type", subscriptionTypeId);
			result = query.list();
			subscriptionTypeId = Integer.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_PRO"));
			Query query2 = session
					.createSQLQuery(
							"CALL getTagsByUserSubscription(:user_id,:sub_type)")
					.addEntity(Tags.class)
					.setParameter("user_id", user.getUserId())
					.setParameter("sub_type", subscriptionTypeId);
			List<Tags> result2 = query2.list();
			if(result2!=null){
				for (Tags tag : result2) {
					result.add(tag);
				}
			}
			return result;
		}
		
		
		
		return result;
	}

}
