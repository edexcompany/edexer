package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.ActAs;
import com.edexer.model.SocialNetworksTypes;

@Repository("socialNetworksTypesEntityDao")
public class SocialNetworksTypesEntityDaoImpl extends
		GenericEntityDaoImpl<SocialNetworksTypes> {

	public List<SocialNetworksTypes> getSocialNetworkTypesList() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<SocialNetworksTypes> list = null;
		Query q = session.createQuery("from SocialNetworksTypes");
		list = (ArrayList<SocialNetworksTypes>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}

}
