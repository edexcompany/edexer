package com.edexer.service;

import java.util.List;
import java.util.Set;

import com.edexer.model.Subscription;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.model.UserSubscriptionId;

public interface UserSubscriptionServiceManager {

	public enum TYPE {
		PERSONAL_FREE, PERSONAL_PRO, CORP
	}

	Object insertUserSubscription(UserSubscription userSubscription);

	/**
	 * Get from database the user subscription object for a certain user id
	 * given the required subscription type.
	 * 
	 * @param userId
	 * @param subType
	 * @return UserSubscription, null if not found
	 */
	public UserSubscription getUserSubscription(int userId, int subType);

	List<UserSubscription> getUserSubscriptions();

	/**
	 * updates user subscription without any validation. Depricataed and
	 * replaced with update(UserSubscription userSubscription) method.
	 * 
	 * @param userSubscription
	 */
	@Deprecated
	void updateUserSubscriptionNoValidation(UserSubscription userSubscription);

	/**
	 * Updates the user subscription. Returns null if user subscription updated
	 * if not then returns the rejection reason.
	 * 
	 * @param userSub
	 *            user subscription to be updates
	 * @return error message
	 */
	public String update(UserSubscription userSub);

	public void updateUserSubscriptionSubType(int userId, int oldSubType,
			int newSubType);

	List<UserSubscription> getUserSubscriptionsByUserId(int userId);

	List<UserSubscription> getUserSubscriptionsByUserId(int userId, int stateId);

	List<UserSubscription> getUserSubscriptionsByState(int stateId);

	List<Subscription> getSubscriptionTypes();

	UserSubscription getUserSubscription(User user, boolean isPersonal);

	public void deleteUserSubScriptionBy(UserSubscription userSubscription);

	public UserSubscriptionId insertFreeUserSubscription(Integer userId);

	public UserSubscriptionId insertProUserSubscription(Integer userId);

	public UserSubscriptionId insertCorpUserSubscription(Integer userId);

	public UserSubscriptionId insertCorpUserSubscription(Integer userId,
			Integer parentUserId);

	public List getUserSubscriptionsUnderUserId(Integer userId);

	public UserSubscriptionId insertCorpUserSubscription(Integer userId,
			Integer parentUserId, Integer ownerId);

	/**
	 * Gets the root of corportate subscription, which is the owner corporatr
	 * subscription
	 * 
	 * @param user
	 * @return UserSubscription represents the root
	 */
	public UserSubscription getParentCorpSubscriptionByUser(User user);

	/**
	 * Get the user subscription object for a certain user given the required
	 * subscription type. The method fetches user subscription from the given
	 * user object
	 * 
	 * @param userSubscrptionSet
	 *            : set of all user subscriptions
	 * @param subScriptionType
	 *            : the type to be returned
	 * @return User Subscription object and null if user doesn't have this user
	 *         subscription type.
	 */
	public UserSubscription getUserSubscriptionByType(
			Set<UserSubscription> userSubscrptionSet, int subScriptionType);

	/**
	 * Get the user subscription object for a certain user given the required
	 * subscription type. The method fetches user subscription from DB
	 * 
	 * @param user
	 *            : the target user
	 * @param type
	 *            : enum type
	 * @return User Subscription object and null if user doesn't have this user
	 *         subscription type.
	 */
	public UserSubscription getUserSubscriptionByType(User user, TYPE type);

	/**
	 * Get the user subscription object for a certain user given the required
	 * subscription type. The method fetches user subscription from DB
	 * 
	 * @param userId
	 *            : the target user id
	 * @param type
	 *            : enum type
	 * @return User Subscription object and null if user doesn't have this user
	 *         subscription type.
	 */
	public UserSubscription getUserSubscriptionByType(int userId, TYPE type);

	public String addSubscription(User user, UserSubscription userSub);

	/**
	 * Gets all user subscriptions in the database, relations are not loaded
	 * 
	 * @return list of user subscriptions representing all user subscriptions
	 */
	public List<UserSubscription> getSubscriptions();

	/**
	 * Gets all user subscriptions in the database
	 * 
	 * @return list of user subscriptions representing all user subscriptions
	 */
	public List<UserSubscription> getSubscriptionsWithRelations();

	/**
	 * Gets list of all user subscriptions under the same corporate subscription
	 * of argument
	 * 
	 * @param us
	 *            User subscription to search with
	 * @return list of user subscriptions
	 */
	public List<UserSubscription> getCorporateSubscriptions(
			UserSubscription parentSubscription);

	/**
	 * Gets the subscription owner id of the corporate subscription for the user
	 * in session
	 * 
	 * @return Owner id, zero if not found
	 */
	public int getSubscriptionOwnerIdForSessionUser();
}
