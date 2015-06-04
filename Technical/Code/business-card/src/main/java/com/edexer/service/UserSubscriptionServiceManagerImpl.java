package com.edexer.service;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.poi.hssf.record.UseSelFSRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.dao.ActAsEntityDaoImpl;
import com.edexer.dao.SubscriptionEntityDaoImpl;
import com.edexer.dao.SubscriptionStatusEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.dao.UserSubscriptionEntityDaoImpl;
import com.edexer.mbeans.Constants;
import com.edexer.model.ActAs;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.UserSubscriptionId;

@Transactional
@Service("userSubScribtionService")
public class UserSubscriptionServiceManagerImpl implements
		UserSubscriptionServiceManager {

	@Autowired
	UserSubscriptionEntityDaoImpl userSubscriptionDao;

	@Autowired
	SubscriptionEntityDaoImpl subscriptionDao;

	@Autowired
	ActAsEntityDaoImpl actDao;

	@Autowired
	SubscriptionStatusEntityDaoImpl subStatusDao;

	@Autowired
	UserServiceManager userService;

	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");

	public UserSubscriptionEntityDaoImpl getUserSubscriptionDao() {
		return userSubscriptionDao;
	}

	public void setUserSubscriptionDao(
			UserSubscriptionEntityDaoImpl userSubscriptionDao) {
		this.userSubscriptionDao = userSubscriptionDao;
	}

	public SubscriptionEntityDaoImpl getSubscriptionDao() {
		return subscriptionDao;
	}

	public void setSubscriptionDao(SubscriptionEntityDaoImpl subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
	}

	public Object insertUserSubscription(UserSubscription userSubscription) {
		return (Object) userSubscriptionDao.add(userSubscription);
	}

	public List<UserSubscription> getUserSubscriptions() {
		return userSubscriptionDao.getUserSubscriptionsList(false);
	}

	public UserSubscription getUserSubscription(int userId, int subType) {
		return userSubscriptionDao.getUserSubscription(userId, subType);
	}

	public void updateUserSubscriptionNoValidation(
			UserSubscription userSubscription) {
		userSubscriptionDao.update(userSubscription);
	}

	@Override
	public String update(UserSubscription userSub) {
		String Error = "";
		try {
			userSubscriptionDao.update(userSub);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			Error = ex.getMessage();
			return Error;
		}
	}

	public void updateUserSubscriptionSubType(int userId, int oldSubType,
			int newSubType) {
		userSubscriptionDao.updateUserSubscriptionWithSubType(userId,
				oldSubType, newSubType);
	}

	public List<UserSubscription> getUserSubscriptionsByUserId(int userId) {
		return userSubscriptionDao.getUserSubscriptionsByUserId(userId);
	}

	public List<UserSubscription> getUserSubscriptionsByUserId(int userId,
			int stateId) {
		return userSubscriptionDao
				.getUserSubscriptionsByUserId(userId, stateId);
	}

	public List<UserSubscription> getUserSubscriptionsByState(int stateId) {
		return userSubscriptionDao.getUserSubscriptionsByStatus(stateId);
	}

	@Override
	public List<Subscription> getSubscriptionTypes() {
		return subscriptionDao.getSubscriptionsList();
	}

	@Override
	public UserSubscription getUserSubscription(User user, boolean isPersonal) {
		// This method to be used while inserting business cards
		return userSubscriptionDao.getUserSubscription(user, isPersonal);
	}

	public UserSubscriptionId insertFreeUserSubscription(Integer userId) {
		return insertUserSubscription(userId, null, Constants.FREE_SUBSCRIB_ID);
	}

	public UserSubscriptionId insertProUserSubscription(Integer userId) {
		return insertUserSubscription(userId, null, Constants.PRO_SUBSCRIB_ID);
	}

	public UserSubscriptionId insertCorpUserSubscription(Integer userId) {
		return insertUserSubscription(userId, null, Constants.CORP_SUBSCRIB_ID);
	}

	@Override
	public UserSubscriptionId insertCorpUserSubscription(Integer userId,
			Integer parentUserId) {
		return insertUserSubscription(userId, parentUserId,
				Constants.CORP_SUBSCRIB_ID);
	}

	@Override
	public UserSubscriptionId insertCorpUserSubscription(Integer userId,
			Integer parentUserId, Integer ownerId) {
		return insertUserSubscription(userId, parentUserId,
				Constants.CORP_SUBSCRIB_ID, ownerId);
	}

	private UserSubscriptionId insertUserSubscription(Integer userId,
			Integer parentUserId, Integer subscriptionId, Integer ownerUserId) {
		UserSubscription userSub = new UserSubscription();

		UserSubscriptionId usid = new UserSubscriptionId();
		usid.setSubType(subscriptionId);
		usid.setUserId(userId);

		if (parentUserId != null) {
			User u = new User();
			u.setUserId(parentUserId);
			// TO DO:userSub.setUserByAdminUser(u);
		}
		userSub.setId(usid);

		Date curDate = new Date();
		userSub.setLastEditDate(new Date());
		userSub.setStartDate(curDate);

		User onTheFlyUser = new User();
		onTheFlyUser.setUserId(userId);
		userSub.setUserByLastEditBy(onTheFlyUser);

		SubscriptionStatus subStatus = new SubscriptionStatus();
		subStatus.setSubStatusId(1);
		userSub.setSubscriptionStatus(subStatus);

		if (ownerUserId != null) {
			User u = new User();
			u.setUserId(ownerUserId);
			// TO DO:userSub.setUserByAdminUser(u);
			userSub.setUserBySubscriptionOwner(u);
		}

		ActAs actas = new ActAs();
		actas.setActAsId(1);
		userSub.setActAs(actas);
		userSub.setIsAdmin("Y");
		return (UserSubscriptionId) userSubscriptionDao.add(userSub);
	}

	private UserSubscriptionId insertUserSubscription(Integer userId,
			Integer parentUserId, Integer subscriptionId) {
		UserSubscription userSub = new UserSubscription();

		UserSubscriptionId usid = new UserSubscriptionId();
		usid.setSubType(subscriptionId);
		usid.setUserId(userId);

		if (parentUserId != null) {
			User u = new User();
			u.setUserId(parentUserId);
			userSub.setUserBySubscriptionOwner(u);
		}
		userSub.setId(usid);

		Date curDate = new Date();
		userSub.setLastEditDate(new Date());
		userSub.setStartDate(curDate);

		User onTheFlyUser = new User();
		onTheFlyUser.setUserId(userId);
		userSub.setUserByLastEditBy(onTheFlyUser);
		
		
		userSub.setCanExport("N");
		userSub.setIsAdmin("N");
		
		SubscriptionStatus subStatus = new SubscriptionStatus();
		subStatus.setSubStatusId(1);
		userSub.setSubscriptionStatus(subStatus);

		ActAs actas = new ActAs();
		actas.setActAsId(1);
		userSub.setActAs(actas);
		return (UserSubscriptionId) userSubscriptionDao.add(userSub);
	}

	@Override
	public void deleteUserSubScriptionBy(UserSubscription userSubscription) {
		userSubscriptionDao.delete(userSubscription);

	}

	public List getUserSubscriptionsUnderUserId(Integer userId) {
		return userSubscriptionDao.getUserSubscriptionsUnderUserId(userId);
	}

	@Override
	public UserSubscription getParentCorpSubscriptionByUser(User user) {
		try {
			UserSubscription childCorpSub = getUserSubscriptionByType(
					user.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_CORP")));
			UserSubscriptionId id = new UserSubscriptionId(childCorpSub
					.getUserBySubscriptionOwner().getUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_CORP")));
			return userSubscriptionDao.get(UserSubscription.class, id);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public UserSubscription getUserSubscriptionByType(

	Set<UserSubscription> userSubscrptionSet, int subScriptionType) {
		if (userSubscrptionSet == null || userSubscrptionSet.size() < 1) {
			return null;
		}

		for (UserSubscription userSub : userSubscrptionSet) {
			if (subScriptionType == 0
					&& (userSub.getId().getSubType() == Integer
							.valueOf(settingsBundle
									.getString("SUBSCRIPTION_TYPE_FREE")) || userSub
							.getId().getSubType() == Integer
							.valueOf(settingsBundle
									.getString("SUBSCRIPTION_TYPE_PRO"))))
				return userSub;

			if (subScriptionType == userSub.getId().getSubType()) {
				return userSub;
			}
		}
		return null;

	}

	/*
	 * Takes user object and user subscription object then inserts this
	 * subscription for the user. Returns null if user subscription added if not
	 * then returns the rejection reason
	 */
	@Override
	public String addSubscription(User user, UserSubscription userSub) {
		String Error = "";
		try {
			UserSubscription us = userSubscriptionDao.get(
					UserSubscription.class, userSub.getId());
			if (us != null) {
				Error = "User Have more than 1 personal or 1 corporat";
				return Error;
			} else {
				userSub.setUserBySubscriptionOwner(user);
				userSubscriptionDao.add(userSub);
				return null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			Error = ex.getMessage();
			return Error;
		}
	}

	@Override
	public List<UserSubscription> getSubscriptions() {
		return userSubscriptionDao.getUserSubscriptionsList(false);
	}

	@Override
	public List<UserSubscription> getSubscriptionsWithRelations() {
		return userSubscriptionDao.getUserSubscriptionsList(true);
	}

	@Override
	public List<UserSubscription> getCorporateSubscriptions(
			UserSubscription parentSubscription) {
		return userSubscriptionDao.getUserSubscriptions(parentSubscription);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserSubscription getUserSubscriptionByType(User user, TYPE type) {
		User u = userService.getUserById(user.getUserId());
		if (type == TYPE.CORP) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_CORP")));
		} else if (type == TYPE.PERSONAL_FREE) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_FREE")));
		} else if (type == TYPE.PERSONAL_PRO) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_PRO")));
		}
		return null;
	}

	@Override
	public UserSubscription getUserSubscriptionByType(int userId, TYPE type) {
		User u = userService.getUserById(userId);
		if (type == TYPE.CORP) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_CORP")));
		} else if (type == TYPE.PERSONAL_FREE) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_FREE")));
		} else if (type == TYPE.PERSONAL_PRO) {
			return getUserSubscriptionByType(u.getUserSubscriptionsForUserId(),
					Integer.valueOf(settingsBundle
							.getString("SUBSCRIPTION_TYPE_PRO")));
		}
		return null;
	}

	@Override
	public int getSubscriptionOwnerIdForSessionUser() {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		UserSubscription corpSubscription = getUserSubscriptionByType(
				user.getUserSubscriptionsForUserId(),
				Integer.parseInt(settingsBundle
						.getString("SUBSCRIPTION_TYPE_CORP")));
		int ownerId = (corpSubscription == null ? 0 : corpSubscription
				.getUserBySubscriptionOwner().getUserId());
		return 0;
	}
}
