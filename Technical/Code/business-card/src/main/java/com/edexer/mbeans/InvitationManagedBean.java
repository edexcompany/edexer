package com.edexer.mbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.edexer.auth.HttpSessionUtil;
import com.edexer.faces.util.FacesUtil;
import com.edexer.model.User;
import com.edexer.model.UserSubscription;
import com.edexer.service.MailService;
import com.edexer.service.UserServiceManager;
import com.edexer.service.UserSubscriptionServiceManager;

@ManagedBean
@RequestScoped
public class InvitationManagedBean {

	private static final Logger LOGGER = Logger
			.getLogger(InvitationManagedBean.class);

	private String emailList;

	@ManagedProperty("#{mailService}")
	private MailService mailService;

	@ManagedProperty("#{userService}")
	private UserServiceManager userService;

	@ManagedProperty("#{userSubScribtionService}")
	private UserSubscriptionServiceManager userSubscriptionService;

	ResourceBundle settings = ResourceBundle.getBundle("settings");

	public String getEmail() {
		return emailList;
	}

	public void setEmail(String email) {
		this.emailList = email;
	}

	public String sendInvitation() {
		List<String> emailList = getEmailList(this.emailList);
		if (emailList == null || emailList.size() == 0) {
			LOGGER.info("no email list was entered.");
			FacesUtil.addWarnMessage("Email list was empty", "");
			return "user/user_home.xhtml";
		}
		List<String> invitationList = new ArrayList<String>();
		for (String string : emailList) {
			User u = userService.getUser(string);
			if (u != null) {
				// check if user has another corp
				UserSubscription us = userSubscriptionService
						.getUserSubscriptionByType(u
								.getUserSubscriptionsForUserId(), Integer
								.valueOf(settings
										.getString("SUBSCRIPTION_TYPE_CORP")));
				if (us == null) {
					// add corporate sub
					userSubscriptionService
							.insertCorpUserSubscription(u.getUserId(),
									HttpSessionUtil.getUser().getUserId());
				}
			} else {
				invitationList.add(string);
			}
		}

		HttpSession session = HttpSessionUtil.getSession();
		User u = (User) session.getAttribute(Constants.USER);
		String senderUserName = u.getFirstName() + " " + u.getLastName();
		String mailBody;
		try {
			for (String email : invitationList) {
				if (!isUserAlreadyExistsInDB(email)) { // if user has email in
														// db, don't send
														// invitation.
					try {
						mailService.sendMail(
								email,
								"from business-card",
								constructEmailBodyForEmail(email,
										senderUserName, u.getUserId()));
						FacesUtil.redirectToPage("/user/index.xhtml");
						FacesUtil.addInfoMessage("Invitation Mails sent", "");
					} catch (Exception e) {
						LOGGER.info("Error happenned while sending emial to: "
								+ email);
						FacesUtil.addInfoMessage(
								"Error occured while sending invitation mail",
								"");
					}
				} else {
					LOGGER.info("user with email: " + email
							+ " already exists in the our db.");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error happenned while processing emialList, here is the stack trace:"
					+ e.getMessage());
		}
		LOGGER.info("All email have been sent to [" + emailList.size()
				+ "]mail list");
		// FacesUtil.addInfoMessage("Invitation Mails sent", "");
		return "user/user_home.xhtml";
	}

	/**
	 * This method construct the email body for a given email, and generate the
	 * activation link per user.
	 * 
	 * @param email
	 * @return String, the body content.
	 */
	private String constructEmailBodyForEmail(String email, String sender,
			Integer userId) {
		StringBuilder body = new StringBuilder();
		body.append("Welcome " + email + ",<br/>\n");
		body.append("This is an invitation initiated by " + sender
				+ " via edexer.com to join our system.<br/>");
		body.append("Please go http://localhost:8080/business-card/index.xhtml?x=3&y="
				+ userId
				+ " to register.<br/>"
				+ "Best Regard, <br>"
				+ "Edexer Team.");
		return body.toString();

	}

	/**
	 * this method, check the user in the db, if exists with this email, will
	 * not send invitation to, otherwise will send.
	 * 
	 * @param email
	 * @return
	 */
	private boolean isUserAlreadyExistsInDB(String email) {
		return userService.isUserExists(email);
	}

	private List<String> getEmailList(String emailList) {
		return Arrays.asList(emailList.split(","));
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
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
}
