package com.edexer.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edexer.dao.BcRequestEntityDaoImpl;
import com.edexer.dao.BcRequestStatusEntityDaoImpl;
import com.edexer.model.BcRequest;
import com.edexer.model.BcRequestStatus;
import com.edexer.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testapplicationContext.xml")
public class BcRequestServiceManagerImplTest extends TestCase {

	private static final Logger LOGGER = Logger
			.getLogger(BcRequestServiceManagerImplTest.class.getName());

	@Autowired
	BcRequestServiceManager bcRequestService;
	@Autowired
	UserServiceManager userService;
	@Autowired
	BcRequestEntityDaoImpl bcReqDao;
	@Autowired
	BcRequestStatusEntityDaoImpl bcReqStatusDao;

	// @Test
	public void sendBcRequest() {
		try {
			User userSender = new User();
			userSender = userService.getUserById(3);
			User userReciver = new User();
			userReciver = userService.getUserById(4);
			String Message = "Can i contact with u but first show me your info";
			bcRequestService.sendBcRequest(userSender, userReciver, Message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// @Test
	public void getListOfSentBcRequest() {
		try {
			User userSender = new User();
			userSender = userService.getUserById(3);
			List<BcRequest> list = new ArrayList<BcRequest>();
			list = bcRequestService.listSentBcRequests(userSender);
			System.out.println(list.get(0).getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// @Test
	public void getListOfReciveBcRequest() {
		try {
			User userRecive = new User();
			userRecive = userService.getUserById(4);
			List<BcRequest> list = new ArrayList<BcRequest>();
			list = bcRequestService.listReciverBcRequests(userRecive);
			System.out.println(list.get(0).getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	//@Test
	public void updtatbcRequest() {
		try {
			BcRequest bcReq = new BcRequest();
			bcReq = bcReqDao.get(BcRequest.class, 3);
			BcRequestStatus bcReStat = bcReqStatusDao.get(
					BcRequestStatus.class, 2);
			bcReq.setBcRequestStatus(bcReStat);
			bcReq.setMessage("kjhsdkj");
			bcRequestService.updateBcRequest(bcReq);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//@Test
	public void acceptBusinessCard()
	{
		try {
			BcRequest bcReq = new BcRequest();
			bcReq = bcReqDao.get(BcRequest.class, 3);
			BcRequestStatus bcReStat = bcReqStatusDao.get(
					BcRequestStatus.class, 1);
			bcReq.setBcRequestStatus(bcReStat);
			bcRequestService.acceptBcRequest(bcReq);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Test
	public void IgnorBusinessCard()
	{
		try {
			BcRequest bcReq = new BcRequest();
			bcReq = bcReqDao.get(BcRequest.class, 3);
			BcRequestStatus bcReStat = bcReqStatusDao.get(
					BcRequestStatus.class, 2);
			bcReq.setBcRequestStatus(bcReStat);
			bcRequestService.ignoreBcRequest(bcReq);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
