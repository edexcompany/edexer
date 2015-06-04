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
@Autowired
MailService mail;
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
			user = userServ.getUserById(4);
			String body="<html><body><table border='0' cellpadding='0' cellspacing='0' style='background-color:white; border:1px solid #353535; border-radius:5px;' width='100%'>"
					+ "<tr><td style='text-align:center;'><h2>row 1 column 1</h2></td><td style='text-align:center;'><h2>row 1 column 2</h2></td></tr>"
					+ "<tr><td style='text-align:center;'><h2>row 2 column 1</h2></td><td style='text-align:center;'><h2>row 2 column 2</h2></td></tr>"
					+ "<tr><td  align='center' valign='middle' style='background-color:gray; font-family:Helvetica, Arial, sans-serif; font-size:16px; font-weight:bold; letter-spacing:-.5px; line-height:150%; padding-top:15px; padding-right:30px; padding-bottom:15px; padding-left:30px;''>"
					+ "<a href='http://www.mailchimp.com/blog/' target='_blank' style='color:green; text-decoration:none;'>Read More Stories On Our Blog</a></td></tr>"
					
					+ "</table><div  style='background-color:#FFFFFF; border:1px solid #CCCCCC;' width='50%'>"
					+ "<div align='center' valign='top' style='background-color:#2C9AB7; color:#FFFFFF; font-family:Helvetica, Arial, sans-serif; font-size:16px; font-weight:bold; padding-top:10px; padding-bottom:10px; text-align:center;'>Septemper</div>"
					+ "<div align='center' valign='top' style='color:#2C9AB7; font-family:Helvetica, Arial, sans-serif; font-size:60px; font-weight:bold; line-height:100%; padding-top:20px; padding-bottom:20px; text-align:center;'>16</div>"
					+ "</div></body></html>";
			mail.sendMail(user.getUserEmail(), "test templat", body);
			System.out.println("Inserted Or Updated Succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

}
