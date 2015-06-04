package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.model.BcPermissions;
import com.edexer.model.BusinessCard;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.BcPermissionManager;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@ViewScoped
public class BcPermissionsManagedBean {

	@ManagedProperty("#{bcPermissionServiceManager}")
	BcPermissionManager permissionService;

	@ManagedProperty("#{userService}")
	UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	UserSubscriptionServiceManager userSubService;

	private List<User> handelUserUnSelectforAdd;
	private List<User> handelUserSelectForRemove;
	private List<User> corportateUsers;
	private List<User> selectedCorporateUsersForRemove;
	private List<BusinessCard> listBCards;
	private List<User> usersRemovedFromSlectedBC;

	private List<User> corportateUsersForAdd;
	private List<User> selectedCorporateUsersForAdd;
	private List<User> userAddedForSelectedBC;

	ResourceBundle settingsBundle = ResourceBundle.getBundle("/settings");

	@PostConstruct
	void init() {

	}

	public String save() {
		try {
			List<BcPermissions> removedPermissions = new ArrayList<BcPermissions>();
			if (usersRemovedFromSlectedBC != null
					&& usersRemovedFromSlectedBC.size() > 0) {
				if (listBCards != null && listBCards.size() > 0) {
					for (User user : usersRemovedFromSlectedBC) {
						for (BusinessCard bc : listBCards) {
							BcPermissions perm = new BcPermissions();
							perm.setBusinessCard(bc);
							UserSubscription userSub = new UserSubscription();
							userSub = userSubService
									.getUserSubscription(
											user.getUserId(),
											Integer.parseInt(settingsBundle
													.getString("SUBSCRIPTION_TYPE_CORP")));
							if (userSub != null)
								perm.setUserSubscription(userSub);
							removedPermissions.add(perm);
						}
					}
					permissionService.deletePermission(removedPermissions);
				}
			}
			List<BcPermissions> addedPermissions = new ArrayList<BcPermissions>();
			if (userAddedForSelectedBC != null
					&& userAddedForSelectedBC.size() > 0) {
				if (listBCards != null && listBCards.size() > 0) {
					for (User user : userAddedForSelectedBC) {
						for (BusinessCard bc : listBCards) {
							BcPermissions perm = new BcPermissions();
							perm.setBusinessCard(bc);
							UserSubscription userSub = new UserSubscription();
							userSub = userSubService
									.getUserSubscription(
											user.getUserId(),
											Integer.parseInt(settingsBundle
													.getString("SUBSCRIPTION_TYPE_CORP")));
							if (userSub != null)
								perm.setUserSubscription(userSub);
							addedPermissions.add(perm);

						}
					}
					permissionService.addPermission(addedPermissions);
				}
			}

			clear();
			String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();		
			return Constants.INDEX;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
			//return Constants.INDEX;
		}
	}

	public void hanleUserLists() {
		try {
			listBCards = (List<BusinessCard>) HttpSessionUtil.getSession()
					.getAttribute(
							Constants.BUSINESS_CARD_SELECTED_LIST_SESSION_KEY);
			if (listBCards.size() > 0) {

				// to get any user to remove from selected bc
				selectedCorporateUsersForRemove = permissionService
						.getUserWithPermissionOnBC(listBCards);
				corportateUsers = selectedCorporateUsersForRemove;

				// for add
				selectedCorporateUsersForAdd = permissionService.getUserWithNoPermOnBC(listBCards);
				corportateUsersForAdd = selectedCorporateUsersForAdd;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void handleUserSelectForRemove(SelectEvent event) {
		User user = (User) event.getObject();
		if (usersRemovedFromSlectedBC == null)
			usersRemovedFromSlectedBC = new ArrayList<User>();
		usersRemovedFromSlectedBC.add(user);
		corportateUsers.remove(user);
		selectedCorporateUsersForRemove = corportateUsers;

	}

	public void handleUserUnselectForRemove(UnselectEvent event) {

		if (usersRemovedFromSlectedBC != null) {
			User user = (User) event.getObject();
			usersRemovedFromSlectedBC.remove(user);
			if (selectedCorporateUsersForRemove != null)
				selectedCorporateUsersForRemove = new ArrayList<User>();
			corportateUsers.add(user);
			selectedCorporateUsersForRemove = corportateUsers;
		}
	}

	public void clear() {
		corportateUsers = new ArrayList<User>();
		selectedCorporateUsersForRemove = new ArrayList<User>();
		usersRemovedFromSlectedBC = new ArrayList<User>();

		corportateUsersForAdd = new ArrayList<User>();
		selectedCorporateUsersForAdd = new ArrayList<User>();
		userAddedForSelectedBC = new ArrayList<User>();
	}

	public List<User> getCorporateUsers(String query) {
		List<User> result = new ArrayList<User>();
		for (User user : selectedCorporateUsersForRemove) {
			if (user.getFirstName().toLowerCase().contains(query.toLowerCase())
					|| user.getLastName().toLowerCase()
							.contains(query.toLowerCase())) {
				result.add(user);
			}
		}
		return result;
	}

	public void handleUserSelectForAdd(SelectEvent event) {
		User user = (User) event.getObject();
		if (userAddedForSelectedBC == null)
			userAddedForSelectedBC = new ArrayList<User>();
		userAddedForSelectedBC.add(user);
		corportateUsersForAdd.remove(user);
		selectedCorporateUsersForAdd = corportateUsersForAdd;

	}

	public void handleUserUnselectForAdd(UnselectEvent event) {
		if (userAddedForSelectedBC != null) {
			User user = (User) event.getObject();
			userAddedForSelectedBC.remove(user);
			if (selectedCorporateUsersForAdd != null)
				selectedCorporateUsersForAdd = new ArrayList<User>();
			selectedCorporateUsersForAdd.add(user);
			corportateUsersForAdd = selectedCorporateUsersForAdd;
		}
	}

	public List<User> getCorporateUsersForAdd(String query) {
		List<User> result = new ArrayList<User>();
		for (User user : selectedCorporateUsersForAdd) {
			if (user.getFirstName().toLowerCase().contains(query.toLowerCase())
					|| user.getLastName().toLowerCase()
							.contains(query.toLowerCase())) {
				result.add(user);
			}
		}
		return result;
	}

	public BcPermissionManager getPermissionService() {
		return permissionService;
	}

	public void setPermissionService(BcPermissionManager permissionService) {
		this.permissionService = permissionService;
	}

	public UserServiceManager getUserService() {
		return userService;
	}

	public void setUserService(UserServiceManager userService) {
		this.userService = userService;
	}

	public UserSubscriptionServiceManager getUserSubService() {
		return userSubService;
	}

	public void setUserSubService(UserSubscriptionServiceManager userSubService) {
		this.userSubService = userSubService;
	}

	public List<User> getHandelUserUnSelectforAdd() {
		return handelUserUnSelectforAdd;
	}

	public void setHandelUserUnSelectforAdd(List<User> handelUserUnSelectforAdd) {
		this.handelUserUnSelectforAdd = handelUserUnSelectforAdd;
	}

	public List<User> getHandelUserSelectForRemove() {
		return handelUserSelectForRemove;
	}

	public void setHandelUserSelectForRemove(
			List<User> handelUserSelectForRemove) {
		this.handelUserSelectForRemove = handelUserSelectForRemove;
	}

	public List<User> getCorportateUsers() {
		return corportateUsers;
	}

	public void setCorportateUsers(List<User> corportateUsers) {
		this.corportateUsers = corportateUsers;
	}

	public List<User> getselectedCorporateUsersForRemove() {
		return selectedCorporateUsersForRemove;
	}

	public void setselectedCorporateUsersForRemove(
			List<User> selectedCorporateUsersForRemove) {
		this.selectedCorporateUsersForRemove = selectedCorporateUsersForRemove;
	}

	public List<BusinessCard> getListBCards() {
		return listBCards;
	}

	public void setListBCards(List<BusinessCard> listBCards) {
		this.listBCards = listBCards;
	}

	public List<User> getUsersRemovedFromSlectedBC() {
		return usersRemovedFromSlectedBC;
	}

	public void setUsersRemovedFromSlectedBC(
			List<User> usersRemovedFromSlectedBC) {
		this.usersRemovedFromSlectedBC = usersRemovedFromSlectedBC;
	}

	public List<User> getCorportateUsersForAdd() {
		return corportateUsersForAdd;
	}

	public void setCorportateUsersForAdd(List<User> corportateUsersForAdd) {
		this.corportateUsersForAdd = corportateUsersForAdd;
	}

	public List<User> getSelectedCorporateUsersForAdd() {
		return selectedCorporateUsersForAdd;
	}

	public void setSelectedCorporateUsersForAdd(
			List<User> selectedCorporateUsersForAdd) {
		this.selectedCorporateUsersForAdd = selectedCorporateUsersForAdd;
	}

	public List<User> getUserAddedForSelectedBC() {
		return userAddedForSelectedBC;
	}

	public void setUserAddedForSelectedBC(List<User> userAddedForSelectedBC) {
		this.userAddedForSelectedBC = userAddedForSelectedBC;
	}

}
