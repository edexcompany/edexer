package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.Countries;
import com.edexer.model.Role;

@Repository("roleEntityDao")
public class RoleEntityDaoImpl extends GenericEntityDaoImpl<Role> {

	public List<Role> getRolesList() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<Role> list = null;
		Query q = session.createQuery("from Role");
		list = (ArrayList<Role>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}

}
