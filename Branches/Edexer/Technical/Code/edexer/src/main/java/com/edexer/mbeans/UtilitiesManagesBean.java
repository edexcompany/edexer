package com.edexer.mbeans;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.Attachment;
import com.edexer.model.BusinessCard;
import com.edexer.model.Countries;
import com.edexer.model.Role;
import com.edexer.model.Sector;
import com.edexer.model.Subscription;
import com.edexer.model.SubscriptionStatus;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BusinessCardServiceManager;
import com.edexer.service.LookUpsServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@ApplicationScoped
public class UtilitiesManagesBean {
	private ResourceBundle settingsBundle;
	@ManagedProperty("#{businessCardServiceManager}")
	private BusinessCardServiceManager bcService;

	@ManagedProperty("#{lookupsServiceManager}")
	private LookUpsServiceManager lookupsService;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	private List<Countries> countriesList;
	private List<Role> rolesList;
	private List<Subscription> subscriptionTypeList;
	private List<SubscriptionStatus> subscriptionStatusList;
	private List<Sector> sectorList;
	public static String baseUrl;
	private UserSubscription userSubscription;

	@PostConstruct
	private void init() {
		settingsBundle = ResourceBundle.getBundle("/settings");
		countriesList = lookupsService.getCountriesList();
		subscriptionTypeList = lookupsService.listSubscriptionTypes();
		rolesList = lookupsService.getRolesList();
		subscriptionStatusList = lookupsService.getSubscriptionStatusList();
		sectorList = lookupsService.getSectorList();
		userSubscription = userSubscriptionService.getUserSubscription(
				Constants.CORP_OWNER, Constants.CORP_SUB_TYPE);
		ExternalContext ext = FacesContext.getCurrentInstance()
				.getExternalContext();
		URI uri;
		try {
			uri = new URI(ext.getRequestScheme(), null,
					ext.getRequestServerName(), ext.getRequestServerPort(),
					ext.getRequestContextPath(), null, null);
			System.out.println("base url:" + uri.toASCIIString());
			// ex: http://localhost:8080/business-card
			baseUrl = uri.toASCIIString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setBaseUrl("http://www.findon.com/");
			System.out.println(getBaseUrl());
		}

	}

	public String fullName() {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		if (user != null)
			return user.getFirstName() + " " + user.getLastName();
		return "";
	}

	@SuppressWarnings("unchecked")
	public UserSubscription getUserInSessionPersonalSubsctiption() {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		return userSubscriptionService.getUserSubscriptionByType(
				user.getUserSubscriptionsForUserId(), 0);
	}

	@SuppressWarnings("unchecked")
	public UserSubscription getUserInSessionCorporateSubsctiption() {
		User user = (User) HttpSessionUtil.getSession().getAttribute(
				Constants.USER);
		return userSubscriptionService.getUserSubscriptionByType(user
				.getUserSubscriptionsForUserId(), Integer
				.valueOf(settingsBundle.getString("SUBSCRIPTION_TYPE_CORP")));
	}
	

	public String getUserAvatarRelativePath() {
		try {
			User user = (User) HttpSessionUtil.getSession().getAttribute(
					Constants.USER);
			String useravatar =user.getUserAvatar();
			//BusinessCard bc = bcService.getUserOwnCard(user.getUserId());
			//return bc.getBusinessCardId() + "/" + bc.getAvatar();
			return user.getUserId()+ "/" + useravatar;
		} catch (Exception e) {
			return "";
		}

	}

	public int getSubscriptionTypeFreeKey() {
		return Integer.valueOf(settingsBundle
				.getString("SUBSCRIPTION_TYPE_FREE"));
	}

	public int getSubscriptionTypeProKey() {
		return Integer.valueOf(settingsBundle
				.getString("SUBSCRIPTION_TYPE_PRO"));
	}

	public int getSubscriptionTypeCorpKey() {
		return Integer.valueOf(settingsBundle
				.getString("SUBSCRIPTION_TYPE_CORP"));
	}

	public boolean isPersonalBusinessCard(int bcId) {
		return (bcService.get(bcId, true).getUserSubscription().getId()
				.getSubType() != Integer.valueOf(settingsBundle
				.getString("SUBSCRIPTION_TYPE_CORP"))) ? true : false;
	}

	public BusinessCardServiceManager getBcService() {
		return bcService;
	}

	public void setBcService(BusinessCardServiceManager bcService) {
		this.bcService = bcService;
	}

	public String getRootUploadPath() {
		return settingsBundle.getString("UPLOAD_PATH");
	}

	public String formateBusinessCardPhotoAddress(int bcId) {
		if (bcId != 0) {

			BusinessCard bc = bcService.get(bcId, true);
			String path = "";
			Iterator it = bc.getAttachments().iterator();
			if (it.hasNext()) {
				Attachment at = (Attachment) it.next();
				path = settingsBundle.getString("UPLOAD_PATH")
						+ at.getAttachmentId() + "//" + at.getPath();
				System.out.println(path);
				return path;
			}
		}
		return null;
	}

	public List<Countries> getCountriesObjectList() {
		return lookupsService.getCountriesList();
	}

	public Countries getCountry(int idCountry) {
		return lookupsService.getCountry(idCountry);
	}

	public Role getRole(int roleId) {
		return lookupsService.getRole(roleId);
	}

	public Sector getSectory(int sectorId) {
		return lookupsService.getSector(sectorId);
	}

	public Subscription getSubscriptionType(int subscriptionTypeId) {
		return lookupsService.getSubscriptionType(subscriptionTypeId);
	}

	public SubscriptionStatus getSubscriptionStatus(int subscriptionStatusId) {
		return lookupsService.getSubscriptionStatus(subscriptionStatusId);
	}

	public User getUser(int userId) {
		return getUserService().getUserById(userId);
	}

	public LookUpsServiceManager getLookupsService() {
		return lookupsService;
	}

	public void setLookupsService(LookUpsServiceManager lookupsService) {
		this.lookupsService = lookupsService;
	}

	public List<Countries> listCountries() {
		return countriesList;
	}

	public List<Subscription> listSubscriptionTypes() {
		return subscriptionTypeList;
	}

	public List<SubscriptionStatus> listSubscriptionStatus() {
		return subscriptionStatusList;
	}

	public List<Sector> listSectors() {
		return sectorList;
	}

	public void setCountriesList(List<Countries> countriesList) {
		this.countriesList = countriesList;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public UserSubscriptionServiceManager getUserSubscriptionService() {
		return userSubscriptionService;
	}

	public void setUserSubscriptionService(
			UserSubscriptionServiceManager userSubscriptionService) {
		this.userSubscriptionService = userSubscriptionService;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public UserSubscription getUserSubscription() {
		return userSubscription;
	}

	public void setUserSubscription(UserSubscription userSubscription) {
		this.userSubscription = userSubscription;
	}

	public List<Sector> getSectorList() {
		return sectorList;
	}

	public void setSectorList(List<Sector> sectorList) {
		this.sectorList = sectorList;
	}

}
