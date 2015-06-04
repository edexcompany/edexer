/**
 * @author Karim
 * This is the generic DAO impl for CRUD operations on DB.
 */
package com.edexer.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



@Transactional
public class GenericEntityDaoImpl<E>/* implements EntityDAO<E>*/ {

	@Autowired
	SessionFactory sessionFactory;
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Object add(E entity) throws HibernateException{
		return (Object) sessionFactory.getCurrentSession().save(entity);
	}

	public void delete(E entity) throws HibernateException{
		sessionFactory.getCurrentSession().delete(entity);
	}

	public void update(E entity) throws HibernateException{
		sessionFactory.getCurrentSession().update(entity);
	}

	
	public E get(Class clazz,Serializable id) throws HibernateException{
		return (E)sessionFactory.getCurrentSession().get(clazz, id);
	}
	
	
	public E find(Object id) throws HibernateException{
		Query q = sessionFactory.getCurrentSession().createQuery("from User");
		List<E> list = (ArrayList<E>) q.list();
		int listSize = list.size();
		if(list == null || listSize > 1){
			throw new RuntimeException("Result shouldn't be greater than 1.");
		}
		else if(listSize == 0){
			return null;
		}
		return list.get(0);
	}
	
}
