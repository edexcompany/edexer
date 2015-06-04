package com.edexer.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcRequest;
import com.edexer.model.BcRequestStatus;

@Repository("bcReuestStatusDao")
public class BcRequestStatusEntityDaoImpl extends GenericEntityDaoImpl<BcRequestStatus>  {
	public List<BcRequestStatus> listAllBcRequestStatus(){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from BcRequestStatus");
		List<BcRequestStatus> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		return res;
	}
	public BcRequestStatus getBcRequestStatusByName(String Name)
	{
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from BcRequestStatus where status_name=:name");
		q.setParameter("name", Name);
		List<BcRequestStatus> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		return res.get(0);
	}
}
