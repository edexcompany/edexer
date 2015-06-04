package com.edexer.mbeans;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.ActAs;
import com.edexer.model.Countries;
import com.edexer.model.Sector;
import com.edexer.model.Tags;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.TagsServiceManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

/**
 * This bean should handle all user activities on the user's home page.
 * 
 * @author Karim
 *
 */
@ManagedBean(name = "userHomeManagedBean", eager = true)
@SessionScoped
public class UserHomeManagedBean extends HttpServlet {

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager subscriptionManager;

	@ManagedProperty("#{actAsBean}")
	private ActAsManagedBean actAsMbean;
	FacesContext facesContext;
	@ManagedProperty("#{tagsServiceManager}")
	private TagsServiceManager tagsManager;

	@ManagedProperty("#{userService}")
	private UserServiceManager userManager;

	private List<Tags> corpUserTags;

	private List<Tags> personalUserTags;

	private User userFromSession;

	private String newTagValue;

	private List<User> userList;

	private boolean showInvitations = true;

	private boolean showUsersList = false;

	private boolean showProfileStats = true;

	private boolean showTags = false;

	private boolean showReports = false;
	private boolean showOtherSettings = false;
	private boolean showTemplateSettings = false;
	private Countries reportCountry;
	private Sector reportSector;
	private StringBuilder builder;

	// hold the subscription under certain user.
	private List<UserSubscription> userSubscriptionList;

	private ActAs actAs;

	private boolean showProUpgradeBtn;
	// DUMP DATA TO BE VIEWED IN UI.
	private Integer userSubscriptionCount = 10;
	private Integer businessCardsForUser = 1000;
	//

	/**
	 * carries the value of role for selected user
	 */
	private String editedUserRole;
	private String editedUserExport;
	ResourceBundle settings = ResourceBundle.getBundle("settings");

	private static final Logger LOGGER = Logger
			.getLogger(UserHomeManagedBean.class);

	@PostConstruct
	public void init() {
		userFromSession = getUserFromSession();
		if (corpUserTags == null) {
			corpUserTags = tagsManager.getCorporateTagsList(userFromSession);
		}
		if (personalUserTags == null) {
			personalUserTags = tagsManager.getPersonalTagsList(userFromSession);
		}
		Set<UserSubscription> userSubscriptions = userFromSession
				.getUserSubscriptionsForUserId();
		UserSubscription freeUserSub = getUserSubscriptionByType(
				userSubscriptions, Constants.FREE_SUBSCRIB_ID);
		if (freeUserSub == null)
			showProUpgradeBtn = false;
		else {
			showProUpgradeBtn = true;
		}
	}

	public String logOutAction() {
		// HttpSession session = HttpSessionUtil.getSession();
		userManager.removeRememberMeCookie();
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).invalidate();
		// session.invalidate();
		try {
			String baseUrl = UtilitiesManagesBean.baseUrl;
			// String baseUrl ="/" + settings.getString("APPLICATION_ROOT");
			String url = baseUrl + "/index.xhtml";
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Constants.ACTION_LOGOUT;
	}

	private User getUserFromSession() {
		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		return u;
	}

	public String upgradeToPersonalPro() {
		try {
			User u = getUserFromSession();
			Set<UserSubscription> userSubscriptions = (Set<UserSubscription>) u
					.getUserSubscriptionsForUserId();
			UserSubscription freeUserSub = getUserSubscriptionByType(
					userSubscriptions, Constants.FREE_SUBSCRIB_ID);

			if (freeUserSub == null) {
				FacesUtil.addInfoMessage("Can't be upgraded",
						"There is no Free subscription for this account");
				return Constants.INDEX;
			}
			// don't delete, just update.
			// update the user free subscription to pro.
			// freeUserSub.setId(new UserSubscriptionId(u.getUserId(),
			// Constants.PRO_SUBSCRIB_ID));
			// subscriptionManager.updateUserSubscription(freeUserSub);
			int userId = u.getUserId();
			subscriptionManager
					.updateUserSubscriptionSubType(userId, freeUserSub.getId()
							.getSubType(), Constants.PRO_SUBSCRIB_ID);
			// subscriptionManager.deleteUserSubScriptionBy(freeUserSub);
			// // add new subscription with Pro subscription type.
			// subscriptionManager.insertProUserSubscription(u.getUserId());

			// CLEARING DATA, AND LOADING THEM AGAIN...
			User updateUser = userManager.getUserById(userId);
			HttpSession session = HttpSessionUtil.getSession();
			session.removeAttribute(Constants.USER);
			session.setAttribute(Constants.USER, updateUser);

			// re-populate tags list again.
			corpUserTags = null;
			personalUserTags = null;
			init();

			FacesUtil.addInfoMessage("updated",
					"Account updated successfully..");
			showProUpgradeBtn = false;
			return Constants.INDEX;
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErrorMessage("Error", "Can't update your account.");
			LOGGER.error("Error occurred while updating user subscription, here is the error: "
					+ e.getMessage());
			return "";
		}

	}

	private void clearAndReinit(int userId) {
		// CLEARING DATA, AND LOADING THEM AGAIN...
		User updateUser = userManager.getUserById(userId);
		HttpSession session = HttpSessionUtil.getSession();
		session.removeAttribute(Constants.USER);
		session.setAttribute(Constants.USER, updateUser);

		// re-populate tags list again.
		corpUserTags = null;
		personalUserTags = null;
		init();
	}

	public String handleCorpUserClick() {

		return handleUserSubscriptionClick(Constants.CORP_SUB_TYPE, "corp",
				true);
	}

	public String handleProUserClick() {
		return "personal/personal_user_home";
	}

	/**
	 * This method handle when user click from home page -> My Subscription ->
	 * Corp Account. if user has corp account, it navigates to corp user home
	 * page, else navigate to corp upgrade form.
	 * 
	 * @return the page to navigate to .
	 */
	private String handleUserSubscriptionClick(Integer subscriptionId,
			String returnPage, boolean gotoUpgrade) {
		User u = getUserFromSession();
		Set<UserSubscription> userSubscriptions = (Set<UserSubscription>) u
				.getUserSubscriptionsForUserId();
		UserSubscription corpUserSub = getUserSubscriptionByType(
				userSubscriptions, Constants.CORP_SUB_TYPE);

		if (corpUserSub == null && gotoUpgrade) {
			// FacesUtil.addInfoMessage("Can't be upgraded",
			// "There is no Free subscription for this account");
			return returnPage + "_upgrade.xhtml"; // this method might return
													// "personal_upgrade" or
													// "corp_upgrade"
		}
		return returnPage + "_user_home.xhtml";
	}

	/**
	 * upgrade to Corp subscription.
	 */
	public String upgradeToCorp() {
		try {
			User u = getUserFromSession();
			Set<UserSubscription> userSubscriptions = (Set<UserSubscription>) u
					.getUserSubscriptionsForUserId();
			UserSubscription corpUserSub = getUserSubscriptionByType(
					userSubscriptions, Constants.CORP_SUB_TYPE);

			if (corpUserSub != null) {
				FacesUtil
						.addInfoMessage("Can't be added",
								"There is already a Corp subscription for this account");
				return "";
			}
			// add new subscription with corp subscription type.
			subscriptionManager.insertCorpUserSubscription(u.getUserId(),
					u.getUserId(), u.getUserId());
			FacesUtil.addInfoMessage("updated",
					"Account updated successfully..");

			clearAndReinit(u.getUserId());
			return Constants.INDEX;
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Error", "Can't update your account.");
			LOGGER.error("Error occurred while updating user subscription, here is the error: "
					+ e.getMessage());
			return "";
		}
	}

	/**
	 * This is a helper method, to get the free user sub, to be upgraded to Pro.
	 * assuming the the subscription type is not null, as it is passed
	 * internally from the caller not input from user.
	 * 
	 * @param userSubscrptionSet
	 * @return
	 */
	private UserSubscription getUserSubscriptionByType(
			Set<UserSubscription> userSubscrptionSet, Integer subScriptionType) {
		if (userSubscrptionSet == null || userSubscrptionSet.size() < 1) {
			return null;
		}
		for (UserSubscription userSub : userSubscrptionSet) {
			if (subScriptionType.equals(userSub.getId().getSubType())) {
				return userSub;
			}
		}
		return null;

	}

	public UserSubscriptionServiceManager getSubscriptionManager() {
		return subscriptionManager;
	}

	public void setSubscriptionManager(
			UserSubscriptionServiceManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	public void viewInvitations() {
		showInvitations = true;
		showProfileStats = false;
		showUsersList = false;
		showTags = false;
		showReports = false;
		showOtherSettings = false;
		showTemplateSettings = false;
		// FacesUtil.addInfoMessage("Invitations","Account updated successfully..");
	}

	public void viewMyProfileStats() {
		showProfileStats = true;
		showInvitations = false;
		showUsersList = false;
		showTags = false;
		showReports = false;
		showOtherSettings = false;
		showTemplateSettings = false;
		// FacesUtil.addInfoMessage("Info", "Account updated successfully..");
	}

	public void viewUsers() {
		showUsersList = true;
		showInvitations = false;
		showProfileStats = false;
		showTags = false;
		showReports = false;
		showOtherSettings = false;
		showTemplateSettings = false;
		// FacesUtil.addInfoMessage("Users", "Account updated successfully..");
	}

	public void viewTags() {
		showTags = true;
		showUsersList = false;
		showInvitations = false;
		showProfileStats = false;
		showReports = false;
		showOtherSettings = false;
		showTemplateSettings = false;
		// FacesUtil.addInfoMessage("Tags", "Account updated successfully..");
	}

	public void viewOtherSettings() {
		showTags = false;
		showUsersList = false;
		showInvitations = false;
		showProfileStats = false;
		showReports = false;
		showOtherSettings = true;
		showTemplateSettings = false;
	}

	public void viewTemplateSettings() {
		showTags = false;
		showUsersList = false;
		showInvitations = false;
		showProfileStats = false;
		showReports = false;
		showOtherSettings = false;
		showTemplateSettings = true;
	}

	public void viewReports() {
		showTags = false;
		showUsersList = false;
		showInvitations = false;
		showProfileStats = false;
		showReports = true;
		showOtherSettings = false;
		showTemplateSettings = false;
		// FacesUtil.addInfoMessage("Tags", "Account updated successfully..");
	}

	public List<User> getUserList() {
		if (userList == null) {
			User uu = getUserFromSession();
			userList = userManager.getCorporateUsers(uu);
			return userList;
		}
		return userList;

		// UserSubscription parentSubscription = subscriptionManager
		// .getParentCorpSubscriptionByUser(uu);
		// userSubscriptionList = subscriptionManager
		// .getCorporateSubscriptions(parentSubscription);
		// List l2 = getUsersFromSubscription(userSubscriptionList);
		// if (l2 == null) {
		// userList = new ArrayList<User>();
		// } else {
		// userList = l2;
		// }
		// }
		// return userList;
	}

	public void onRowEdit(RowEditEvent event) {
		User u = (User) event.getObject();
		User user = userManager.getUserById(u.getUserId());
		UserSubscription us = subscriptionManager.getUserSubscriptionByType(
				user.getUserSubscriptionsForUserId(),
				Integer.valueOf(settings.getString("SUBSCRIPTION_TYPE_CORP")));
		us.setIsAdmin(editedUserRole);
		us.setCanExport(editedUserExport);
		subscriptionManager.update(us);
		facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.addMessage(null, new FacesMessage("Row Edited",
				"Row Edited"));
	}

	public boolean isExportEnabled(int userId) {
		UserSubscription us = subscriptionManager.getUserSubscriptionByType(
				userId, UserSubscriptionServiceManager.TYPE.CORP);
		if (us != null) {
			if (us.getCanExport() != null && !us.getCanExport().equals("")) {
				return us.getCanExport().equals("Y");
			}
		}
		return false;
	}

	private List getUsersFromSubscription(List<UserSubscription> subSet) {
		if (subSet == null || subSet.size() == 0) {
			return null;
		}
		List user = new ArrayList<User>();
		for (UserSubscription sub : subSet) {
			user.add(userManager.initializeUserRelations(sub.getUserByUserId()));
		}
		return user;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public boolean isShowInvitations() {
		return showInvitations;
	}

	public void setShowInvitations(boolean showInvitations) {
		this.showInvitations = showInvitations;
	}

	public boolean isShowUsersList() {
		return showUsersList;
	}

	public void setShowUsersList(boolean showUsersList) {
		this.showUsersList = showUsersList;
	}

	public boolean isShowProfileStats() {
		return showProfileStats;
	}

	public void setShowProfileStats(boolean showProfileStats) {
		this.showProfileStats = showProfileStats;
	}

	public boolean isShowTags() {
		return showTags;
	}

	public void setShowTags(boolean showTags) {
		this.showTags = showTags;
	}

	public Integer getUserSubscriptionCount() {
		return userSubscriptionCount;
	}

	public void setUserSubscriptionCount(Integer userSubscriptionCount) {
		this.userSubscriptionCount = userSubscriptionCount;
	}

	public Integer getBusinessCardsForUser() {
		return businessCardsForUser;
	}

	public void setBusinessCardsForUser(Integer businessCardsForUser) {
		this.businessCardsForUser = businessCardsForUser;
	}

	public TagsServiceManager getTagsManager() {
		return tagsManager;
	}

	public void setTagsManager(TagsServiceManager tagsManager) {
		this.tagsManager = tagsManager;
	}

	public List<Tags> getUserTags() {
		return corpUserTags;
	}

	public void setUserTags(List<Tags> userTags) {
		this.corpUserTags = userTags;
	}

	public void addTag(ActionEvent actionEvent) {
		Tags t = new Tags();
		User u = getUserFromSession();
		int subscriptionType = Integer.parseInt((String) actionEvent
				.getComponent().getAttributes().get("subscriptionType"));
		//
		UserSubscription userSubscription = null;
		if (Integer.valueOf(settings.getString("SUBSCRIPTION_TYPE_FREE")) == subscriptionType
				|| Integer.valueOf(settings.getString("SUBSCRIPTION_TYPE_PRO")) == subscriptionType) {
			// case personal
			userSubscription = subscriptionManager.getUserSubscriptionByType(
					u.getUserSubscriptionsForUserId(), 0);

		} else if (Integer
				.valueOf(settings.getString("SUBSCRIPTION_TYPE_CORP")) == subscriptionType) {
			// case corp
			userSubscription = subscriptionManager.getUserSubscriptionByType(u
					.getUserSubscriptionsForUserId(), Integer.valueOf(settings
					.getString("SUBSCRIPTION_TYPE_CORP")));

		} else {
			userSubscription = null;
			// FacesUtil.addInfoMessage("No Subscription found for this user",
			// "");
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"No Subscription found for this user",
					"No Subscription found for this user"));
			return;
		}

		try {
			t.setTagName(newTagValue);
			t.setUserSubscription(userSubscription);
			tagsManager.addTag(t);
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"Tag Added successfully", "Tag Saved successfully"));
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Failed to add tags", "");
			facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().getFlash().setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage(
					"Error when adding new tag , try again.",
					"Error when adding new tag , try again."));
			LOGGER.error("Error happened during adding tags for UserId ["
					+ u.getUserId() + "] with error message: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		// FacesUtil.addInfoMessage("Tag Added successfully", "");
		if (Constants.FREE_SUBSCRIB_ID == subscriptionType
				|| Constants.PRO_SUBSCRIB_ID == subscriptionType) {
			personalUserTags.add(t);
		}
		if (Constants.CORP_SUB_TYPE == subscriptionType) {
			corpUserTags.add(t);
		}
		newTagValue = "";
	}

	public String getNewTagValue() {
		return newTagValue;
	}

	public void setNewTagValue(String newTagValue) {
		this.newTagValue = newTagValue;
	}

	public void onTagRowEdit(RowEditEvent event) {
		try {
			// String newTagValue =
			// (String)event.getComponent().getAttributes().get("newTagValue");
			// int rowId =
			// ((Integer)event.getComponent().getAttributes().get("rowId")).intValue();
			Tags t = (Tags) event.getObject();
			tagsManager.updateTag(t);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil
					.addErrorMessage("Error happened please contact CS.", null);
		}
		FacesUtil.addInfoMessage("Updated.", null);
	}

	public void onTagCellEdit(int rId, String subTypeStr, String newVal) {
		try {
			String newTagValue = newVal;
			int rowId = rId;
			Tags t = null;
			int subType = Integer.parseInt(subTypeStr);
			if (subType == Constants.CORP_SUB_TYPE) {
				t = corpUserTags.get(rowId);
			} else {
				t = personalUserTags.get(rowId);
			}

			t.setTagName(newTagValue);
			tagsManager.updateTag(t);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil
					.addErrorMessage("Error happened please contact CS.", null);
			return;
		}
		FacesUtil.addInfoMessage("Updated.", null);
	}

	public void deleteTag(ActionEvent e) {
		try {
			Tags tag = (Tags) e.getComponent().getAttributes().get("tagObj");
			Integer rowId = (Integer) e.getComponent().getAttributes()
					.get("rowId");
			String subTypeStr = (String) e.getComponent().getAttributes()
					.get("subscriptionType");
			int subscriptionType = Integer.parseInt(subTypeStr);
			tagsManager.delete(tag);
			if (Constants.CORP_SUB_TYPE == subscriptionType) {
				corpUserTags.remove(rowId.intValue());
			} else {
				personalUserTags.remove(rowId.intValue());
			}
			// FacesUtil.addInfoMessage("Tag list count=" +
			// corpUserTags.size(),"");
		} catch (Exception ex) {
			FacesUtil.addInfoMessage("Tag Deleted", "");
			ex.printStackTrace();
			LOGGER.error("Error happened while deleting tag from the db with error message:"
					+ ex.getMessage());
		}
	}

	public ActAs getActAs() {
		return actAs;
	}

	public void setActAs(ActAs actAs) {
		this.actAs = actAs;
	}

	public void updateUserActAsRole(Integer userId) {
		if (userSubscriptionList == null) {
			// this call is just to init userSubscription list.
			getUserList();
		}
		// error happened and still == null;
		if (userSubscriptionList == null) {
			FacesUtil.addInfoMessage(
					"Error occureed while retreiving subscriptions", "");
			return;
		}
		FacesUtil.addInfoMessage("welcome" + userId + "=="
				+ actAsMbean.getActAs().getActAsName(), "");

		UserSubscription userSubToBeUpdated = getUserSubscriptionForUser(userId);
		userSubToBeUpdated.setActAs(actAsMbean.getActAs());

		subscriptionManager
				.updateUserSubscriptionNoValidation(userSubToBeUpdated);
		FacesUtil.addInfoMessage("Sub scription Updated successfully...", "");

	}

	public UserSubscription getUserSubscriptionForUser(Integer userId) {
		User user = userManager.getUserById(userId);
		UserSubscription us = subscriptionManager.getUserSubscriptionByType(
				user.getUserSubscriptionsForUserId(),
				Integer.valueOf(settings.getString("SUBSCRIPTION_TYPE_CORP")));

		return us;
		// UserSubscription userSubToBeUpdated = null;
		// for (UserSubscription u : userSubscriptionList) {
		// // this should not be null by any mean.
		// if (u.getUserByUserId().getUserId() == userId) {
		// userSubToBeUpdated = u;
		// break;
		// }
		// }
		// return userSubToBeUpdated;
	}

	public void deleteUser(int userId) {
		UserSubscription us = subscriptionManager.getUserSubscriptionByType(
				userId, UserSubscriptionServiceManager.TYPE.CORP);
		subscriptionManager.deleteUserSubScriptionBy(us);
		userList.remove(userManager.getUserById(userId));
	}

	public void deleteUser(ActionEvent event) {
		try {
			User userToDelete = (User) event.getComponent().getAttributes()
					.get("userToDelete");
			int rowId = ((Integer) event.getComponent().getAttributes()
					.get("rowId")).intValue();
			User adminUser = getUserFromSession();
			userManager.delete(userToDelete, adminUser.getUserId());
			userList.remove(rowId);
			// FacesUtil.addInfoMessage("User list count=" +
			// corpUserTags.size(),"");
		} catch (Exception ex) {
			FacesUtil.addInfoMessage("User Deleted", "");
			ex.printStackTrace();
			LOGGER.error("Error happened while deleting tag from the db with error message:"
					+ ex.getMessage());
		}
	}

	public UserSubscription getUserSubscription(int userId) {
		return subscriptionManager.getUserSubscriptionByType(userManager
				.getUserById(userId).getUserSubscriptionsForUserId(), Integer
				.valueOf(settings.getString("SUBSCRIPTION_TYPE_CORP")));
	}

	public boolean isSubscriptionOwner(User user) {
		if (user.getUserId() == Constants.CORP_OWNER)
			return true;
		return false;
	}

	public boolean isSubscriptionAdmin(User user) {
		User u = userManager.getUserById(user.getUserId());
		UserSubscription us = subscriptionManager.getUserSubscriptionByType(
				u.getUserSubscriptionsForUserId(), Constants.CORP_SUB_TYPE);
		// TODO check is string is null
		if (us.getIsAdmin() != null && us.getIsAdmin().equals("Y"))
			return true;
		return false;
	}

	public String getActAsNameBasedOnUser(Integer userId) {
		UserSubscription userSubToBeUpdated = getUserSubscriptionForUser(userId);
		if (userSubToBeUpdated == null) {
			return null;
		}
		ActAs userActAs = userSubToBeUpdated.getActAs();
		return userActAs.getActAsName();
	}

	public ActAs getActAsObjectBasedOnUser(Integer userId) {
		UserSubscription userSubToBeUpdated = getUserSubscriptionForUser(userId);
		if (userSubToBeUpdated == null) {
			return null;
		}
		ActAs userActAs = userSubToBeUpdated.getActAs();
		return userActAs;
	}

	public ActAsManagedBean getActAsMbean() {
		return actAsMbean;
	}

	public void setActAsMbean(ActAsManagedBean actAsMbean) {
		this.actAsMbean = actAsMbean;
	}

	public List<UserSubscription> getUserSubscriptionList() {
		return userSubscriptionList;
	}

	public void setUserSubscriptionList(
			List<UserSubscription> userSubscriptionList) {
		this.userSubscriptionList = userSubscriptionList;
	}

	public boolean isShowProUpgradeBtn() {
		return showProUpgradeBtn;
	}

	public void setShowProUpgradeBtn(boolean showProUpgradeBtn) {
		this.showProUpgradeBtn = showProUpgradeBtn;
	}

	public int shouldShowUpgradeBtn() {
		return showProUpgradeBtn ? 1 : 0;
	}

	public UserServiceManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserServiceManager userManager) {
		this.userManager = userManager;
	}

	public List<Tags> getCorpUserTags() {
		return corpUserTags;
	}

	public void setCorpUserTags(List<Tags> corpUserTags) {
		this.corpUserTags = corpUserTags;
	}

	public List<Tags> getPersonalUserTags() {
		return personalUserTags;
	}

	public void setPersonalUserTags(List<Tags> personalUserTags) {
		this.personalUserTags = personalUserTags;
	}

	public boolean isShowReports() {
		return showReports;
	}

	public void setShowReports(boolean showReports) {
		this.showReports = showReports;
	}

	public Countries getReportCountry() {
		return reportCountry;
	}

	public void setReportCountry(Countries reportCountry) {
		this.reportCountry = reportCountry;
	}

	public Sector getReportSector() {
		return reportSector;
	}

	public void setReportSector(Sector reportSector) {
		this.reportSector = reportSector;
	}

	public String getEditedUserRole() {
		return editedUserRole;
	}

	public void setEditedUserRole(String editedUserRole) {
		this.editedUserRole = editedUserRole;
	}

	public String getEditedUserExport() {
		return editedUserExport;
	}

	public void setEditedUserExport(String editedUserExport) {
		this.editedUserExport = editedUserExport;
	}

	public boolean isShowOtherSettings() {
		return showOtherSettings;
	}

	public void setShowOtherSettings(boolean showOtherSettings) {
		this.showOtherSettings = showOtherSettings;
	}

	public boolean isShowTemplateSettings() {
		return showTemplateSettings;
	}

	public void setShowTemplateSettings(boolean showTemplateSettings) {
		this.showTemplateSettings = showTemplateSettings;
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(StringBuilder builder) {
		this.builder = builder;
	}

}
