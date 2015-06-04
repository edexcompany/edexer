package com.edexer.service;

import java.io.Serializable;
import java.lang.Thread.State;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.edexer.dao.AdminConfigDaoImpl;
import com.edexer.model.AdminConfig;
import com.edexer.model.AdminConfigId;
import com.edexer.model.User;

@Service("adminConfigManager")
public class AdminConfigManagerImpl implements AdminConfigManager,Serializable {

	@Autowired
	AdminConfigDaoImpl adminConfigDao;
	@Autowired
	MailSender mailSender;
	@Autowired
	MailService ms;
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");

	@Override
	public String getAdminConfigValueByKey(String key) {
		return adminConfigDao.getAdminConfigValueByKey(key);
	}

	/*
	 * FindOn application has three registration states either opened,
	 * ViaInvitation or closed. This method updates this state in the database.
	 */
	@Override
	public void updateRegistrationState(String state) {
		try {
			AdminConfig admin = new AdminConfig();
			AdminConfigId ids = new AdminConfigId();
			ids.setConfigKeyName("Regstration_State");
			String Value = adminConfigDao.getAdminConfigValueByKey(ids
					.getConfigKeyName());
			if (Value == null) {
				ids.setConfigValue(String.valueOf(state));
				admin.setId(ids);
				adminConfigDao.add(admin);
			} else {
				ids.setConfigValue(String.valueOf(Value));
				admin.setId(ids);
				adminConfigDao.delete(admin);
				ids.setConfigValue(String.valueOf(state));
				admin.setId(ids);
				adminConfigDao.add(admin);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	 * FindOn application has three registration states either opened,
	 * ViaInvitation or closed. User can send invitation to unregistered user to
	 * join the system. This invitation is sent via mail.
	 */
	@Override
	public Boolean sendInvitation(User user, String Mail, String Subject,
			String Message) {
		Boolean result = false;
		try {
			// u can send email to Hash_Function method to encrypt the email
			// ....
			// String HahshMail=this.HashFunction(Mail);

			ms.sendMail(user.getUserEmail(), Mail, Subject, Message);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	/*
	 * Updates the maximum allowed count of records to be uploaded in excel
	 * sheet
	 */
	@Override
	public void updateMaxRecordsCount(int count) {
		try {

			AdminConfig admin = new AdminConfig();
			AdminConfigId ids = new AdminConfigId();
			ids.setConfigKeyName("WORKBOOK_UPLOAD_MAX");
			String Value = adminConfigDao.getAdminConfigValueByKey(ids
					.getConfigKeyName());
			if (Value == null) {
				ids.setConfigValue(String.valueOf(count));
				admin.setId(ids);
				adminConfigDao.add(admin);
			} else {
				ids.setConfigValue(String.valueOf(Value));
				admin.setId(ids);
				adminConfigDao.delete(admin);
				ids.setConfigValue(String.valueOf(count));
				admin.setId(ids);
				adminConfigDao.add(admin);
			}

			/*
			 * if(settingsBundle.containsKey("WORKBOOK_UPLOAD_MAX")) {
			 * System.out.println("EXIST"); } else {
			 * System.out.println("Not EXIST"); }
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Hash Function that recieve any string and return hash string .......
	 */
	public String HashFunction(String Mail) {
		MessageDigest md;
		StringBuffer hexString = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(Mail.getBytes());

			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return hexString.toString();
		}

	}

	
	
}
