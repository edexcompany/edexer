package com.edexer.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.AdminConfig;
/**
 * This class responsible for managing admin attributes in the db 
 * @author Karim
 *
 */
@Repository("adminConfigDao")
public class AdminConfigDaoImpl extends GenericEntityDaoImpl<AdminConfig>{

	public String getAdminConfigValueByKey(String key){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from AdminConfig where config_key_name=:key");
		q.setParameter("key", key);
		List<AdminConfig> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}
		AdminConfig config = res.get(0);
		//chcek that the result return from db is fully valid record to be used by upper layers.
		String result=null; 
		if(config != null || config.getId() != null || config.getId().getConfigValue() != null){
			result = config.getId().getConfigValue();
		}
		return result;
	}
}
