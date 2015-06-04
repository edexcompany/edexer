package com.edexer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.edexer.model.*;

@Repository("MailConfigEntityDao")
public class MailConfigEntityDaoImpl extends GenericEntityDaoImpl<MailConfig> {

	private static final Logger logger = Logger
			.getLogger(MailConfigEntityDaoImpl.class);

	public MailConfig checkIfExist(int User_Id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			ArrayList<MailConfig> list = null;
			Query q = session
					.createQuery("from MailConfig where user_id =:user_id");
			q.setParameter("user_id", User_Id);
			list = (ArrayList<MailConfig>) q.list();
			if (list == null || list.size() < 1) {
				return null;
			}
			return list.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void updateMailConfig(int mail_config_id, MailConfig mConfig) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query q = session
					.createSQLQuery("update mail_config set user_id =:user_id ,from_email=:from_email,from_email_password=:f_e_pass,encryption_type=:en_Type,outgoing_mail_server=:out_mail,port_number=:port where mail_config_id=:m_con_id ");
			q.setParameter("m_con_id", mail_config_id);
			q.setParameter("user_id", mConfig.getUser().getUserId());
			q.setParameter("from_email", mConfig.getFromEmail());
			q.setParameter("f_e_pass", mConfig.getFromEmailPassword());
			q.setParameter("en_Type", mConfig.getEncryptionType());
			q.setParameter("out_mail", mConfig.getOutgoingMailServer());
			q.setParameter("port", mConfig.getPortNumber());
			// q.setParameter("auth", mConfig.getAuthentication());
			q.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
