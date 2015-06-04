package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcPermissions;



@Repository("bcPermissionsEntityDao")
public class BcPermissionsEntityDaoImpl extends GenericEntityDaoImpl<BcPermissions>{

	public List<BcPermissions> getUserList(){
		Session session = sessionFactory.getCurrentSession();
		ArrayList<BcPermissions> list = null;
		Query q = session.createQuery("from bc_permissions");
		list = (ArrayList<BcPermissions>) q.list();
		//logger.info("Returning from getAdminNotes");
		return list;
	}
	
	
	public void updateByUserIdAndSubScribeType(Integer adminUserId, Integer userId){
		Session session = sessionFactory.getCurrentSession();
//		Query q = session.createQuery("delete BcPermissions where user_subscription_sub_type= :userSubType and user_subscription_user_id =:userId");
		//to avoid bc loss. will move all bc to the admin user.
		Query q = session.createSQLQuery("update bc_permissions set user_subscription_user_id =:adminUserId where user_subscription_sub_type= 3 and user_subscription_user_id =:userId");
		q.setParameter("adminUserId", adminUserId);
		q.setParameter("userId", userId);
		q.executeUpdate();
	}
}
