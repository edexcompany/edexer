package com.edexer.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.AdminConfig;
import com.edexer.model.WebsiteMessage;

@Repository("wesiteMessageDao")
public class WebsiteMessageDaoImpl extends GenericEntityDaoImpl<WebsiteMessage> {

	public String getwebsiteMessageValueByKey(String key) {
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from WebsiteMessage where key_name=:paramKey");
		q.setParameter("paramKey", key);
		//List<AdminConfig> resAd = q.list();
		List<WebsiteMessage> res = q.list();
		if (res == null || res.size() == 0) {
			return null;
		}
		WebsiteMessage message = res.get(0);
		// chcek that the result return from db is fully valid record to be used
		// by upper layers.
		String result = null;
		if (message != null || message.getId() != null
				|| message.getId().getValue() != null) {
			result = message.getId().getValue();
		}
		return result;
	}
}
