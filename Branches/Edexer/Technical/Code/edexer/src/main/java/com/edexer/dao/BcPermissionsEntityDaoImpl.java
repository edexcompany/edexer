package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcPermissions;
import com.edexer.model.UserSubscription;

@Repository("bcPermissionsEntityDao")
public class BcPermissionsEntityDaoImpl extends
		GenericEntityDaoImpl<BcPermissions> {

	public List<BcPermissions> getUserList() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<BcPermissions> list = null;
		Query q = session.createQuery("from bc_permissions");
		list = (ArrayList<BcPermissions>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}

	public void updateByUserIdAndSubScribeType(Integer adminUserId,
			Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		// Query q =
		// session.createQuery("delete BcPermissions where user_subscription_sub_type= :userSubType and user_subscription_user_id =:userId");
		// to avoid bc loss. will move all bc to the admin user.
		Query q = session
				.createSQLQuery("update bc_permissions set user_subscription_user_id =:adminUserId where user_subscription_sub_type= 3 and user_subscription_user_id =:userId");
		q.setParameter("adminUserId", adminUserId);
		q.setParameter("userId", userId);
		q.executeUpdate();
	}

	public BcPermissions get(int id, boolean loadRelations) {
		BcPermissions bcp = get(BcPermissions.class, id);
		if (!loadRelations)
			return bcp;
		Hibernate.initialize(bcp.getBusinessCard());
		Hibernate.initialize(bcp.getUserSubscription());
		return bcp;
	}
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
