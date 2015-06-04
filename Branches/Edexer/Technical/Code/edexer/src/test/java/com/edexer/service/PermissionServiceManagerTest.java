package com.edexer.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class PermissionServiceManagerTest extends TestCase {

	@Autowired
	BcPermissionManager addPerm;

	@Autowired
	BusinessCardServiceManager bcService;

	@Autowired
	UserSubscriptionServiceManager userSubService;

	// @Test
	public void addPermission() {
		try {
			List<BcPermissions> listPerm = new ArrayList<BcPermissions>();

			BcPermissions perm1 = new BcPermissions();
			BusinessCard bc1 = bcService.get(2, false);
			UserSubscription userSub1 = userSubService
					.getUserSubscription(2, 1);
			perm1.setBusinessCard(bc1);
			perm1.setUserSubscription(userSub1);
			listPerm.add(perm1);

			BcPermissions perm2 = new BcPermissions();
			BusinessCard bc2 = bcService.get(3, false);
			UserSubscription userSub2 = userSubService
					.getUserSubscription(2, 1);
			perm2.setBusinessCard(bc2);
			perm2.setUserSubscription(userSub2);
			listPerm.add(perm2);

			BcPermissions perm3 = new BcPermissions();
			BusinessCard bc3 = bcService.get(4, false);
			UserSubscription userSub3 = userSubService
					.getUserSubscription(3, 1);
			perm3.setBusinessCard(bc3);
			perm3.setUserSubscription(userSub3);
			listPerm.add(perm3);

			addPerm.addPermission(listPerm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// @Test
	public void delete() {
		try {
			List<BcPermissions> listPerm = new ArrayList<BcPermissions>();

			BcPermissions perm1 = new BcPermissions();
			perm1 = addPerm.getBcPermissionByBCANDUserSub(2, 2, 1);

			BcPermissions perm2 = new BcPermissions();
			perm2 = addPerm.getBcPermissionByBCANDUserSub(3, 2, 1);

			BcPermissions perm3 = new BcPermissions();
			perm3 = addPerm.getBcPermissionByBCANDUserSub(4, 2, 1);

			BcPermissions perm4 = new BcPermissions();
			perm4 = addPerm.getBcPermissionByBCANDUserSub(2, 3, 1);

			BcPermissions perm5 = new BcPermissions();
			perm5 = addPerm.getBcPermissionByBCANDUserSub(3, 3, 1);

			BcPermissions perm6 = new BcPermissions();
			perm6 = addPerm.getBcPermissionByBCANDUserSub(4, 3, 1);

			listPerm.add(perm1);
			listPerm.add(perm2);
			listPerm.add(perm3);
			listPerm.add(perm4);
			listPerm.add(perm5);
			listPerm.add(perm6);

			addPerm.deletePermission(listPerm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	 //@Test
	public void getUsersHasNoPerm() {
		try {
			List<BusinessCard> list = new ArrayList<BusinessCard>();

			BusinessCard bc1 = bcService.get(2, false);
			BusinessCard bc2 = bcService.get(3, false);
			//BusinessCard bc3 = bcService.get(4, false);

			list.add(bc1);
			list.add(bc2);
			//list.add(bc3);

			List<User> listUser =addPerm.getUserWithNoPermOnBC(list);
			for(User u:listUser)
			{
				System.out.println(u.getUserId());	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void getUsersHasPerm() {
		try {
			List<BusinessCard> list = new ArrayList<BusinessCard>();

			//BusinessCard bc1 = bcService.get(2, false);
			 BusinessCard bc2 = bcService.get(3, false);
			BusinessCard bc3 = bcService.get(4, false);

			//list.add(bc1);
			 list.add(bc2);
			list.add(bc3);

			List<User> listUser = addPerm.getUserWithPermissionOnBC(list);
			for(User u:listUser)
			{
				System.out.println(u.getUserId());	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
