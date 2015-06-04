package com.edexer.service;

import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.WebsiteMessageDaoImpl;
import com.edexer.model.WebsiteMessage;
import com.edexer.model.WebsiteMessageId;

@Service("webSiteServiceManager")
public class WebsiteMessageServiceManagerImpl implements
		WebsiteMessageServiceManager {
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");

	@Autowired
	WebsiteMessageDaoImpl websiteMessageDao;

	@Override
	public String getwebSiteMessageValueByKey(String key) {
		return websiteMessageDao.getwebsiteMessageValueByKey(key);
	}

	@Override
	public void updateSubjectMessage(String Subject) {
		// TODO Auto-generated method stub
		try {
			// ---check for subject key and value first then update if it exist
			WebsiteMessage websiteSubject = new WebsiteMessage();
			WebsiteMessageId websiteSubId = new WebsiteMessageId();
			String KeyfromBundel = settingsBundle
					.getString("WEB_SITE_MESSAGE_SUBJECT_KEY");
			String sub = websiteMessageDao
					.getwebsiteMessageValueByKey(KeyfromBundel);

			if (sub != null && !sub.equals("")) {
				websiteSubId.setKeyName(KeyfromBundel);
				websiteSubId.setValue(String.valueOf(sub));
				websiteSubject.setId(websiteSubId);
				websiteMessageDao.delete(websiteSubject);
				// -- new instance from two objects to add new row with the same
				// key with new value
				websiteSubject = new WebsiteMessage();
				websiteSubId = new WebsiteMessageId();
				websiteSubId.setKeyName(KeyfromBundel);
				websiteSubId.setValue(String.valueOf(Subject));
				websiteSubject.setId(websiteSubId);
				websiteMessageDao.add(websiteSubject);
			}

		 else {
			 insertSubjectMessage(Subject);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void updateBodyMessage(String Body) {
		// TODO Auto-generated method stub
		try {
			
			// ---check for Message key and value first then update if it exist
			WebsiteMessage websiteMessage = new WebsiteMessage();
			WebsiteMessageId websiteMessageId = new WebsiteMessageId();
			String KeyMessagefromBundel = settingsBundle
					.getString("WEB_SITE_MESSAGE_KEY");
			String MessageValue = websiteMessageDao
					.getwebsiteMessageValueByKey(KeyMessagefromBundel);

			if (MessageValue != null && !MessageValue.equals("")) {
				websiteMessageId.setKeyName(KeyMessagefromBundel);
				websiteMessageId.setValue(String.valueOf(MessageValue));
				websiteMessage.setId(websiteMessageId);
				websiteMessageDao.delete(websiteMessage);
				// -- new instance from two objects to add new row with the same
				// key with new value
				websiteMessage = new WebsiteMessage();
				websiteMessageId = new WebsiteMessageId();
				websiteMessageId.setKeyName(KeyMessagefromBundel);
				websiteMessageId.setValue(String.valueOf(Body));
				websiteMessage.setId(websiteMessageId);
				websiteMessageDao.add(websiteMessage);
			}

		else {
			insertBodyMessage(Body);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void updateFooterMessage(String Footer) {
		// TODO Auto-generated method stub
		try {		

			// ---check for Footer key and value first then update if it exist
			WebsiteMessage websiteFooter = new WebsiteMessage();
			WebsiteMessageId websiteFootId = new WebsiteMessageId();
			String KeyFooterfromBundel = settingsBundle
					.getString("WEB_SITE_FOOTER_KEY");
			String FooterValue = websiteMessageDao
					.getwebsiteMessageValueByKey(KeyFooterfromBundel);

			if (FooterValue != null && !FooterValue.equals("")) {
				websiteFootId.setKeyName(KeyFooterfromBundel);
				websiteFootId.setValue(String.valueOf(FooterValue));
				websiteFooter.setId(websiteFootId);
				websiteMessageDao.delete(websiteFooter);
				// -- new instance from two objects to add new row with the same
				// key with new value
				websiteFooter = new WebsiteMessage();
				websiteFootId = new WebsiteMessageId();
				websiteFootId.setKeyName(KeyFooterfromBundel);
				websiteFootId.setValue(String.valueOf(Footer));
				websiteFooter.setId(websiteFootId);
				websiteMessageDao.add(websiteFooter);
			} else {
				insertFooterMessage(Footer);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public void insertSubjectMessage(String Subject) {
		// TODO Auto-generated method stub
		try {
			// -----Insert Subject Key and Value
			WebsiteMessage websiteSubject = new WebsiteMessage();
			WebsiteMessageId websiteSubId = new WebsiteMessageId();
			String SubjectKey = settingsBundle
					.getString("WEB_SITE_MESSAGE_SUBJECT_KEY");
			websiteSubId.setKeyName(SubjectKey);
			websiteSubId.setValue(Subject);
			websiteSubject.setId(websiteSubId);
			websiteMessageDao.add(websiteSubject);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public void insertBodyMessage(String Body) {
		// TODO Auto-generated method stub
		try {			
			// --------Insert Message Key and Value
			WebsiteMessage websiteMessage = new WebsiteMessage();
			WebsiteMessageId websiteMessId = new WebsiteMessageId();
			String MessageKey = settingsBundle
					.getString("WEB_SITE_MESSAGE_KEY");
			websiteMessId.setKeyName(MessageKey);
			websiteMessId.setValue(Body);
			websiteMessage.setId(websiteMessId);
			websiteMessageDao.add(websiteMessage);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public void insertFooterMessage(String Footer) {
		// TODO Auto-generated method stub
		try {			
			// --------Insert Footer Key and Value
			WebsiteMessage websiteFooter = new WebsiteMessage();
			WebsiteMessageId websiteFootId = new WebsiteMessageId();
			String FooterKey = settingsBundle.getString("WEB_SITE_FOOTER_KEY");
			websiteFootId.setKeyName(FooterKey);
			websiteFootId.setValue(Footer);
			websiteFooter.setId(websiteFootId);
			websiteMessageDao.add(websiteFooter);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
