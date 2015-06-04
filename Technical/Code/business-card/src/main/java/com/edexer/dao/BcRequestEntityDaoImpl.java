package com.edexer.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.BcRequest;
import com.edexer.model.User;

@Repository("BcRequestDao")
public class BcRequestEntityDaoImpl  extends GenericEntityDaoImpl<BcRequest> {
	
	public List<BcRequest> getRequestsBySender(Integer SenderUserID){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from BcRequest where sender_user_id=:senderId");
		q.setParameter("senderId", SenderUserID);
		@SuppressWarnings("unchecked")
		List<BcRequest> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}	
		if (res != null) {
			for (BcRequest bcRequest : res) {
				Hibernate.initialize(bcRequest.getUserBySenderUserId());
				Hibernate.initialize(bcRequest.getUserByReciverUserId());				
				Hibernate.initialize(bcRequest.getBcRequestStatus());
			}}
		
		return res;
	}
	public List<BcRequest> getRequestsByReciver(Integer ReciverUserID){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from BcRequest where reciver_user_id=:reciverId");
		q.setParameter("reciverId", ReciverUserID);
		@SuppressWarnings("unchecked")
		List<BcRequest> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}		
		if (res != null) {
			for (BcRequest bcRequest : res) {
				Hibernate.initialize(bcRequest.getUserBySenderUserId());
				Hibernate.initialize(bcRequest.getUserByReciverUserId());				
				Hibernate.initialize(bcRequest.getBcRequestStatus());
			}}
		
		return res;
	}
	public List<BcRequest> getRequestsByReciverANDSender(Integer SenderUserID,Integer ReciverUserID){
		Session s = sessionFactory.getCurrentSession();
		Query q = s.createQuery("from BcRequest where sender_user_id=:senderId and reciver_user_id=:reciverId");
		q.setParameter("senderId", SenderUserID);
		q.setParameter("reciverId", ReciverUserID);
		@SuppressWarnings("unchecked")
		List<BcRequest> res = q.list();
		if(res == null || res.size() == 0){
			return null;
		}		
		if (res != null) {
			for (BcRequest bcRequest : res) {
				Hibernate.initialize(bcRequest.getUserBySenderUserId());
				Hibernate.initialize(bcRequest.getUserByReciverUserId());				
				Hibernate.initialize(bcRequest.getBcRequestStatus());
			}}
		
		return res;
	}
}
