package com.edexer.service;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class AdminConfigServiceManagerTest extends TestCase {
	private static final Logger LOGGER = Logger
			.getLogger(BusinessCardServiceManagerImplTest.class.getName());
	@Autowired
	AdminConfigManager adminCon;
	 @Autowired 
	 UserServiceManager usreim;
	
	@Test
	public void testInsert()
	{
		try
		{
			adminCon.updateRegistrationState("OPENED");
			//System.out.println("Successful");
		}
		catch(Exception e){e.printStackTrace();}
		
		
	}
	//@Test 
	public void testSendMail()
	{
		try
		{
			User user=usreim.getUserById(1);
			if(adminCon.sendInvitation(user,"radysoft2013@gmail.com","WELEOELEOELEO","M3 tegy wengeeb meligy"))
			{
				System.out.println("Sended Successfuly");
			}
			else
			{
				System.out.println("Sended Error");
			}
		}
		catch(Exception ex){ex.printStackTrace();}
		
	}
	//@Test
	public void testHash()
	{
		AdminConfigManagerImpl imp=new AdminConfigManagerImpl();
		String all=imp.HashFunction("radysoft2013@gmail.com");
		System.out.println(all);
	}
	//@Test 
	public void testUpdateCount()
	{
		adminCon.updateMaxRecordsCount(10);
	}
	
	

}
