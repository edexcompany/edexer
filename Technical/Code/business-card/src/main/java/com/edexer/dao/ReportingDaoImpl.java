package com.edexer.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.edexer.model.Countries;
import com.edexer.model.Sector;

@Repository("reportingDao")
public class ReportingDaoImpl {

	@Autowired
	SessionFactory sessionFactory;

	public List<Object[]> runReport(int userId, int sub_type,
			Countries country, Sector sector) {
		Session session = sessionFactory.getCurrentSession();
		Query query;
		if (sector == null) {
			query = session
					.createSQLQuery(
							"CALL getSectorsCountByUserSub (:user_id, :sub_type, :country_id);")
					.setParameter("user_id", userId)
					.setParameter("sub_type", sub_type)
					.setParameter("country_id",
							(country == null ? 0 : country.getIdCountry()));
		} else {
			query = session
					.createSQLQuery(
							"CALL getSectorsCountByUserSubCountry (:user_id, :sub_type, :country_id, :sector_id);")
					.setParameter("user_id", userId)
					.setParameter("sub_type", sub_type)
					.setParameter("country_id",
							(country == null ? 0 : country.getIdCountry()))
					.setParameter("sector_id",
							(sector == null ? 0 : sector.getSectorId()));
		}
		List<Object[]> result = query.list();
		if (result != null /* && result.size() != 0 */) {
			return result;
		}
		return null;
	}

}
