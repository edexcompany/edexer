package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcPermissions;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

@Repository("bcPermissionDao")
public class BcPermissionEntityDaoImpl extends
		GenericEntityDaoImpl<BcPermissions> {

	public List<UserSubscription> getUsersHasNoPermForThisBC(int bcId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("CALL edexer_edexer.getUsersHasNoPermForThisBC(:bcId)").addEntity(UserSubscription.class).setParameter("bcId", bcId);
		//query.setParameter("bcId", bcId);
		List<UserSubscription> result = query.list();
		if (result != null && result.size() > 0) {
			return result;
		}
		return null;
	}

	public List<UserSubscription> getUsersHasPermForThisBC(int bcId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("CALL edexer_edexer.getUsersHasPermForThisBC(:bcId)").addEntity(UserSubscription.class).setParameter("bcId", bcId) ;
		//query.setParameter("bcId", bcId);
		List<UserSubscription> result = query.list();
		if (result != null && result.size() > 0) {
			return result;
		}
		return null;
	}

	public Boolean chkIfPermIsExistOrNot(int bcId, int userId, int subType) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("from BcPermissions where bc_id =:bcID and user_subscription_user_id=:userID and user_subscription_sub_type=:subType");
		query.setParameter("bcID", bcId);
		query.setParameter("userID", userId);
		query.setParameter("subType", subType);
		List<BcPermissions> result = query.list();
		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}

	public BcPermissions getPermByBCANDUserSub(int bcId, int userId, int subType) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BcPermissions where bc_id = :bcID and user_subscription_user_id = :userID and user_subscription_sub_type = :subType");
		query.setParameter("bcID", bcId);
		query.setParameter("userID", userId);
		query.setParameter("subType", subType);
		List<BcPermissions> result =(ArrayList<BcPermissions>) query.list();
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
