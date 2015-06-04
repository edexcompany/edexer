package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.Sector;

@Repository("sectorEntityDao")
public class SectorEntityDaoImpl extends GenericEntityDaoImpl<Sector> {
	private static final Logger logger = Logger
			.getLogger(UserEntityDaoImpl.class);

	public List<Sector> getSectorList() {
		Session session = sessionFactory.getCurrentSession();
		ArrayList<Sector> list = null;
		Query q = session.createQuery("from Sector");
		list = (ArrayList<Sector>) q.list();
		// logger.info("Returning from getAdminNotes");
		return list;
	}

	public Sector getsectorByName(String name) {
		logger.info("Trying to get sector with name = " + name);
		Session session = sessionFactory.getCurrentSession();
		Sector u = null;
		String query = "from Sector where sector_name like :name";
		logger.debug(query);
		logger.debug("name: " + name);
		Query q = session.createQuery(query);
		q.setParameter("name", "%" + name + "%");
		@SuppressWarnings("unchecked")
		List<Sector> l = (ArrayList<Sector>) q.list();
		if (l != null && l.size() != 0) {
			u = (Sector) l.get(0);
		}
		return u;
	}
}
