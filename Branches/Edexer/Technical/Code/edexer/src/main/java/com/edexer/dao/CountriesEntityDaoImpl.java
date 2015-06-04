package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.Countries;
import com.edexer.model.User;

@Repository("countriesEntityDao")
public class CountriesEntityDaoImpl extends GenericEntityDaoImpl<Countries> {
	
	public List<Countries> getCountriesList(){
		Session session = sessionFactory.getCurrentSession();
		ArrayList<Countries> list = null;
		Query q = session.createQuery("from Countries");
		list = (ArrayList<Countries>) q.list();
		//logger.info("Returning from getAdminNotes");
		return list;
	}

	public Countries getCountryByName(String countryName) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session
				.createQuery("from Countries where countryName = :name");
		q.setParameter("name", countryName);
		List<Countries> list = (List<Countries>) q.list();
		if(list.size()>0)
			return list.get(0);
		return null;
	}
	
	
}
