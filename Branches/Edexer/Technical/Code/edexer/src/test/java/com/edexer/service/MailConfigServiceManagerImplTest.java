package com.edexer.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.dao.MailConfigEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.model.BusinessCard;
import com.edexer.model.MailConfig;
import com.edexer.model.User;

//TODO: add junit 4 dependency in pom.xml
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class MailConfigServiceManagerImplTest extends TestCase {

	@Autowired
	MailConfigServiceManager mConfService;
	@Autowired
	UserServiceManager userServ;
	@Autowired
	UserEntityDaoImpl userDao;
	@Autowired
	MailConfigEntityDaoImpl mailConfigDao;
	@Autowired
	BusinessCardServiceManager bcs;

	// @Test
	public void InsertUser() {
		try {

			User user = new User();
			user = userServ.getUserById(1);
			MailConfig mConf = new MailConfig();
			mConf.setUser(user);
			mConf.setFromEmail("radysoft2013@gmail.com");
			mConf.setFromEmailPassword("csaapp1781992");
			mConf.setOutgoingMailServer("smtp.gmail.com");
			mConf.setPortNumber(111);
			mConf.setEncryptionType("TSL");
			mConfService.InsertOrUpdateMailConfig(mConf, user);
			System.out.println("Inserted Or Updated Succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

	// @Test
	public void ValidateOutGoing() {
		try {

			User user = new User();
			user = userServ.getUserById(1);
			MailConfig obj = null;
			obj = mailConfigDao.checkIfExist(user.getUserId());
			mConfService.validateOutgoingMailConfig(user, obj);
			System.out.println("Inserted Or Updated Succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

	@Test
	public void SendMail() {
		try {

			User user = new User();
			user = userServ.getUserById(1);
			MailConfig obj = null;
			obj = mailConfigDao.checkIfExist(user.getUserId());
			List<BusinessCard> bCards=bcs.getBusinessCards(2);
			mConfService.sendEmail(user, "Hello", "ana rady ya yasser", bCards);
			System.out.println("Inserted Or Updated Succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

}
