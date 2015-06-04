package com.edexer.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcRequestStatus;
import com.edexer.model.PrivacyLevel;

@Repository("privacyLevelDao")
public class PrivacyLevelEntityDaoImpl extends GenericEntityDaoImpl<PrivacyLevel> {
	public List<PrivacyLevel> listAllPrivacyLevel(){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from PrivacyLevel");
		List<PrivacyLevel> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		return res;
	}
	public PrivacyLevel getPrivacyLevelById(Integer Id)
	{
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from PrivacyLevel where privacy_id=:Id");
		q.setParameter("Id", Id);
		List<PrivacyLevel> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		return res.get(0);
	}
	public PrivacyLevel getPrivacyLevelByName(String name)
	{
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from PrivacyLevel where privacy_name=:name");
		q.setParameter("name", name);
		List<PrivacyLevel> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		return res.get(0);
	}
}
