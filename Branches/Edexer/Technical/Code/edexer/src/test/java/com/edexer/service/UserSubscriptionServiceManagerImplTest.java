package com.edexer.service;

import java.util.ResourceBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.dao.ActAsEntityDaoImpl;
import com.edexer.dao.SubscriptionEntityDaoImpl;
import com.edexer.dao.SubscriptionStatusEntityDaoImpl;
import com.edexer.dao.UserSubscriptionEntityDaoImpl;
import com.edexer.model.ActAs;
import com.edexer.model.Role;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.UserSubscriptionId;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class UserSubscriptionServiceManagerImplTest extends TestCase{
	@Autowired
	UserSubscriptionServiceManager userSubscriptionService;
	@Autowired
	SubscriptionEntityDaoImpl subDao;
	@Autowired
	UserSubscriptionEntityDaoImpl UserSubDao;
	@Autowired
	UserServiceManager userService;
	@Autowired
	ActAsEntityDaoImpl actDao;
	@Autowired
	SubscriptionStatusEntityDaoImpl subStatusDao;
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");
	//@Test
	public void testInsertSubscription() {
//		fail("Not yet implemented");
		//UserSubscription u = new UserSubscription();
		//TO_DO
		//userSubscriptionService.insertUserSubscription(u);
	}
	/*@Test
	public void test(){
		try{
			UserSubscription ui=new UserSubscription();
			UserSubscriptionId id=new UserSubscriptionId();
			id.setSubType(1);
			id.setUserId(1);
			ui=UserSubDao.get(UserSubscription.class,id);
			//ui=userSubscriptionService.getUserSubscription(1,1);
			ui.setNote("Welcom");
			userSubscriptionService.updateUserSubscription(ui);
			System.out.println("Updated Succesfully");
			}
			catch(Exception e){e.printStackTrace();System.out.println("Error");}
	}*/
	@Test
	public void addUserSubTest()
	{
		try{
			UserSubscription ui=new UserSubscription();
			UserSubscriptionId id=new UserSubscriptionId();
			
			User user=userService.getUserById(1);
			User userSubscription=userService.getUserById(3);
			
			int subTypefree=Integer.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_FREE")) ;
			int subAdmin= Integer.valueOf(settingsBundle.getString("ACTAS_SUBSCRIPTION_ADMIN"));
			int subStatusAct= Integer.valueOf(settingsBundle.getString("SUBSCRIPTION_STATUS_ACTIVE"));
			
			id.setSubType(subTypefree);
			id.setUserId(userSubscription.getUserId());
			ui.setId(id);
			
			Subscription sub=subDao.get(Subscription.class,subTypefree);
			ui.setSubscription(sub);
			
			ActAs act=actDao.get(ActAs.class,subAdmin);
			ui.setActAs(act);
			
			SubscriptionStatus subSts=subStatusDao.get(SubscriptionStatus.class,subStatusAct);
			ui.setSubscriptionStatus(subSts);
			ui.setNote("Radrood");	
			ui.setUserBySubscriptionOwner(user);
		    //ui.setUserByLastEditBy(user);
			String Result=userSubscriptionService.addSubscription(user,ui);
			if(Result==null)
			{
				System.out.println("Added Succesfully");
			}
			else
			{				
				System.out.println(Result);
			}
		}
			catch(Exception e){e.printStackTrace();System.out.println("Error");}
	}
}
