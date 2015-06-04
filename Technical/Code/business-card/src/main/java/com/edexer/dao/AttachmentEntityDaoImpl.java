package com.edexer.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Repository;

import com.edexer.mbeans.Constants;
import com.edexer.model.Attachment;
import com.edexer.model.BusinessCard;
import com.edexer.model.Fax;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

@Repository("attachmentEntityDao")
public class AttachmentEntityDaoImpl extends GenericEntityDaoImpl<Attachment> {

	public List<Attachment> subscriptionHasFax(int bcId) {
		Session session = sessionFactory.getCurrentSession();
		List<Attachment> list = null;
		Query q = session
				.createQuery("from Attachment where business_card_business_card_id=:bcId");
		q.setParameter("bcId", bcId);
		list = (ArrayList<Attachment>) q.list();
		if (list != null && list.size() != 0) {
			return list;
		}
		return new ArrayList<Attachment>();
	}
}
