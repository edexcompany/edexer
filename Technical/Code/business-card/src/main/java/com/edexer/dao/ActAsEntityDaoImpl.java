package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.ActAs;

@Repository("actAsEntityDao")
public class ActAsEntityDaoImpl extends GenericEntityDaoImpl<ActAs> {
	
	public List<ActAs> getActAsList(){
		Session session = sessionFactory.getCurrentSession();
		ArrayList<ActAs> list = null;
		Query q = session.createQuery("from ActAs");
		list = (ArrayList<ActAs>) q.list();
		//logger.info("Returning from getAdminNotes");
		return list;
	}
	
	public List<ActAs> getActAsListByUserId(int userId) {
		
		return null;
	}

	public ActAs getActAsByName(String actAsName) {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<ActAs> list = null;
		Query q = session.createQuery("from actas aa where aa.act_as_name = :" + actAsName);
		list = (ArrayList<ActAs>) q.list();
		//logger.info("Returning from getAdminNotes");
		if(list==null || list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
}
