package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.PasswordReset;
import com.edexer.model.User;

@Repository("passwordResetEntityDao")
public class PasswordResetEntityDaoImpl extends
		GenericEntityDaoImpl<PasswordReset> {

	private static final Logger logger = Logger
			.getLogger(PasswordResetEntityDaoImpl.class);

	public PasswordReset getPasswordResetByToken(String token) {
		Session session = sessionFactory.getCurrentSession();
		List<PasswordReset> list = null;
		Query q = session.createQuery("from PasswordReset where token=:token");
		q.setParameter("token", token);
		PasswordReset u = null;
		list = (ArrayList<PasswordReset>) q.list();
		if (list != null && list.size() != 0) {
			logger.info("Found reset with token = " + token + " in the db");
			u = (PasswordReset) list.get(0);
			Hibernate.initialize(u.getUser());
		}
		logger.info("returning from get Password reset ");
		return u;

	}

}
