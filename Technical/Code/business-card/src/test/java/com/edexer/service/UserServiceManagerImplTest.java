package com.edexer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.dao.RoleEntityDaoImpl;
import com.edexer.model.BusinessCard;
import com.edexer.model.Role;
import com.edexer.model.User;

//TODO: add junit 4 dependency in pom.xml
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class UserServiceManagerImplTest extends TestCase {

	@Autowired
	UserServiceManager userService;

	@Autowired
	RoleEntityDaoImpl roleDao;
	@Autowired 
	BusinessCardServiceManager bcService;
	
	@Test
	public void test(){
		List<BusinessCard> bcList = new ArrayList<BusinessCard>();
		BusinessCard bc = bcService.get(50, true);
		bcList.add(bcService.get(50, true));
		bcList.add(bcService.get(51, true));
		JSONObject a = new JSONObject(bcList);
		System.out.println(a);
	}
	/*
	 * @Autowired BusinessCardServiceManager bcService;
	 * 
	 * @Autowired TagsServiceManager tagsService;
	 * 
	 * @Autowired UserSubscriptionServiceManager subService;
	 */

	/*
	 * //@Test public void testInsertSubscription(){ BusinessCard bc = new
	 * BusinessCard(); Set<Tags> s = new HashSet(); User u =
	 * null;//userService.getUserById(18);
	 * 
	 * 
	 * 
	 * UserSubscription uSub = new UserSubscription();
	 * //uSub.setUserByUserId(u); // uSub.setUserId(18);
	 * 
	 * ActAs actAs = new ActAs(); actAs.setActAsId(1); uSub.setActAs(actAs);
	 * uSub.setUserByLastEditBy(u);
	 * 
	 * Subscription sub = new Subscription(); sub.setSubscriptionId(1);
	 * uSub.setSubscription(sub);
	 * 
	 * SubscriptionStatus subStatus = new SubscriptionStatus();
	 * subStatus.setSubStatusId(1);
	 * 
	 * uSub.setSubscriptionStatus(subStatus);
	 * 
	 * Tags t = new Tags(); t.setTagName("My tag");
	 * 
	 * //t.setUserSubscription(uSub); s.add(t); uSub.setTagses(s);
	 * 
	 * // uSub.set subService.insertUserSubscription(uSub);
	 * 
	 * } /* //@Test public void testInsertBusinessCard(){ BusinessCard bc = new
	 * BusinessCard(); Set<Tags> s = new HashSet(); Tags t = new Tags();
	 * t.setTagName("My tag"); UserSubscription sub =
	 * subService.getUserSubscriptionsByUserId(18).get(0);
	 * t.setUserSubscription(sub); tagsService.addTag(t); // s.add(t);
	 * bc.setBcFirstName("Karim3"); bc.setBcLastName("Karim"); bc.setTagses(s);
	 * Integer x = bcService.addBusinessCard(bc); // System.out.println(x); }
	 * 
	 * //@Test public void testInsertUser() { // fail("Not yet implemented");
	 * User u = new User(); u.setFirstName("Karim2"); u.setLastName("Karim2");
	 * u.setMobile("01256598"); u.setUserEmail("k@tawfik.com");
	 * u.setPassword("karim2"); int result = userService.insertUser(u);
	 * UserSubscription userSub = new UserSubscription();
	 * 
	 * UserSubscriptionId usid = new UserSubscriptionId(); usid.setSubType(1);
	 * usid.setUserId(result);
	 * 
	 * userSub.setId(usid);
	 * 
	 * Date curDate = new Date(); //userSub.setUserByUserId(u);
	 * userSub.setLastEditDate(new Date()); userSub.setStartDate(curDate);
	 * userSub.setUserByLastEditBy(u);
	 * 
	 * SubscriptionStatus subStatus = new SubscriptionStatus();
	 * subStatus.setSubStatusId(1); userSub.setSubscriptionStatus(subStatus);
	 * 
	 * ActAs actas = new ActAs(); actas.setActAsId(1); userSub.setActAs(actas);
	 * 
	 * subService.insertUserSubscription(userSub); }
	 * 
	 * // public void testGetUserById() { //// fail("Not yet implemented"); // }
	 * // // public void testGetUser() { //// fail("Not yet implemented"); // }
	 * // // public void testGetUsers() { //// fail("Not yet implemented"); // }
	 * //@Test public void test(){
	 * 
	 * }
	 */
	// @Test
	/*
	 * public void changeStaffPass() { boolean result=false; try{ User user=new
	 * User(); user=userService.getUserById(1);
	 * result=userService.changeStaffPassword(user, "12345", user, "1781992");
	 * if(result) { System.out.println("Updated Succesfully");
	 * 
	 * } else{ System.out.println("didn't work"); }
	 * 
	 * } catch(Exception e){e.printStackTrace();}
	 * 
	 * }
	 */
	/*
	 * @Test public void UpdateUser() { try{ User user=new User();
	 * user=userService.getUserById(1); user.setFirstName("Mohamed");
	 * userService.update(user); System.out.println("Updated Succesfully"); }
	 * catch(Exception e){e.printStackTrace();System.out.println("Error");} }
	 */
	/*
	 * @Test public void DeleteUser() { try{ User user=new User();
	 * user=userService.getUserById(2);
	 * 
	 * userService.delete(user); System.out.println("Deleted Succesfully"); }
	 * catch(Exception e){e.printStackTrace();System.out.println("Error");} }
	 */
	//@Test
	public void InsertUser() {
		try {
			User user = new User();
			user.setUserEmail("Medo@gmail.com");
			user.setFirstName("MEDO");
			user.setLastName("Rady");
			user.setPassword("09876");
			user.setPhone("01873298");
			Role role = new Role();
			// RoleEntityDaoImpl dao=new RoleEntityDaoImpl();

			role = roleDao.get(Role.class, 1);
			user.setRole(role);
			userService.insertUser(user);
			System.out.println("Inserted Succesfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
	}

	@Test
	public void getColleagues() {
		User user = userService.getUserById(59);
		System.out.println("AA");
		List<User> list = userService.getCorporateUsers(user);
		System.out.println(list.size());
	}

}
