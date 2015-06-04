package com.edexer.mbeans.staffmbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.edexer.model.UserSubscription;
import com.edexer.service.MailService;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@ViewScoped
public class StaffSubscriptionsManagedBean {

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	@ManagedProperty("#{mailService}")
	private MailService mailService;

	private List<UserSubscription> userSubscriptionList;
	private List<UserSubscription> filteredUserSubscriptionList;

	@PostConstruct
	public void init() {
		userSubscriptionList = getUserSubscriptionService()
				.getSubscriptionsWithRelations();
	}

	public List<UserSubscription> getUserSubscriptionList() {
		return userSubscriptionList;
	}

	public void onRowEdit(RowEditEvent event) {
		String error = getUserSubscriptionService().update(
				(UserSubscription) event.getObject());
		FacesMessage msg = null;
		if (error == null)
			msg = new FacesMessage("User Subscription Edited", "updated");
		else
			msg = new FacesMessage("User Subscription Edited", error);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void setUserSubscriptionList(
			List<UserSubscription> userSubscriptionList) {
		this.userSubscriptionList = userSubscriptionList;
	}

	public List<UserSubscription> getFilteredUserSubscriptionList() {
		return filteredUserSubscriptionList;
	}

	public void setFilteredUserSubscriptionList(
			List<UserSubscription> filteredUserSubscriptionList) {
		this.filteredUserSubscriptionList = filteredUserSubscriptionList;
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

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
