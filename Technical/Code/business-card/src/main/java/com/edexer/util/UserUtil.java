package com.edexer.util;

import java.util.Set;

import com.edexer.model.UserSubscription;

/**
 * This class contains all methods related to User and user subscription that repeated many times in the code, hence added here, what writting this code, it is only
 * one method :)
 * @author Karim
 *
 */
public class UserUtil {

	
	/**
	 * This is a helper method, to get the free user sub, to be upgraded to Pro.
	 * assuming the the subscription type is not null, as it is passed internally from the caller not input from user.
	 * @param userSubscrptionSet
	 * @return
	 */
	public static UserSubscription getUserSubscriptionByType(Set<UserSubscription> userSubscrptionSet, Integer subScriptionType){
		if(userSubscrptionSet == null || userSubscrptionSet.size() < 1){
			return null;
		}
		for(UserSubscription userSub : userSubscrptionSet){
			if(subScriptionType.equals(userSub.getId().getSubType())){
				return userSub;
			}
		}
		return null;
		
	}
}
