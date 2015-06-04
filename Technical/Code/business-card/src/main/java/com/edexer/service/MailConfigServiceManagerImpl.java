package com.edexer.service;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.BusinessCardEntityDaoImpl;
import com.edexer.dao.MailConfigEntityDaoImpl;
import com.edexer.model.BusinessCard;
import com.edexer.model.Email;
import com.edexer.model.MailConfig;
import com.edexer.model.User;
import com.edexer.util.encryptDecrypt;

@Transactional
@Service("mailConfigServiceManagerImpl")
public class MailConfigServiceManagerImpl implements MailConfigServiceManager {
	@Autowired
	MailConfigEntityDaoImpl mailConfigDao;
	@Autowired
	DynamicMailService ms;
	@Autowired
	BusinessCardEntityDaoImpl bCardDao;
	@Autowired
	UserServiceManager userServic;

	/*
	 * Updates or creates a record for outgoing mail configurations.
	 */
	@Override
	public String InsertOrUpdateMailConfig(MailConfig mail, User user) {
		try {
			MailConfig obj = null;
			obj = mailConfigDao.checkIfExist(user.getUserId());
			if (obj == null) {
				mailConfigDao.add(mail);
			} else if (obj != null) {
				obj.setUser(mail.getUser());
				obj.setFromEmail(mail.getFromEmail());
				obj.setFromEmailPassword(mail.getFromEmailPassword());
				obj.setOutgoingMailServer(mail.getOutgoingMailServer());
				obj.setPortNumber(mail.getPortNumber());
				obj.setAuthentication(mail.getAuthentication());
				obj.setEncryptionType(mail.getEncryptionType());
				obj.setTimeOut(mail.getTimeOut());
				mailConfigDao.update(obj);
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	/*
	 * Tests the mail configuration given by sending test mail to the user.
	 * Returns null if valid configuration or returns error message on error.
	 */
	@Override
	public String validateOutgoingMailConfig(User user, MailConfig mail) {
		try {
			String To = user.getUserEmail();
			InternetAddress[] toAddress = { new InternetAddress(To) };
			String Subject = "Find On Activate";
			String Body = "to Activate your Find On Account Follow this Instructions..";
			ms.sendMail(user, mail, toAddress, Subject, Body);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return ex.getMessage();
		}
	}

	/*
	 * Sends email from the preconfigured outgoing mail server for user to the
	 * destination user contacts. Before sending validation is made if the
	 * configurations are found or not.
	 */
	@Override
	public String sendEmail(User user, String subject, String body,
			List<BusinessCard> bCard) {
		try {
			List<InternetAddress> toAddress = new ArrayList<InternetAddress>();
			MailConfig mConfig = new MailConfig();
			for (int i = 0; i < bCard.size(); i++) {
				BusinessCard businessCard = bCardDao.get(BusinessCard.class, bCard.get(i).getBusinessCardId());
				Hibernate.initialize(businessCard.getEmails());
				Iterator it = businessCard.getEmails().iterator();
				while (it.hasNext()) {
					String email = ((Email) it.next()).getId().getEmailAddress();
					toAddress.add(new InternetAddress(email));
				}	
			}
			InternetAddress[] arrAddress =toAddress.toArray(new InternetAddress[toAddress.size()]);
			mConfig = (MailConfig) mailConfigDao.checkIfExist(user.getUserId());
			encryptDecrypt enClass=new encryptDecrypt();
			mConfig.setFromEmailPassword(enClass.decrypt(mConfig.getFromEmailPassword()));
			ms.sendMail(user, mConfig, arrAddress, subject, body);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

}
