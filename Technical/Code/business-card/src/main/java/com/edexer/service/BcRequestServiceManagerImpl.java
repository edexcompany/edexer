package com.edexer.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.atmosphere.util.StringEscapeUtils;
import org.hibernate.HibernateException;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.BcRequestEntityDaoImpl;
import com.edexer.dao.BcRequestStatusEntityDaoImpl;
import com.edexer.dao.BusinessCardEntityDaoImpl;
import com.edexer.dao.UserSubscriptionEntityDaoImpl;
import com.edexer.model.BcRequest;
import com.edexer.model.BcRequestStatus;
import com.edexer.model.BusinessCard;
import com.edexer.model.MailConfig;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;

@Service("bcRequestServiceManagerImpl")
public class BcRequestServiceManagerImpl implements BcRequestServiceManager,
		Serializable {
	private static final Logger logger = Logger
			.getLogger(BusinessCardServiceManagerImpl.class);
	@Autowired
	BcRequestEntityDaoImpl BcRequestDao;

	@Autowired
	BcRequestStatusEntityDaoImpl BcRequestStatusDao;

	@Autowired
	MailService mailService;

	@Autowired
	BusinessCardEntityDaoImpl businessCardDao;

	@Autowired
	UserSubscriptionEntityDaoImpl userSubscriptionDao;

	/*
	 * Create business card request from sender to receiver. Receiver will be
	 * notified by mail where the method call the mail service and notifies the
	 * user. The business card of sender will be copied for receiver.
	 */
	@Override
	public String sendBcRequest(User senderUser, User reciverUser,
			String Message) {
		// TODO Auto-generated method stub

		try {
			BcRequestStatus bcReqStatus = BcRequestStatusDao
					.getBcRequestStatusByName("Sent");
			// -- save business card request in db first----
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String dateNow = dateFormat.format(date);
			Date dateFinal = dateFormat.parse(dateNow);
			BcRequest bcRequest = new BcRequest();
			bcRequest.setUserBySenderUserId(senderUser);
			bcRequest.setUserByReciverUserId(reciverUser);
			bcRequest.setSendDate(dateFinal);
			bcRequest.setMessage(Message);
			bcRequest.setBcRequestStatus(bcReqStatus);
			BcRequestDao.add(bcRequest);
			logger.info("Business card Request Had Inserted In Database");
			// User userInSession=HttpSessionUtil.getUser();
			// if (reciverUser == userInSession) {

			// }

			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}

	}

	public void sendEmailAfterRequest(User reciverUser, String Message) {
		try {
			// -- send email for user to ask him about business card request
			// ----
			String To = reciverUser.getUserEmail();
			String Subject = "Business Card Request";
			String Body = Message;
			mailService.sendMail(To, Subject, Body);
			logger.info("Business card Request Had sent to Reciver Email");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Lists the sent business card requests for a given.
	 */
	@Override
	public List<BcRequest> listSentBcRequests(User senderUser) {
		// TODO Auto-generated method stub
		List<BcRequest> bcRequestList = new ArrayList<BcRequest>();
		try {
			bcRequestList = BcRequestDao.getRequestsBySender(senderUser
					.getUserId());
			return bcRequestList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BcRequest> listReciverBcRequests(User reciverUser) {
		// TODO Auto-generated method stub
		List<BcRequest> bcRequestList = new ArrayList<BcRequest>();
		try {
			bcRequestList = BcRequestDao.getRequestsByReciver(reciverUser
					.getUserId());
			return bcRequestList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*
	 * Updates a Business Card request in DB.
	 */
	@Override
	public void updateBcRequest(BcRequest bcRequest) {
		try {
			BcRequest bcRequestfromDB = BcRequestDao.get(BcRequest.class,
					bcRequest.getBcRequestId());
			if (bcRequestfromDB != null) {
				bcRequestfromDB.setBcRequestStatus(bcRequest
						.getBcRequestStatus());
				bcRequestfromDB.setMessage(bcRequest.getMessage());
				bcRequestfromDB.setSendDate(bcRequest.getSendDate());
				bcRequestfromDB.setUserByReciverUserId(bcRequest
						.getUserByReciverUserId());
				bcRequestfromDB.setUserBySenderUserId(bcRequest
						.getUserBySenderUserId());
				BcRequestDao.update(bcRequestfromDB);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Updates a Business Card request in as accepted. Makes a copy of receiver
	 * business card for the sender.
	 */
	@Override
	public void acceptBcRequest(BcRequest bcRequest) {
		try {
			BcRequest bcRequestfromDB = BcRequestDao.get(BcRequest.class,
					bcRequest.getBcRequestId());
			bcRequestfromDB.setBcRequestStatus(bcRequest.getBcRequestStatus());
			BcRequestDao.update(bcRequestfromDB);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Updates a Business Card request status as ignored.
	 */
	@Override
	public void ignoreBcRequest(BcRequest bcRequest) {
		// TODO Auto-generated method stub
		try {
			BcRequest bcRequestfromDB = BcRequestDao.get(BcRequest.class,
					bcRequest.getBcRequestId());
			bcRequestfromDB.setBcRequestStatus(bcRequest.getBcRequestStatus());
			BcRequestDao.update(bcRequestfromDB);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public BcRequest getBcRequestById(Integer id) {
		try {
			BcRequest request = null;
			if (!id.equals(0)) {
				request = (BcRequest) BcRequestDao.get(BcRequest.class, id);
			}
			return request;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public BcRequestEntityDaoImpl getBcRequestDao() {
		return BcRequestDao;
	}

	public void setBcRequestDao(BcRequestEntityDaoImpl bcRequestDao) {
		BcRequestDao = bcRequestDao;
	}

	public BcRequestStatusEntityDaoImpl getBcRequestStatusDao() {
		return BcRequestStatusDao;
	}

	public void setBcRequestStatusDao(
			BcRequestStatusEntityDaoImpl bcRequestStatusDao) {
		BcRequestStatusDao = bcRequestStatusDao;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public List<BcRequest> listBcRequestsBySenderANDreciver(User senderUser,
			User reciverUser) {
		List<BcRequest> bcRequestList = new ArrayList<BcRequest>();
		try {
			bcRequestList = BcRequestDao.getRequestsByReciverANDSender(
					senderUser.getUserId(), reciverUser.getUserId());
			return bcRequestList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void exchangeCardswhenAccept(User sender, User reciver) {
		// TODO Auto-generated method stub
		try {
			// ---get sender findon card and put it under reciver account
			BusinessCard senderCard = new BusinessCard();
			senderCard = businessCardDao.getFinOnCardByCreator(sender
					.getUserId());
			if (senderCard != null) {
				senderCard.setCreator(reciver.getUserId());
				UserSubscription reciverUserSub = userSubscriptionDao
						.getUserSubscription(reciver, true);
				if (reciverUserSub != null)
					senderCard.setUserSubscription(reciverUserSub);
				prepareBusinessCard(senderCard);
			}
			// --get reciver findon card and put it under sender account
			BusinessCard reciverCard = new BusinessCard();
			reciverCard = businessCardDao.getFinOnCardByCreator(reciver
					.getUserId());
			if (reciverCard != null) {
				reciverCard.setCreator(sender.getUserId());
				UserSubscription senderUserSub = userSubscriptionDao
						.getUserSubscription(sender, true);
				if (senderUserSub != null)
					reciverCard.setUserSubscription(senderUserSub);
				prepareBusinessCard(reciverCard);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Object prepareBusinessCard(BusinessCard bc) {
		Object obj = null;
		if (bc != null) {
			try {
				 obj = (Object) businessCardDao.add(bc, null);
			} catch (HibernateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("PrepareBC Error Addinf bc");
			}
		}		
		return obj;
	}

}
