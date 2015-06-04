package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.hibernate.usertype.UserVersionType;

import com.edexer.model.ActAs;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.UserSubscription;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;
import com.mysql.jdbc.NotImplemented;


@ManagedBean
@SessionScoped
public class SubscriptionsManagedBean {

	@ManagedProperty("#{userSubscriptionService}")
	private UserSubscriptionServiceManager userSubsciptionService;
	
	@ManagedProperty("#{lookUpsService}")
	private LookUpsServiceManager lookUpsService;
	
	
	private UserSubscription userSubscription;

	private String subType;
	private String actAs;
	private String status;
	
	

	public void addSubscription(){
		UserSubscription newUserSubscription = new UserSubscription();
		newUserSubscription.setActAs(lookUpsService.getActAsByName(actAs));
		//newUserSubscription.setSubscription(lookUpsService.getSubscriptionStatusByName(status));
		newUserSubscription.setSubscriptionDate(new Date());
		userSubsciptionService.insertUserSubscription(userSubscription);
		//Set Start and End Date
	}
	
	public List<String> completeUserName(String query) {
        List<String> results = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            results.add(query + i);
        }
         
        return results;
    }

	
	public Map<String, String> getSubStatuses() {
        List<SubscriptionStatus> subStatusList = lookUpsService.getSubscriptionStatusList();
        Map<String, String> subStatusMap = new HashMap<String, String>();
        for (SubscriptionStatus subStatusItem : subStatusList) {
        	subStatusMap.put(subStatusItem.getSubStatusName(), subStatusItem.getSubStatusName());
		}
		return subStatusMap;
    }
	
	public Map<String, String> getSubTypes() {
        List<Subscription> subTypesList = userSubsciptionService.getSubscriptionTypes();
        Map<String, String> subTypesMap = new HashMap<String, String>();
        for (Subscription subTypeItem : subTypesList) {
        	subTypesMap.put(subTypeItem.getSubscriptionTypeName(), subTypeItem.getSubscriptionTypeName());
		}
		return subTypesMap;
    }
 	
	public Map<String, String> getActAses() {
        List<ActAs> actAsList = getLookUpsService().getActAsList();
        Map<String, String> actAsMap = new HashMap<String, String>();
        for (ActAs actAsItem : actAsList) {
        	actAsMap.put(actAsItem.getActAsName(), actAsItem.getActAsName());
		}
		return actAsMap;
    }
	
	
	
	public SubscriptionsManagedBean() {
	}

	public UserSubscriptionServiceManager getUserSubsciptionService() {
		return userSubsciptionService;
	}

	public void setUserSubsciptionService(UserSubscriptionServiceManager userSubsciptionService) {
		this.userSubsciptionService = userSubsciptionService;
	}
	
	public UserSubscription getUserSubscription() {
		return userSubscription;
	}

	public void setUserSubscription(UserSubscription userSubscription) {
		this.userSubscription = userSubscription;
	}

	public String getSubType() {
		return subType;
	}



	public void setSubType(String subType) {
		this.subType = subType;
	}



	public String getActAs() {
		return actAs;
	}



	public void setActAs(String actAs) {
		this.actAs = actAs;
	}

	public LookUpsServiceManager getLookUpsService() {
		return lookUpsService;
	}

	public void setLookUpsService(LookUpsServiceManager lookUpsService) {
		this.lookUpsService = lookUpsService;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
